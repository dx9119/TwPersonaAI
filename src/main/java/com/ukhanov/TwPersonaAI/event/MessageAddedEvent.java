package com.ukhanov.TwPersonaAI.event;

import com.ukhanov.TwPersonaAI.message.model.IMessage;

public class MessageAddedEvent {
    private final IMessage message;

    public MessageAddedEvent(IMessage message) {
        this.message = message;
    }

    public IMessage getMessage() {
        return message;
    }
}
