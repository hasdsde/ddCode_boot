package com.hasd.ddcodeboot.controller.web;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.User;
import com.hasd.ddcodeboot.entity.dto.LoginUser;
import com.hasd.ddcodeboot.mapper.AuthorityMapper;
import com.hasd.ddcodeboot.mapper.MenuMapper;
import com.hasd.ddcodeboot.mapper.UserMapper;
import com.hasd.ddcodeboot.service.IUserService;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/20 14:20
 **/

@RestController
@RequestMapping("/user")
@Api(tags = "web-user")
@Slf4j
public class UserWebController {

    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private AuthorityMapper authorityMapper;

    @Value("${constom.JwtSecret}")
    public String JwtSecret;

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result Login(
            @RequestBody LoginUser user
    ) {
        User userInfo = userService.getOne(new QueryWrapper<User>()
                .eq("user_name", user.getUserName())
                .isNull("deleted_at")
        );
        if (userInfo == null) {
            throw new AppException("用户名或密码错误");
        }
        String password = SecureUtil.md5(user.getPassword());
        if (!userInfo.getPassword().toLowerCase().equals(password)) {
            throw new AppException("用户名或密码错误");
        }
        List<String> authorityList = authorityMapper.getAuthorityNameFromUserRole(userInfo.getRoleId());

        //返回信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", userInfo.getUserName());
        map.put("userId", userInfo.getId());
        map.put("nickName", userInfo.getNickName());
        map.put("roleId", userInfo.getRoleId());
        map.put("deptId", userInfo.getDeptId());
        map.put("authorities", authorityList);
        String token = JwtUtil.generateJwt(map, JwtSecret + userInfo.getPassword());
        userInfo.setPassword("");
        HashMap<String, Object> data = new HashMap<>();
        data.put("userInfo", userInfo);
        data.put("token", token);
        return Result.OKWithData(data);
    }
}
