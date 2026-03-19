package com.ukhanov.TwPersonaAI.message.service;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.repoaitory.MessageRepository;
import com.ukhanov.TwPersonaAI.utils.LogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageFetchService {

    private final MessageRepository repository;
    private final LogService logService;

    public MessageFetchService(MessageRepository repository, LogService logService) {
        this.repository = repository;
        this.logService = logService;
    }

    /**
     * Возвращает последние 10 непрочитанных сообщений.
     */
    public List<MessageEntity> fetchLastUnchecked() {

        List<MessageEntity> result = repository.findLastUnchecked(10);

        logService.write("LLM_FETCH", "Получены данные для LLM: " + result.size());

        return result;
    }

    public List<MessageEntity> fetchMessagesBetween(LocalDateTime from, LocalDateTime to) {
        return repository.findMessagesBetweenDates(from, to);
    }

}
