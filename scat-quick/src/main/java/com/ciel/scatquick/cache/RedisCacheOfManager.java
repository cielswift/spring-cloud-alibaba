package com.ciel.scatquick.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 开启缓存
 */

@EnableCaching(proxyTargetClass = true, order = Ordered.LOWEST_PRECEDENCE)
@Configuration
public class RedisCacheOfManager {

    /**
     * 缓存专用redis;
     * autowireCandidate = false 不会作为其他bean的依赖而自动注入
     * initMethod = "",destroyMethod = "" 初始化和销毁方法
     */
    @Bean(value = "cacheRedis",autowireCandidate = false)
    @Lookup //返回新的对象
    @Lazy //懒加载
    /**
     * RedisTemplate创建的时候会先将@DependsOn中指定的个bean先创建好
     */
    @DependsOn({"redisConnectionFactory"})
    public RedisTemplate<String, Object> cacheRedis(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.java());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.java());
        template.afterPropertiesSet();
        return template;
    }


    /**
     * redis
     */
    @Bean
    @Lazy
    @Primary
    public RedisTemplate<String, Object> redisString(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // redisTemplate.setDefaultSerializer(RedisSerializer.string());
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());



//        //下面代码解决LocalDateTime序列化与反序列化不一致问题
//        Jackson2JsonRedisSerializer<Object> j2jrs = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 解决jackson2无法反序列化LocalDateTime的问题
//        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        om.registerModule(new JavaTimeModule());
//        om.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
//        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//        j2jrs.setObjectMapper(om);
//        // 序列化 value 时使用此序列化方法
//        redisTemplate.setValueSerializer(j2jrs);
//        redisTemplate.setHashValueSerializer(j2jrs);
        return redisTemplate;
    }

    /**
     * 缓存key的生成器
     */
    @Bean
    public KeyGenerator autoGenSP() { //定义缓存 key 的生成策略
        return (target, method, params) ->
                target.getClass().getName() + "_"
                        + method.getName() + "_"
                        + Arrays.asList(params).toString();
    }

    @Bean
    public CacheManager redisCacheManagerSP(@Autowired RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(30) //过期时间
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(configuration)
                .transactionAware()
                .build();
    }

//    @Bean
//    @Primary
//    @SuppressWarnings("all")
//    public CacheManager redisCacheManagerSP(@Qualifier("cacheRedis") RedisTemplate<String, Object> redisTemplate) throws AlertException {
//
//        RedisCacheWriter redisCacheWriter =
//                RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
//
//        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                        .serializeValuesWith(RedisSerializationContext.SerializationPair
//                        .fromSerializer(redisTemplate.getValueSerializer()));
//
//        //设置默认超过期时间是30秒
//        redisCacheConfiguration.entryTtl(Duration.ofSeconds(3));
//        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
//    }

}
