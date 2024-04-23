package com.crazy.rain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crazy.rain.common.ErrorCode;
import com.crazy.rain.common.InterfaceStatusEnum;
import com.crazy.rain.common.MethodEnum;
import com.crazy.rain.constant.CommonConstant;
import com.crazy.rain.converter.InterfaceConverter;
import com.crazy.rain.exception.ThrowUtils;
import com.crazy.rain.mapper.InterfaceInfoMapper;
import com.crazy.rain.model.dto.interface_info.InterfaceAddDto;
import com.crazy.rain.model.dto.interface_info.InterfaceQueryDto;
import com.crazy.rain.model.dto.interface_info.InterfaceStatusDto;
import com.crazy.rain.model.dto.interface_info.InterfaceUpdateDto;
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
        ThrowUtils.throwIf(StringUtils.isBlank(name) || name.length() > 50,
                ErrorCode.NOT_FOUND_ERROR, "接口名为空或接口名称过长");
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
        Integer status = interfaceQueryDto.getStatus();
        InterfaceStatusEnum interfaceStatusEnum = InterfaceStatusEnum.getEnum(status);
        LambdaQueryWrapper<InterfaceInfo> interfaceInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        interfaceInfoLambdaQueryWrapper.eq(id != null, InterfaceInfo::getId, id);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(name), InterfaceInfo::getName, name);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(url), InterfaceInfo::getUrl, url);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(detail), InterfaceInfo::getDetail, detail);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(requestHeader), InterfaceInfo::getRequestHeader, requestHeader);
        interfaceInfoLambdaQueryWrapper.like(StringUtils.isNotBlank(responseHeader), InterfaceInfo::getResponseHeader, responseHeader);
        interfaceInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(method), InterfaceInfo::getMethod, method);
        if (interfaceStatusEnum != null) {
            interfaceInfoLambdaQueryWrapper.eq(InterfaceInfo::getStatus, interfaceStatusEnum.getValue());
        }
        interfaceInfoLambdaQueryWrapper.orderBy(
                SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                i -> sortField
        );
        return interfaceInfoLambdaQueryWrapper;
    }

    @Override
    public Boolean modifyInterfaceInformation(InterfaceUpdateDto interfaceUpdateDto) {
        InterfaceInfo interfaceInfo = interfaceConverter.interfaceInfoConvert(interfaceUpdateDto);
        String name = interfaceInfo.getName();
        if(StringUtils.isNotBlank(name)){
            ThrowUtils.throwIf( name.length() > 50, ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
        Integer status = interfaceInfo.getStatus();
        if (status != null) {
            InterfaceStatusEnum interfaceStatusEnum = InterfaceStatusEnum.getEnum(status);
            interfaceInfo.setStatus(interfaceStatusEnum.getValue());
        }
        String method = interfaceInfo.getMethod();
        if (StringUtils.isNotBlank(method)) {
            MethodEnum methodEnum = MethodEnum.valueOf(method.toUpperCase());
            interfaceInfo.setMethod(methodEnum.getValue());
        }
        return this.updateById(interfaceInfo);
    }

    @Override
    public InterfaceUpdateDto verifyData(InterfaceStatusDto interfaceQueryDto) {
        ThrowUtils.throwIf(interfaceQueryDto == null, ErrorCode.NOT_FOUND_ERROR, "请求参数为空");
        Long interfaceId = interfaceQueryDto.getId();
        ThrowUtils.throwIf(interfaceId == null || interfaceId <= 0, ErrorCode.NOT_FOUND_ERROR, "接口id为空");
        InterfaceUpdateDto interfaceUpdateDto = new InterfaceUpdateDto();
        interfaceUpdateDto.setId(interfaceId);
        return interfaceUpdateDto;
    }
}




