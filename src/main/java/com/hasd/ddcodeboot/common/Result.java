package com.hasd.ddcodeboot.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 15:17
 **/


@AllArgsConstructor
@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;


    public static Result OK(Integer c, String m, Object t) {
        return new Result(c, m, t);
    }

    public static Result OK() {
        return new Result(200, "操作成功", null);
    }

    public static Result OKWithData(Object objects) {
        return new Result(200, "", objects);
    }


    public static Result OKWithPageQuery(Page t) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", t.getTotal());
        map.put("currentPage", t.getCurrent());
        map.put("pageSize", t.getSize());
        map.put("records", t.getRecords());
        return new Result(200, "", map);
    }


    public static Result OKWithMessage(String msg) {
        return new Result(Constants.CODE_OK, msg, null);
    }

    public static Result failWithMessage(Integer code, String m) {
        return new Result(code, m, null);
    }
}
