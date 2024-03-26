package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.File;
import com.hasd.ddcodeboot.mapper.FileMapper;
import com.hasd.ddcodeboot.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024-01-26
 **/

@RestController
@RequestMapping("/file")
@Api(tags = "文件表")
@Slf4j
public class FileController {

    @Resource
    private IFileService fileService;
    @Resource
    private FileMapper fileMapper;


    /**
     * @param file
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 新增/修改文件表
     * @author hasd
     * @Date 2024-01-26
     */
    @PostMapping()
    @ApiOperation("新增/修改文件表")
    @HasAuthority("server_file_update")
    @CacheEvict(cacheNames = "file.page", allEntries = true)
    public Result Save(@RequestBody File file) {

        if (file.getId() == null) {
            file.setCreatedAt(LocalDateTime.now());
        } else {
            file.setUpdatedAt(LocalDateTime.now());
        }
        fileService.saveOrUpdate(file);
        return Result.OK();
    }


    /**
     * @param currentPage int
     * @param pageSize    int
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 分页查询
     * @author hasd
     * @Date 2024-01-26
     */
    @GetMapping("/page")
    @HasAuthority("server_file_page")
    @ApiOperation("分页查询")
    public Result Get(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam("name") String name,
            @RequestParam("format") String format
    ) {
        Page<File> page = fileService.selectPage(currentPage, pageSize, name, format);
        return Result.OKWithPageQuery(page);
    }

    /**
     * @param id int
     * @return common.com.hasd.ddcodeboot.Result
     * @Description 根据id恢复
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/recover/{id}")
    @HasAuthority("server_file_update")
    @ApiOperation("根据id恢复")
    @CacheEvict(cacheNames = "file.page", allEntries = true)
    public Result recoverById(
            @PathVariable(value = "id") int id
    ) {
        UpdateWrapper<File> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id).set("deleted_at", null);
        fileService.update(wrapper);
        return Result.OK();
    }


    /**
     * @param ids list<integer>
     * @return common.com.hasd.ddcodeboot.Result
     * @Description 批量删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/del/batch")
    @HasAuthority("server_file_update")
    @ApiOperation("批量删除")
    @CacheEvict(cacheNames = "file.page", allEntries = true)
    public Result deleteByIds(
            @RequestBody List<Integer> ids
    ) {
        fileService.removeBatchByIds(ids);
        return Result.OK();
    }


    /**
     * @param ids list<integer>
     * @return common.com.hasd.ddcodeboot.Result
     * @Description 标记删除
     * @author hasd
     * @Date 2023-12-19
     */
    @DeleteMapping("/del/sign")
    @HasAuthority("server_file_update")
    @ApiOperation("标记删除")
    @CacheEvict(cacheNames = "file.page", allEntries = true)
    public Result deleteSignByIds(
            @RequestBody List<Integer> ids
    ) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        ids.forEach(id -> {
            File file = new File();
            file.setDeletedAt(LocalDateTime.now());
            file.setId(id);
            fileService.updateById(file);
        });
        return Result.OK();
    }

}

