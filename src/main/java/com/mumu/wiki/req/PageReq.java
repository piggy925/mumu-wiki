package com.mumu.wiki.req;

import lombok.Data;

/**
 * 分页父类：其他需要分页的类会继承该类
 */
@Data
public class PageReq {
    private int pageNum;

    private int pageSize;
}