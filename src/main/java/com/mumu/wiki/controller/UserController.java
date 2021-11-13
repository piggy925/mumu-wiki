package com.mumu.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.req.UserLoginReq;
import com.mumu.wiki.req.UserQueryReq;
import com.mumu.wiki.req.UserResetPasswordReq;
import com.mumu.wiki.req.UserSaveReq;
import com.mumu.wiki.resp.PageResp;
import com.mumu.wiki.resp.UserLoginResp;
import com.mumu.wiki.resp.UserResp;
import com.mumu.wiki.service.UserService;
import com.mumu.wiki.util.SnowFlake;
import io.swagger.annotations.Api;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private SnowFlake snowFlake;

    @GetMapping("/all")

    public ApiRestResponse<List<UserResp>> getAllUser() {
        return ApiRestResponse.success(userService.getAllUser());
    }

    @PostMapping("/save")
    public ApiRestResponse save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.save(req);
        return ApiRestResponse.success();
    }

    @PostMapping("/reset-password")
    public ApiRestResponse resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        userService.resetPassword(req);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    public ApiRestResponse<UserLoginResp> login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        return ApiRestResponse.success(userLoginResp);
    }

    @GetMapping("/logout/{token}")
    public ApiRestResponse delete(@PathVariable("token") String token) {
        redisTemplate.delete(token);
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
    public ApiRestResponse<UserResp> getUserByName(UserQueryReq userQueryReq) {
        UserResp user = userService.searchByName(userQueryReq);
        return ApiRestResponse.success(user);
    }
}