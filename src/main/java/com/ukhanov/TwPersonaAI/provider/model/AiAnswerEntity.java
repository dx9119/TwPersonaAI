package com.ukhanov.TwPersonaAI.provider.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_answers")
public class AiAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AiAnswerEntity() {}

    public AiAnswerEntity(String prompt, String answer) {
        this.prompt = prompt;
        this.answer = answer;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getAnswer() {
        return answer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
