package com.hasd.ddcodeboot.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hasd.ddcodeboot.entity.Log;

/**
 * <p>
 * 服务类
 *
 * @author hasd
 * @since 2024-01-22
 */
public interface ILogService extends IService<Log> {


    Page<Log> getPage(int currentPage, int pageSize, String userName, String methodName, String authorityName);

}
