package com.mumu.wiki.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文档
 */
@Data
@TableName("doc")
public class Doc {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 电子书id
     */
    private Long ebookId;

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

    /**
     * 阅读数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer voteCount;
}