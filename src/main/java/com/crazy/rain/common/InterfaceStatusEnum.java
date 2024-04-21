package com.crazy.rain.common;

import lombok.Getter;

@Getter
public enum InterfaceStatusEnum {
    START(1),
    CLOSED(0),
    ;


    private Integer value;

    InterfaceStatusEnum(Integer value) {
        this.value = value;
    }

    public static InterfaceStatusEnum getEnum(Integer value) {
        if (value == null)
            return null;
        for (InterfaceStatusEnum e : InterfaceStatusEnum.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}
