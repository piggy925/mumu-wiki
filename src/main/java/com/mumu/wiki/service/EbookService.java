package com.mumu.wiki.service;

import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;

public interface EbookService {
    PageResp<EbookResp> getEbookList(EbookReq req);

    PageResp<EbookResp> searchByNameOrId(EbookReq ebookReq);
}