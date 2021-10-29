package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mumu.wiki.model.mapper.EbookMapper;
import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookQueryReq;
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
    public List<EbookResp> getAllEbook() {
        List<Ebook> ebookList = ebookMapper.selectEbookList();
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);
        return ebookRespList;
    }

    @Override
    public PageResp<EbookResp> getEbookList(EbookQueryReq ebookQueryReq) {
        if (PageUtil.needPage(ebookQueryReq)) {
            PageHelper.startPage(ebookQueryReq.getPageNum(), ebookQueryReq.getPageSize());
        }
        List<Ebook> ebookList = ebookMapper.selectEbookList();
        return getPageResp(ebookList);
    }

    @Override
    public PageResp<EbookResp> searchByNameOrId(EbookQueryReq ebookQueryReq) {
        if (PageUtil.needPage(ebookQueryReq)) {
            PageHelper.startPage(ebookQueryReq.getPageNum(), ebookQueryReq.getPageSize());
        }
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(ebookQueryReq.getName()), "name", ebookQueryReq.getName()).or().eq("id", ebookQueryReq.getId());
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        return getPageResp(ebookList);
    }

    private PageResp<EbookResp> getPageResp(List<Ebook> ebookList) {
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        List<EbookResp> ebookRespList = CopyUtil.copyList(ebookList, EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookRespList);
        return pageResp;
    }
}