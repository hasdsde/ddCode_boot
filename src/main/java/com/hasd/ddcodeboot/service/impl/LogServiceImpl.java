package com.hasd.ddcodeboot.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.Log;
import com.hasd.ddcodeboot.mapper.LogMapper;
import com.hasd.ddcodeboot.service.ILogService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2024-01-22
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Resource
    LogMapper logMapper;

    @Override
    @Cacheable(cacheNames = "log.page", key = "#currentPage+#pageSize+#userName+#methodName+#authorityName")
    public Page<Log> getPage(int currentPage, int pageSize, String userName, String methodName, String authorityName) {
        Page<Log> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        wrapper.like(!userName.isEmpty(), "user_name", userName);
        wrapper.like(!methodName.isEmpty(), "method_name", methodName);
        wrapper.like(!authorityName.isEmpty(), "authority_name", authorityName);
        wrapper.orderByDesc("id");
        return logMapper.selectPage(page, wrapper);
    }
}
