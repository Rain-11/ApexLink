package com.crazy.rain.common;

/**
 * @ClassName: ResultUtils
 * @Description: 返回工具类
 * @author: CrazyRain
 */
public class ResultUtil {

    private ResultUtil() {
    }

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "ok");
    }

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(200, null, "ok");
    }

    /**
     * 失败
     */
    public static BaseResponse<Void> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     */
    public static BaseResponse<Void> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     */
    public static BaseResponse<Void> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
