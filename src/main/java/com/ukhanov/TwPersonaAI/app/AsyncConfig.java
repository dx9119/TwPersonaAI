package com.ukhanov.TwPersonaAI.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "singleExecutor")
    public Executor singleExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);      // один поток
        executor.setMaxPoolSize(1);       // не больше одного
        executor.setQueueCapacity(1000);  // очередь задач
        executor.setThreadNamePrefix("analysis-");
        executor.initialize();
        return executor;
    }
}

