package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限 Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Mapper
public interface AuthorityMapper extends BaseMapper<Authority> {

    List<String> getAuthorityNameFromUserRole(@Param("roleId") Integer roleId);

    List<Authority> getAuthorityByRoleId(@Param("roleId") String roleId);

    List<Integer> getAuthorityIdByRoleId(@Param("roleId") String roleId);


}
