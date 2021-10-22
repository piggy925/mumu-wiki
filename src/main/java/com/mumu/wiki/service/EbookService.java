package com.mumu.wiki.service;

import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;

import java.util.List;

public interface EbookService {
    List<Ebook> getEbookList();

    List<EbookResp> searchByNameOrId(EbookReq ebookReq);
}