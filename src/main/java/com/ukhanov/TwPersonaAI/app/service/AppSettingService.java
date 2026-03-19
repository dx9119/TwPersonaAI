package com.ukhanov.TwPersonaAI.app.service;

import com.ukhanov.TwPersonaAI.app.model.AppSettingEntity;

import com.ukhanov.TwPersonaAI.app.repository.AppSettingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AppSettingService {

    private final AppSettingRepository repository;
    private Boolean chatReplyEnabled;

    public AppSettingService(AppSettingRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        AppSettingEntity setting = repository.findById(1L)
                .orElseGet(() -> {
                    AppSettingEntity s = new AppSettingEntity(false);
                    repository.save(s);
                    return s;
                });

        this.chatReplyEnabled = setting.isEnabled();
    }

    public Boolean isChatReplyEnabled() {
        return chatReplyEnabled;
    }

    public void setChatReplyEnabled(boolean chatReplyEnabled) {
        this.chatReplyEnabled = chatReplyEnabled;

        AppSettingEntity entity = repository.findById(1L)
                .orElse(new AppSettingEntity());

        entity.setChatReplyEnabled(chatReplyEnabled);
        repository.save(entity);
    }
}
