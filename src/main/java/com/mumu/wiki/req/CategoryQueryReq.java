package com.mumu.wiki.req;

import lombok.Data;

/**
 * 电子书
 */
@Data
public class CategoryQueryReq extends PageReq {
    private String name;
}