package com.crazy.rain.dubboService.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crazy.rain.common.ErrorCode;
import com.crazy.rain.dubboService.UserInterface;
import com.crazy.rain.exception.ThrowUtils;
import com.crazy.rain.model.entity.User;
import com.crazy.rain.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @ClassName: UserInterfaceImpl
 * @Description: 用户
 * @author: CrazyRain
 * @date: 2024/4/28 下午1:55
 */
@DubboService
public class UserInterfaceImpl implements UserInterface {

    @Resource
    private UserService userService;

    @Override
    public User verifyUserAccessKey(String secretId) {
        ThrowUtils.throwIf(StringUtils.isBlank(secretId), ErrorCode.PARAMS_ERROR, "请求参数不存在");
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getSecretId, secretId);
        return userService.getOne(userLambdaQueryWrapper);
    }

    @Override
    public boolean deductionAmount() {
        return false;
    }
}
