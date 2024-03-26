package com.hasd.ddcodeboot.aspect;

import com.hasd.ddcodeboot.entity.Log;
import com.hasd.ddcodeboot.service.impl.LogServiceImpl;
import com.hasd.ddcodeboot.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/22 14:42
 **/

@Aspect
@Component
@Slf4j
public class PermissionLog {
    @Resource
    private HttpServletRequest request;

    @Resource
    LogServiceImpl logService;

    @AfterReturning("@annotation(logAuthority)")
    @CacheEvict(cacheNames = "log.page")
    public void insertLog(JoinPoint joinPoint, LogAuthority logAuthority) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        String token = request.getHeader("token");
        String name = JwtUtil.getTokenInfo(token, "name");
        String userId = JwtUtil.getTokenInfo(token, "userId");
        String methodName = joinPoint.getSignature().getClass().toString() + ":" + joinPoint.getSignature().getName();
        Log log = new Log();
        log.setInfo(Arrays.toString(joinPoint.getArgs()));
        log.setAuthorityName(Arrays.toString(logAuthority.authorities()));
        log.setUserId(Integer.valueOf(userId));
        log.setUserName(name);
        log.setMethodName(methodName);
        log.setCreatedAt(LocalDateTime.now());
        logService.save(log);
    }
}
