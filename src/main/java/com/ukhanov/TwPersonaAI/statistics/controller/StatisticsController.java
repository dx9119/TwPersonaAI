package com.ukhanov.TwPersonaAI.statistics.controller;

import com.ukhanov.TwPersonaAI.statistics.service.StatisticsService;
import com.ukhanov.TwPersonaAI.statistics.model.AuthorStats;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    // Страница для OBS
    @GetMapping("/stats/top-authors")
    public String topAuthorsPage() {
        return "stats/top-authors";
    }

    // Данные для автообновления
    @GetMapping("/stats/top-authors/data")
    @ResponseBody
    public List<AuthorStats> topAuthorsData() {
        return statisticsService.getTopAuthorsLast24Hours();
    }
}
