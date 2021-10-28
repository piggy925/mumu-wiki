package com.mumu.wiki.util;

import com.mumu.wiki.req.PageReq;

public class PageUtil {
    public static boolean needPage(PageReq req) {
        return (req.getPageNum() != 0 && req.getPageSize() != 0);
    }
}