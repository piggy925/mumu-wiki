package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.service.EbookService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "电子书接口")
@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public ApiRestResponse<List<Ebook>> getEbookList() {
        return ApiRestResponse.success(ebookService.getEbookList());
    }

    @GetMapping("/name")
    public ApiRestResponse<List<EbookResp>> getEbookByNameOrId(EbookReq ebookReq) {
        List<EbookResp> ebookList = ebookService.searchByNameOrId(ebookReq);
        return ApiRestResponse.success(ebookList);
    }
}