package com.crazy.rain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class InterfaceProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InterfaceProducerApplication.class, args);
    }
}