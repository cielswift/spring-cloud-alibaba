package com.ciel.scareactive.es.nat;


import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;

public class EsTemplate3 {

    public static RestHighLevelClient client = ESConfig.restHighLevelClient();

    public static final String SCA_GIRLS = "scagirls";

    public static void main(String[] args) throws IOException {

        //query();

        //query2();

        query3();
    }

    public static void query() throws IOException {

        AggregationBuilder aggr = AggregationBuilders.stats("salary_stats").field("price");

//        AggregationBuilder aggr = AggregationBuilders.min("salary_min").field("price");
//        AggregationBuilder aggr = AggregationBuilders.max("salary_max").field("price");
//        AggregationBuilder aggr = AggregationBuilders.avg("salary_avg").field("price");
//        SumAggregationBuilder aggr = AggregationBuilders.sum("salary_sum").field("price");
//        AggregationBuilder aggr = AggregationBuilders.count("employee_count").field("price");
//        //百分比
//        AggregationBuilder aggr = AggregationBuilders.percentiles("salary_percentiles").field("price");

        // 查询源构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggr);
        // 设置查询结果不返回，只返回聚合结果
        searchSourceBuilder.size(0);
        // 创建查询请求对象，将查询条件配置到其中
        SearchRequest request = new SearchRequest(SCA_GIRLS);
        request.source(searchSourceBuilder);
        // 执行请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 获取响应中的聚合信息
        Aggregations aggregations = response.getAggregations();
        // 输出内容
        if (RestStatus.OK.equals(response.status()) || aggregations != null) {
            // 转换为 Stats 对象
            ParsedStats aggregation = aggregations.get("salary_stats");
            System.out.println("-------------------------------------------");
            System.out.println("聚合信息:");
            System.out.println("count：" + aggregation.getCount());
            System.out.println("avg：" + aggregation.getAvg());
            System.out.println("max：" + aggregation.getMax());
            System.out.println("min：" + aggregation.getMin());
            System.out.println("sum：" + aggregation.getSum());
            System.out.println("-------------------------------------------");
        }
        // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
        String string = response.toString();
        System.out.println(string);
    }

    public static void query2() throws IOException {

        //字段值分桶
        //AggregationBuilder aggr = AggregationBuilders.terms("price_bucket").field("price");

        //范围桶
//        AggregationBuilder aggr = AggregationBuilders.range("price_bucket")
//                .field("price")
//                .addUnboundedTo("低级员工", 3000)
//                .addRange("中级员工", 5000, 9000)
//                .addUnboundedFrom("高级员工", 9000);

        //时间范围
//        AggregationBuilder aggr = AggregationBuilders.dateRange("date_bucket")
//                .field("birthday")
//                .format("yyyy")
//                .addRange("1985-1990", "1985", "1990")
//                .addRange("1990-1995", "1990", "1995");

        //区间范围桶，设置统计的最小值为 0，最大值为 12000，区段间隔为 3000
//        AggregationBuilder aggr = AggregationBuilders.histogram("price_bucket")
//                .field("price")
//                .extendedBounds(0, 12000)
//                .interval(3000);

        //出生日期桶
        AggregationBuilder aggr = AggregationBuilders.dateHistogram("birthday_histogram")
                .field("birthday")
                .calendarInterval(DateHistogramInterval.YEAR)
                .format("yyyy");

        // 查询源构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(10);
        searchSourceBuilder.aggregation(aggr);
        // 创建查询请求对象，将查询条件配置到其中
        SearchRequest request = new SearchRequest(SCA_GIRLS);
        request.source(searchSourceBuilder);
        // 执行请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 获取响应中的聚合信息
        Aggregations aggregations = response.getAggregations();
        // 输出内容
        if (RestStatus.OK.equals(response.status())) {

            // 分桶
           // Terms byCompanyAggregation = aggregations.get("price_bucket");
           // List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
            // 输出各个桶的内容
           // for (Terms.Bucket bucket : buckets) {
            //    System.out.println("桶名：" + bucket.getKeyAsString() + "总数：{}"+ bucket.getDocCount());
          //  }

            //范围桶
//            Range byCompanyAggregation = aggregations.get("price_bucket");
//            List<? extends Range.Bucket> buckets = byCompanyAggregation.getBuckets();
//            for (Range.Bucket bucket : buckets) {
//                System.out.println("桶名：" + bucket.getKeyAsString() + "总数：{}"+ bucket.getDocCount());
//            }

            //区间范围桶
            Histogram byCompanyAggregation = aggregations.get("birthday_histogram");
            List<? extends Histogram.Bucket> buckets = byCompanyAggregation.getBuckets();
            // 输出各个桶的内容
            for (Histogram.Bucket bucket : buckets) {
                System.out.println("桶名：" + bucket.getKeyAsString() + "总数：{}"+ bucket.getDocCount());
            }
        }

    }

    public static void query3() throws IOException {

        // 按岁数分桶、然后统计每个员工工资最高值

        AggregationBuilder testTop = AggregationBuilders.topHits("salary_max_user")
                .size(1)
                .sort("price", SortOrder.DESC);
        AggregationBuilder salaryBucket = AggregationBuilders.terms("salary_bucket")
                .field("id")
                .size(10);

        salaryBucket.subAggregation(testTop);
        // 查询源构建器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        searchSourceBuilder.aggregation(salaryBucket);
        // 创建查询请求对象，将查询条件配置到其中
        SearchRequest request = new SearchRequest(SCA_GIRLS);
        request.source(searchSourceBuilder);
        // 执行请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 获取响应中的聚合信息
        Aggregations aggregations = response.getAggregations();
        // 输出内容
        if (RestStatus.OK.equals(response.status())) {
            // 分桶
            Terms byCompanyAggregation = aggregations.get("salary_bucket");
            List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
            // 输出各个桶的内容
            for (Terms.Bucket bucket : buckets) {
                System.out.println("桶名"+bucket.getKeyAsString());
                ParsedTopHits topHits = bucket.getAggregations().get("salary_max_user");
                for (SearchHit hit:topHits.getHits()){
                    System.out.println(hit.getSourceAsString());
                }
            }
        }
    }
}