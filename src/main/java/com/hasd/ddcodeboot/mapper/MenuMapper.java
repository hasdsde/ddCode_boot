package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2023-12-19
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getParentMenuFromUser(@Param("roleId") Integer roleId);

    List<Menu> getChildMenuFromUser(@Param("roleId") Integer roleId);

    List<Integer> getMenuIdByRoleId(@Param("roleId") Integer integer);

    List<Menu> getMenuByRoleId(@Param("roleId") String roleId);
}
