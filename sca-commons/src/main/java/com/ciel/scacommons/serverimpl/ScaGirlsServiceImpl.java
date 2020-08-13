package com.ciel.scacommons.serverimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.util.AppContext;
import com.ciel.scacommons.mapper.ScaGirlsMapper;
import com.ciel.scaentity.entity.ScaGirls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service

/**
 * 指定缓存通用配置; cacheManager指定使用哪一个缓存管理器
 */
@Slf4j
@CacheConfig(cacheNames = "scaGirls", cacheManager = "redisCacheManagerSP")
public class ScaGirlsServiceImpl extends ServiceImpl<ScaGirlsMapper, ScaGirls> implements IScaGirlsService {

    @Autowired
    protected ThreadPoolExecutor threadPoolExecutor;

    /**
     * 开启缓存,
     * cacheName/value 把返回结果放在哪几个缓存中, 比如放在 user缓存 app缓存
     * key缓存的key,默认方法参数;可以使用el表达式,使用方法名称 ,key = "#root.methodName" #root.targetClass ;
     *  和keyGenerator2选1, KeyGenerator是自定义的key生成器(keyGenerator = "autoGenMy")
     *  #root.target 当前被调用对象; #root.targetClass 当前被调用对象class #root.args[0] 当前方法参数数组
     * condition 取出参数,当符合条件才会缓存, 可以使用el表达式,#a0第一个参数  例如第二个参数
     *   可以通过{@code＃root.args [1]}，{@ code＃p1}或{@code＃a1}访问。 争论
     * unless : 符合条件否定缓存,可以使用el表达式, 返回结果为空 result==null
     * sync :是否异步模式 和unless 冲突
     *
     * @Cacheable 标注的方法会在执行前检查缓存中有没有这个数据, 默认按照参数的值作为key查找, 如果没有就执行方法
     */
    @Override
    @Cacheable(value = "scaGirls", keyGenerator = "autoGenSP", sync=true,condition = "#a0 != null && #a1 != null")

    /**
     * CachePut 先调用方法(一定会执行),然后把返回结果更新缓存数据; key要和查询的方法的key相同,也就是覆盖另一个方法的缓存
     */
   // @CachePut(value = "scaGirls",keyGenerator = "autoGenSP")

    /**
     * CacheEvict key 指定要清楚的缓存; allEntries = true 删除myApN下的所有缓存;
     * beforeInvocation: 是否在方法执行之前清楚缓存
     */
    //@CacheEvict(value = "scaGirls",key ="'xia1'")

    /**
     * 可以可以定义多个key,其他方法用这些key就可以取查缓存了;
     */
//    @Caching(
//            cacheable = {@Cacheable(value ="myApN",key = "#name")},
//            put = {@CachePut(value = "myApN",key = "#result.id"),
//                    @CachePut(value = "myApN",key = "#result.id")
//            }
//    )
    public List<ScaGirls> girlsByPrice(double str, double end) {


        log.info("获取到token: {} ",AppContext.getToken());
        log.info("aa获取到innToken: {} ",AppContext.innGet());

        new Thread(() -> {

            //从父线程获取 ;
            log.info("AA获取到innToken: {} ",AppContext.innGet());

        }).start();

        //线程池无效 因为池里的线程不是父线程创建的
        threadPoolExecutor.submit(() -> {
            log.info("获取到token: {} ",AppContext.getToken());
            log.info("获取到innToken: {} ",AppContext.innGet());
        });

        log.info("清除token:");
        AppContext.CURRENT.remove();
        AppContext.traceIdKD.remove();

        return list(new QueryWrapper<ScaGirls>().lambda().between(ScaGirls::getPrice, str, end));
    }


}
