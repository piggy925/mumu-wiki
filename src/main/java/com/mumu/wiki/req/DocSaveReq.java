package com.mumu.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 电子书
 */
@Data
public class DocSaveReq {
    /**
     * id
     */
    private Long id;

    /**
     * 电子书id
     */
    @NotNull(message = "【电子书id】不能为空")
    private Long ebookId;

    /**
     * 父id
     */
    @NotNull(message = "【父文档id】不能为空")
    private Long parent;

    /**
     * 名称
     */
    @NotBlank(message = "【名称】不能为空")
    private String name;

    /**
     * 顺序
     */
    @NotNull(message = "【顺序】不能为空")
    private Integer sort;

    /**
     * 阅读数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer voteCount;
}