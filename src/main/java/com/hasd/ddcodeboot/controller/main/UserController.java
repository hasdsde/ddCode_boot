package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.User;
import com.hasd.ddcodeboot.mapper.UserMapper;
import com.hasd.ddcodeboot.service.IUserService;
import com.hasd.ddcodeboot.service.impl.DeptServiceImpl;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023-12-18
 **/

@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;

    @Resource
    DeptServiceImpl deptService;
    @Resource
    HttpServletRequest request;

    /**
     * @param user
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改用户表
     * @author hasd
     * @Date 2023-12-18
     */
    @PostMapping("/save")
    @ApiOperation("新增/修改用户表")
    @LogAuthority(authorities = "server_user_update")
    @HasAuthority("server_user_update")
    @CacheEvict(cacheNames = "user.page", allEntries = true)
    public Result save(@RequestBody User user) {

        //验证部门权限
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        if (!new HashSet<>(availableDeptId).contains(user.getDeptId())) {
            throw new AppException(401, "没有权限操作该用户");
        }

        if (user.getId() == null) {
            user.setCreatedAt(LocalDateTime.now());
        } else {
            user.setUpdatedAt(LocalDateTime.now());
        }

        //验证角色权限
        List<String> authorities = JwtUtil.getAuthorities(request.getHeader("token"));
        if (!authorities.contains("server_user_role_update")) {
            user.setRoleId(null);
        }

        userService.saveOrUpdate(user);
        return Result.OK();
    }


    /**
     * @param currentPage int
     * @param pageSize    int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 分页查询
     * @author hasd
     * @Date 2023-12-18
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    @HasAuthority("server_user_page")
    public Result getPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "deptId") String deptIds
    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        Page<User> page = userService.getPage(currentPage, pageSize, deptId, nickName, deptIds);
        return Result.OKWithPageQuery(page);
    }

    /**
     * @param id int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 根据id删除
     * @author hasd
     * @Date 2023-12-18
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除")
    @LogAuthority(authorities = "server_user_update")
    @HasAuthority("server_user_update")
    @CacheEvict(cacheNames = "user.page", allEntries = true)
    public Result deleteById(
            @PathVariable(value = "id") int id
    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> userId = userService.getUserIdByDeptId(deptId);
        if (!new HashSet<>(userId).contains(id)) {
            throw new AppException(401, "没有权限操作该用户");
        }
        userService.removeById(id);
        return Result.OK();
    }

    /**
     * @param ids list<integer>
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 批量删除
     * @author hasd
     * @Date 2023-12-18
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除")
    @LogAuthority(authorities = "server_user_update")
    @HasAuthority("server_user_update")
    @CacheEvict(cacheNames = "user.page", allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> userId = userService.getUserIdByDeptId(deptId);
        if (!new HashSet<>(userId).containsAll(ids)) {
            throw new AppException(401, "没有权限操作该用户");
        }
        userService.removeBatchByIds(ids);
        return Result.OK();
    }

    @DeleteMapping("/sign")
    @ApiOperation("标记删除")
    @LogAuthority(authorities = "server_user_update")
    @HasAuthority("server_user_update")
    @CacheEvict(cacheNames = "user.page", allEntries = true)
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> userId = userService.getUserIdByDeptId(deptId);
        if (!new HashSet<>(userId).containsAll(ids)) {
            throw new AppException(401, "没有权限操作该用户");
        }
        ids.forEach(id -> {
            User user = new User();
            user.setDeletedAt(LocalDateTime.now());
            user.setId(id);
            userService.updateById(user);
        });
        return Result.OK();
    }

    @DeleteMapping("/recover/{id}")
    @ApiOperation("根据id恢复")
    @LogAuthority(authorities = "server_user_update")
    @HasAuthority("server_user_update")
    @CacheEvict(cacheNames = "user.page", allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> userId = userService.getUserIdByDeptId(deptId);
        if (!new HashSet<>(userId).containsAll(userId)) {
            throw new AppException(401, "没有权限操作该用户");
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        userMapper.update(wrapper);
        return Result.OK();
    }
}

