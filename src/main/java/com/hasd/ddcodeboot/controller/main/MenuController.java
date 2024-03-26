package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.Menu;
import com.hasd.ddcodeboot.mapper.MenuMapper;
import com.hasd.ddcodeboot.service.IMenuService;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023-12-19
 **/

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单")
@Slf4j
public class MenuController {

    @Resource
    private IMenuService menuService;
    @Resource
    private MenuMapper menuMapper;

    @Resource
    HttpServletRequest request;

    /**
     * @param menu
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改
     * @author hasd
     * @Date 2023-12-19
     */
    @PostMapping()
    @ApiOperation("新增/修改")
    @HasAuthority("server_menu_update")
    @LogAuthority(authorities = "server_menu_update")
    @CacheEvict(cacheNames = {"menu.page", "menu.page.parent", "menu.page.child"}, allEntries = true)
    public Result save(@RequestBody Menu menu) {

        if (menu.getId() == null) {
            menu.setCreatedAt(LocalDateTime.now());
        } else {
            menu.setUpdatedAt(LocalDateTime.now());
        }
        menuService.saveOrUpdate(menu);
        return Result.OK();
    }

    /**
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 获取全部父级
     * @author hasdsd
     * @Date 2023/12/21
     */
    @GetMapping("/parent")
    @ApiOperation("获取全部权限内父级菜单")
    @Cacheable(cacheNames = {"menu.page.parent"})
    public Result getParent() {
        String roleId = JwtUtil.getTokenInfo(request.getHeader("token"), "roleId");
        List<Menu> menu = menuMapper.getParentMenuFromUser(Integer.valueOf(roleId));
        return Result.OKWithData(menu);
    }

    /**
     * @param currentPage int
     * @param pageSize    int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 分页查询
     * @author hasd
     * @Date 2023-12-19
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    @HasAuthority("server_menu_page")
    @Cacheable(cacheNames = "menu.page")
    public Result getByPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "name", required = false) String name
    ) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("orders", "id");
        wrapper.eq(parentId != null, "parent_id", parentId);
        wrapper.like(!Objects.equals(name, ""), "name", name);

        Page<Menu> page = menuService.page(new Page<>(currentPage, pageSize), wrapper);
        return Result.OKWithPageQuery(page);
    }

    @GetMapping("/child")
    @ApiOperation("获取全部权限内子类菜单")
    @Cacheable(cacheNames = {"menu.page.child"})
    public Result getChild(
    ) {
        String roleId = JwtUtil.getTokenInfo(request.getHeader("token"), "roleId");
        List<Menu> menu = menuMapper.getChildMenuFromUser(Integer.valueOf(roleId));
        return Result.OKWithData(menu);
    }

    /**
     * @param id int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 根据id删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除")
    @LogAuthority(authorities = "server_menu_update")
    @HasAuthority("server_menu_update")
    @CacheEvict(cacheNames = {"menu.page", "menu.page.parent", "menu.page.child"}, allEntries = true)
    public Result deleteById(
            @PathVariable(value = "id") int id
    ) {
        menuService.removeById(id);
        return Result.OK();
    }

    /**
     * @param id int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 根据id恢复
     * @author hasd
     * @Date 2023-12-19
     */
    @PutMapping("/recover/{id}")
    @ApiOperation("根据id恢复")
    @LogAuthority(authorities = "server_menu_update")
    @HasAuthority("server_menu_update")
    @CacheEvict(cacheNames = {"menu.page", "menu.page.parent", "menu.page.child"}, allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        Menu menu = new Menu();
        UpdateWrapper<Menu> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        menuService.update(wrapper);
        return Result.OK();
    }

    /**
     * @param ids list<integer>
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 批量删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/del/batch")
    @ApiOperation("批量删除")
    @LogAuthority(authorities = "server_menu_update")
    @HasAuthority("server_menu_update")
    @CacheEvict(cacheNames = {"menu.page", "menu.page.parent", "menu.page.child"}, allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        menuService.removeBatchByIds(ids);
        return Result.OK();
    }


    /**
     * @param ids list<integer>
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 标记删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/del/sign")
    @ApiOperation("标记删除")
    @LogAuthority(authorities = "server_menu_update")
    @CacheEvict(cacheNames = {"menu.page", "menu.page.parent", "menu.page.child"}, allEntries = true)
    @HasAuthority("server_menu_update")
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        ids.forEach(id -> {
            Menu menu = new Menu();
            menu.setDeletedAt(LocalDateTime.now());
            menu.setId(id);
            menuService.updateById(menu);
        });
        return Result.OK();
    }

    /**
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 获取全部主要信息
     * @author hasdsd
     * @Date 2024/1/19
     */
    @ApiOperation("获取全部主要信息")
    @GetMapping("/all")
    public Result getAll() {
        String roleId = JwtUtil.getTokenInfo(request.getHeader("token"), "roleId");
        List<Menu> menus = menuService.getMenuByRoleId(roleId);
        return Result.OKWithData(menus);
    }
}

