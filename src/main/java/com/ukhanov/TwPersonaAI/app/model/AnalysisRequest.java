package com.ukhanov.TwPersonaAI.app.model;

import java.time.LocalDateTime;

public record AnalysisRequest(
        String systemPrompt,
        LocalDateTime from,
        LocalDateTime to,
        int depth
) {}
