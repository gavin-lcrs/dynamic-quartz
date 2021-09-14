package com.gavin.quartz.service.impl;

import com.gavin.quartz.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author jiwen.cao
 * @Date 2021/9/14
 * @Description
 */
@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {

    @Override
    public void dealWithWork(String taskNo) {
        log.info("处理具体业务逻辑, 任务编号：{}", taskNo);
    }
}
