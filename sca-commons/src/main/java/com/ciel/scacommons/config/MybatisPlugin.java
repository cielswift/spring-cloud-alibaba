package com.ciel.scacommons.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.ciel.scacommons.method.DeleteAll;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * mybatis plus 全局拦截器
 */
@Configuration
public class MybatisPlugin {

    /**
     * 注入数据中心和机器序列
     *
     */
    @Bean
    @Primary
    public SnowFlake snowFlake(@Value("${clusters.datacenterId}")Integer datacenterId ,
                               @Value("${clusters.machineId}")Integer machineId) {
        return new SnowFlake(datacenterId, machineId);
    }

    /**
     * 自定义全局拦截器
     * @return
     */
    @Bean
    public Interceptor globalInterceptor(){
        return new GlobalInterceptor();
    }

    /**
     * mybatis plus全局拦截器
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {  //自动分页
        PaginationInterceptor interceptor = new PaginationInterceptor();

        /**
         * 分页插件
         */
        interceptor.setDbType(DbType.MYSQL);
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(-1);
        // 开启 count 的 join 优化,只针对部分 left join
        interceptor.setCountSqlParser(new JsqlParserCountOptimize(true));


        /**
         * 创建SQL解析器集合
         */
        //
        List<ISqlParser> sqlParserList = new ArrayList<>(); //解析链

        /**
         * 攻击SQL阻断解析器
         */
        sqlParserList.add(new BlockAttackSqlParser() {

            //防止全局删除
            @Override
            public void processDelete(Delete delete) {
                // 如果你想自定义做点什么，可以重写父类方法像这样子  //自定义跳过某个表，
                if ("user".equals(delete.getTable().getName())) {
                    return ;
                }
                super.processDelete(delete);
            }

            //防止全局更新
            @Override
            public void processUpdate(Update update) {
                super.processUpdate(update);
            }

        });

        /**
         * 动态表名SQL解析器
         */
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        Map<String, ITableNameHandler> tableNameHandlerMap = new HashMap<>();
        // Map的key就是需要替换的原始表名, 其他表不会经过这个解析器;
        tableNameHandlerMap.put("sca_order",new ITableNameHandler(){
            @Override
            public String dynamicTableName(MetaObject metaObject, String sql, String tableName) {
                // 自定义表名规则，或者从配置文件、request上下文中读取
                // 假设这里的用户表根据年份来进行分表操作
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM"));
                // 返回最后需要操作的表名，sys_user_2019
                return "sca_order_" + format;
            }
        });

        dynamicTableNameParser.setTableNameHandlerMap(tableNameHandlerMap);

        sqlParserList.add(dynamicTableNameParser);

        //添加解析链
        interceptor.setSqlParserList(sqlParserList);
        return interceptor;
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

    /**
     * 分布式id生成器 配合 @TableId(value = "ID", type = IdType.ASSIGN_ID)
     * @return
     */
    @Bean
    public IdentifierGenerator identifierGenerator(@Autowired  SnowFlake snowFlake) {
        return new IdentifierGenerator() {

            /**
             * nextId 支持主键策略为IdType.ASSIGN_ID,主键类型为Long,Integer,String
             */
            @Override
            public Number nextId(Object entity) {
//                //可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成;
//                String bizKey = entity.getClass().getName();
                return snowFlake.nextId(false);
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

    /**
     * sql 注入器
     */
    @Bean
    public ISqlInjector iSqlInjector() {
        return new DefaultSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {

                //先注入默认的 list selectById 等...
                List<AbstractMethod> methodList = super.getMethodList(mapperClass);

                /**
                 * 注入自己的
                 */
                methodList.add(new DeleteAll());
                return methodList;
            }
        };
    }

    /**
     * mybatis plus 全局配置
     * @return
     */
    //@Bean id生产器配置 填充配置注入即可 这里没有用到全局配置
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
        return (pro) -> {

            //全局配置
            GlobalConfig globalConfig = pro.getGlobalConfig();

           // @Autowired  SnowFlake snowFlake
            //globalConfig.setIdentifierGenerator(identifierGenerator()); //id生产器

            globalConfig.setBanner(true); //显示banner;

            globalConfig.setMetaObjectHandler(new MybatisPlusAuto()); //自动填充

            GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
            dbConfig.setLogicDeleteValue("1");
            dbConfig.setLogicNotDeleteValue("0");
            dbConfig.setInsertStrategy(FieldStrategy.NOT_NULL);
            dbConfig.setUpdateStrategy(FieldStrategy.NOT_NULL);


            globalConfig.setDbConfig(dbConfig);

        };
    }


   // @Bean //Map下划线自动转驼峰
    public ConfigurationCustomizer configurationCustomizer() {
        return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }

 //   @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
        scannerConfigurer.setBasePackage("com.yourpackage.*.mapper");
        return scannerConfigurer;
    }

    public MybatisConfiguration mybatisConfiguration(){
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);//开启下划线转驼峰
    //    sqlSessionFactory.setConfiguration(configuration);
        return configuration;
    }

    /**
     * Sequence自增主键 ,主键生成策略必须使用INPUT
     */
//    @Bean
//    public IKeyGenerator keyGenerator() {
//        return new H2KeyGenerator();
//    }

}
