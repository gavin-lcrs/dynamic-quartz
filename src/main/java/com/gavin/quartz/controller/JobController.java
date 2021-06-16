package com.gavin.quartz.controller;

import com.gavin.quartz.quartz.DynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author jiwen.cao
 * @Date 2021/6/4
 * @Description
 */
@Slf4j
@RestController
public class JobController {

    @Autowired
    private DynamicTask task;

    @GetMapping("/start")
    public String jobStart(@RequestParam(value = "sync", required = false) String sync) {
        task.jobAdd(sync);
        return "success";
    }

    @GetMapping("/stop")
    public String jobStop(@RequestParam(value = "sync", required = false) String sync) {
        task.stopCron(sync);
        return "success";
    }
}
