package com.mumu.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mumu.wiki.exception.BusinessException;
import com.mumu.wiki.exception.BusinessExceptionCode;
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
import org.springframework.util.ObjectUtils;

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
    public UserResp searchByName(UserQueryReq userQueryReq) {
        User user = getUserByLoginName(userQueryReq.getLoginName());
        return CopyUtil.copy(user, UserResp.class);
    }

    @Override
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (user.getId() != null) {
            user.setPassword(null);
            //登录名Login_Name不允许修改与重复
            user.setLoginName(null);
            //用户id不为空，执行更新操作
            userMapper.updateByPrimaryKeySelective(user);
        } else {
            User userDB = getUserByLoginName(req.getLoginName());
            //登录名Login_Name不允许修改与重复
            if (!ObjectUtils.isEmpty(userDB)) {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
            //用户id为空，执行插入操作
            userMapper.insertSelective(user);
        }
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    private User getUserByLoginName(String loginName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("login_name", loginName);
        return userMapper.selectOne(wrapper);
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