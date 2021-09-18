package com.gavin.quartz.quartz;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author jiwen.cao
 * @Date 2021/9/18
 * @Description
 */
@Slf4j
@Component
public class JobExecutor {

    @Autowired
    private DynamicTask task;

    @XxlJob("dynamicTask")
    public void dynamicTask(){
        log.info("调度中心执行任务");
        task.xxljobExecutor();
    }
}
