package com.ukhanov.TwPersonaAI.chat.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.ukhanov.TwPersonaAI.chat.twitch.model.TwitchMessage;
import com.ukhanov.TwPersonaAI.message.model.IMessage;
import com.ukhanov.TwPersonaAI.message.service.MessageBuffer;
import com.ukhanov.TwPersonaAI.utils.LogService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwitchChatClient {

    @Value("${twitch.oauth}")
    private String oauthToken;

    @Value("${twitch.channel}")
    private String channel;

    private final MessageBuffer buffer;
    private final LogService logService;
    private TwitchClient client;

    public TwitchChatClient(MessageBuffer buffer, LogService logService) {
        this.buffer = buffer;
        this.logService = logService;
    }

    @PostConstruct
    public void start() {

        OAuth2Credential credential = new OAuth2Credential("twitch", oauthToken);

        client = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();

        // Ловим ошибки авторизации
        client.getEventManager().onEvent(IRCMessageEvent.class, event -> {
            String raw = event.getRawMessage();

            if (raw != null && raw.contains("Login authentication failed")) {
                logService.write("TWITCH", "Ошибка авторизации: неверный OAuth токен");
            }
        });

        try {
            client.getChat().joinChannel(channel);
            logService.write("TWITCH", "Подключено к каналу: " + channel);
        } catch (Exception e) {
            logService.write("TWITCH", "Не удалось подключиться к каналу: " + e.getMessage());
        }

        client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            logService.write("TWITCH", "Получено событие ChannelMessageEvent");

            IMessage msg = new TwitchMessage(
                    event.getUser().getName(),
                    event.getMessage()
            );

            buffer.add(msg);
        });
    }
}
