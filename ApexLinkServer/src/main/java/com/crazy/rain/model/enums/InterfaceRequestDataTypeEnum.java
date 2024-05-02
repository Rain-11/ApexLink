package com.crazy.rain.model.enums;

import lombok.Getter;

/**
 * @ClassName: InterfaceRequestDataTypeEnum
 * @Description: 请求参数类型枚举
 * @author: CrazyRain
 * @date: 2024/4/30 下午9:22
 */
@Getter
public enum InterfaceRequestDataTypeEnum {
    JSON("application/json"),
    FORM("application/x-www-form-urlencoded");


    private String requestDataType;

    InterfaceRequestDataTypeEnum(String requestDataType) {
        this.requestDataType = requestDataType;
    }
}
