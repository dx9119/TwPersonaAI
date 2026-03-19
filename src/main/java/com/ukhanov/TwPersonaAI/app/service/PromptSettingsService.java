package com.ukhanov.TwPersonaAI.app.service;

import com.ukhanov.TwPersonaAI.provider.model.ChatReplyPromptEntity;
import com.ukhanov.TwPersonaAI.app.repository.ChatReplyPromptRepository;
import org.springframework.stereotype.Service;

@Service
public class PromptSettingsService {

    private final ChatReplyPromptRepository repository;

    public PromptSettingsService(ChatReplyPromptRepository repository) {
        this.repository = repository;
    }

    public String getReplyPrompt() {
        return repository.findById(1L)
                .map(ChatReplyPromptEntity::getPrompt)
                .orElseGet(() -> {
                    ChatReplyPromptEntity created = new ChatReplyPromptEntity("Just a kind assistant.");
                    repository.save(created);
                    return created.getPrompt();
                });
    }

    public void setReplyPrompt(String newPrompt) {
        ChatReplyPromptEntity entity = repository.findById(1L)
                .orElseGet(() -> {
                    ChatReplyPromptEntity created = new ChatReplyPromptEntity("");
                    repository.save(created);
                    return created;
                });

        entity.setPrompt(newPrompt);
        repository.save(entity);
    }
}

