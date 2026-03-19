package com.ukhanov.TwPersonaAI.provider.service;

import com.ukhanov.TwPersonaAI.context.ContextService;
import com.ukhanov.TwPersonaAI.provider.model.LlmTaskDone;
import com.ukhanov.TwPersonaAI.provider.repository.AiAnswerRepository;
import com.ukhanov.TwPersonaAI.provider.repository.LlmTaskDoneRepository;
import com.ukhanov.TwPersonaAI.message.service.MessageFetchService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMessageAnalysisService {

    private final ChatClient chatClient;
    private final ContextService contextService;
    private final LlmTaskDoneRepository llmTaskDoneRepository;

    public UserMessageAnalysisService(ChatClient chatClient,
                                      MessageFetchService messageFetchService,
                                      ContextService contextService,
                                      AiAnswerRepository aiAnswerRepository,
                                      ApplicationEventPublisher publisher,
                                      LlmTaskDoneRepository llmTaskDoneRepository) {
        this.chatClient = chatClient;
        this.contextService = contextService;
        this.llmTaskDoneRepository = llmTaskDoneRepository;
    }

    public void analyze(String id,
                        String systemPrompt,
                        LocalDateTime from,
                        LocalDateTime to,
                        int depth) {

        Map<String, String> preparedContext =
                contextService.buildChatContextForAnalyze(from, to, depth);

        for (Map.Entry<String, String> entry : preparedContext.entrySet()) {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Задача анализа прервана");
                return;
            }

            String username = entry.getKey();
            String combinedMessages = entry.getValue();

            // Конвертация строки в список UserMessage (костыль)
            List<Message> contextMessages = convertToUserMessages(combinedMessages);

            String aiResponse = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .messages(contextMessages)
                    .call()
                    .content();

            LlmTaskDone task = new LlmTaskDone(
                    id,
                    username,
                    combinedMessages,
                    systemPrompt,
                    aiResponse
            );

            llmTaskDoneRepository.save(task);
        }
    }

    public List<LlmTaskDone> getResultsByTaskId(String taskId) {
        return llmTaskDoneRepository.findByTaskId(taskId);
    }

    /**
     * Превращает строку "msg1 | msg2 | msg3"
     * в список UserMessage.
     */
    private List<Message> convertToUserMessages(String combinedMessages) {
        return Arrays.stream(combinedMessages.split("\\|"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(UserMessage::new)
                .map(m -> (Message) m)
                .collect(Collectors.toList());
    }
}
