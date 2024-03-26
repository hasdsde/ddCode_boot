package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2024-01-20
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> selectMenuByRoleId(@Param("id") int id);

    void deleteTable(@Param("menuId") Integer menuId, @Param("roleId") int roleId);
}
