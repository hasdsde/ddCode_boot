package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.User;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
public interface IUserService extends IService<User> {

    User getUserByUserId(String userId);


    Page<User> getPage(int currentPage, int pageSize, String availableDeptId, String nickName, String deptIds);

    List<Integer> getUserIdByDeptId(String deptId);
}
