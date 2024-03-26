package com.hasd.ddcodeboot.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.entity.Log;
import com.hasd.ddcodeboot.mapper.LogMapper;
import com.hasd.ddcodeboot.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024-01-22
 **/

@RestController
@RequestMapping("/log")
@Api(tags = "日志")
@Slf4j
public class LogController {

    @Resource
    private ILogService logService;
    @Resource
    private LogMapper logMapper;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    @HasAuthority("server_authority_page")
    public Result getByPage(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "methodName") String methodName,
            @RequestParam(value = "authorityName") String authorityName
    ) {
        Page<Log> page = logService.getPage(currentPage, pageSize, userName, methodName, authorityName);
        return Result.OKWithPageQuery(page);
    }

    /**
     * @param log
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 保持log对象存在
     * @author hasd
     * @Date 2023-12-18
     */
    @PostMapping("/save")
    @ApiOperation("新增")
    @CacheEvict(cacheNames = "authority.page", allEntries = true)
    public Result save(@RequestBody Log log) {

        return Result.OK();
    }
}

