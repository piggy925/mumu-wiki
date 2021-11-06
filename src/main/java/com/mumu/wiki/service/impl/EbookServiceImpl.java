package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mumu.wiki.model.mapper.EbookMapper;
import com.mumu.wiki.model.pojo.Ebook;
import com.mumu.wiki.req.EbookQueryReq;
import com.mumu.wiki.req.EbookSaveReq;
import com.mumu.wiki.resp.EbookResp;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.service.EbookService;
import com.mumu.wiki.util.CopyUtil;
import com.mumu.wiki.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        Long category2Id = ebookQueryReq.getCategory2Id();
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(category2Id)) {
            wrapper.eq("category2_Id", category2Id);
        }
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        return getPageResp(ebookList);
    }

    @Override
    public PageResp<EbookResp> searchByName(EbookQueryReq ebookQueryReq) {
        if (PageUtil.needPage(ebookQueryReq)) {
            PageHelper.startPage(ebookQueryReq.getPageNum(), ebookQueryReq.getPageSize());
        }
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name", ebookQueryReq.getName());
        List<Ebook> ebookList = ebookMapper.selectList(wrapper);
        return getPageResp(ebookList);
    }

    @Override
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ebook.getId() != null) {
            //图书id不为空，执行更新操作
            ebookMapper.updateByPrimaryKeySelective(ebook);
        } else {
            //图书id为空，执行插入操作
            ebookMapper.insertSelective(ebook);
        }
    }

    @Override
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
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