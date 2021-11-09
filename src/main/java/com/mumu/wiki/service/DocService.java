package com.mumu.wiki.service;

import com.mumu.wiki.req.DocQueryReq;
import com.mumu.wiki.req.DocSaveReq;
import com.mumu.wiki.resp.DocResp;

import java.util.List;

public interface DocService {
    List<DocResp> getAllDoc();

    List<DocResp> searchByName(DocQueryReq req);

    void save(DocSaveReq req);

    void delete(Long id);

    void delete(List<String> ids);

    String getContent(Long id);
}