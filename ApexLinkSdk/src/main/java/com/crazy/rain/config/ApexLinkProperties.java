package com.crazy.rain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: ApexLinkProperties
 * @Description: api开放平台配置文件属性
 * @author: CrazyRain
 * @date: 2024/4/28 上午7:55
 */
@ConfigurationProperties("apex.link")
@Data
public class ApexLinkProperties {

    private String secretId;

    private String secretKey;

}
