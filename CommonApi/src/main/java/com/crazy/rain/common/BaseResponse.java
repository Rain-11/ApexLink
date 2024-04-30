package com.crazy.rain.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName: BaseResponse
 * @Description: 通用返回结果
 * @author: CrazyRain
 */
@Getter
@Setter
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
