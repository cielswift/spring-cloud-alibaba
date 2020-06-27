package com.ciel.scaconsumer.controller;

import com.alibaba.excel.EasyExcel;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class ExcelController {

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {

        List<PersonCon> dataList = new ArrayList<>();
        dataList.add(new PersonCon("夏培鑫",22, Faster.now()));

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), PersonCon.class)
                .sheet("模板")
                .doWrite(dataList);
    }


    /**
     * 导出excel 根据字段导出
     * @param list 数据载体
     * @param response  响应对象
     * @param fileName  导出文件名
     * @param cloums  需要导出的字段 "类属性名:列名称" 例如 "residentName:反馈居民姓名"
     *                          如果只想要列名 没有具体值,那么直接写列名 例如 "满意","基本满意","不满意"
     *
     *                      测试案例 ExcelImportExportUtils.writeExcelByCloum(list,response,"导出",
     *                            "residentName:反馈居民姓名",
     *                           "residentPhone:反馈居民手机号",
     *                            "category:意见建议类别",
     *                            "content:意见建议内容",
     *                            "createTime:创建时间",
     *                          "满意","基本满意","不满意");
     */
    public static <T> void writeExcelByCloum(List<T> list, HttpServletResponse response,
                                             String fileName, List<String> cloums) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet(fileName);

        XSSFRow row = sheet.createRow(0);

        List<String> cloun = new ArrayList<>();

        for (int i = 0; i < cloums.size(); i++) {

            String[] split = cloums.get(i).split(":");

            if(split.length==2){
                cloun.add(split[0]);
                row.createCell(i).setCellValue(split[1]);
            }else{
                row.createCell(i).setCellValue(cloums.get(i));
            }
        }

        for (int i = 0; i < list.size(); i++) {
            XSSFRow temp = sheet.createRow(i + 1);

            T t = list.get(i);

            Class<?> aClass = t.getClass();

            for (int j = 0; j < cloun.size(); j++) {

                Field field = null;
                try{
                    field = aClass.getDeclaredField(cloun.get(j));
                }catch (Exception e){
                    Class<?> superclass = aClass.getSuperclass();
                    field = superclass.getDeclaredField(cloun.get(j));
                }

                field.setAccessible(true);
                Object o = field.get(t);

                Object resul = null;

                if(o instanceof Date){
                  //  resul =  Dates.formatDetail().format((Date) o);
                }else{
                    resul = o;
                }

                temp.createCell(j).setCellValue(resul == null ? "" : resul.toString());
            }
        }


        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(fileName.concat(".xlsx"), CharEncoding.UTF_8).replace("+", "%20"));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //文档输出
        workbook.write(response.getOutputStream());
        response.getOutputStream().close();
    }

    @Data
    @AllArgsConstructor
    public static class PersonCon{
        private String name;
        private Integer age;
        private Date birthday;
    }
}
