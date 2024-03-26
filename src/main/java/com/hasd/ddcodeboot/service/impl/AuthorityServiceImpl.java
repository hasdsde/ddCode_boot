package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.Authority;
import com.hasd.ddcodeboot.mapper.AuthorityMapper;
import com.hasd.ddcodeboot.service.IAuthorityService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {

    @Resource
    public AuthorityMapper authorityMapper;

    /**
     * @param roleId
     * @return java.util.List<java.lang.Integer>
     * @Description 根据角色id获取权限id
     * @author hasdsd
     * @Date 2024/1/21
     */
    @Override
    @Cacheable(cacheNames = {"authority.page"})
    public List<Integer> getAuthorityIdByUserId(String roleId) {
        return authorityMapper.getAuthorityIdByRoleId(roleId);
    }

    @Override
    @Cacheable(cacheNames = "authority.page", key = "#roleId")
    public List<Authority> getAuthorityByRoleId(String roleId) {
        return authorityMapper.getAuthorityByRoleId(roleId);
    }

    @Override
    @Cacheable(value = "authority.page", key = "#currentPage + #pageSize+#name+#description+#type")
    public Page<Authority> getPage(int currentPage, int pageSize, String name, String description, String type) {
        QueryWrapper<Authority> wrapper = new QueryWrapper<>();
        wrapper.like(!name.isEmpty(), "name", name);
        wrapper.like(!description.isEmpty(), "description", description);
        wrapper.eq(!type.isEmpty(), "type", type);
        wrapper.orderByDesc("id");
        Page<Authority> page = new Page<>(currentPage, pageSize);
        return authorityMapper.selectPage(page, wrapper);
    }

}
