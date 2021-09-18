package com.gavin.quartz.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Slf4j
@Configuration
public class XxlJobConfig {

    @Value(value = "${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value(value = "${xxl.job.accessToken}")
    private String accessToken;

    @Value(value = "${xxl.job.executor.appname}")
    private String appname;

    @Value(value = "${xxl.job.executor.address}")
    private String address;

    @Value(value = "${xxl.job.executor.ip}")
    private String ip;

    @Value(value = "${xxl.job.executor.port}")
    private int port;

    @Value(value = "${xxl.job.executor.logpath}")
    private String logPath;

    @Value(value = "${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }
}
