package com.gavin.quartz.service;

/**
 * @Author jiwen.cao
 * @Date 2021/9/14
 * @Description
 */
public interface QuartzService {

    /**
     * 处理业务
     * @param taskNo 任务编号
     */
    void dealWithWork(String taskNo);
}
