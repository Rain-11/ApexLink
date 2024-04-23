package com.crazy.rain.common;

import lombok.Getter;

@Getter
public enum MethodEnum {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");;

    private String value;

    MethodEnum(String value) {
        this.value = value;
    }

}
