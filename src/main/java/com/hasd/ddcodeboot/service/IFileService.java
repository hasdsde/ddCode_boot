package com.hasd.ddcodeboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.File;


/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author hasd
 * @since 2024-01-26
 */
public interface IFileService extends IService<File> {
    Page<File> selectPage(int currentPage, int pageSize, String name, String format);
}
