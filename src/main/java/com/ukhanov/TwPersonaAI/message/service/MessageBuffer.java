package com.ukhanov.TwPersonaAI.message.service;

import com.ukhanov.TwPersonaAI.event.MessageAddedEvent;
import com.ukhanov.TwPersonaAI.message.model.IMessage;
import com.ukhanov.TwPersonaAI.utils.LogService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageBuffer {

    private final Deque<IMessage> messages = new ArrayDeque<>();
    private final LogService logService;
    private final ApplicationEventPublisher publisher;

    public MessageBuffer(LogService logService, ApplicationEventPublisher publisher) {
        this.logService = logService;
        this.publisher = publisher;
    }

    public synchronized void add(IMessage message) {
        messages.addLast(message);
        logService.write("BUFFER", "Сообщение добавлено в буфер");

        publisher.publishEvent(new MessageAddedEvent(message));
    }

    public synchronized List<IMessage> takeLastMessages() {
        List<IMessage> result = new ArrayList<>(messages);
        result.sort(Comparator.comparing(IMessage::getTimestamp));

        messages.clear();
        logService.write("BUFFER", "Буфер выгружен и очищен");
        return result;
    }
}
