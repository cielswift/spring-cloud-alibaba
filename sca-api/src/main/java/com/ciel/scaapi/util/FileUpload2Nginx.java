package com.ciel.scaapi.util;

import cn.hutool.core.lang.Snowflake;
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

    protected Snowflake snowflake = new Snowflake(7,1);

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

        //文件名
        String fileName = getFileName(multipartFile);

        //日期路径
        String pathByDate = pathByDate(Faster.now());

        //日期路径 +  文件名
        String path = pathByDate + fileName;

        //是否存在 创建
        chekAndCreat(imgSaveUri + pathByDate);

        multipartFile.transferTo(new File(imgSaveUri + path));

        //文件权限
        setPremmers(imgSaveUri + path);

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

        //获取一个随机的文件名称
        String fileName = getFileName(multipartFile);

        String pathByDate = pathByDate(Faster.now());

        String path = pathByDate + fileName;

        chekAndCreat(fileSaveUri + pathByDate);

        multipartFile.transferTo(new File(fileSaveUri + path));

        setPremmers(fileSaveUri +  path);

        log.info("文件保存完成-> 原始文件名:{}, 保存路径:{}  耗时:{}"
                ,multipartFile.getOriginalFilename(),imgSaveUri + path,System.currentTimeMillis() -str);

        return fileAccUri + path;
    }


    public String getFileName(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        int lastIndexOf = filename.lastIndexOf(".");
        String exName = filename.substring(lastIndexOf, filename.length());
        return String.valueOf(snowflake.nextId()).concat(exName);
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
