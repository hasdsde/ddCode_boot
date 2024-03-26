package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.Authority;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
public interface IAuthorityService extends IService<Authority> {
    List<Integer> getAuthorityIdByUserId(String userId);

    List<Authority> getAuthorityByRoleId(String roleId);

    Page<Authority> getPage(int currentPage, int pageSize, String name, String description, String type);

}
