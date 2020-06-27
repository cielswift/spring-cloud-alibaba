package com.ciel.scaapi.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JWTUtils {
    /**
     * 签名算法
     */
    private final static Algorithm ALGORITHM = Algorithm.HMAC256("xiaPeiXin#202$ciel"); //密钥
    /**
     * token保存时间 秒
     */
    public static final int time = 86400 ; // 24小时
    /**
     * token刷新时间 秒
     */
    public static final int refresh = 43200; // 12

    /**
     * 生产md5 摘要
     */
    public static String md5(byte[] bytes){
        //  byte[] bytes = Files.readAllBytes(Path.of("C:\\ciel\\spring-cloud-provider1\\log\\log_info.log"));
        return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * 生成Token
     *
     * JWT分成3部分：1.头部（header),2.载荷（payload, 类似于飞机上承载的物品)，3.签证（signature)
     *
     * header 和payload 都是明文的;可以被解密; 主要看signature;
     *
     * 加密后这3部分密文的字符位数为：
     *  1.头部（header)：36位，Base64编码
     *  2.载荷（payload)：没准，BASE64编码
     *  3.签证（signature)：43位，将header和payload拼接生成一个字符串，
     *                          使用HS256算法和我们提供的密钥（secret,服务器自己提供的一个字符串），
     *                          对str进行加密生成最终的JWT
     */

    /**
     * 头部 固定
     */
    private static final Map<String, Object> HEAD;

    static {
        HEAD = new HashMap<String, Object>();
        HEAD.put("alg", "HS256");
        HEAD.put("typ", "JWT");
    }


    public static <T> String createToken(T load) {

        return JWT.create().withHeader(HEAD)  //生成 头
                /* 设置 载荷 Payload */
                .withClaim("BODY", JSON.toJSONString(load))
                .withIssuer("SCA_SERVER") // 签名是有谁生成 例如 服务器
                .withSubject("JWT_TOKEN")// 签名的主题
                // .withNotBefore(new Date())//定义在什么时间之前，该jwt都是不可用的
                .withAudience("SCA_USER")// 签名的观众 也可以理解谁接受签名的
                .withIssuedAt(new Date()) // 生成签名的时间
                .withExpiresAt(new Date(System.currentTimeMillis()+(1000*time))) //过期时间
                /* 签名 Signature */
                .sign(ALGORITHM);
    }

    /**
     * 公共解析部分
     */
    private static DecodedJWT commonParse(String token){
        JWTVerifier verifier = JWT.require(ALGORITHM) //验证
                .withIssuer("SCA_SERVER")
                .build();

        return verifier.verify(token);
    }

    /**
     * 解析token
     * @param token
     * @return
     */

    public static <T> T parseToken(String token, Class<T> t){
        DecodedJWT jwt = null;
        try {
            jwt =  commonParse(token); //获取值
        }catch (TokenExpiredException exception){
            throw new RuntimeException("token已过期");
        }catch (Exception exception){
            throw new RuntimeException("解析异常");
        }

        //String subject = jwt.getSubject(); //主题
        //List<String> audience = jwt.getAudience(); //观众

        Map<String, Claim> claims = jwt.getClaims();
        Claim body = claims.get("BODY");

        return JSON.parseObject(body.asString(),t);
    }

    /**
     * 获取token创建时间
     */
    public static LocalDateTime getTokenCreateTime(String token){
        DecodedJWT jwt = null;
        try {
            jwt =  commonParse(token); //获取值
        }catch (TokenExpiredException exception){
            throw new RuntimeException("token已过期");
        }catch (Exception exception){
            throw new RuntimeException("解析异常");
        }

        Date issuedAt = jwt.getIssuedAt();
        return issuedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     *token 是否需要刷新
     */
    public static boolean isRefresh(String token, int second){
        DecodedJWT jwt = null;
        try {
            jwt =  commonParse(token); //获取值
        }catch (TokenExpiredException exception){
            throw new RuntimeException("token已过期");
        }catch (Exception exception){
            throw new RuntimeException("解析异常");
        }

        LocalDateTime create =
                jwt.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime refreshPoint = create.plusSeconds(second); //刷新点

        return LocalDateTime.now().isAfter(refreshPoint);
    }

    /**
     *token 是否需要刷新
     */
    public static boolean isRefresh(String token){
        return isRefresh(token,refresh);
    }

}