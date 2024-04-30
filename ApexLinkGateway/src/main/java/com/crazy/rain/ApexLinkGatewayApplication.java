package com.crazy.rain;


import com.crazy.rain.config.CustomGlobalFilter;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class ApexLinkGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApexLinkGatewayApplication.class, args);
    }
    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }
}