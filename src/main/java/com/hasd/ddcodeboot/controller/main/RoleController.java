package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.Role;
import com.hasd.ddcodeboot.mapper.RoleMapper;
import com.hasd.ddcodeboot.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023-12-18
 **/

@RestController
@RequestMapping("/role")
@Api(tags = "角色")
@Slf4j
public class RoleController {

    @Resource
    private IRoleService roleService;
    @Resource
    private RoleMapper roleMapper;


    /**
     * @param role
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改
     * @author hasd
     * @Date 2023-12-18
     */
    @PostMapping("/save")
    @ApiOperation("新增/修改")
    @LogAuthority(authorities = "server_role_update")
    @HasAuthority("server_role_update")
    @CacheEvict(cacheNames = "role.page", allEntries = true)
    public Result save(@RequestBody Role role) {

        if (role.getId() == null) {
            role.setCreatedAt(LocalDateTime.now());
        } else {
            role.setUpdatedAt(LocalDateTime.now());
        }
        roleService.saveOrUpdate(role);
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
    @HasAuthority("server_role_page")
    public Result getPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description
    ) {
        Page<Role> page = roleService.getPage(currentPage, pageSize, name, description);
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
    @LogAuthority(authorities = "server_role_update")
    @HasAuthority("server_role_update")
    @CacheEvict(cacheNames = "role.page", allEntries = true)
    public Result deleteById(
            @PathVariable(value = "id") int id
    ) {
        roleService.removeById(id);
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
    @LogAuthority(authorities = "server_role_update")
    @HasAuthority("server_role_update")
    @CacheEvict(cacheNames = "role.page", allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        roleService.removeBatchByIds(ids);
        return Result.OK();
    }

    /**
     * @param ids
     * @return com.hasd.ddcodeboot.common.Result
     * @Description deleteSignByIds
     * @author hasdsd
     * @Date 2024/1/19
     */
    @DeleteMapping("/sign")
    @ApiOperation("标记删除")
    @LogAuthority(authorities = "server_role_update")
    @HasAuthority("server_role_update")
    @CacheEvict(cacheNames = "role.page", allEntries = true)
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        ids.forEach(id -> {
            Role role = new Role();
            role.setDeletedAt(LocalDateTime.now());
            role.setId(id);
            roleService.updateById(role);
        });
        return Result.OK();
    }

    /**
     * @param id
     * @return com.hasd.ddcodeboot.common.Result
     * @Description recoverById
     * @author hasdsd
     * @Date 2024/1/19
     */
    @DeleteMapping("/recover/{id}")
    @ApiOperation("根据id恢复")
    @LogAuthority(authorities = "server_role_update")
    @HasAuthority("server_role_update")
    @CacheEvict(cacheNames = "role.page", allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        Role role = new Role();
        UpdateWrapper<Role> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        roleMapper.update(wrapper);
        return Result.OK();
    }

    /**
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 获取全部可用角色id和名称
     * @author hasdsd
     * @Date 2024/1/19
     */
    @GetMapping("/all")
    @Cacheable(cacheNames = "role.page")
    public Result getAll() {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.select("name", "id").isNull("deleted_at");
        List<Role> list = roleMapper.selectList(wrapper);
        return Result.OKWithData(list);
    }
}

