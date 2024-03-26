package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.RoleAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Mapper
public interface RoleAuthorityMapper extends BaseMapper<RoleAuthority> {

    List<Integer> selectAuthorityByRoleId(@Param("id") int id);

    Boolean deleteTable(@Param("authorityId") Integer authorityId, @Param("roleId") int roleId);
}
