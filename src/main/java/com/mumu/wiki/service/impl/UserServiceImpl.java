package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mumu.wiki.model.mapper.UserMapper;
import com.mumu.wiki.model.pojo.User;
import com.mumu.wiki.req.UserQueryReq;
import com.mumu.wiki.req.UserSaveReq;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.resp.UserResp;
import com.mumu.wiki.service.UserService;
import com.mumu.wiki.util.CopyUtil;
import com.mumu.wiki.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserResp> getAllUser() {
        List<User> userList = userMapper.selectList(null);
        List<UserResp> userRespList = CopyUtil.copyList(userList, UserResp.class);
        return userRespList;
    }

    @Override
    public PageResp<UserResp> getUserList(UserQueryReq userQueryReq) {
        if (PageUtil.needPage(userQueryReq)) {
            PageHelper.startPage(userQueryReq.getPageNum(), userQueryReq.getPageSize());
        }
        List<User> userList = userMapper.selectList(null);
        return getPageResp(userList);
    }

    @Override
    public PageResp<UserResp> searchByName(UserQueryReq userQueryReq) {
        if (PageUtil.needPage(userQueryReq)) {
            PageHelper.startPage(userQueryReq.getPageNum(), userQueryReq.getPageSize());
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("login_name", userQueryReq.getLoginName());
        List<User> userList = userMapper.selectList(wrapper);
        return getPageResp(userList);
    }

    @Override
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (user.getId() != null) {
            //用户id不为空，执行更新操作
            userMapper.updateByPrimaryKeySelective(user);
        } else {
            //用户id为空，执行插入操作
            userMapper.insertSelective(user);
        }
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private PageResp<UserResp> getPageResp(List<User> userList) {
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        List<UserResp> userRespList = CopyUtil.copyList(userList, UserResp.class);
        PageResp<UserResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(userRespList);
        return pageResp;
    }
}