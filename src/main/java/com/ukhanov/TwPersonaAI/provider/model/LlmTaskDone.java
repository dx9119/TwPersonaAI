package com.ukhanov.TwPersonaAI.provider.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "llm_task_done")
public class LlmTaskDone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String user;

    @Column(nullable = false)
    private String taskId;

    @Lob
    @Column(nullable = false)
    private String inputData;

    @Lob
    @Column(nullable = false)
    private String promptText;

    @Lob
    @Column(nullable = false)
    private String resultText;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public LlmTaskDone() {}

    public LlmTaskDone(String taskId, String user, String inputData, String promptText, String resultText) {
        this.taskId = taskId;
        this.user = user;
        this.inputData = inputData;
        this.promptText = promptText;
        this.resultText = resultText;
    }

    public Long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInputData() {
        return inputData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
