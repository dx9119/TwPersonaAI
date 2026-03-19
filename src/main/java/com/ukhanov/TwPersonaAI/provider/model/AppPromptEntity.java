package com.ukhanov.TwPersonaAI.provider.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_prompt")
public class AppPromptEntity {

    @Id
    private Long id = 1L;

    @Column(name = "prompt", columnDefinition = "TEXT")
    private String prompt;

    public AppPromptEntity() {}

    public AppPromptEntity(String prompt) {
        this.id = 1L;
        this.prompt = prompt;
    }

    public Long getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
