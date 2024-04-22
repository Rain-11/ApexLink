package com.crazy.rain.model.dto.interface_info;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: InterfaceStatusDto
 * @Description: 接口发布与下线
 * @author: CrazyRain
 * @date: 2024/4/22 上午11:20
 */
@Data
public class InterfaceStatusDto implements Serializable {

    /**
     * 接口id
     */
    private Long id;
}
