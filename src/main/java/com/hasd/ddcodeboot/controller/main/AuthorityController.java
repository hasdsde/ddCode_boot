package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.Authority;
import com.hasd.ddcodeboot.mapper.AuthorityMapper;
import com.hasd.ddcodeboot.service.IAuthorityService;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023-12-18
 **/

@RestController
@RequestMapping("/authority")
@Api(tags = "权限")
@Slf4j
public class AuthorityController {

    @Resource
    private IAuthorityService authorityService;
    @Resource
    private AuthorityMapper authorityMapper;

    @Resource
    HttpServletRequest request;

    /**
     * @param authority
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改权限
     * @author hasd
     * @Date 2023-12-18
     */
    @PostMapping("/save")
    @ApiOperation("新增/修改权限")
    @HasAuthority("server_authority_update")
    @LogAuthority(authorities = "server_authority_update")
    @CacheEvict(cacheNames = "authority.page", allEntries = true)
    public Result save(@RequestBody Authority authority) {

        if (authority.getId() == null) {
            authority.setCreatedAt(LocalDateTime.now());
        } else {
            authority.setUpdatedAt(LocalDateTime.now());
        }
        authorityService.saveOrUpdate(authority);
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
    @HasAuthority("server_authority_page")
    public Result getByPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "type") String type

    ) {

        Page<Authority> page = authorityService.getPage(currentPage, pageSize, name, description, type);
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
    @LogAuthority(authorities = "server_authority_update")
    @HasAuthority("server_authority_update")
    @CacheEvict(cacheNames = "authority.page")
    public Result deleteById(
            @PathVariable(value = "id") int id
    ) {
        authorityService.removeById(id);
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
    @LogAuthority(authorities = "server_authority_update")
    @HasAuthority("server_authority_update")
    @CacheEvict(cacheNames = "authority.page", allEntries = true)
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        ids.forEach(id -> {
            Authority authority = new Authority();
            authority.setDeletedAt(LocalDateTime.now());
            authority.setId(id);
            authorityService.updateById(authority);
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
    @LogAuthority(authorities = "server_authority_update")
    @HasAuthority("server_authority_update")
    @CacheEvict(cacheNames = "authority.page", allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        Authority authority = new Authority();
        UpdateWrapper<Authority> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        authorityMapper.update(wrapper);
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
    @LogAuthority(authorities = "server_authority_update")
    @HasAuthority("server_authority_update")
    @CacheEvict(cacheNames = "authority.page", allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        authorityService.removeBatchByIds(ids);
        return Result.OK();
    }

    /**
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 查看全部主要信息
     * @author hasdsd
     * @Date 2024/1/19
     */
    @ApiOperation("查询可用权限")
    @GetMapping("/all")
    public Result getAll() {
        String roleId = JwtUtil.getTokenInfo(request.getHeader("token"), "roleId");
        List<Authority> auth = authorityService.getAuthorityByRoleId(roleId);
        return Result.OKWithData(auth);
    }


}

