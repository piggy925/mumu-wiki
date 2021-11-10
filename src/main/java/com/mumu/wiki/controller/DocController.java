package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.DocQueryReq;
import com.mumu.wiki.req.DocSaveReq;
import com.mumu.wiki.resp.DocResp;
import com.mumu.wiki.service.DocService;
import io.swagger.annotations.Api;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Api(tags = "文档接口")
@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/all/{ebookId}")
    public ApiRestResponse<List<DocResp>> getDoc(@PathVariable("ebookId") Long ebookId) {
        return ApiRestResponse.success(docService.getDoc(ebookId));
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody DocSaveReq req) {
        docService.save(req);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/delete/{idsStr}")
    public ApiRestResponse delete(@PathVariable("idsStr") String idsStr) {
        if (!ObjectUtils.isEmpty(idsStr)) {
            List<String> list = Arrays.asList(idsStr.split(","));
            docService.delete(list);
        }
        return ApiRestResponse.success();
    }

    @GetMapping("/search")
    public ApiRestResponse<List<DocResp>> getDocByName(DocQueryReq docQueryReq) {
        List<DocResp> docList = docService.searchByName(docQueryReq);
        return ApiRestResponse.success(docList);
    }

    @GetMapping("/get-content/{id}")
    public ApiRestResponse<String> getContent(@PathVariable("id") Long id) {
        String content = docService.getContent(id);
        return ApiRestResponse.success(content);
    }
}