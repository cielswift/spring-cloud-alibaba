package com.ciel.scatquick.controller;

import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scatquick.aoptxspi.LogsAnnal;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tco")

/**
 * 二维码
 */
public class TwoCodeController {

    private volatile static int WIDTH = 800;
    private volatile static int HEIGHT = 800;

    private BufferedImage insertLogo(BufferedImage matrixImage) throws IOException {
        Graphics2D g2 = matrixImage.createGraphics();
        int matrixWidth = matrixImage.getWidth();
        int matrixHeigh = matrixImage.getHeight();

        ClassPathResource classPathResource = new ClassPathResource("sources/girl.PNG");

        BufferedImage logo = ImageIO.read(classPathResource.getInputStream());
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

    @LogsAnnal
    @RequestMapping("/cre")
    public void cre() throws IOException, WriterException {

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置容错等级
        hints.put(EncodeHintType.MARGIN, 2);//设置边距默认是5

        BitMatrix bitMatrix = new MultiFormatWriter().encode("http://106.12.213.120:3000/", BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        //MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, outputStream);//写到指定路径下
        BufferedImage bufferedImage =
                new BufferedImage(bitMatrix.getWidth(),bitMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < bitMatrix.getWidth(); x++) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        BufferedImage resultImg = insertLogo(bufferedImage);


        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resultImg, "png", os);
        InputStream input = new ByteArrayInputStream(os.toByteArray());

        Faster.binary(input,SysUtils.currentResponse());

    }

    @PostMapping("/read")
    public com.ciel.scaapi.retu.Result read(@RequestParam("file") MultipartFile file) throws Exception {
        MultiFormatReader reader = new MultiFormatReader();

        BufferedImage image = ImageIO.read(file.getInputStream());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");//

        Result result = reader.decode(binaryBitmap, hints);

        System.out.println("解析结果:" + result.toString());
        System.out.println("二维码格式:" + result.getBarcodeFormat());
        System.out.println("二维码文本内容:" + result.getText());

        return com.ciel.scaapi.retu.Result.ok().data(result);
    }

}