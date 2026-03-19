package com.ukhanov.TwPersonaAI.app.controller.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AiAnswerViewController {

    @GetMapping("/ai/answers")
    public String aiAnswersPage() {
        return "ai_answers";
    }
}
