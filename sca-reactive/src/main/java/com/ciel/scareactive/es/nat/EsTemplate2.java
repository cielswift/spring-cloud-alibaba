package com.ciel.scareactive.es.nat;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Arrays;

public class EsTemplate2 {

    public static RestHighLevelClient client = new ESConfig().restHighLevelClient();

    public static final String SCA_GIRLS = "scagirls";

    public static void main(String[] args) throws IOException {

        query2();
    }

    public static void query() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

       // searchSourceBuilder.query(QueryBuilders.termQuery("name", "安娜"));
        searchSourceBuilder.query(QueryBuilders.termsQuery("name", "安娜","安妮"));

        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest(SCA_GIRLS);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 根据状态和数据条数验证是否返回了数据
    }

    public static void query2() throws IOException {
        // 构建查询条件

        // 创建查询源构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //词语匹配查询
       // searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("imgs", "美国"));

        //内容查询多个字段
        //searchSourceBuilder.query(QueryBuilders.multiMatchQuery("美国", "imgs","name"));

        //模糊查询
       // searchSourceBuilder.query(QueryBuilders.fuzzyQuery("密西根州", "女孩").fuzziness(Fuzziness.AUTO));


        /**  范围查询
         * .gte("now-30y")  includeLower（是否包含下边界）、includeUpper（是否包含上边界）
         *  查询距离现在 30 年间的员工数据
         *      * [年(y)、月(M)、星期(w)、天(d)、小时(h)、分钟(m)、秒(s)]
         *      * 例如：
         *      * now-1h 查询一小时内范围
         *      * now-1d 查询一天内时间范围
         *      * now-1y 查询最近一年内的时间范围
         */
//        searchSourceBuilder.query(QueryBuilders.rangeQuery("price")
//                .gte(30).lte(1000)
//                .includeLower(true)
//                .includeUpper(true));

        /**
         * 通配符查询
         *
         *   *表示多个字符（0个或多个字符）
         *   ?表示单个字符
         */
        //searchSourceBuilder.query(QueryBuilders.wildcardQuery("imgs", "*女孩"));


        // 创建 Bool 查询构建器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 构建查询条件
        boolQueryBuilder.must(QueryBuilders.termsQuery("id", Arrays.asList(1597926154931L,1597927280011L)))
                .filter().add(QueryBuilders.rangeQuery("birthday").format("yyyy").gte("1990").lte("2040"));

        searchSourceBuilder.query(boolQueryBuilder);

        // 设置分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(3);
        // 设置排序
        searchSourceBuilder.sort("id", SortOrder.ASC);
        searchSourceBuilder.sort("price", SortOrder.DESC);

        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest(SCA_GIRLS);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(searchResponse.status());

        searchResponse.getHits().forEach(t -> {
            System.out.println(t.getSourceAsString());
        });
    }
}
