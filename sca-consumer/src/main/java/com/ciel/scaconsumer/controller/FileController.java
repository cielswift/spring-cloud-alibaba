package com.ciel.scaconsumer.controller;

import com.alibaba.excel.EasyExcel;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaconsumer.config.PersonCon;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class FileController {

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file){
        String name = file.getOriginalFilename();
        try {
            file.transferTo(Paths.get("c:/ciel/".concat(name)));
        } catch (IOException e) {
           return Result.error("文件上传失败");
        }
        return Result.error("文件上传成功".concat(name));
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {

//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" +
//                new String("oth.png".getBytes("UTF-8"),
//                        "iso-8859-1"));
//
//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//        InputStream inputStream = Files.newInputStream(Paths.get("C:/ciel/0.png"));
//        BufferedInputStream bis = new BufferedInputStream(inputStream);
//        byte [] temp = new byte[1024*1024*5];
//        int read = bis.read(temp);
//        response.getOutputStream().write(temp,0,read);
//        response.getOutputStream().close();


        List<PersonCon> dataList = new ArrayList<>();
        dataList.add(new PersonCon("夏培鑫",22, new Date()));
//
//        Workbook workbook = DefaultExcelBuilder.of(PersonCon.class)
//                .build(dataList);
   //  AttachmentExportUtil.export(workbook, "艺术生信息", response);

      //  AttachmentExportUtil.encryptExport(workbook, "艺术生信息", response,"123456");


        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
      //  EasyExcel.write("c:/ciel/a.xlsx", PersonCon.class).sheet("模板").doWrite(dataList);

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), PersonCon.class).sheet("模板").doWrite(dataList);
    }

}
