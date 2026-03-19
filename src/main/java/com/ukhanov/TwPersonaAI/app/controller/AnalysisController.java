package com.ukhanov.TwPersonaAI.app.controller;

import com.ukhanov.TwPersonaAI.app.manager.AnalysisQueueManager;
import com.ukhanov.TwPersonaAI.app.model.AnalysisRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisQueueManager queueManager;

    public AnalysisController(AnalysisQueueManager queueManager) {
        this.queueManager = queueManager;
    }

    // Главная страница + точка входа для всех состояний
    @GetMapping
    public String mainPage(
            @RequestParam(required = false) String taskId,
            Model model) {

        // Всегда показываем список задач
        model.addAttribute("tasks", queueManager.getAllTasks());

        // Если передан taskId — показываем информацию по нему
        if (taskId != null && !taskId.isBlank()) {
            model.addAttribute("taskId", taskId);
            model.addAttribute("status", queueManager.getStatus(taskId));
        }

        return "analysis";
    }

    @PostMapping("/run")
    public String runTask(
            @ModelAttribute AnalysisRequest req,
            RedirectAttributes redirectAttrs) {

        String taskId = queueManager.submitTask(req);

        redirectAttrs.addFlashAttribute("message", "Задача успешно поставлена в очередь");
        redirectAttrs.addFlashAttribute("messageType", "success");

        return "redirect:/analysis";
    }


    @PostMapping("/cancel/{id}")
    public String cancelTask(
            @PathVariable String id,
            RedirectAttributes redirectAttrs) {

        boolean success = queueManager.cancel(id);

        redirectAttrs.addFlashAttribute("message",
                success ? "Задача успешно отменена" : "Не удалось отменить задачу");
        redirectAttrs.addFlashAttribute("messageType", success ? "success" : "error");
        redirectAttrs.addAttribute("taskId", id);

        return "redirect:/analysis";
    }

}