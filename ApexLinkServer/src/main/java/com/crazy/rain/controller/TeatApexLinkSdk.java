package com.crazy.rain.controller;

import cn.hutool.http.HttpResponse;
import com.crazy.rain.common.ErrorCode;
import com.crazy.rain.common.ResultUtil;
import com.crazy.rain.config.ApexLinkClient;
import com.crazy.rain.exception.ThrowUtils;
import com.crazy.rain.model.dto.sdk.ExecutionGoalsDto;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.service.InterfaceInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("/invoke")
@Slf4j
@Tag(name = "测试sdk")
public class TeatApexLinkSdk {

    @Resource
    private ApexLinkClient apexLinkClient;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @PostMapping("/handler")
    public String invoke(@RequestBody ExecutionGoalsDto executionGoalsDto) {
        ThrowUtils.throwIf(executionGoalsDto == null, ErrorCode.PARAMS_ERROR, "未接收到请求参数");
        Long id = executionGoalsDto.getId();
        String body = executionGoalsDto.getBody();
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "请求参数为空");
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(executionGoalsDto.getId());
        HttpResponse httpResponse = null;
        try {
            httpResponse = apexLinkClient.invokeHandler(interfaceInfo.getUrl(),
                    body, interfaceInfo.getHost(), interfaceInfo.getMethod());
        } catch (Exception e) {
            return ResultUtil.error(ErrorCode.SYSTEM_ERROR).toString();
        } finally {
            if (httpResponse != null) {
                httpResponse.close();
            }
        }
        return httpResponse.body();
    }
}
