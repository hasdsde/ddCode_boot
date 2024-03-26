package com.hasd.ddcodeboot.aspect;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/20 16:49
 **/

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Resource
    private HttpServletRequest request;

    @Before("@annotation(hasAuthority)")
    public void checkPermission(JoinPoint joinPoint, HasAuthority hasAuthority) {
        List<String> requiredPermissions = Arrays.asList(hasAuthority.value());
        String token = request.getHeader("token");

        JSONArray list = JSONUtil.parseArray(JwtUtil.getTokenInfo(token, "authorities"));
        List<String> havePermissions = JSONUtil.toList(list, String.class);

        if (!new HashSet<>(havePermissions).containsAll(requiredPermissions)) {
            throw new AppException(401, "无权限：" + requiredPermissions.toString());
        }
    }
}
