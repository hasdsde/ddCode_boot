package com.hasd.ddcodeboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hasd.ddcodeboot.entity.File;
import com.hasd.ddcodeboot.mapper.FileMapper;
import com.hasd.ddcodeboot.service.IFileService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author hasd
 * @since 2024-01-26
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Resource
    private FileMapper fileMapper;


    @Override
    @Cacheable(cacheNames = "file.page", key = "#currentPage + #pageSize+#name+#format")
    public Page<File> selectPage(int currentPage, int pageSize, String name, String format) {
        Page<File> page = new Page<>(currentPage, pageSize);
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.like(!name.isEmpty(), "name", name);
        wrapper.like(!format.isEmpty(), "format", format);
        wrapper.orderByDesc("id");
        return fileMapper.selectPage(page, wrapper);
    }
}
