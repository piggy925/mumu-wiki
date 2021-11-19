package com.mumu.wiki.model.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 电子书快照表
 */
@Data
public class EbookSnapshot {
    /**
     * id
     */
    private Long id;

    /**
     * 电子书id
     */
    private Long ebookId;

    /**
     * 快照日期
     */
    private Date date;

    /**
     * 阅读数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer voteCount;

    /**
     * 阅读增长
     */
    private Integer viewIncrease;

    /**
     * 点赞增长
     */
    private Integer voteIncrease;
}