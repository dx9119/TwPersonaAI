package com.ukhanov.TwPersonaAI.provider.service;

import com.ukhanov.TwPersonaAI.app.service.AppSettingService;
import com.ukhanov.TwPersonaAI.context.ContextService;
import com.ukhanov.TwPersonaAI.event.AiAnswerDoneEvent;
import com.ukhanov.TwPersonaAI.provider.model.AiAnswerEntity;
import com.ukhanov.TwPersonaAI.provider.model.ChatReplyPromptEntity;
import com.ukhanov.TwPersonaAI.provider.repository.AiAnswerRepository;
import com.ukhanov.TwPersonaAI.app.repository.ChatReplyPromptRepository;
import com.ukhanov.TwPersonaAI.message.model.MessageEntity;
import com.ukhanov.TwPersonaAI.message.service.MessageFetchService;
import com.ukhanov.TwPersonaAI.utils.LogService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGenerationService {

    private final ChatClient chatClient;
    private final MessageFetchService messageFetchService;
    private final ContextService contextService;
    private final AiAnswerRepository aiAnswerRepository;
    private final ApplicationEventPublisher publisher;
    private final AppSettingService appSettingService;
    private final ChatReplyPromptRepository chatReplyPromptRepository;
    private final LogService logService;

    public ChatGenerationService(ChatClient chatClient,
                                 MessageFetchService messageFetchService,
                                 ContextService contextService,
                                 AiAnswerRepository aiAnswerRepository,
                                 ApplicationEventPublisher publisher,
                                 AppSettingService appSettingService,
                                 ChatReplyPromptRepository chatReplyPromptRepository,
                                 LogService logService) {
        this.chatClient = chatClient;
        this.messageFetchService = messageFetchService;
        this.contextService = contextService;
        this.aiAnswerRepository = aiAnswerRepository;
        this.publisher = publisher;
        this.appSettingService = appSettingService;
        this.chatReplyPromptRepository = chatReplyPromptRepository;
        this.logService = logService;
    }

    public void generateReply() {

        // если уже идёт генерация — выходим
        if (LmStudioState.isBusy()) return;
        LmStudioState.setBusy(true);

        try {
            List<MessageEntity> unchecked = fetchUncheckedMessages();
            if (unchecked.isEmpty()) return;

            List<Message> context = buildContext(unchecked);
            String prompt = loadPrompt();

            String aiResponse = callLlm(prompt, context);
            if (aiResponse == null) return;

            AiAnswerEntity entity = saveAnswer(prompt, aiResponse);
            publishEvent(entity, unchecked);

        } finally {
            LmStudioState.setBusy(false);
        }
    }

    private List<MessageEntity> fetchUncheckedMessages() {
        // получаем непрочитанные сообщения
        return messageFetchService.fetchLastUnchecked();
    }

    private List<Message> buildContext(List<MessageEntity> messages) {
        // собираем список имён
        List<String> names = messages.stream()
                .map(MessageEntity::getName)
                .distinct()
                .toList();

        // строим контекст для LLM
        return contextService.buildChatContextAboutUsers(names);
    }

    private String loadPrompt() {
        // загружаем промпт из БД
        return chatReplyPromptRepository.findById(1L)
                .map(ChatReplyPromptEntity::getPrompt)
                .orElse("Just a kind assistant.");
    }

    private String callLlm(String prompt, List<Message> contextMessages) {
        // замеряем время ответа
        long start = System.currentTimeMillis();

        String response = chatClient
                .prompt()
                .system(prompt)
                .messages(contextMessages)
                .call()
                .content();

        long duration = System.currentTimeMillis() - start;

        // если ответ слишком долгий — перегрузка
        if (duration > 8000) {
            logService.write("AI", "Превышено время ожидания ответа от LLM");
            return null;
        }

        return response;
    }

    private AiAnswerEntity saveAnswer(String prompt, String aiResponse) {
        // сохраняем ответ в БД
        AiAnswerEntity entity = new AiAnswerEntity(prompt, aiResponse);
        aiAnswerRepository.save(entity);
        return entity;
    }

    private void publishEvent(AiAnswerEntity entity, List<MessageEntity> unchecked) {
        // отправляем событие о готовности ответа
        publisher.publishEvent(
                new AiAnswerDoneEvent(
                        entity.getId(),
                        unchecked.stream().map(MessageEntity::getId).toList()
                )
        );
    }

    @Scheduled(fixedRate = 5000)
    public void tryGenerateReply() {
        // проверяем включена ли генерация
        if (!appSettingService.isChatReplyEnabled()) return;

        // если не занято — запускаем
        if (!LmStudioState.isBusy()) generateReply();
    }
}
