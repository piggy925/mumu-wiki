package com.mumu.wiki.req;

import lombok.Data;

import javax.validation.constraints.Max;

/**
 * 分页父类：其他需要分页的类会继承该类
 */
@Data
public class PageReq {
    private int pageNum;

    @Max(value = 1000, message = "【每页条数】不能超过1000")
    private int pageSize;
}