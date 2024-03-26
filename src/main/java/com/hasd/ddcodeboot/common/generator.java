package com.hasd.ddcodeboot.common;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/16 10:34
 **/


public class generator {
    public static final String TABLE_NAME = "log";
    public static final String AUTHOR = "hasd";
    public static final String DIR = "D:/Code/ddCode-boot/";
    public static final String PARENT_CLASS = "com.hasd.ddcodeboot";

    public static final String DB_URL = "jdbc:mysql://localhost:3306/ddcode?useUnicode=true&characterEncoding=utf8&useSSL=true";
    public static final String DB_USERNAME = "admin";
    public static final String DB_PASSWORD = "123";
    //Mapper 路径
    public static final String OUT_PUT_FILE = DIR + "src/main/resources/mapper";
    // 父级路径
    public static final String PARENT_PATH = DIR + "src/main/java";


    public static void main(String[] args) {
        FastAutoGenerator.create(
                        DB_URL,
                        DB_USERNAME,
                        DB_PASSWORD)
                .globalConfig(builder -> {
                    //全局配置
                    builder.author(AUTHOR)
                            .fileOverride()
                            .outputDir(PARENT_PATH)
                            .disableOpenDir()
                            .enableSwagger()
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    // 包配置
                    builder.parent(PARENT_CLASS)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, OUT_PUT_FILE));
                })
                .templateConfig(builder -> {
                    //模板配置
                    builder.controller("/templates/controller.java.vm");
                })
                //自定义模板配置
//                .injectionConfig(consumer -> {
//                    HashMap<String, String> customFile  = new HashMap<>();
//                    consumer.customFile(customFile);
//                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAME)// 设置表名
                            .entityBuilder()    //entity 前置，才能用 lombok
                            .enableLombok() //lombok 注解
                            .mapperBuilder()//mapper 注解
                            .enableMapperAnnotation()// 使用 lombok
                            .controllerBuilder() //RestController 前置
                            .enableRestStyle();// 使用 RestController
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();
    }
}
