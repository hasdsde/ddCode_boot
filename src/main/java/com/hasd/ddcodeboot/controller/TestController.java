package com.hasd.ddcodeboot.controller;

import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.common.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/20 17:38
 **/

@RestController
@RequestMapping("/test")
@Api(tags = "web-test")
@Slf4j
public class TestController {

    @HasAuthority({"auth2"})
    @GetMapping("/test1")
    public Result Test1() {
        return Result.OK();
    }
}
