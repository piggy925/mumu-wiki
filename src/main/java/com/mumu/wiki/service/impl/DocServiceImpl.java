package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mumu.wiki.model.mapper.DocMapper;
import com.mumu.wiki.model.pojo.Doc;
import com.mumu.wiki.req.DocQueryReq;
import com.mumu.wiki.req.DocSaveReq;
import com.mumu.wiki.resp.DocResp;
import com.mumu.wiki.service.DocService;
import com.mumu.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocServiceImpl implements DocService {
    @Resource
    private DocMapper docMapper;

    @Override
    public List<DocResp> getAllDoc() {
        List<Doc> docList = docMapper.selectList(new QueryWrapper<>());
        return getDocResp(docList);
    }

    @Override
    public List<DocResp> searchByName(DocQueryReq docQueryReq) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        wrapper.like("name", docQueryReq.getName());
        List<Doc> docList = docMapper.selectList(wrapper);
        return getDocResp(docList);
    }

    @Override
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (doc.getId() != null) {
            //分类id不为空，执行更新操作
            docMapper.updateByPrimaryKeySelective(doc);
        } else {
            //分类id为空，执行插入操作
            docMapper.insertSelective(doc);
        }
    }

    @Override
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    private List<DocResp> getDocResp(List<Doc> docList) {
        List<DocResp> docRespList = CopyUtil.copyList(docList, DocResp.class);
        return docRespList;
    }
}