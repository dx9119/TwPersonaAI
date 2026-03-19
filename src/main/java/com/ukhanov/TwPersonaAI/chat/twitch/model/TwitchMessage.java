package com.ukhanov.TwPersonaAI.chat.twitch.model;

import com.ukhanov.TwPersonaAI.message.model.IMessage;
import com.ukhanov.TwPersonaAI.message.model.MessageSource;

import java.time.LocalDateTime;

public class TwitchMessage implements IMessage {

    private final String name;
    private final String message;
    private Boolean answered = false;
    private final LocalDateTime timestamp;
    private final MessageSource source;

    public TwitchMessage(String name, String message) {
        this.name = name;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.source = MessageSource.TWITCH;
    }

    public TwitchMessage(String name, String message, Boolean answered, LocalDateTime timestamp, MessageSource source) {
        this.name = name;
        this.message = message;
        this.answered = answered;
        this.timestamp = timestamp;
        this.source = source;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public MessageSource getSource() {
        return source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Boolean isAnswered() {
        return answered;
    }

    @Override
    public void setAnsweredStatus(Boolean checked) {
        this.answered = checked;
    }
}
