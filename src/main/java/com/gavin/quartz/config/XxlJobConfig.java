package com.gavin.quartz.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
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

    @NacosValue(value = "${xxl.job.admin.addresses}", autoRefreshed = true)
    private String adminAddresses;

    @NacosValue(value = "${xxl.job.accessToken}", autoRefreshed = true)
    private String accessToken;

    @NacosValue(value = "${xxl.job.executor.appname}", autoRefreshed = true)
    private String appname;

    @NacosValue(value = "${xxl.job.executor.address}", autoRefreshed = true)
    private String address;

    @NacosValue(value = "${xxl.job.executor.ip}", autoRefreshed = true)
    private String ip;

    @NacosValue(value = "${xxl.job.executor.port}", autoRefreshed = true)
    private int port;

    @NacosValue(value = "${xxl.job.executor.logpath}", autoRefreshed = true)
    private String logPath;

    @NacosValue(value = "${xxl.job.executor.logretentiondays}", autoRefreshed = true)
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
