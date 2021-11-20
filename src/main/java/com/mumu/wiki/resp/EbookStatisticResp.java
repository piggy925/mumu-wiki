package com.mumu.wiki.resp;

import lombok.Data;

import java.util.Date;

@Data
public class EbookStatisticResp {
    private Date date;

    private int viewCount;

    private int voteCount;

    private int viewIncrease;

    private int voteIncrease;
}