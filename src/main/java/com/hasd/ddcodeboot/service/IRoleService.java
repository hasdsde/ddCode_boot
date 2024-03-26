package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.Role;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
public interface IRoleService extends IService<Role> {

    Page<Role> getPage(int currentPage, int pageSize, String name, String description);
}
