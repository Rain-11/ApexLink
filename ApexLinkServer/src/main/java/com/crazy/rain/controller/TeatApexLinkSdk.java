package com.crazy.rain.controller;

import cn.hutool.http.HttpResponse;
import com.crazy.rain.config.ApexLinkClient;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: TeatApexLinkSdk
 * @Description: 测试sdk
 * @author: CrazyRain
 * @date: 2024/4/28 上午8:29
 */
@RestController
@RequestMapping("/test")
@Slf4j
@Tag(name = "测试sdk")
public class TeatApexLinkSdk {

    @Resource
    private ApexLinkClient apexLinkClient;

    @GetMapping("/username")
    public String test(String username) {
        HttpResponse response = apexLinkClient.getUserName(username);
        return response.body();
    }
}
