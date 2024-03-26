package com.hasd.ddcodeboot.utils;

import com.hasd.ddcodeboot.common.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import sun.applet.AppletIOException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : hasd
 * @version 1.0.0
 * @since : 2023/11/17 16:14
 **/


public class FileUtil {

    @Value("${constom.filePath}")
    public String fileUploadPath;

    @Value("${constom.fileUrl}")
    public String fileUploadUrl;

    /**
     * @param fileName string
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Description 将文件名拆分
     * @author hasdsd
     * @Date 2023/12/17
     */
    public static Map<String, Object> SplitFileName(String fileName) {
        HashMap<String, Object> map = new HashMap<>();
        int dotIndex = fileName.indexOf(".");
        String beforeDot = fileName.substring(0, dotIndex);
        String afterDot = fileName.substring(dotIndex + 1);
        map.put("name", beforeDot);
        map.put("type", afterDot);
        return map;
    }


    /**
     * @param file MultipartFile
     * @return java.lang.String
     * @Description 将文件保存到本地
     * @author hasdsd
     * @Date 2023/12/17
     */
    public String Upload(String path, MultipartFile file) throws IOException {

        fileUploadPath = fileUploadPath + "/" + path;
        fileUploadUrl = fileUploadUrl + "/" + path;

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null) {
            throw new AppException("文件名为空");
        }

        Map<String, Object> splitName = FileUtil.SplitFileName(originalFilename);
        String name = (String) splitName.get("name");
        String type = (String) splitName.get("type");

        File fileUploadPath = new File(this.fileUploadPath);
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        String day = new SimpleDateFormat("yy-MM-dd").format(new Date());
        String newFileName = name + "-" + day + "." + type;
        // 文件路径  UUID + 点 + 文件类型
        File uploadFile = new File(fileUploadPath + "/" + newFileName);

        String url;
        file.transferTo(uploadFile);
        url = fileUploadUrl + newFileName;

        return url;
    }

    /**
     * @param fileName string
     * @param response HttpServletResponse
     * @return void
     * @Description 下载文件
     * @author hasdsd
     * @Date 2023/12/17
     */
    public void Download(String fileName, HttpServletResponse response) throws IOException {
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
