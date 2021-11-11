package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.UserQueryReq;
import com.mumu.wiki.req.UserSaveReq;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.resp.UserResp;
import com.mumu.wiki.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/all")
    public ApiRestResponse<List<UserResp>> getAllUser() {
        return ApiRestResponse.success(userService.getAllUser());
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody UserSaveReq req) {
        userService.save(req);
        return ApiRestResponse.success();
    }

    @DeleteMapping("/delete/{id}")
    public ApiRestResponse delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/list")
    public ApiRestResponse<PageResp<UserResp>> getUserList(@Valid UserQueryReq req) {
        return ApiRestResponse.success(userService.getUserList(req));
    }

    @GetMapping("/search")
    public ApiRestResponse<PageResp<UserResp>> getUserByName(UserQueryReq userQueryReq) {
        PageResp<UserResp> userList = userService.searchByName(userQueryReq);
        return ApiRestResponse.success(userList);
    }
}