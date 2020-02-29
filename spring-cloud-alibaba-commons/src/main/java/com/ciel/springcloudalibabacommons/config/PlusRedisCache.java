package com.ciel.springcloudalibabacommons.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.ehcache.EhcacheCache;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 代替 private final Logger logger = LoggerFactory.getLogger(当前类名.class);
 *
 *
 *   二级缓存注解
 *
 * @CacheNamespace(eviction = EhcacheCache.class,implementation=EhcacheCache.class)

    配置文件需要开启
mybatis-plus:
    configuration:
        #cache-enabled: true #开启二级缓存
 */
@Slf4j
public class PlusRedisCache { //implements Cache {

//
//    // 读写锁
//    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
//
//    //这里使用了redis缓存，使用springboot自动注入
//    private RedisTemplate<String, Object> redisTemplate;
//
//    private String id;
//
//    public PlusRedisCache(final String id) {
//        if (id == null) {
//            throw new IllegalArgumentException("Cache instances require an ID");
//        }
//        this.id = id;
//
//    }
//
//    @Override
//    public String getId() {
//        return this.id;
//    }
//
//    @Override
//    public void putObject(Object key, Object value) {
//        if (redisTemplate == null) {
//            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
//            redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
//        }
//        if (value != null) {
//            redisTemplate.opsForValue().set(key.toString(), value);
//        }
//    }
//
//    @Override
//    public Object getObject(Object key) {
//        if (redisTemplate == null) {
//            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
//            redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
//        }
//        try {
//            if (key != null) {
//                return redisTemplate.opsForValue().get(key.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("缓存出错 ");
//        }
//        return null;
//    }
//
//    @Override
//    public Object removeObject(Object key) {
//        if (redisTemplate == null) {
//            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
//            redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
//        }
//        if (key != null) {
//            redisTemplate.delete(key.toString());
//        }
//        return null;
//    }
//
//    @Override
//    public void clear() {
//        log.debug("清空缓存");
//        if (redisTemplate == null) {
//            redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
//        }
//        Set<String> keys = redisTemplate.keys("*:" + this.id + "*");
//        if (!CollectionUtils.isEmpty(keys)) {
//            redisTemplate.delete(keys);
//        }
//    }
//
//    @Override
//    public int getSize() {
//        if (redisTemplate == null) {
//            //由于启动期间注入失败，只能运行期间注入，这段代码可以删除
//            redisTemplate = (RedisTemplate<String, Object>) SpringUtil.getBean("redisTemplate");
//        }
//        Long size = redisTemplate.execute((RedisCallback<Long>) RedisServerCommands::dbSize);
//        return size.intValue();
//    }
//
//    @Override
//    public ReadWriteLock getReadWriteLock() {
//        return this.readWriteLock;
//    }
}
