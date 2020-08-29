package com.ciel.scatquick.j8;

import javax.crypto.Cipher;
import javax.crypto.Cipher;
import java.math.BigDecimal;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class J8RSA {

    /**
     * -  java.security.Signature.getInstance(String algorithm); //根据对应算法，初始化签名对象
     * - KeyFactory.getInstance(String algorithm);// 根据对应算法,生成KeyFactory对象
     * - KeyFactory.generatePrivate(KeySpec keySpec); //生成私钥
     * - java.security.Signature.initSign(PrivateKey privateKey) //由私钥，初始化加签对象
     * - java.security.Signature.update(byte[] data)  //把原始报文更新到加签对象
     * - java.security.Signature.sign();//加签
     *
     * - java.security.Signature.getInstance(String algorithm); //根据对应算法，初始化签名对象
     * - KeyFactory.getInstance(String algorithm);// 根据对应算法,生成KeyFactory对象
     * - KeyFactory.generatePublic(KeySpec keySpec); //生成公钥
     * - java.security.Signature.initVerify(publicKey); //由公钥，初始化验签对象
     * - java.security.Signature.update(byte[] data)  //把原始报文更新到验签对象
     * - java.security.Signature.verify(byte[] signature);//验签
     */
    public static final String privatekey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHgwP36Jk5zQSEwFMOkW6iTtMDGXWSB7YsuUIGYZ2oNR7Jiqit+vJ0CfUMXcSZ2to0GDZ6rf+AZIAyyGRkGOm0Pp95uI5SS0XyN+xDk6Vz10A8APdHXLxkeOrnlMG+JXXG5TRS/NhwImYWnq1+WTBO1eezebnR6EmpE6yXTn1QEJIT1/70PbYo+vXvtRIIb2nfZMG5w6ncZcuY3U6xLYmZ/Wq1hZZkI4jyDV7P5Di6Un07NVMFj19PDNkWeZyA/vv3mxRydosLzESomC1f0NVDEV+2yX4pw1YlFOEQSiWgVDuprjaZ0pf0jSaR7QJFr1NkzPvdJazo/eq1CtWocCttAgMBAAECggEAIMExV134k3kTy4a1TMMoa8EDzi78NST6CstowyQbOIpGe86xQQR0UEoL2kZb/BhjP85OdM7UAFPKjPvVEEDZVIECiShr9kZNjOOpjxgEGQEBfL97BdvLibmUhg912SEy27WRNTH6mvgllvgZ1X13aMbZb49PtAYir+SahiQptA1AdQ8wVrZtRX/4vz7RMZvePqAG5pVxOf3Bgyt+FhdeAMgXJ6OBTMhBZ+lr2H4YkLXrU6LleV4zeki8LhVZ1uBzQ/2o5PAkzqxaLD04WjrZ3J7nZHlH2jvgj/IsZviFg7P6VXOvUrJvM6PAgsxPyDPPqCRQLTuPgLBRy+f1MfkMQQKBgQDvFNxM9Ls3vWvHJOUueQ6mZDbXiyxTlvcqjXeIZrwi9OAAlyP2z60faXQerd5HRpYj/I2ef7PzWzonk/Opa9IVdZJXIe6IGXVM863VqpBPyeoMg3Sls1XbYUHZpUcGGLv7vF2BAklZ87htHVQay5MGfAoShMcCPNYBNvOxxneBPQKBgQCRGemWyQUexEsP11f0Ox2aP11WbpTiNwiI0iw9HDdiBUj8CX2+aOJ1fV6sGm294wKAS8H7Atkxq2NGVfGYtX/+86kWOHOOamDHkiXJspUOVGiAHvEpZxh6ZWb5PirzgSvHeaMuNV1Jed/jk8Yg2t7gU7JPNngXcgd0KbhKwAOV8QKBgBBJ2ox/q/WrORmTQO3+n2nkr/vVZoq3YVWL19X6Md0r08sWgQPCuGfIdnnUnK08eOQww7FFwAvXbkneAZ7MUr7ViMfY7vhky6IXhANnoHdfKUv69MqJQlM0+BiM8x7ONph7B9/PORIg0bLVabJ9piGt9721QB296VKh3M6C2Ad9AoGADhBE46h4JpM/8zkb/T/9joW1tjrhk0tiOGCmiQXDGG0KteL1nQ7tZBXSpzuoh08JXwX4tyt/gaDq7lZGJFbzIPLc4Jp4GMWWu8EeiH4WlFz6A/D7ztd/N928LUwpPZC3fY38ku35LLXzTUt1WJneID/8eFD/MsaKkme4xQX3UwECgYEArpMqXE4X8dnJtCHOb/p3W7YZE9Q9IWUcclyFeS2vgd2mUjjqvaYaY5FEN2hGS0eTzeBeL2hdOt6moXrvEywIF/8KRMoSqatAf9RZ3HHgpcfPUmaJW0LzOe2frigTsJ8KwNW7Dhmzob7bt01cyiIki7r3jaTLjs7jFkw0iMPkL+4=";
    public static final String publickey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh4MD9+iZOc0EhMBTDpFuok7TAxl1kge2LLlCBmGdqDUeyYqorfrydAn1DF3EmdraNBg2eq3/gGSAMshkZBjptD6febiOUktF8jfsQ5Olc9dAPAD3R1y8ZHjq55TBviV1xuU0UvzYcCJmFp6tflkwTtXns3m50ehJqROsl059UBCSE9f+9D22KPr177USCG9p32TBucOp3GXLmN1OsS2Jmf1qtYWWZCOI8g1ez+Q4ulJ9OzVTBY9fTwzZFnmcgP7795sUcnaLC8xEqJgtX9DVQxFftsl+KcNWJRThEEoloFQ7qa42mdKX9I0mke0CRa9TZMz73SWs6P3qtQrVqHArbQIDAQAB";

    public static void main(String[] args) throws Exception {
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        //私钥加密, 私钥和公钥都可以解密;
        //公钥加密, 只能私钥解密

//        比如A和B都有一套自己的公钥和私钥，当A要给B发送消息时，
//        先用B的公钥对消息加密，再对加密的消息使用A的私钥加签名，达到既不泄露也不被篡改，更能保证消息的安全性。
//　　     总结：公钥加密、私钥解密、私钥签名、公钥验签。

        //加密字符串
        String message = "夏培鑫是你爹";

        //System.out.println("随机生成的公钥为:" + keyMap.get(0));
        // System.out.println("随机生成的私钥为:" + keyMap.get(1));

        String messageEn = encrypt(message, publickey);
        System.out.println(message + "\t 公钥:加密后的字符串为:" + messageEn);

        String encryptS = encryptS(message, privatekey);
        System.out.println(message + "\t 私钥:加密后的字符串为:" + encryptS);

        String messageDe = decrypt(messageEn, privatekey);
        System.out.println("私钥: 还原后的字符串为:" + messageDe);

        String decryptG = decryptG(encryptS, publickey);
        System.out.println("公钥: 还原后的字符串为:" + decryptG);

        String sign = sign(message, privatekey, "utf-8");
        System.out.println("签名后的字符串:" + sign);

        boolean verify = verify(message, sign, publickey, "utf-8");
        System.out.println("合法吗:" + verify);
        System.out.println(System.currentTimeMillis()-timeMillis);

    }

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    public static String encryptS(String str, String privatekey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(privatekey);
        RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static String decryptG(String str, String publickey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publickey);
        PublicKey priKey = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }


    public static String sign(String content, String privateKey, String input_charset) {
        try
        {
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec(  Base64.decodeBase64(privateKey) );
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );

            byte[] signed = signature.sign();

            return Base64.encodeBase64String(signed);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return null;
    }

    public static boolean verify(String content, String sign, String ali_public_key, String input_charset) {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(ali_public_key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update( content.getBytes(input_charset) );

            boolean bverify = signature.verify(Base64.decodeBase64(sign) );
            return bverify;

        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return false;
    }

}