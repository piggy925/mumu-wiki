package com.mumu.wiki.req;

import lombok.Data;

@Data
public class UserQueryReq extends PageReq {
    /**
     * 用户名
     */
    private String loginName;
}