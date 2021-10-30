package com.mumu.wiki.req;

import lombok.Data;

/**
 * 电子书
 */
@Data
public class EbookQueryReq extends PageReq {
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}