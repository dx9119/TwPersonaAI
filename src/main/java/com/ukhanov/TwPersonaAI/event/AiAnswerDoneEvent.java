package com.ukhanov.TwPersonaAI.event;

import java.util.List;

public class AiAnswerDoneEvent {
    private final Long answerId;
    private final List<Long> messageIds;

    public AiAnswerDoneEvent(Long answerId, List<Long> messageIds) {
        this.answerId = answerId;
        this.messageIds = messageIds;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public List<Long> getMessageIds() {
        return messageIds;
    }
}

