package com.ukhanov.TwPersonaAI.context;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.repoaitory.MessageRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContextService {

    private final MessageRepository messageRepository;

    public ContextService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Возвращает список USER сообщений для Spring AI
     * на основе последних 5 сообщений каждого пользователя.
     */
    public List<Message> buildChatContextAboutUsers(List<String> names) {

        return names.stream()
                .flatMap(name ->
                        messageRepository
                                .findTop5ByNameOrderByTimestampDesc(name)
                                .stream()
                                .map(m -> (Message) new UserMessage(m.getMessage()))
                )
                .toList();
    }

    public Map<String, String> buildChatContextForAnalyze(LocalDateTime from,
                                                          LocalDateTime to,
                                                          int depth) {

        return messageRepository.findMessagesBetweenDates(from, to).stream()

                // группируем по имени пользователя
                .collect(Collectors.groupingBy(MessageEntity::getName))

                .entrySet().stream()

                // для каждого пользователя сортируем и берём последние depth сообщений
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparing(MessageEntity::getTimestamp).reversed())
                                .limit(depth)
                                .map(MessageEntity::getMessage)
                                .collect(Collectors.joining(" | "))
                ));
    }


}
