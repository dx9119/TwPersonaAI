package com.ukhanov.TwPersonaAI.statistics.service;

import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.repoaitory.MessageRepository;
import com.ukhanov.TwPersonaAI.statistics.model.AuthorStats;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final MessageRepository messageRepository;

    public StatisticsService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<AuthorStats> getTopAuthorsLast24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusHours(24);

        List<MessageEntity> messages = messageRepository.findMessagesBetweenDates(from, now);

        Map<String, Long> grouped = messages.stream()
                .collect(Collectors.groupingBy(MessageEntity::getName, Collectors.counting()));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(e -> new AuthorStats(e.getKey(), e.getValue()))
                .toList();
    }
}
