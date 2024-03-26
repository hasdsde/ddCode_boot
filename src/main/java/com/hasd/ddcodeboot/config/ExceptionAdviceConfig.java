package com.hasd.ddcodeboot.config;

import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 15:16
 **/


@ControllerAdvice
public class ExceptionAdviceConfig {
    @ResponseBody
    @ExceptionHandler(AppException.class)
    public Result handleAppException(AppException e) {
        return Result.failWithMessage(e.getCode(), "错误:" + e.getMessage());
    }
}
