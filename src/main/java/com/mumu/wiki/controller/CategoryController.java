package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.CategoryQueryReq;
import com.mumu.wiki.req.CategorySaveReq;
import com.mumu.wiki.resp.CategoryResp;
import com.mumu.wiki.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "分类接口")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/all")
    public ApiRestResponse<List<CategoryResp>> getAllCategory() {
        return ApiRestResponse.success(categoryService.getAllCategory());
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody CategorySaveReq req) {
        categoryService.save(req);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    public ApiRestResponse delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/search")
    public ApiRestResponse<List<CategoryResp>> getCategoryByName(CategoryQueryReq categoryQueryReq) {
        List<CategoryResp> categoryList = categoryService.searchByName(categoryQueryReq);
        return ApiRestResponse.success(categoryList);
    }
}