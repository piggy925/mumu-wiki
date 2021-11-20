package com.mumu.wiki.service;

import com.mumu.wiki.resp.EbookStatisticResp;

import java.util.List;

public interface EbookSnapshotService {
    void genEbookSnapshot();

    List<EbookStatisticResp> getEbookStatistic();
}