package com.ciel.scatquick.controller;

import com.ciel.scaapi.retu.Result;
import com.ciel.scatquick.el.ElasticMapper;
import com.ciel.scatquick.el.Human;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
public class ELController {

    protected ElasticsearchRestTemplate elasticsearchTemplate;

    protected ElasticMapper elasticMapper;

    @GetMapping("/els")
    public Result els() {

        //boolean cielswift = elRestTemplate.createIndex("cielswift"); //构建索引
        //elasticsearchTemplate.deleteIndex()  //删除索引

        //boolean b = elasticsearchTemplate.putMapping(Human.class); //构建索引和type

        //使用queryStringQuery完成单字符串查询

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("夏培鑫"))
                .withQuery(QueryBuilders.matchQuery("content", "在心碎中认清遗憾"))
                //matchPhraseQuery //短语查询
                //termQuery 严格查询 适合id
                //multiMatchQuery 多个字段匹配某字符串

                //之前的查询中，当我们输入“我天”时，ES会把分词后所有包含“我”和“天”的都查询出来，
                //如果我们希望必须是包含了两个字的才能被查询出来，那么我们就需要设置一下Operator
                .withQuery(QueryBuilders.matchQuery("content", "在心碎中认清遗憾")
                        .operator(Operator.AND))

                .withPageable(PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"))) //从0开始

                //.withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC));
                .build();
        //查询list
        List<Human> humans = elasticsearchTemplate.queryForList(searchQuery, Human.class);

        AggregatedPage<Human> humans1 = elasticsearchTemplate.queryForPage(searchQuery, Human.class);

        Map<String, Object> mapping = elasticsearchTemplate.getMapping(Human.class);
        Human human = new Human();
        human.setId(System.currentTimeMillis());
        human.setName("夏培鑫");
        human.setAge(24);
        human.setBirthday(new Date());
        human.setAddress("山东省烟台市芝罘区");

        human.setContent("也许很远或是昨天\n" +
                "在这里或在对岸\n" +
                "长路辗转离合悲欢\n" +
                "人聚又人散\n" +
                "放过对错才知答案\n" +
                "活着的勇敢\n" +
                "没有神的光环\n" +
                "你我生而平凡\n" +
                "在心碎中认清遗憾\n" +
                "生命漫长也短暂\n" +
                "跳动心脏长出藤蔓\n" +
                "愿为险而战\n" +
                "跌入灰暗坠入深渊\n" +
                "沾满泥土的脸\n" +
                "没有神的光环\n" +
                "握紧手中的平凡\n" +
                "此心此生无憾\n" +
                "生命的火已点燃\n" +
                "有一天也许会走远\n" +
                "也许还能再相见\n" +
                "无论在人群在天边\n" +
                "让我再看清你的脸\n" +
                "任泪水铺满了双眼\n" +
                "虽无言泪满面\n" +
                "不要神的光环\n" +
                "只要你的平凡\n" +
                "\n" +
                "音乐制作人：黄超\n" +
                "编曲：黄超\n" +
                "吉他：黄超\n" +
                "单簧管：刘峤\n" +
                "和音编写：黄超\n" +
                "和音演唱：张杰 张碧晨\n" +
                "人声录音师：汝文博/朱玉婷\n" +
                "录音工作室：BIG.J Studio/香蕉计划录音棚\n" +
                "混音师：周天澈\n" +
                "母带工程师：周天澈\n" +
                "母带工作室：TC Faders\n" +
                "\n" +
                "在心碎中认清遗憾\n" +
                "生命漫长也短暂\n" +
                "跳动心脏长出藤蔓\n" +
                "愿为险而战\n" +
                "跌入灰暗坠入深渊\n" +
                "沾满泥土的脸\n" +
                "没有神的光环\n" +
                "握紧手中的平凡\n" +
                "有一天也许会走远\n" +
                "也许还能再相见\n" +
                "无论在人群在天边\n" +
                "让我再看清你的脸\n" +
                "任泪水铺满了双眼\n" +
                "虽无言泪满面\n" +
                "不要神的光环\n" +
                "只要你的平凡\n" +
                "此心此生无憾\n" +
                "生命的火已点燃");

        elasticMapper.save(human); //修改也是这个,id区分
        // elasticMapper.saveAll(List<Human>...)
        Iterable<Human> all = elasticMapper.findAll(Sort.by("id").ascending());//正序
        all.forEach(System.out::println);
///////////////////////////////////////////////////////////////////////////////////////

//        must代表返回的文档必须满足must子句的条件，会参与计算分值；
//        filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
//        should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
//        mustnot代表必须不满足子句的条件。
        SearchQuery seqy = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("id", 1590463901595L))
                .should(QueryBuilders.rangeQuery("age").lt(30)).must(QueryBuilders.matchQuery("name", "夏培鑫"))).build();

        List<Human> humans2 = elasticsearchTemplate.queryForList(seqy, Human.class);

///////////////////////////////////////////////////////////////////////////////////////////
        NativeSearchQueryBuilder qq = new NativeSearchQueryBuilder();
        // 不查询任何结果
        qq.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));

        // 1、添加一个新的聚合，聚合类型为terms，聚合名称为brands，聚合字段为brand
        qq.addAggregation(
                AggregationBuilders.terms("count_uid").field("age"));
        // 2、查询,需要把结果强转为AggregatedPage类型
        AggregatedPage<Human> aggPage = (AggregatedPage<Human>) elasticMapper.search(qq.build());

        Aggregation brands = aggPage.getAggregation("count_uid");


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

        return Result.ok().data(humans2);
    }
}
