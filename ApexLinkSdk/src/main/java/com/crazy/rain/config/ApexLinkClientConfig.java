package com.crazy.rain.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ApexLinkClientConfig
 * @Description: sdk客户端篇配置类
 * @author: CrazyRain
 * @date: 2024/4/27 下午9:02
 */
@Configuration
@EnableConfigurationProperties(ApexLinkProperties.class)
public class ApexLinkClientConfig {

    private final ApexLinkProperties apexLinkProperties;

    public ApexLinkClientConfig(ApexLinkProperties apexLinkProperties) {
        this.apexLinkProperties = apexLinkProperties;
    }

    @Bean
    public ApexLinkClient apexLinkClient() {
        return new ApexLinkClient(apexLinkProperties.getSecretId(), apexLinkProperties.getSecretKey());
    }

}
