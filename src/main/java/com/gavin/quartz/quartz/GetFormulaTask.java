package com.gavin.quartz.common;

import com.gavin.quartz.quartz.DynamicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author jiwen.cao
 * @Date 2021/6/2
 * @Description 服务启动自动执行任务
 */
@Component
public class GetFormulaTask implements ApplicationRunner {

    @Autowired
    private DynamicTask task;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        task.startCron();
    }
}
