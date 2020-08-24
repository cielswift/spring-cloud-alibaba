package com.ciel.scatquick.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ciel.scaapi.crud.IScaDictService;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.crud.IScaRoleService;
import com.ciel.scaapi.crud.IScaUserService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.AppContext;
import com.ciel.scaapi.util.Faster;
import com.ciel.scaapi.util.FileUpload2Nginx;
import com.ciel.scaapi.util.SysUtils;
import com.ciel.scacommons.config.SnowFlake;
import com.ciel.scacommons.mapper.ScaGirlsMapper;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaentity.entity.ScaUser;
import com.ciel.scatquick.aoptxspi.LogsAnnal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sharding")

@Slf4j
@AllArgsConstructor
public class ShardingJDBCController {

    protected IScaGirlsService scaGirlsService;

    protected IScaUserService scaUserService;

    protected IScaRoleService scaRoleService;

    protected IScaDictService scaDictService;

    protected WebApplicationContext webApplicationContext;

    protected AutowireCapableBeanFactory autowireCapableBeanFactory;


    protected ScaGirlsMapper scaGirlsMapper;

    protected RedisTemplate<String, Object> redisTemplate;

    protected SnowFlake snowFlake;

    //pamr/123;name=javaboy

    @GetMapping("/pamr/{id}")
    public Result parm(@PathVariable("id") Integer id, @MatrixVariable String name) {

        return Result.ok();
    }


    @LogsAnnal
    @GetMapping("/cache")
    public Result users(double str, double end) {

        /**
         * redis 分布式锁
         * setIfAbsent 没有这个key才会返回true;
         * setIfPresent 有这个key 才会返回true ,也就是必须覆盖
         */
        Boolean absent = redisTemplate.opsForValue()
                .setIfAbsent("cl-lock", 123578, 2, TimeUnit.SECONDS);
        if (null != absent && absent) { //设置成功

            //业务逻辑
            redisTemplate.delete("cl-lock");
        }

        //redisson 分布式锁
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://120.27.69.29:6379")
//                //.setPassword()
//                .setDatabase(1);
//
//        Redisson redisson = (Redisson) Redisson.create(config);
//
//        RLock lock = redisson.getLock("cl-lock");
//        lock.lock(5, TimeUnit.SECONDS);

        //业务逻辑
        //       lock.unlock();

        AppContext.setToken("xiapeixin");

        AppContext.innSet("xiapeixin");

        List<ScaGirls> girls = scaGirlsService.girlsByPrice(str, end);
        return Result.ok().data(girls);

    }



    /**
     * 本地事务:
     * 完全支持非跨库事务，例如：仅分表，或分库但是路由的结果在单库中；
     * 完全支持因逻辑异常导致的跨库事务。例如：同一事务中，跨两个库更新。更新完毕后，抛出空指针，则两个库的内容都能回滚
     * 不支持因网络、硬件异常导致的跨库事务。例如：同一事务中，跨两个库更新，更新完毕后、未提交之前，第一个库宕机，则只有第二个库数据提交
     * XA两阶段事务:
     * 支持数据分片后的跨库事务；两阶段提交保证操作的原子性和数据的强一致性；
     * 服务宕机重启后，提交/回滚中的事务可自动恢复；支持同时使用 XA 和非 XA 的连接池;
     * 服务宕机后，在其它机器上恢复提交/回滚中的数据。
     * SEATA 柔性事务:
     * 支持数据分片后的跨库事务；支持RC隔离级别；通过undo快照进行事务回滚；支持服务宕机后的，自动恢复提交中的事务
     */
    @LogsAnnal
    @GetMapping("/tran")
    @Transactional(rollbackFor = Exception.class)
    //Sharding 事务注解 支持LOCAL,XA,BASE  并且引入相应jar包
    //XAShardingTransactionManager xa 的事务管理器
    @ShardingTransactionType(TransactionType.XA)
    public Result tran(@RequestParam("type") Integer type) throws AlertException {


        List<ScaGirls> list = scaGirlsService.list();

        scaGirlsService.remove(new LambdaQueryWrapper<ScaGirls>()
                .in(ScaGirls::getId, list.stream().limit(5).map(ScaGirls::getId).collect(Collectors.toList())));

        if (type.equals(2)) {
            throw new AlertException("人为制造的异常 %s: 测试是否回滚","TEST-EXCEPTION");
        }

        return Result.ok().data(list);
    }

    @GetMapping("/tran/test")
    public Result tranTest() throws MissingServletRequestParameterException {

       // scaGirlsMapper.deleteAll(7L);

        QueryWrapper<ScaGirls> wrapper = SysUtils.autoCnd(ScaGirls.class);
        IPage<ScaGirls> page = SysUtils.autoPage(ScaGirls.class);
        IPage<ScaGirls> result = scaGirlsService.page(page, wrapper);

        return Result.ok("ok").data(result);
    }

    public static void main(String[] args) {
        System.out.println(1297888313860173824L % 4);
    }

    @GetMapping("/hello")
    public Result hello() {

        List<ScaGirls> gs = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ScaGirls scaGirls = new ScaGirls();
            scaGirls.setName("Ciel夏");
            scaGirls.setPrice(new BigDecimal(String.format("%.2f", Math.random() * 100)));
            scaGirls.setUserId(System.currentTimeMillis() - 77777);
            scaGirls.setBirthday(Faster.now());
            scaGirls.setImgs("http://127.0.0.1/image/2cd39f06e1b2c06977d751bbf75ebb1b.jpg");
            gs.add(scaGirls);

            boolean save = scaGirlsService.save(scaGirls);
        }

        return Result.ok().data(gs);
    }

}
