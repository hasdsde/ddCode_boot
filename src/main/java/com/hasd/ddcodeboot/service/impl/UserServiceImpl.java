package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.User;
import com.hasd.ddcodeboot.mapper.UserMapper;
import com.hasd.ddcodeboot.service.IUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    UserMapper userMapper;
    @Resource
    HttpServletRequest request;

    @Resource
    DeptServiceImpl deptService;

    @Override
    @Cacheable(cacheNames = "user.page")
    public User getUserByUserId(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @Cacheable(cacheNames = "user.page", key = "#currentPage + #pageSize + #deptId + #nickName + #deptIds")
    public Page<User> getPage(int currentPage, int pageSize, String deptId, String nickName, String deptIds) {
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        System.out.println(nickName + deptIds);
        wrapper.like(!nickName.isEmpty(), "nick_name", nickName);
        wrapper.eq(!deptIds.isEmpty(), "dept_id", deptIds);
        wrapper.orderByDesc("id");
        wrapper.in("dept_id", availableDeptId);
        Page<User> page = new Page<>(currentPage, pageSize);
        return userMapper.selectPage(page, wrapper);
    }

    @Override
    @Cacheable(cacheNames = "user.id", key = "#deptId")
    public List<Integer> getUserIdByDeptId(String deptId) {
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        userMapper.getUserIdByDeptId(availableDeptId);
        return null;
    }
}
