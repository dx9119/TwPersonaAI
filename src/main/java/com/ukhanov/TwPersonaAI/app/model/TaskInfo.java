package com.ukhanov.TwPersonaAI.app.model;

import java.util.concurrent.Future;

public class TaskInfo {
    private final String id;
    private final AnalysisRequest request;
    private volatile TaskStatus status = TaskStatus.QUEUED;
    private Future<?> future;

    public TaskInfo(String id, AnalysisRequest request) {
        this.id = id;
        this.request = request;
    }

    @Override
    public String toString() {
        return status.toString();
    }

    public String getId() {
        return id;
    }

    public AnalysisRequest getRequest() {
        return request;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}
