package com.ukhanov.TwPersonaAI.event;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.repoaitory.MessageRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiEventListener {

    private final MessageRepository messageRepository;

    public AiEventListener(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @EventListener
    public void handleAiAnswerDone(AiAnswerDoneEvent event) {
        List<MessageEntity> messages = messageRepository.findAllById(event.getMessageIds());
        messages.forEach(m -> m.setAnswered(true));

        messageRepository.saveAll(messages);
    }
}
