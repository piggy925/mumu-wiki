package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mumu.wiki.model.mapper.EbookMapper;
import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.service.EbookService;
import com.mumu.wiki.util.CopyUtil;
import com.mumu.wiki.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {
    @Resource
    private EbookMapper ebookMapper;

    @Override
    public PageResp<EbookResp> getEbookList(EbookReq ebookReq) {
        if (PageUtil.needPage(ebookReq)) {
            PageHelper.startPage(ebookReq.getPageNum(), ebookReq.getPageSize());
        }
        List<Ebook> ebookList = ebookMapper.selectEbookList();
        return getPageResp(ebookList);
    }

    @Override
    public PageResp<EbookResp> searchByNameOrId(EbookReq ebookReq) {
        if (PageUtil.needPage(ebookReq)) {
            PageHelper.startPage(ebookReq.getPageNum(), ebookReq.getPageSize());
        }
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(ebookReq.getName()), "name", ebookReq.getName()).or().eq("id", ebookReq.getId());
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        return getPageResp(ebookList);
    }

    private PageResp<EbookResp> getPageResp(List<Ebook> ebookList) {
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);

        PageInfo<EbookResp> pageInfo = new PageInfo<>(ebookRespList);
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookRespList);
        return pageResp;
    }
}