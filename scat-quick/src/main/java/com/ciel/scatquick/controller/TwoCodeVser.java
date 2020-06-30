package com.ciel.scatquick.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tco")

/**
 * 二维码
 */
public class TwoCodeVser {

    private volatile static int WIDTH = 800;
    private volatile static int HEIGHT = 800;
    private volatile static String FORMAT = "png";

    @RequestMapping("/cre")
    public String cre() throws IOException, WriterException {
        String replace = UUID.randomUUID().toString().replace("-", "");

        Map hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置容错等级
        hints.put(EncodeHintType.MARGIN, 2);//设置边距默认是5

        BitMatrix bitMatrix = new MultiFormatWriter().encode(replace, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        DateTime dt = new DateTime(new Date());
        String ph = dt.getYear() + "/" + dt.getMonthOfYear() + "/" + dt.getDayOfMonth() + "/";
        File filePath = new File("");

        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File dest = new File(filePath, replace + ".png");
        OutputStream outputStream = new FileOutputStream(dest);

        //MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, outputStream);//写到指定路径下

        BufferedImage bufferedImage = new BufferedImage(bitMatrix.getWidth(),bitMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < bitMatrix.getWidth(); x++) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        BufferedImage bufferedImage1 = insertLogo(bufferedImage);

        ImageIO.write(bufferedImage1,FORMAT,outputStream); //写到指定路径下


        ObjectMapper objectMapper = new ObjectMapper(); //jackjson的转换器
        Map<String,String> map = new HashMap<>();
        map.put("path","" + ph + replace + ".png");
        objectMapper.writeValueAsString(map);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", "" + ph + replace + ".png");

        return jsonObject.toJSONString();
    }

    @PostMapping("/read")
    public String read(@RequestParam("file") MultipartFile file) throws Exception {
        MultiFormatReader reader = new MultiFormatReader();

        BufferedImage image = ImageIO.read(file.getInputStream());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Map hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        Result result = reader.decode(binaryBitmap, hints);
        System.out.println("解析结果:" + result.toString());
        System.out.println("二维码格式:" + result.getBarcodeFormat());
        System.out.println("二维码文本内容:" + result.getText());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("txt", result.toString());
        return jsonObject.toString();

    }

    private BufferedImage insertLogo(BufferedImage matrixImage) throws IOException {

        Graphics2D g2 = matrixImage.createGraphics();

        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        /**
         * 读取Logo图片
         */
        BufferedImage logo = ImageIO.read(new File("c:/ciel/LOGO.PNG"));

        //开始绘制图片
        g2.drawImage(logo,matrixWidth/5*2,matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5, null);//绘制
        BasicStroke stroke = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke);// 设置笔画对象
        //指定弧度的圆角矩形
        RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth/5*2, matrixHeigh/5*2, matrixWidth/5, matrixHeigh/5,20,20);
        g2.setColor(Color.white);
        g2.draw(round);// 绘制圆弧矩形

        //设置logo 有一道灰色边框
        BasicStroke stroke2 = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);// 设置笔画对象
        RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth/5*2+2, matrixHeigh/5*2+2, matrixWidth/5-4, matrixHeigh/5-4,20,20);
        g2.setColor(new Color(128,128,128));
        g2.draw(round2);// 绘制圆弧矩形

        g2.dispose();
        matrixImage.flush() ;
        return matrixImage ;
    }


}