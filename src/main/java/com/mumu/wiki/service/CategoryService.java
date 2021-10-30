package com.mumu.wiki.service;

import com.mumu.wiki.req.CategoryQueryReq;
import com.mumu.wiki.req.CategorySaveReq;
import com.mumu.wiki.resp.CategoryResp;
import com.mumu.wiki.resp.PageResp;

import java.util.List;

public interface CategoryService {
    List<CategoryResp> getAllCategory();

    PageResp<CategoryResp> getCategoryList(CategoryQueryReq req);

    PageResp<CategoryResp> searchByName(CategoryQueryReq req);

    void save(CategorySaveReq req);

    void delete(Long id);
}