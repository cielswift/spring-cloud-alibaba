package com.ciel.scaproducer3.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.ciel.scaapi.crud.IScaGirlsService;
import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaGirls;
import com.ciel.scaproducer3.el.ElasticMapper;
import com.ciel.scaproducer3.el.Human;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController

@Slf4j
@AllArgsConstructor
public class IndexController {

    protected IScaGirlsService scaGirlsService;

    @GetMapping("/")
    @Transactional(rollbackFor = Exception.class)
    public Result index(Byte code) throws AlertException {

        List<ScaGirls> list = scaGirlsService.list();

//        UpdateWrapper<ScaGirls> up = new UpdateWrapper<ScaGirls>();
//        up.set("name","安妮海瑟薇").eq("name","特朗普");
//
//        boolean update1 = scaGirlsService.update(up);

        boolean update = scaGirlsService.update(new LambdaUpdateWrapper<ScaGirls>()
                .set(ScaGirls::getName, "安妮海瑟薇丝袜美腿")
                .eq(ScaGirls::getName, "安妮海瑟薇丝袜"));

        if(code==0){
            throw new AlertException("%2 ex");
        }

        return Result.ok("welcome").data(list);
    }

    @GetMapping("/cud")
    public Result cud(){
        for(int i = 0; i< 20 ; i++){
            ScaGirls scaGirls = new ScaGirls();
            scaGirls.setName("特朗普");
            scaGirls.setPrice(new BigDecimal("55.49"));
            scaGirls.setUserId(System.currentTimeMillis());
            boolean save = scaGirlsService.save(scaGirls);
            System.out.println(save);
        }
       return Result.ok();
    }
    
    protected AutowireCapableBeanFactory beanFactory;

    @GetMapping("/fb")
    public Result fb(){
        Object o1 = beanFactory.getBean("customFactorBean");
        Object o2 = beanFactory.getBean("&customFactorBean");
        return Result.ok();
    }

    protected ElasticsearchRestTemplate elasticsearchTemplate;

    protected ElasticMapper elasticMapper;

    @GetMapping("/els")
    public Result els(){

        //boolean cielswift = elRestTemplate.createIndex("cielswift");

        //boolean b = elasticsearchTemplate.putMapping(Human.class); //构建索引和type

        Map<String, Object> mapping = elasticsearchTemplate.getMapping(Human.class);
        Human human = new Human(System.currentTimeMillis(),"夏培鑫",25);
        elasticMapper.save(human); //修改也是这个,id区分
       // elasticMapper.saveAll(List<Human>...)

        Iterable<Human> all = elasticMapper.findAll(Sort.by("id").ascending());//正序

        all.forEach(System.out::println);
///////////////////////////////////////////////////////////////////////////////////////
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本分词查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("content", "夏培鑫"));
        queryBuilder.withPageable(PageRequest.of(0,20)); //Elasticsearch中的分页是从第0页开始
        queryBuilder.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));

        // 搜索，获取结果
        Page<Human> search = elasticMapper.search(queryBuilder.build());

///////////////////////////////////////////////////////////////////////////////////////////
        NativeSearchQueryBuilder qq = new NativeSearchQueryBuilder();
        // 不查询任何结果
        qq.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));

        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        qq.addAggregation(
                AggregationBuilders.terms("brands").field("type"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Human> aggPage = (AggregatedPage<Human>) elasticMapper.search(qq.build());


        Aggregation brands = aggPage.getAggregation("brands");

        // 3.2、获取桶
//        List<StringTerms.Bucket> buckets = brands.getBuckets();
//        // 3.3、遍历
//        for (StringTerms.Bucket bucket : buckets) {
//            // 3.4、获取桶中的key，即品牌名称
//            System.out.println(bucket.getKeyAsString());
//            // 3.5、获取桶中的文档数量
//            System.out.println(bucket.getDocCount());
//        }

//        （1）统计某个字段的数量
//        ValueCountBuilder vcb=  AggregationBuilders.count("count_uid").field("uid");
//（2）去重统计某个字段的数量（有少量误差）
//        CardinalityBuilder cb= AggregationBuilders.cardinality("distinct_count_uid").field("uid");
//（3）聚合过滤
//        FilterAggregationBuilder fab= AggregationBuilders.filter("uid_filter").filter(QueryBuilders.queryStringQuery("uid:001"));
//（4）按某个字段分组
//        TermsBuilder tb=  AggregationBuilders.terms("group_name").field("name");
//（5）求和
//        SumBuilder  sumBuilder=	AggregationBuilders.sum("sum_price").field("price");
//（6）求平均
//        AvgBuilder ab= AggregationBuilders.avg("avg_price").field("price");
//（7）求最大值
//        MaxBuilder mb= AggregationBuilders.max("max_price").field("price");
//（8）求最小值
//        MinBuilder min=	AggregationBuilders.min("min_price").field("price");
//（9）按日期间隔分组
//        DateHistogramBuilder dhb= AggregationBuilders.dateHistogram("dh").field("date");
//（10）获取聚合里面的结果
//        TopHitsBuilder thb=  AggregationBuilders.topHits("top_result");
//（11）嵌套的聚合
//        NestedBuilder nb= AggregationBuilders.nested("negsted_path").path("quests");
//（12）反转嵌套
//        AggregationBuilders.reverseNested("res_negsted").path("kps ");



        return Result.ok().data(search);
    }


}
