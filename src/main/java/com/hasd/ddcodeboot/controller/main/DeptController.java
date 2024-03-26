package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.Dept;
import com.hasd.ddcodeboot.mapper.DeptMapper;
import com.hasd.ddcodeboot.service.IDeptService;
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
@RequestMapping("/dept")
@Api(tags = "部门")
@Slf4j
public class DeptController {

    @Resource
    private IDeptService deptService;
    @Resource
    private DeptMapper deptMapper;

    @Resource
    HttpServletRequest request;

    /**
     * @param dept
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改部门
     * @author hasd
     * @Date 2023-12-18
     */
    @PostMapping("/save")
    @ApiOperation("新增/修改部门")
    @LogAuthority(authorities = "server_dept_update")
    @HasAuthority("server_dept_update")
    @CacheEvict(cacheNames = {"dept.page", "dept.id"}, allEntries = true)
    public Result save(@RequestBody Dept dept) {
        //权限验证
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        if (dept.getId() == null) {
            if (!availableDeptId.contains(dept.getParentId())) {
                throw new AppException(401, "不能设置的父级");
            }
            dept.setCreatedAt(LocalDateTime.now());
        } else {
            if (!availableDeptId.contains(dept.getParentId()) || !availableDeptId.contains(dept.getId())) {
                throw new AppException(401, "不能设置的父级或范围外的id");
            }
            dept.setUpdatedAt(LocalDateTime.now());
        }
        deptService.saveOrUpdate(dept);
        return Result.OK();
    }


    /**
     * @param currentPage int
     * @param pageSize    int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 权限内分页查询
     * @author hasd
     * @Date 2023-12-18
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    @HasAuthority("server_dept_page")
    public Result getByPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "parentId") String parentId

    ) {
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        Page<Dept> page = deptService.getPage(currentPage, pageSize, deptId, name, description, parentId);
        return Result.OKWithPageQuery(page);
    }

    /**
     * @param id int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 根据id删除
     * @author hasd
     * @Date 2023-12-18
     */
    @DeleteMapping("/recover/{id}")
    @LogAuthority(authorities = "server_dept_update")
    @ApiOperation("根据id恢复")
    @HasAuthority("server_dept_update")
    @CacheEvict(cacheNames = {"dept.page", "dept.id"}, allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        //权限验证
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        if (!availableDeptId.contains(id)) {
            throw new AppException(401, "无权限");
        }

        UpdateWrapper<Dept> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        deptMapper.update(wrapper);
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
    @HasAuthority("server_dept_update")
    @LogAuthority(authorities = "server_dept_update")
    @ApiOperation("批量删除")
    @CacheEvict(cacheNames = {"dept.page", "dept.id"}, allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        //权限验证
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        if (!new HashSet<>(availableDeptId).containsAll(ids)) {
            throw new AppException(401, "无权限");
        }

        deptService.removeBatchByIds(ids);
        return Result.OK();
    }


    /**
     * @param ids list<integer>
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 标记删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/sign")
    @LogAuthority(authorities = "server_dept_update")
    @ApiOperation("标记删除")
    @HasAuthority("server_dept_update")
    @CacheEvict(cacheNames = {"dept.page", "dept.id"}, allEntries = true)
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        //权限验证
        String deptId = JwtUtil.getTokenInfo(request.getHeader("token"), "deptId");
        List<Integer> availableDeptId = deptService.getDeptIdByDeptId(deptId);
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        log.warn(availableDeptId.toString());
        if (!new HashSet<>(availableDeptId).containsAll(ids)) {
            throw new AppException(401, "无权限");
        }

        ids.forEach(id -> {
            Dept dept = new Dept();
            dept.setDeletedAt(LocalDateTime.now());
            dept.setId(id);
            deptService.updateById(dept);
        });
        return Result.OK();
    }


    /**
     * @return com.hasd.ddcodeboot.common.Result
     * @Description getAllDept
     * @author hasdsd
     * @Date 2024/1/19
     */
    @GetMapping("/all")
    @ApiOperation("获取权限内部门分类")
    public Result getAll(
    ) {
        String token = request.getHeader("token");
        String deptId = JwtUtil.getTokenInfo(token, "deptId");
        List<Dept> dept = deptService.getDeptByDeptId(deptId);
        return Result.OKWithData(dept);
    }

}

