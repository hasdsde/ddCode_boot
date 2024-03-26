package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.Menu;
import com.hasd.ddcodeboot.mapper.MenuMapper;
import com.hasd.ddcodeboot.service.IMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2023-12-19
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    MenuMapper menuMapper;

    /**
     * @param roleId 角色id
     * @return java.util.List<com.hasd.ddcodeboot.entity.Menu>
     * @Description 根据角色ID获取全部menu的id
     * @author hasdsd
     * @Date 2024/1/21
     */
    @Cacheable(cacheNames = {"menu.page"}, key = "#roleId")
    public List<Integer> getMeuIdByRoleId(String roleId) {
        return menuMapper.getMenuIdByRoleId(Integer.valueOf(roleId));
    }

    @Override
    @Cacheable(cacheNames = {"menu.page"}, key = "#roleId")
    public List<Menu> getMenuByRoleId(String roleId) {
        return menuMapper.getMenuByRoleId(roleId);
    }
}
