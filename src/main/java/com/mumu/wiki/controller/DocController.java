package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.DocQueryReq;
import com.mumu.wiki.req.DocSaveReq;
import com.mumu.wiki.resp.DocResp;
import com.mumu.wiki.service.DocService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "文档接口")
@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private DocService docService;

    @GetMapping("/all")
    public ApiRestResponse<List<DocResp>> getAllDoc() {
        return ApiRestResponse.success(docService.getAllDoc());
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody DocSaveReq req) {
        docService.save(req);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    public ApiRestResponse delete(@PathVariable("id") Long id) {
        docService.delete(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/search")
    public ApiRestResponse<List<DocResp>> getDocByName(DocQueryReq docQueryReq) {
        List<DocResp> docList = docService.searchByName(docQueryReq);
        return ApiRestResponse.success(docList);
    }
}