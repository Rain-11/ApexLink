package com.crazy.rain.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 钱包金额
     */
    private Integer wallet;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}