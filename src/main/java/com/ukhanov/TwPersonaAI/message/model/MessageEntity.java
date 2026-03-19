package com.ukhanov.TwPersonaAI.message.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class MessageEntity implements IMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private Boolean answered;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageSource source;

    public MessageEntity() {}

    public MessageEntity(Long id, String name, String message, Boolean answered, LocalDateTime timestamp, MessageSource source) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.answered = answered;
        this.timestamp = timestamp;
        this.source = source;
    }

    public Long getId() {
        return id;
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
    public void setAnsweredStatus(Boolean answered) {
        this.answered = answered;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSource(MessageSource source) {
        this.source = source;
    }


}
