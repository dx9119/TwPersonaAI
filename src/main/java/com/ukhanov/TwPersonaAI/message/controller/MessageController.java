package com.ukhanov.TwPersonaAI.message.controller;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.service.MessageFetchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageFetchService fetchService;

    public MessageController(MessageFetchService fetchService) {
        this.fetchService = fetchService;
    }

    /**
     * Возвращает последние 10 непрочитанных сообщений.
     */
    @GetMapping("/api/messages/unchecked")
    public List<MessageEntity> getLastUncheckedMessages() {
        return fetchService.fetchLastUnchecked();
    }
}
