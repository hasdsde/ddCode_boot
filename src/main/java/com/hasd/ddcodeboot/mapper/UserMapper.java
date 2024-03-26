package com.hasd.ddcodeboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hasd.ddcodeboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author hasd
 * @since 2023-12-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<Integer> getUserIdByDeptId(@Param("availableDeptId") List<Integer> availableDeptId);
}
