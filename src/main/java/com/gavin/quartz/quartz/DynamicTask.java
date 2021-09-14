package com.gavin.quartz.quartz;

import com.gavin.quartz.common.RedisUtils;
import com.gavin.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private QuartzService quartzService;

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;


    public static final String TASK = "task";
    public static final String TASK_CRON = "sync_cron";

    public static ConcurrentHashMap<String, ScheduledFuture> map = new ConcurrentHashMap<>();

    public void startCron() {
        try {
            while (true) {
                // 查询所有task集合
                List<Object> list = redisUtils.lGet(TASK, 0, -1);
                if (CollectionUtils.isEmpty(list)) {
                    log.info("没有任务 休息10s");
                    Thread.sleep(1000 * 10L);
                } else {
                    for (Object obj : list) {
                        if (Objects.isNull(obj)) {
                            continue;
                        }
                        String taskNo = String.valueOf(obj);
                        ScheduledFuture future = map.get(taskNo);
                        if (Objects.isNull(future)) {
                            // 根据task名称获取任务执行规则cron
                            Object o = redisUtils.get(String.format("%s_%s", TASK_CRON, taskNo));
                            String cron = Objects.isNull(o) ? null : o.toString();
                            if (StringUtils.isBlank(cron)) {
                                continue;
                            }
                            Thread thread = new Thread(() -> quartzService.dealWithWork(taskNo));
                            future = threadPoolTaskScheduler.schedule(thread, new CronTrigger(cron));
                            map.put(taskNo, future);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("定时任务异常", e);
        }
    }

    /**
     * 停止任务
     * @param taskNo 任务编号
     */
    public void stopCron(String taskNo){
        log.info("停止{}的定时任务", taskNo);
        // 删除任务列表中的taskNo
        redisUtils.ldel(TASK, taskNo);
        // 删除taskNo对应的执行规则
        redisUtils.del(String.format("%s_%s", TASK_CRON, taskNo));
        // 停止执行任务并移出任务集合
        ScheduledFuture future = map.get(taskNo);
        cancelFuture(future);
        map.remove(taskNo);
    }

    /**
     * 添加任务
     * @param taskNo 任务编号
     * @param cron 任务执行规则
     */
    public void jobAdd(String taskNo, String cron){
        log.info("添加{}的定时任务，执行规则:{}", taskNo, cron);

        List<String> jobList = redisUtils.lGetString(TASK, 0, -1);
        if (CollectionUtils.isEmpty(jobList) || !jobList.contains(taskNo)) {
            redisUtils.lSet(TASK, taskNo);
        }
        redisUtils.set(String.format("%s_%s", TASK_CRON, taskNo), cron);
        /*
        // 如果有起止时间限制 则根据taskNo获取起止时间
        Date startTime = null;
        Date endTime = null;
        if (endTime.getTime() > System.currentTimeMillis()) {
            redisUtils.expire(String.format("%s_%s", SYNC_CRON, taskNo),
                    (endTime.getTime() - System.currentTimeMillis()) / 1000L);
        }*/
    }

    /**
     * 结束任务
     * @param future
     */
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
