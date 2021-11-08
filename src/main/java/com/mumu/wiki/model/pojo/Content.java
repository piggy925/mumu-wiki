package com.mumu.wiki.model.pojo;

import lombok.Data;

/**
 * 文档内容
 */
@Data
public class Content {
    /**
     * 文档id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;
}