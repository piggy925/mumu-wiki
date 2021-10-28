package com.mumu.wiki.service;

import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;

import java.util.List;

public interface EbookService {
    List<EbookResp> getAllEbook();

    PageResp<EbookResp> getEbookList(EbookReq req);

    PageResp<EbookResp> searchByNameOrId(EbookReq ebookReq);
}