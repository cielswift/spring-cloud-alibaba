package com.ciel.scatquick.guavautils;

import io.lettuce.core.ScriptOutputType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class IOUtilsing {

    public static final String PATH = "C:\\ciel\\spring\\alibaba\\quick\\info.log";
    public static final Charset CHARSET = Charset.forName("utf-8");
    public static void main(String[] args) throws IOException {

        String s = IOUtils.toString(new FileInputStream(PATH), CHARSET);

//        closeQuietly：关闭一个io流、socket、或者selector且不抛出异常，通常放在finally块
//        toString：转换io流、uri、byte[]为String
//        copy：IO流数据复制，从输入流写道输出流中，最大支持2GB
//        toByteArray：从输入流、URI获取byte[]
//        write：把字节，字符等写入输出流
//        toInputStream：把字符转换为输入流
//        readLines：从输入流中读取多行数据，返回List<String>
//        copyLarge：同copy，支持2GB以上数据的复制
//        lineIterator：从输入流返回一个迭代器，根据参数要求读取的数据量，全部读取，如果数据不够，则失败。

        String s1 = FileUtils.readFileToString(new File(PATH), CHARSET);
//        deleteDirectory：删除文件夹
//        readFileToString：以字符形式读取文件内容
//        deleteQueitly：删除文件或文件夹且不会抛出异常
//        copyFile：复制文件
//        writeStringToFile：把字符写到目标文件，如果文件不存在，则创建
//        forceMkdir：强制创建文件夹，如果该文件夹父级目录不存在，则创建父级
//        write：把字符写到指定文件中
//        listFiles：列举某个目录下的文件(根据过滤器)
//        copyDirectory：复制文件夹
//        forceDelete：强制删除文件

        String a = FilenameUtils.getExtension("a.avi");
//        getExtension：返回文件后缀名
//        getBaseName：返回文件名，不包含后缀名
//        getName：返回文件全名
//        concat：按命令行风格组合文件路径
//        removeExtension：删除后缀名
//        normalize：使路径正常化
//        wildcardMatch：匹配通配符
//        seperatorToUnix：路径分隔符改成unix系统格式的，即/
//                getFullPath：获取文件路径，不包含文件名
//        isExtension：检查文件后缀名是不是传入参数（List<String>）中的一个


    }
}
