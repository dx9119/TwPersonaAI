package com.ukhanov.TwPersonaAI.app.controller.rest;

import com.ukhanov.TwPersonaAI.provider.model.AiAnswerEntity;
import com.ukhanov.TwPersonaAI.app.service.AiAnswerReadService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
public class AiAnswerSseController {

    private final AiAnswerReadService aiAnswerReadService;

    public AiAnswerSseController(AiAnswerReadService aiAnswerReadService) {
        this.aiAnswerReadService = aiAnswerReadService;
    }

    @GetMapping(value = "/api/ai/answers/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<AiAnswerEntity>> streamAnswers() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> aiAnswerReadService.getLast15Answers());
    }

    @GetMapping("/api/ai/answers")
    public List<AiAnswerEntity> getLastAnswers() {
        return aiAnswerReadService.getLast15Answers();
    }
}
