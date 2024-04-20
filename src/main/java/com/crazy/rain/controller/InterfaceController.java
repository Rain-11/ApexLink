package com.crazy.rain.controller;

import com.crazy.rain.converter.InterfaceConverter;
import com.crazy.rain.service.InterfaceInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@Slf4j
@AllArgsConstructor
@Tag(name = "接口信息控制器")
public class InterfaceController {


    private final InterfaceInfoService interfaceInfoService;
    private final InterfaceConverter interfaceConverter;


}
