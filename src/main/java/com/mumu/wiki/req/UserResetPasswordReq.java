package com.mumu.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 用户
 */
@Data
public class UserResetPasswordReq {
    /**
     * ID
     */
    private Long id;

    /**
     * 密码
     */
    @NotBlank(message = "【密码】不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "【密码】至少包含 数字和英文，长度6-32")
    private String password;
}