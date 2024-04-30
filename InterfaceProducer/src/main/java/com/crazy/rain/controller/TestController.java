package com.crazy.rain.controller;

import com.crazy.rain.common.BaseResponse;
import com.crazy.rain.common.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Description: 测试接口
 * @author: CrazyRain
 * @date: 2024/4/26 上午9:29
 */
@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 返回用户名
     *
     * @param username 用户名
     * @return 用户名
     */
    @GetMapping("/getUserName")
    public BaseResponse<String> getUserName(String username) {
        return ResultUtil.success(username);
    }

}
