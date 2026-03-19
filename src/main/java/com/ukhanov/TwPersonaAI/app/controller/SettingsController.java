package com.ukhanov.TwPersonaAI.app.controller;

import com.ukhanov.TwPersonaAI.app.service.AppSettingService;
import com.ukhanov.TwPersonaAI.app.service.PromptSettingsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final AppSettingService appSettingService;
    private final PromptSettingsService promptSettingsService;

    public SettingsController(AppSettingService appSettingService,
                              PromptSettingsService promptSettingsService) {
        this.appSettingService = appSettingService;
        this.promptSettingsService = promptSettingsService;
    }

    @GetMapping
    public String getSettings(Model model) {
        model.addAttribute("enabled", appSettingService.isChatReplyEnabled());
        model.addAttribute("prompt", promptSettingsService.getReplyPrompt());
        return "settings";
    }

    @PostMapping("/toggle")
    public String toggleAutoReply(@RequestParam(value = "enabled", required = false) String enabled) {
        boolean flag = enabled != null;
        appSettingService.setChatReplyEnabled(flag);
        return "redirect:/settings";
    }

    @PostMapping("/prompt")
    public String savePrompt(@RequestParam("prompt") String prompt) {
        promptSettingsService.setReplyPrompt(prompt);
        return "redirect:/settings";
    }
}
