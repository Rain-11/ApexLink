package com.crazy.rain.converter;

import com.crazy.rain.model.dto.interface_info.InterfaceAddDto;
import com.crazy.rain.model.dto.interface_info.InterfaceUpdateDto;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.model.vo.InterfaceInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName: InterfaceConverter
 * @Description: 接口详情转换器
 * @author: CrazyRain
 * @date: 2024/4/20 下午1:26
 */
@Mapper(componentModel = "spring")
public interface InterfaceConverter {

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    InterfaceInfo interfaceInfoConvert(InterfaceAddDto interfaceAddDto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    InterfaceInfo interfaceInfoConvert(InterfaceUpdateDto interfaceUpdateDto);


    @Mapping(target = "userName", ignore = true)
    InterfaceInfoVo interfaceInfoVoConvert(InterfaceInfo interfaceInfo);

    List<InterfaceInfoVo> interfaceInfoVoConvert(List<InterfaceInfo> interfaceInfoList);

}
