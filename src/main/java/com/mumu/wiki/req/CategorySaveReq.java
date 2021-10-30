package com.mumu.wiki.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 电子书
 */
@Data
public class CategorySaveReq {
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
    @NotBlank(message = "【名称】不能为空")
    private String name;

    /**
     * 顺序
     */
    @NotNull(message = "【排序】不能为空")
    private Integer sort;
}