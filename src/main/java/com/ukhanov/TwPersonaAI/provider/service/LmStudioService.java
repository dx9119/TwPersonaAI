package com.ukhanov.TwPersonaAI.provider.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LmStudioService {

    private final ChatClient chatClient;

    public LmStudioService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String prompt) {
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
}
