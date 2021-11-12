package com.mumu.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户
 */
@Data
public class UserLoginReq {
    /**
     * 登录名
     */
    @NotBlank(message = "【用户名】不能为空")
    private String loginName;

    /**
     * 密码
     */
    @NotBlank(message = "【密码】不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】格式不正确")
    private String password;
}