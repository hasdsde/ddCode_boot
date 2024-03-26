package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.Dept;

import java.util.List;

/**
 * <p>
 * 部门 服务类
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
public interface IDeptService extends IService<Dept> {

    Page<Dept> getPage(int currentPage, int pageSize, String deptId, String name, String description, String parentId);

    List<Dept> getDeptByDeptId(String deptId);

    List<Integer> getDeptIdByDeptId(String deptId);
}
