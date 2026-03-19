package com.ukhanov.TwPersonaAI.app.service;

import com.ukhanov.TwPersonaAI.provider.model.AiAnswerEntity;
import com.ukhanov.TwPersonaAI.provider.repository.AiAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiAnswerReadService {

    private final AiAnswerRepository aiAnswerRepository;

    public AiAnswerReadService(AiAnswerRepository aiAnswerRepository) {
        this.aiAnswerRepository = aiAnswerRepository;
    }

    public List<AiAnswerEntity> getLast15Answers() {
        return aiAnswerRepository.findTop15ByOrderByTimestampDesc();
    }
}

