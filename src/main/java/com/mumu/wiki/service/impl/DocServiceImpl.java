package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mumu.wiki.exception.BusinessException;
import com.mumu.wiki.exception.BusinessExceptionCode;
import com.mumu.wiki.model.mapper.ContentMapper;
import com.mumu.wiki.model.mapper.DocMapper;
import com.mumu.wiki.model.pojo.Content;
import com.mumu.wiki.model.pojo.Doc;
import com.mumu.wiki.req.DocQueryReq;
import com.mumu.wiki.req.DocSaveReq;
import com.mumu.wiki.resp.DocResp;
import com.mumu.wiki.service.DocService;
import com.mumu.wiki.util.CopyUtil;
import com.mumu.wiki.util.RedisUtil;
import com.mumu.wiki.util.RequestContext;
import com.mumu.wiki.websocket.WebSocketService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocServiceImpl implements DocService {
    @Resource
    private DocMapper docMapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WebSocketService webSocketService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public List<DocResp> getDoc(Long ebookId) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        wrapper.eq("ebook_id", ebookId);
        List<Doc> docList = docMapper.selectList(wrapper);
        return getDocResp(docList);
    }

    @Override
    public List<DocResp> searchByName(DocQueryReq docQueryReq) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        wrapper.like("name", docQueryReq.getName());
        List<Doc> docList = docMapper.selectList(wrapper);
        return getDocResp(docList);
    }

    @Transactional
    @Override
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (doc.getId() != null) {
            //分类id不为空，执行更新操作
            docMapper.updateByPrimaryKeySelective(doc);
            int count = contentMapper.updateByPrimaryKeySelective(content);
            if (count == 0) {
                contentMapper.insertSelective(content);
            }
        } else {
            //分类id为空，执行插入操作
            docMapper.insertSelective(doc);
        }
    }

    @Override
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(List<String> ids) {
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        docMapper.delete(wrapper);
    }

    @Override
    public void vote(Long id) {
        // 远程IP+doc.id作为key，24小时内不能重复
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 24)) {
            docMapper.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        //点赞后向前端发送消息
        Doc doc = docMapper.selectByPrimaryKey(id);
        rocketMQTemplate.convertAndSend("VOTE_TOPIC", "「" + doc.getName() + "」被点赞");
    }

    @Override
    public String getContent(Long id) {
        String txt = "";
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数+1
        docMapper.increaseViewCount(id);
        if (!ObjectUtils.isEmpty(content)) {
            txt = content.getContent();
        }
        return txt;
    }

    private List<DocResp> getDocResp(List<Doc> docList) {
        List<DocResp> docRespList = CopyUtil.copyList(docList, DocResp.class);
        return docRespList;
    }
}