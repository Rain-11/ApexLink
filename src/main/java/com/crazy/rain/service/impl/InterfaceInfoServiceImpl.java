package com.crazy.rain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crazy.rain.common.ErrorCode;
import com.crazy.rain.common.MethodEnum;
import com.crazy.rain.constant.CommonConstant;
import com.crazy.rain.converter.InterfaceConverter;
import com.crazy.rain.exception.ThrowUtils;
import com.crazy.rain.mapper.InterfaceInfoMapper;
import com.crazy.rain.model.dto.interface_info.InterfaceAddDto;
import com.crazy.rain.model.dto.interface_info.InterfaceQueryDto;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.service.InterfaceInfoService;
import com.crazy.rain.utils.SqlUtils;
import com.crazy.rain.utils.UserInfoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author CrazyRain
 * @description 针对表【interface_info】的数据库操作Service实现
 * @createDate 2024-04-20 13:22:22
 */
@Service
@AllArgsConstructor
@Slf4j
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    private final InterfaceConverter interfaceConverter;

    @Override
    public void addInterface(InterfaceAddDto interfaceAddDto) {
        String name = interfaceAddDto.getName();
        ThrowUtils.throwIf(StringUtils.isBlank(name), ErrorCode.NOT_FOUND_ERROR, "接口名为空");
        String url = interfaceAddDto.getUrl();
        ThrowUtils.throwIf(StringUtils.isBlank(url), ErrorCode.NOT_FOUND_ERROR, "接口地址");
        String method = interfaceAddDto.getMethod();
        MethodEnum methodEnum = MethodEnum.valueOf(method.toUpperCase());
        InterfaceInfo interfaceInfo = interfaceConverter.interfaceInfoConvert(interfaceAddDto);
        interfaceInfo.setMethod(methodEnum.getValue());
        interfaceInfo.setUserId(UserInfoUtil.getUserInfo().getId());
        boolean result = save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "添加接口信息失败");
    }


    @Override
    public LambdaQueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceQueryDto interfaceQueryDto) {
        ThrowUtils.throwIf(interfaceQueryDto == null, ErrorCode.NOT_FOUND_ERROR);
        Long id = interfaceQueryDto.getId();
        String name = interfaceQueryDto.getName();
        String url = interfaceQueryDto.getUrl();
        String detail = interfaceQueryDto.getDetail();
        String requestHeader = interfaceQueryDto.getRequestHeader();
        String responseHeader = interfaceQueryDto.getResponseHeader();
        String method = interfaceQueryDto.getMethod();
        String sortField = interfaceQueryDto.getSortField();
        String sortOrder = interfaceQueryDto.getSortOrder();
        LambdaQueryWrapper<InterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper.eq(id != null, InterfaceInfo::getId, id);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(name), InterfaceInfo::getName, name);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(url), InterfaceInfo::getUrl, url);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(detail), InterfaceInfo::getDetail, detail);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(requestHeader), InterfaceInfo::getRequestHeader, requestHeader);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(responseHeader), InterfaceInfo::getResponseHeader, responseHeader);
        interfaceInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(method), InterfaceInfo::getMethod, method);
        interfaceInfoLambdaQueryWrapper.orderBy(
                SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                i -> sortField
        );
        return interfaceInfoLambdaQueryWrapper;
    }
}



