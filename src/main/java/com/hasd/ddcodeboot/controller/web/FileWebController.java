package com.hasd.ddcodeboot.controller.web;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hasd.ddcodeboot.aspect.HasAuthority;
import com.hasd.ddcodeboot.aspect.LogAuthority;
import com.hasd.ddcodeboot.common.AppException;
import com.hasd.ddcodeboot.common.Result;
import com.hasd.ddcodeboot.service.IFileService;
import com.hasd.ddcodeboot.utils.FileUtil;
import com.hasd.ddcodeboot.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.applet.AppletIOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2024/1/27 10:47
 **/

@RestController
@RequestMapping("/file")
@Api(tags = "web-file")
@Slf4j
public class FileWebController {

    @Resource
    IFileService fileService;

    @Resource
    HttpServletRequest request;

    @Value("${constom.filePath}")
    public String fileUploadPath;

    @Value("${constom.fileUrl}")
    public String fileUploadUrl;


    /**
     * @param file
     * @return com.hasd.ddcodeboot.common.Result
     * @Description 上传
     * @author hasdsd
     * @Date 2024/1/27
     */
    @PostMapping("/upload")
    @LogAuthority(authorities = "uploadFile")
    @CacheEvict(cacheNames = "file.page", allEntries = true)
    @HasAuthority("server_file_upload")
    public Result Upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        String userId = JwtUtil.getTokenInfo(request.getHeader("token"), "userId");

        if (file.getOriginalFilename() == null) {
            throw new AppException("文件名为空");
        }

        File fileUploadPath = new File(this.fileUploadPath);
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        long size = file.getSize() / 1024 / 1024;
        if (size > 200) {
            throw new AppException("文件过大");
        }

        //md5存在直接返回
        String md5 = SecureUtil.md5(inputStream);
        com.hasd.ddcodeboot.entity.File existFile = fileService.getOne(new QueryWrapper<com.hasd.ddcodeboot.entity.File>().eq("md5", md5));
        if (existFile != null) {
            return Result.OKWithData(existFile.getUrl());
        }

        //保存到本地
        String type = (String) FileUtil.SplitFileName(originalFilename).get("type");
        String name = IdUtil.simpleUUID() + "." + type;
        String path = fileUploadPath + "/" + name;
        File uploadFile = new File(path);
        file.transferTo(uploadFile);

        //上传数据库
        com.hasd.ddcodeboot.entity.File upload = new com.hasd.ddcodeboot.entity.File();
        upload.setFormat(type);
        upload.setName(name);
        upload.setPath(path);
        upload.setUrl(fileUploadUrl + name);
        upload.setSize((int) size);
        upload.setCreatedAt(LocalDateTime.now());
        upload.setMd5(md5);
        upload.setUserId(Integer.valueOf(userId));
        fileService.save(upload);
        return Result.OK();
    }


    /**
     * @param fileName
     * @Description Download
     * @author hasdsd
     * @Date 2024/1/27
     */
    @GetMapping("/{fileName}")
    @ApiOperation("下载文件")
    @HasAuthority("server_file_download")
    public void Download(
            @PathVariable("fileName") String fileName,
            HttpServletResponse response
    ) throws IOException {

        if (fileName.isEmpty()) {
            throw new AppException("文件名为空");
        }

        File file = new File(fileUploadPath + fileName);

        if (file.exists()) {
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "utf-8"));

            byte[] buffer = new byte[1024];

            FileInputStream fis;
            BufferedInputStream bis;

            OutputStream os;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                throw new AppletIOException("文件不存在");
            }
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
