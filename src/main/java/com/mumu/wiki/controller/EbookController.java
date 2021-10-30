package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.EbookQueryReq;
import com.mumu.wiki.req.EbookSaveReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.service.EbookService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "电子书接口")
@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/all")
    public ApiRestResponse<List<EbookResp>> getAllEbook() {
        return ApiRestResponse.success(ebookService.getAllEbook());
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody EbookSaveReq req) {
        ebookService.save(req);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    public ApiRestResponse delete(@PathVariable("id") Long id) {
        ebookService.delete(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/list")
    public ApiRestResponse<PageResp<EbookResp>> getEbookList(@Valid EbookQueryReq req) {
        return ApiRestResponse.success(ebookService.getEbookList(req));
    }

    @GetMapping("/name")
    public ApiRestResponse<PageResp<EbookResp>> getEbookByNameOrId(EbookQueryReq ebookQueryReq) {
        PageResp<EbookResp> ebookList = ebookService.searchByNameOrId(ebookQueryReq);
        return ApiRestResponse.success(ebookList);
    }
}