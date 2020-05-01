package com.ciel.scatquick.cache;

import com.ciel.scaapi.exception.AlertException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Arrays;

/**
 * 开启缓存
 */
@EnableCaching(proxyTargetClass = true)
@Configuration
public class RedisCacheOfManager {

    /**
     * 缓存key的生成器
     * @return
     */
    @Bean
    public KeyGenerator autoGenMy(){ //定义缓存 key 的生成策略
        return (target, method, params) ->
                target.getClass().getName() + "_"
                        + method.getName() + "_"
                        + Arrays.asList(params).toString();
    }


    @Bean
    @Primary
    @SuppressWarnings("all")
    public RedisCacheManager cacheManagerJSON(@NotNull RedisTemplate<String, Object> redisTemplate) throws AlertException {

        RedisCacheWriter redisCacheWriter =
                RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig().serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate
                                .getValueSerializer()));
        //设置默认超过期时间是30秒
        redisCacheConfiguration.entryTtl(Duration.ofSeconds(30));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

}
