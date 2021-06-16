package com.gavin.quartz.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Scope(value = "singleton")
@Component
@EnableScheduling
public class DynamicTaskScheduler {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

//        return new ThreadPoolTaskScheduler();

        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(100);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }
}
