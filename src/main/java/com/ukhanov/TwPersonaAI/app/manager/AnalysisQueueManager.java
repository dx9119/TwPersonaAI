package com.ukhanov.TwPersonaAI.app.manager;

import com.ukhanov.TwPersonaAI.app.model.AnalysisRequest;
import com.ukhanov.TwPersonaAI.app.model.TaskInfo;
import com.ukhanov.TwPersonaAI.app.model.TaskStatus;
import com.ukhanov.TwPersonaAI.provider.service.UserMessageAnalysisService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class AnalysisQueueManager {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, TaskInfo> tasks = new ConcurrentHashMap<>();
    private final UserMessageAnalysisService analysisService;

    public AnalysisQueueManager(UserMessageAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public String submitTask(AnalysisRequest req) {

        String id = UUID.randomUUID().toString();
        TaskInfo info = new TaskInfo(id, req);
        tasks.put(id, info);

        Future<?> future = executor.submit(() -> {
            try {
                info.setStatus(TaskStatus.RUNNING);
                analysisService.analyze(
                        id,
                        req.systemPrompt(),
                        req.from(),
                        req.to(),
                        req.depth()
                );
                info.setStatus(TaskStatus.DONE);
            } catch (Exception e) {
                info.setStatus(TaskStatus.FAILED);
            }
        });

        info.setFuture(future);
        return id;
    }

    public TaskStatus getStatus(String id) {
        return tasks.get(id).getStatus();
    }

    public boolean cancel(String id) {
        TaskInfo info = tasks.get(id);
        if (info == null) return false;

        boolean cancelled = info.getFuture().cancel(true);
        if (cancelled) info.setStatus(TaskStatus.CANCELLED);

        return cancelled;
    }

    public Map<String, TaskInfo> getAllTasks() {
        return tasks;
    }
}
