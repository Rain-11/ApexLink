package com.crazy.rain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crazy.rain.model.dto.interface_info.InterfaceAddDto;
import com.crazy.rain.model.dto.interface_info.InterfaceQueryDto;
import com.crazy.rain.model.dto.interface_info.InterfaceUpdateDto;
import com.crazy.rain.model.entity.InterfaceInfo;


/**
 * @author CrazyRain
 * @description 针对表【interface_info】的数据库操作Service
 * @createDate 2024-04-20 13:22:22
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 添加接口
     *
     * @param interfaceAddDto 接口基本西信息
     */
    void addInterface(InterfaceAddDto interfaceAddDto);

    LambdaQueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceQueryDto interfaceQueryDto);

    /**
     * 添加接口信息
     */
    Boolean modifyInterfaceInformation(InterfaceUpdateDto interfaceUpdateDto);
}
