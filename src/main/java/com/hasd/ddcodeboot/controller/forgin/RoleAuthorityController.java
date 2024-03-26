package com.hasd.ddcodeboot.controller.forgin;

import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.RoleAuthority;
import com.hasd.ddcodeboot.mapper.RoleAuthorityMapper;
import com.hasd.ddcodeboot.service.IRoleAuthorityService;
import com.hasd.ddcodeboot.service.impl.AuthorityServiceImpl;
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
 * @since : 2023-12-18
 **/

@RestController
@RequestMapping("/roleAuthority")
@Api(tags = "角色-权限")
@Slf4j
public class RoleAuthorityController {

    @Resource
    private IRoleAuthorityService roleAuthorityService;
    @Resource
    private RoleAuthorityMapper roleAuthorityMapper;
    @Resource
    private HttpServletRequest request;

    @Resource
    AuthorityServiceImpl authorityService;

    /**
     * @param id
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 根据roleId获取AuthorityId
     * @author hasdsd
     * @Date 2024/1/20
     */
    @GetMapping("/{id}")
    @ApiOperation("根据roleId查询")
    @HasAuthority("server_role_authority_page")
    @Cacheable(cacheNames = {"role_authority.page"}, key = "#id")
    public Result GetIdByRole(@PathVariable("id") int id) {
        List<Integer> list = roleAuthorityMapper.selectAuthorityByRoleId(id);
        return Result.OKWithData(list);
    }

    /**
     * @param roleId
     * @return com.hasd.ddcodeboot.common.Result
     * @Description Update
     * @author hasdsd
     * @Date 2024/1/20
     */
    @PostMapping("/{id}")
    @ApiOperation("更新数据")
    @HasAuthority("server_role_authority_update")
    @LogAuthority(authorities = "server_role_authority_update")
    @CacheEvict(cacheNames = {"role_authority.page", "role.page", "authority.page"}, allEntries = true, key = "#roleId+#newList")
    public Result Update(@PathVariable("id") int roleId, @RequestBody HashSet<Integer> newList) {

        //权限验证
        String token = request.getHeader("token");
        List<Integer> AvailableAuthorityId = authorityService.getAuthorityIdByUserId(JwtUtil.getTokenInfo(token, "roleId"));
        if (!new HashSet<>(AvailableAuthorityId).containsAll(newList) && !JwtUtil.getAuthorities(token).contains("admin")) {
            throw new AppException(401, "修改权限：权限不足");
        }
        if (String.valueOf(roleId).equals(JwtUtil.getTokenInfo(token, "roleId"))) {
            throw new AppException(401, "不能修改自己的权限");
        }

        List<Integer> list = roleAuthorityMapper.selectAuthorityByRoleId(roleId);
        HashSet<Integer> originList = new HashSet<>(list);
        //做并集,保留相同部分
        originList.retainAll(AvailableAuthorityId);

        //需要删除的
        HashSet<Integer> moreList = new HashSet<>(originList);
        moreList.removeAll(newList);
        log.info("需要删除的" + moreList.toString());
        moreList.forEach(authorityId -> {
            roleAuthorityMapper.deleteTable(authorityId, roleId);
        });
        //需要新增的
        HashSet<Integer> lessList = new HashSet<>(newList);
        lessList.removeAll(originList);
        log.info("需要新增的" + lessList.toString());
        lessList.forEach(authorityId -> {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(roleId);
            roleAuthority.setAuthorityId(authorityId);
            roleAuthorityMapper.insert(roleAuthority);
        });
        return Result.OK();
    }
}

