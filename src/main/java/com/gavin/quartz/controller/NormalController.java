package com.gavin.quartz.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author jiwen.cao
 * @Date 2021/9/22
 * @Description
 */
@Slf4j
@RestController
public class NormalController {

    @NacosValue(value = "${testmsg}", autoRefreshed = true)
    private String testmsg;


    @GetMapping("/aaa")
    public String getNacosTest(){
        log.info("testmsg={}", testmsg);
        return "success";
    }
}
