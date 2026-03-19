package com.ukhanov.TwPersonaAI.message.service;

import com.ukhanov.TwPersonaAI.event.MessageAddedEvent;
import com.ukhanov.TwPersonaAI.message.MessageMapper;
import com.ukhanov.TwPersonaAI.message.model.IMessage;
import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.repoaitory.MessageRepository;
import com.ukhanov.TwPersonaAI.utils.LogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class MessagePersistenceService {

    private final MessageBuffer buffer;
    private final MessageRepository repository;
    private final MessageMapper mapper;
    private final LogService logService;

    // Планировщик, который умеет запускать задачи с задержкой
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    // Ссылка на запланированную задачу, чтобы можно было её отменить
    private ScheduledFuture<?> future;

    // Время первого сообщения в серии (для ограничения в 10 секунд)
    private long firstMessageTime = 0;

    // Максимальное время ожидания перед принудительным сохранением
    private static final long MAX_WAIT_MS = 10_000; // 10 секунд

    public MessagePersistenceService(MessageBuffer buffer,
                                     MessageRepository repository,
                                     MessageMapper mapper,
                                     LogService logService) {
        this.buffer = buffer;
        this.repository = repository;
        this.mapper = mapper;
        this.logService = logService;
    }

    @EventListener
    public synchronized void onMessageAdded(MessageAddedEvent event) {

        long now = System.currentTimeMillis(); // текущее время

        // Если это первое сообщение в серии — запоминаем время
        if (firstMessageTime == 0) {
            firstMessageTime = now;
        }

        // Если уже есть запланированная задача — отменяем её
        // потому что пришло новое сообщение, и нужно подождать ещё
        if (future != null && !future.isDone()) {
            future.cancel(false);
        }

        // Сколько времени прошло с первого сообщения
        long elapsed = now - firstMessageTime;

        // Если прошло больше 10 секунд — сохраняем принудительно
        if (elapsed >= MAX_WAIT_MS) {
            flushBuffer();      // сохраняем
            firstMessageTime = 0; // сбрасываем таймер серии
            return;
        }

        // Иначе планируем сохранение через 2 секунды
        // Если за эти 2 секунды придёт новое сообщение — задача будет отменена
        future = executor.schedule(() -> {
            flushBuffer();      // сохраняем
            firstMessageTime = 0; // сбрасываем таймер серии
        }, 2, TimeUnit.SECONDS);
    }

    private void flushBuffer() {
        // Забираем все сообщения из буфера
        List<IMessage> messages = buffer.takeLastMessages();

        if (messages.isEmpty()) {
            return; // если буфер пуст — ничего не делаем
        }

        logService.write("PERSIST", "Сохраняю сообщения: " + messages.size());

        // Преобразуем IMessage → MessageEntity
        List<MessageEntity> entities = messages.stream()
                .map(mapper::toEntity)
                .toList();

        // Сохраняем пачкой
        repository.saveAll(entities);
    }
}
