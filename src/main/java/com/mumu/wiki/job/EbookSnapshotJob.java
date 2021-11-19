package com.mumu.wiki.job;

import com.mumu.wiki.service.EbookSnapshotService;
import com.mumu.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @Scheduled(cron = "5/30 * * * * ?")
    public void doSnapshot() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        long startTime = System.currentTimeMillis();
        LOG.info("生成今日电子书快照开始");
        ebookSnapshotService.genEbookSnapshot();
        LOG.info("生成今日电子书快照结束, 耗时：{}", System.currentTimeMillis() - startTime);
    }
}