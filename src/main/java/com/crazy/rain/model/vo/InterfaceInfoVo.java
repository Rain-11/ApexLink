package com.crazy.rain.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName interface_info
 */
@Data
public class InterfaceInfoVo implements Serializable {
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
     * 接口状态 0-关闭，1-开启
     */
    private Integer status;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 创建人姓名
     */
    private String userName;
    /**
     * 创建人Id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除0-未删，1-已删除
     */
    private Integer isDelete;


}