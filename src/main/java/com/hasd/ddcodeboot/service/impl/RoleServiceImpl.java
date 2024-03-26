package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.Role;
import com.hasd.ddcodeboot.mapper.RoleMapper;
import com.hasd.ddcodeboot.service.IRoleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    RoleMapper roleMapper;

    @Override
    @Cacheable(value = "role.page", key = "#currentPage + #pageSize+#name+#description")
    public Page<Role> getPage(int currentPage, int pageSize, String name, String description) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.like(!name.isEmpty(), "name", name);
        wrapper.like(!description.isEmpty(), "description", description);
        wrapper.orderByDesc("id");
        return roleMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
    }
}
