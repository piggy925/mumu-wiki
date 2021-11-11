package com.mumu.wiki.controller;

import com.mumu.wiki.common.ApiRestResponse;
import com.mumu.wiki.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 校验异常统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public ApiRestResponse validExceptionHandler(BindException e) {
        ApiRestResponse response = new ApiRestResponse<>();
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setSuccess(false);
        response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return response;
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ApiRestResponse businessExceptionHandler(BusinessException e) {
        ApiRestResponse response = new ApiRestResponse<>();
        LOG.warn("业务异常：{}", e.getCode().getDesc());
        response.setSuccess(false);
        response.setMessage(e.getCode().getDesc());
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiRestResponse businessExceptionHandler(Exception e) {
        ApiRestResponse response = new ApiRestResponse<>();
        LOG.error("系统异常：", e);
        response.setSuccess(false);
        response.setMessage("服务器异常，请联系管理员");
        return response;
    }
}