package com.mumu.wiki.service;

import com.mumu.wiki.req.CategoryQueryReq;
import com.mumu.wiki.req.CategorySaveReq;
import com.mumu.wiki.resp.CategoryResp;

import java.util.List;

public interface CategoryService {
    List<CategoryResp> getAllCategory();

    List<CategoryResp> searchByName(CategoryQueryReq req);

    void save(CategorySaveReq req);

    void delete(Long id);
}