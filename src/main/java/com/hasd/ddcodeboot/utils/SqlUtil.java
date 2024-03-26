package com.hasd.ddcodeboot.utils;

import java.util.Objects;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 17:37
 **/


public class SqlUtil {
    /**
     * @param currentPage int 当前页
     * @param pageSize    int 页大小
     * @return java.lang.String
     * @Description 分页查询
     * @author hasdsd
     * @Date 2023/11/17
     */
    public static String getByPage(int currentPage, int pageSize) {
        return "limit " + (currentPage - 1) * pageSize + "," + pageSize;
    }


    /**
     * @param object 任意对象
     * @return boolean
     * @Description SQL可选参数
     * @author hasdsd
     * @Date 2023/11/17
     */
    public static boolean condition(Object object) {
        return !Objects.isNull(object);
    }
}
