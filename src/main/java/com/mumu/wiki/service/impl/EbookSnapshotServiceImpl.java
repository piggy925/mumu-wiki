package com.mumu.wiki.service.impl;

import com.mumu.wiki.model.mapper.EbookSnapshotMapper;
import com.mumu.wiki.service.EbookSnapshotService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotServiceImpl implements EbookSnapshotService {
    @Resource
    private EbookSnapshotMapper ebookSnapshotMapper;

    @Override
    public void genEbookSnapshot() {
        ebookSnapshotMapper.genEbookSnapshot();
    }
}