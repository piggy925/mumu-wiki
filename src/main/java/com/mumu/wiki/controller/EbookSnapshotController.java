package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.resp.EbookStatisticResp;
import com.mumu.wiki.service.EbookSnapshotService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "电子书快照接口")
@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {
    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public ApiRestResponse<List<EbookStatisticResp>> getEbookStatistics() {
        return ApiRestResponse.success(ebookSnapshotService.getEbookStatistic());
    }

    @GetMapping("/get-30statistic")
    public ApiRestResponse<List<EbookStatisticResp>> get30EbookStatistics() {
        return ApiRestResponse.success(ebookSnapshotService.get30EbookStatistic());
    }
}