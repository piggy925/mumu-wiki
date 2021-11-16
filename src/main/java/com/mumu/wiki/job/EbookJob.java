package com.mumu.wiki.job;

import com.mumu.wiki.service.EbookService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookJob {
    @Resource
    private EbookService ebookService;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void updateEbookInfo() {
        ebookService.updateEbookInfo();
    }
}