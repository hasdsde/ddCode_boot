package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    List<Dept> getDeptByDeptId(@Param("deptId") String deptId);

    List<Integer> getDeptIdByDeptId(@Param("deptId") String deptId);

    List<Dept> getPage(@Param("currentPage") int currentPage, @Param("pageSize") int pageSize, @Param("deptId") String deptId, @Param("name") String name, @Param("description") String description, @Param("parentId") String parentId);

    Integer getPageCount(@Param("deptId") String deptId);
}
