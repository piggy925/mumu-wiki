package com.mumu.wiki.resp;

import lombok.Data;

/**
 * 用户
 */
@Data
public class UserResp {
    /**
     * ID
     */
    private Long id;

    /**
     * 登录名
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