package com.hasd.ddcodeboot.common;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/22 15:22
 **/

@Component
public class ThreadPool {
    //不是多线程用不起，而是@after切面更有性价比
    @Getter
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    private ThreadPool() {
    }

}
