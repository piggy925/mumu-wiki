package com.mumu.wiki.resp;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户
 */
@Data
public class UserLoginResp {
    /**
     * ID
     */
    private Long id;

    /**
     * 登录名
     */
    @NotBlank(message = "【用户名】不能为空")
    private String loginName;

    /**
     * 昵称
     */
    @NotBlank(message = "【昵称】不能为空")
    private String name;

    private String token;
}