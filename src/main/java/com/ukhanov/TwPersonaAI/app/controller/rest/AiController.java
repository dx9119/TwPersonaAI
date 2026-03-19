package com.ukhanov.TwPersonaAI.app.controller.rest;

import com.ukhanov.TwPersonaAI.provider.service.LmStudioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final LmStudioService service;

    public AiController(LmStudioService service) {
        this.service = service;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        return service.ask(q);
    }
}

