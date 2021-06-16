package com.gavin.quartz.quartz;

import com.gavin.quartz.common.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
public class DynamicTask {

    public static ConcurrentHashMap<String, ScheduledFuture> map = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture future;

    @Autowired
    private RedisUtils redisUtils;

    public static final String LABEL_JOB = "label_job";

    public void startCron() {
        String cron = "0/5 * * * * ?";
        try {
            while (true) {
                List<Object> list = redisUtils.lGet(LABEL_JOB, 0, -1);
                if (CollectionUtils.isEmpty(list)) {
                    log.info("没有任务 休息10s");
                    Thread.sleep(1000 * 10L);
                } else {
                    for (Object obj : list) {
                        String key = String.valueOf(obj);
                        ScheduledFuture future = map.get(key);
                        if (Objects.isNull(future)) {
                            future = threadPoolTaskScheduler.schedule(new Thread(() -> log.info("执行定时任务{}", key)), new CronTrigger(cron));
                            map.put(key, future);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("定时任务异常", e);
        }
    }

    public void stopCron(String sync){
        log.info("停止标签{}的定时任务", sync);
        redisUtils.ldel(LABEL_JOB, sync);
        ScheduledFuture future = map.get(sync);
        cancelFuture(future);
        map.remove(sync);
    }

    public void jobAdd(String sync){
        List<String> jobList = redisUtils.lGetString(LABEL_JOB, 0, -1);
        if (CollectionUtils.isEmpty(jobList) || !jobList.contains(sync)) {
            redisUtils.lSet(LABEL_JOB, sync);
        }
    }

    private void cancelFuture(ScheduledFuture future){
        if (Objects.isNull(future)) {
            return;
        }
        future.cancel(true);
        // 查看任务是否在正常执行之前结束,正常返回true
        boolean cancelled = future.isCancelled();
        while (!cancelled) {
            future.cancel(true);
        }
    }

}
