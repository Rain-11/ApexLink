package com.crazy.rain.model.dto.interface_info;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: InterfaceAddDto
 * @Description: 接口详情添加dto
 * @author: CrazyRain
 * @date: 2024/4/20 下午1:33
 */
@Data
public class InterfaceUpdateDto implements Serializable {
    /**
     * 接口id
     */
    private Long id;
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口描述
     */
    private String detail;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 接口状态 0-关闭，1-开启
     */
    private Integer status;
}
