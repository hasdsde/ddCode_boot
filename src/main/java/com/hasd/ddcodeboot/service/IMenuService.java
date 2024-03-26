package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.Menu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hasd
 * @since 2023-12-19
 */
public interface IMenuService extends IService<Menu> {
    List<Integer> getMeuIdByRoleId(String roleId);

    List<Menu> getMenuByRoleId(String roleId);
}
