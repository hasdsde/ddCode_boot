package com.hasd.ddcodeboot.common;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/12/17 18:26
 **/
public interface Constants {
    Integer CODE_OK = 200;    // 成功
    Integer CODE_CREATED = 201;    // 操作成功
    Integer CODE_NO_CONTENT = 204;    // 内容为空
    Integer CODE_BAD_REQUEST = 400;    // 请求参数错误
    Integer CODE_UNAUTHORIZED = 401;    // 未授权
    Integer CODE_FORBIDDEN = 403;    //禁止访问
    Integer CODE_NOT_FOUND = 404;    // 未找到
    Integer CODE_INTERNAL_SERVER_ERROR = 500;    // 服务器内部错误
}
