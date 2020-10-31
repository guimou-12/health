package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理类
@RestControllerAdvice //通过对异常的拦截实现的统一异常返回处理
public class HealExceptionAdvice {
    //打印日志
    private static final Logger log = LoggerFactory.getLogger(HealExceptionAdvice.class);

    //自定义异常
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        return new Result(false,he.getMessage());
    }

    //未知异常
    @ExceptionHandler(Exception.class)
    public Result handleHealthException(Exception e){
        log.error("发生异常",e);
        return new Result(false,"发生未知错误，操作失败，请联系管理员");
    }
}
