package com.mumu.wiki.model.pojo;

import lombok.Data;

/**
 * 用户
 */
@Data
public class User {
    /**
     * ID
     */
    private Long id;

    /**
     * 登陆名
     */
    private String loginName;

    /**
     * 昵称
     */
    private String name;

    /**
     * 密码
     */
    private String password;
}