package com.mumu.wiki.service;

import com.mumu.wiki.req.UserQueryReq;
import com.mumu.wiki.req.UserSaveReq;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.resp.UserResp;

import java.util.List;

public interface UserService {
    List<UserResp> getAllUser();

    PageResp<UserResp> getUserList(UserQueryReq req);

    UserResp searchByName(UserQueryReq userQueryReq);

    void save(UserSaveReq req);

    void delete(Long id);
}