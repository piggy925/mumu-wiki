package com.mumu.wiki.service;

import com.mumu.wiki.req.EbookQueryReq;
import com.mumu.wiki.req.EbookSaveReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;

import java.util.List;

public interface EbookService {
    List<EbookResp> getAllEbook();

    PageResp<EbookResp> getEbookList(EbookQueryReq req);

    PageResp<EbookResp> searchByName(EbookQueryReq ebookQueryReq);

    void save(EbookSaveReq req);

    void delete(Long id);
}