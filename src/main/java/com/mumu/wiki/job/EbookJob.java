package com.mumu.wiki.job;

import com.mumu.wiki.service.EbookService;
import com.mumu.wiki.util.SnowFlake;
import org.jboss.logging.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookJob {
    @Resource
    private EbookService ebookService;
    @Resource
    private SnowFlake snowFlake;

    /**
     * 每30秒更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void updateEbookInfo() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        ebookService.updateEbookInfo();
    }
}