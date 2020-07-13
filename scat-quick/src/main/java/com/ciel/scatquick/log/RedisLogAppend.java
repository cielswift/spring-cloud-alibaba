package com.ciel.scatquick.log;

import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.JWTUtils;
import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志提交到redis
 */
@Slf4j
public class RedisLogAppend {

    public static void main(String[] args) {

        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setName("刘学文");
        scaGirls.setBirthday(Faster.now());

        String token = JWTUtils.createToken(scaGirls);

        System.out.println(token);

        ScaGirls scaGirls1 = JWTUtils.parseToken(token, ScaGirls.class);

        System.out.println(scaGirls);
    }

}
