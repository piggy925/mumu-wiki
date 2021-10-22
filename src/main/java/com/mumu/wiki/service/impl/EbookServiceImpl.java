package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mumu.wiki.model.mapper.EbookMapper;
import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.service.EbookService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {
    @Resource
    private EbookMapper ebookMapper;

    @Override
    public List<Ebook> getEbookList() {
        return ebookMapper.selectEbookList();
    }

    @Override
    public List<EbookResp> searchByNameOrId(EbookReq ebookReq) {
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(ebookReq.getName()), "name", ebookReq.getName()).or().eq("id", ebookReq.getId());
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        ArrayList<EbookResp> ebookRespList = new ArrayList<>();
        ebookList.forEach(ebook -> {
            EbookResp resp = new EbookResp();
            BeanUtils.copyProperties(ebook, resp);
            ebookRespList.add(resp);
        });
        return ebookRespList;
    }
}