package com.ciel.scaapi.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Random;

@Component
@ConfigurationProperties(prefix = "upload")
@Getter
@Setter
@Slf4j
public class FileUpload2Nginx {

    protected String fileSaveUri;

    protected String imgSaveUri;

    protected String fileAccUri;

    protected String imgAccUri;

    /**
     * 图片保存
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String imgSaveReturn(MultipartFile multipartFile) throws IOException {

        log.info("图片上传开始-> 原始文件名:{}",multipartFile.getOriginalFilename());
        long str = System.currentTimeMillis();

        String fileName = getFileName(multipartFile);

        String pathByDate = pathByDate(Faster.now());

        String path = pathByDate + fileName;

        chekAndCreat(imgSaveUri + pathByDate);

        multipartFile.transferTo(new File(imgSaveUri + path));

        setPremmers(path);

        log.info("图片保存完成-> 原始文件名:{}, 保存路径:{}  耗时:{}"
                ,multipartFile.getOriginalFilename(),imgSaveUri + path,System.currentTimeMillis() -str);

        return imgAccUri + path;
    }

    /**
     * 文件保存
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String fileSaveReturn(MultipartFile multipartFile) throws IOException {

        log.info("文件上传开始-> 原始文件名:{}",multipartFile.getOriginalFilename());
        long str = System.currentTimeMillis();

        String fileName = getFileName(multipartFile);

        String pathByDate = pathByDate(Faster.now());

        String path = pathByDate + fileName;

        chekAndCreat(fileSaveUri + pathByDate);

        multipartFile.transferTo(new File(fileSaveUri + path));

        setPremmers(path);

        log.info("文件保存完成-> 原始文件名:{}, 保存路径:{}  耗时:{}"
                ,multipartFile.getOriginalFilename(),imgSaveUri + path,System.currentTimeMillis() -str);

        return fileAccUri + path;
    }


    public String getFileName(MultipartFile multipartFile) {
        int lastIndexOf = multipartFile.getOriginalFilename().lastIndexOf(".");
        String exName = multipartFile.getOriginalFilename().substring(lastIndexOf,
                multipartFile.getOriginalFilename().length());
        return String.valueOf(System.currentTimeMillis() + 17L) + exName;
    }


    public void chekAndCreat(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    public String pathByDate(Date date) {
       return Faster.formatFile(date);
    }

    public void setPremmers(String path) throws IOException {
        // 设置文件权限
        if (!System.getProperty("os.name").startsWith("Win")) { //设置文件权限, 执行终端/脚本命令
            String cmdGrant = "chmod -R 777 " + path;
            Runtime.getRuntime().exec(cmdGrant);
        }
    }

}
