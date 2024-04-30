package com.crazy.rain.dubboService.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.crazy.rain.dubboService.InterfaceDetailsInterface;
import com.crazy.rain.model.entity.InterfaceCallRecord;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.model.entity.User;
import com.crazy.rain.service.InterfaceCallRecordService;
import com.crazy.rain.service.InterfaceInfoService;
import com.crazy.rain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @ClassName: interfaceDetailsInterfaceImp
 * @Description: 接口详情
 * @author: CrazyRain
 * @date: 2024/4/28 下午1:56
 */
@DubboService
public class InterfaceDetailsInterfaceImp implements InterfaceDetailsInterface {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    @Resource
    private InterfaceCallRecordService interfaceCallRecordService;

    @Resource
    private UserService userService;

    @Override
    public InterfaceInfo verifyIfItExists(String path, String host, String method) {
        LambdaQueryWrapper<InterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper.eq(InterfaceInfo::getUrl, path)
                .eq(InterfaceInfo::getMethod, method)
                .eq(InterfaceInfo::getHost, host);
        return interfaceInfoService.getOne(interfaceInfoLambdaQueryWrapper);
    }

    @Override
    public boolean increaseNumberCalls(Long interfaceId, Long userId) {
        LambdaQueryWrapper<InterfaceCallRecord> interfaceCallRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceCallRecordLambdaQueryWrapper.eq(InterfaceCallRecord::getInterfaceId, interfaceId)
                .eq(InterfaceCallRecord::getUserId, userId);
        InterfaceCallRecord interfaceCallRecord = interfaceCallRecordService.getOne(interfaceCallRecordLambdaQueryWrapper);
        boolean res;
        if (interfaceCallRecord != null) {
            interfaceCallRecord.setNumbercalls(interfaceCallRecord.getNumbercalls());
            res = interfaceCallRecordService.updateById(interfaceCallRecord);
        } else {
            InterfaceCallRecord newinterfaceCallRecord = new InterfaceCallRecord();
            newinterfaceCallRecord.setInterfaceId(interfaceId);
            newinterfaceCallRecord.setUserId(userId);
            res = interfaceCallRecordService.save(newinterfaceCallRecord);
        }
        if (res) {
            User user = userService.getById(userId);
            user.setWallet(user.getWallet() - 1);
            userService.updateById(user);
            return true;
        }
        return false;
    }
}
