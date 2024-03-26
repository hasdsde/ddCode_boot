package com.hasd.ddcodeboot.controller.forgin;

import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.RoleMenu;
import com.hasd.ddcodeboot.mapper.RoleMenuMapper;
import com.hasd.ddcodeboot.service.IMenuService;
import com.hasd.ddcodeboot.service.IRoleMenuService;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024-01-20
 **/

@RestController
@RequestMapping("/roleMenu")
@Api(tags = "")
@Slf4j
public class RoleMenuController {

    @Resource
    private IRoleMenuService roleMenuService;
    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    private HttpServletRequest request;

    /**
     * @param id
     * @return com.hasd.ddcodeboot.common.Result
     * @Description GetIdByRole
     * @author hasdsd
     * @Date 2024/1/20
     */
    @GetMapping("/{id}")
    @ApiOperation("根据roleId查询")
    @HasAuthority("server_role_menu_page")
    @Cacheable(cacheNames = {"role_menu.page"}, key = "#id")
    public Result GetIdByRole(@PathVariable("id") int id) {
        List<Integer> list = roleMenuMapper.selectMenuByRoleId(id);
        return Result.OKWithData(list);
    }


    /**
     * @param roleId
     * @param newList
     * @return com.hasd.ddcodeboot.common.Result
     * @Description Update
     * @author hasdsd
     * @Date 2024/1/20
     */
    @PostMapping("/{id}")
    @ApiOperation("更新数据")
    @LogAuthority(authorities = "server_role_menu_update")
    @HasAuthority("server_role_menu_update")
    @CacheEvict(cacheNames = {"role_menu.page", "role.page", "menu.page"}, allEntries = true, key = "#roleId+#newList")
    public Result Update(@PathVariable("id") int roleId, @RequestBody HashSet<Integer> newList) {

        //权限验证
        String token = request.getHeader("token");
        List<Integer> AvailableMenuId = menuService.getMeuIdByRoleId(JwtUtil.getTokenInfo(token, "roleId"));
        if (!new HashSet<>(AvailableMenuId).containsAll(newList) && !JwtUtil.getAuthorities(token).contains("admin")) {
            throw new AppException(401, "修改菜单：权限不足");
        }
        if (String.valueOf(roleId).equals(JwtUtil.getTokenInfo(token, "roleId"))) {
            throw new AppException(401, "不能修改自己的权限");
        }

        List<Integer> list = roleMenuMapper.selectMenuByRoleId(roleId);
        HashSet<Integer> originList = new HashSet<>(list);
        //两个list交集，筛出公共部分，其余部分不做修改
        originList.retainAll(AvailableMenuId);

        //需要删除的
        HashSet<Integer> moreList = new HashSet<>(originList);
        moreList.removeAll(newList);
        moreList.forEach(menuId -> {
            roleMenuMapper.deleteTable(menuId, roleId);
        });
        //需要新增的
        HashSet<Integer> lessList = new HashSet<>(newList);
        lessList.removeAll(originList);
        lessList.forEach(authorityId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(authorityId);
            roleMenuMapper.insert(roleMenu);
        });
        return Result.OK();
    }
}

