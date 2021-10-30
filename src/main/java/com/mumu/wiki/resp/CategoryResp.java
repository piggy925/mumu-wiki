package com.mumu.wiki.resp;

import lombok.Data;

@Data
public class CategoryResp {
    /**
     * id
     */
    private Long id;

    /**
     * 父id
     */
    private Long parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;
}