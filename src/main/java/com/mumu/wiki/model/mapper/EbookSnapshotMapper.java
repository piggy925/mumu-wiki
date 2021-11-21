package com.mumu.wiki.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mumu.wiki.model.pojo.EbookSnapshot;
import com.mumu.wiki.resp.EbookStatisticResp;

import java.util.List;

public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {
    int deleteByPrimaryKey(Long id);

    int insert(EbookSnapshot record);

    int insertSelective(EbookSnapshot record);

    EbookSnapshot selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EbookSnapshot record);

    int updateByPrimaryKey(EbookSnapshot record);

    void genEbookSnapshot();

    List<EbookStatisticResp> getEbookStatistic();

    List<EbookStatisticResp> get30EbookStatistic();
}