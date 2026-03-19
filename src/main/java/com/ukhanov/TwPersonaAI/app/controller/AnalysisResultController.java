package com.ukhanov.TwPersonaAI.app.controller;

import com.ukhanov.TwPersonaAI.provider.model.LlmTaskDone;
import com.ukhanov.TwPersonaAI.provider.service.UserMessageAnalysisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/analysis/results")
public class AnalysisResultController {

    private final UserMessageAnalysisService analysisService;

    public AnalysisResultController(UserMessageAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // Страница поиска + возможность передать taskId через параметр
    @GetMapping
    public String searchPage(@RequestParam(required = false) String taskId,
                             Model model) {

        if (taskId != null && !taskId.isBlank()) {
            List<LlmTaskDone> results = analysisService.getResultsByTaskId(taskId);
            model.addAttribute("results", results);
            model.addAttribute("taskId", taskId);
        } else {
            model.addAttribute("results", List.of());
        }

        return "analysis-results";
    }


    // Обработка формы поиска
    @PostMapping
    public String findByForm(@RequestParam String taskId) {
        return "redirect:/analysis/results?taskId=" + taskId;
    }
}
