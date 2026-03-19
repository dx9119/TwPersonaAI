package com.ukhanov.TwPersonaAI.message.model;

import java.time.LocalDateTime;

public interface IMessage {
    LocalDateTime getTimestamp();
    MessageSource getSource();
    String getName();
    String getMessage();
    Boolean isAnswered();
    void setAnsweredStatus(Boolean checked);


}
