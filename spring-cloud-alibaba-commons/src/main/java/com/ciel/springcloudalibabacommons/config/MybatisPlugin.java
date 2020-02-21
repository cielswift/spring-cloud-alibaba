package com.ciel.springcloudalibabacommons.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.ciel.springcloudalibabacommons.method.DeleteAll;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class MybatisPlugin {

    /**
     * 注入数据中心和机器序列
     *
     * @return
     */

    @Value("${clusters.datacenterId}")
    private Integer datacenterId;

    @Value("${clusters.machineId}")
    private Integer machineId;

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(datacenterId, machineId);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {  //自动分页
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDbType(DbType.MYSQL);

        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(-1);
        // 开启 count 的 join 优化,只针对部分 left join
        page.setCountSqlParser(new JsqlParserCountOptimize(true));


        List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser() {
            @Override
            public void processDelete(Delete delete) {
                // 如果你想自定义做点什么，可以重写父类方法像这样子
                if ("user".equals(delete.getTable().getName())) {
                    // 自定义跳过某个表，其他关联表可以调用 delete.getTables() 判断
                    return ;
                }
                super.processDelete(delete);
            }
        });
        page.setSqlParserList(sqlParserList);


        return page;
    }

    /**
     * 乐观锁插件
     *
     *支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
     * 整数类型下 newVersion = oldVersion + 1
     * newVersion 会回写到 entity 中
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     *
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    @Bean //分布式id生成器
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator() {

            /**
             * nextId 支持主键策略为IdType.ASSIGN_ID,主键类型为Long,Integer,String
             */
            @Override
            public Number nextId(Object entity) {

//                //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成;
//                String bizKey = entity.getClass().getName();
//                //根据bizKey调用分布式ID生成
//                return bizKey.hashCode()+entity.hashCode() +
//                        System.currentTimeMillis() +
//                        new Random().nextInt(95276);

                return snowFlake().nextId();
            }

            /**
             * nextUUID 支持主键策略为IdType.ASSIGN_UUID,主键类型为String
             */
//            @Override
//            public String nextUUID(Object entity) {
//                return null;
//            }
        };
    }

//    @Bean 另一种配置全局id方式
//    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
//        return plusProperties -> plusProperties.getGlobalConfig().setIdentifierGenerator(identifierGenerator());
//    }

    /**
     * sql 注入器
     *
     * @return
     */
//    @Bean
//    public ISqlInjector iSqlInjector() {
//        return new AbstractSqlInjector() {
//            @Override
//            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//
//                /**
//                 * 这个地方还需要注入 SelectById 等Method,没有注入则无效;
//                 */
//                return Stream.of(new DeleteAll())
//                        .collect(Collectors.toList());
//            }
//        };
//    }

    /**
     * ==========================================================================================================================
     *
     * Sequence主键 ,主键生成策略必须使用INPUT
     */
//    @Bean
//    public IKeyGenerator keyGenerator() {
//        return new H2KeyGenerator();
//    }

//-------------------------------------------------------------------------------------------------------------
//    /**
//     * MyBatisPLus全局配置
//     */
//    @Bean
//    public GlobalConfig globalConfig() {
//        GlobalConfig globalConfig = new GlobalConfig();
//        // 不显示 MyBatisPlus Banner
//        globalConfig.setBanner(false);
//        // 自动填充配置
//        globalConfig.setMetaObjectHandler(new CommonMetaObjectHandler());
//        return globalConfig;
//    }

}
