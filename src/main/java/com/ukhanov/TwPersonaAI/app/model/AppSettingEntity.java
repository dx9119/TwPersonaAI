package com.ukhanov.TwPersonaAI.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_setting")
public class AppSettingEntity {

    @Id
    private Long id = 1L;

    @Column(name = "enabled", nullable = false)
    private Boolean chatReplyEnabled = false;

    public AppSettingEntity() {}

    public AppSettingEntity(Boolean chatReplyEnabled) {
        this.id = 1L;
        this.chatReplyEnabled = chatReplyEnabled;
    }

    public Long getId() {
        return id;
    }

    public Boolean isEnabled() {
        return chatReplyEnabled;
    }

    public void setChatReplyEnabled(Boolean chatReplyEnabled) {
        this.chatReplyEnabled = chatReplyEnabled;
    }
}
