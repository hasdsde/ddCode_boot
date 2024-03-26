package com.hasd.ddcodeboot.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 15:15
 * 自定义错误类
 **/

@Getter
@Setter
public class AppException extends RuntimeException {
    private Integer code;
    private String Message;

    public AppException() {
        super();
    }

    public AppException(String msg) {
        super(msg);
        this.code = 201;
        this.Message = msg;
    }

    public AppException(Integer code, String msg) {
        super();
        this.code = code;
        this.Message = msg;
    }
}
