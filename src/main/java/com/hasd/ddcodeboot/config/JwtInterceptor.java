package com.hasd.ddcodeboot.config;


import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.entity.User;
import com.hasd.ddcodeboot.service.IAuthorityService;
import com.hasd.ddcodeboot.service.IUserService;
import com.hasd.ddcodeboot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/17 22:27
 **/

@Configuration
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${constom.JwtSecret}")
    public String JwtSecret;

    @Resource
    private IUserService userService;

    @Resource
    private IAuthorityService authorityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Objects.equals(request.getMethod(), "OPTIONS")) {
            return true;
        }

        String token = request.getHeader("token");
        String userId = JwtUtil.getTokenInfo(token, "userId");
        User userInfo = userService.getUserByUserId(userId);
        //检查用户基本信息
        if (userInfo.getDeletedAt() != null) {
            return false;
        }
        if (userId == null) {
            return false;
        }
        if (token == null) {
            throw new AppException(401, "token is null");
        }
        //检测权限变化
        //我认为没有必要检测用户权限变化，预期这样不如缩短过期时间
//        authorityService.getAuthorityNameFromRoleId(userInfo.getRoleId());
        return JwtUtil.checkJwt(token, JwtSecret + userInfo.getPassword());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
