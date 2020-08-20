package com.ciel.scareactive.es.nat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ciel.scaentity.entity.ScaGirls;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public class EsTemplate {

    public static RestHighLevelClient client = ESConfig.restHighLevelClient();

    public static final String SCA_GIRLS = "scagirls";

    public static void main(String[] args) throws IOException {
        //createIndex();

        //qer();

        ScaGirls girls = new ScaGirls();
        girls.setName("安娜");
        girls.setBirthday(new Date());
        girls.setId(System.currentTimeMillis());
        girls.setImgs("美国密西根州农场的清纯女孩");
        girls.setPrice(new BigDecimal("966.4"));

        //create(girls);

        //get(1597926154931L);

        // update(girls);

        del(1597927269298L);
    }

    public static void createIndex() throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "long");
                }
                builder.endObject();

                builder.startObject("name");
                {
                    builder.field("type", "keyword");
                }
                builder.endObject();

                builder.startObject("price");
                {
                    builder.field("type", "double");
                }
                builder.endObject();

                builder.startObject("birthday");
                {
                    builder.field("type", "date")
                            .field("format", "yyyy-MM-dd HH:mm:ss");

                }
                builder.endObject();

                builder.startObject("imgs");
                {
                    builder.field("type", "text")
                            .field("analyzer", "ik_max_word")
                            .field("search_analyzer", "ik_max_word");
                }
                builder.endObject();

            }
            builder.endObject();
        }
        builder.endObject();


        // 创建索引配置信息，配置
        Settings settings = Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build();
        // 新建创建索引请求对象，然后设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
        CreateIndexRequest request = new CreateIndexRequest(SCA_GIRLS);
        request.settings(settings);
        request.mapping(builder);

        // RestHighLevelClient 执行创建索引
        CreateIndexResponse response =
                client.indices().create(request, RequestOptions.DEFAULT);
        // 判断是否创建成功
        boolean isCreated = response.isAcknowledged();
        System.out.println("索引是否创建成功:" + isCreated);
    }

    /**
     * 删除索引
     */
    public static void deleteIndex() throws IOException {
        // 新建删除索引请求对象
        DeleteIndexRequest request = new DeleteIndexRequest(SCA_GIRLS);
        // 执行删除索引
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        // 判断是否删除成功
        boolean siDeleted = acknowledgedResponse.isAcknowledged();
    }

    public static void create(ScaGirls scaGirls) throws IOException {

        // 创建索引请求对象
        IndexRequest indexRequest = new IndexRequest(SCA_GIRLS);
        indexRequest.id(String.valueOf(scaGirls.getId()));

        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String string = JSON.toJSONString(scaGirls, SerializerFeature.WriteDateUseDateFormat);
        // 设置文档内容
        indexRequest.source(string, XContentType.JSON);
        // 执行增加文档
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);

        System.out.println(response);
    }

    public static void get(Long id) throws IOException {
        // 获取请求对象
        GetRequest getRequest = new GetRequest(SCA_GIRLS);
        getRequest.id(String.valueOf(id));
        // 获取文档信息
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

        System.out.println(getResponse);
    }

    public static void update(ScaGirls scaGirls) throws IOException {
        // 创建索引请求对象
        UpdateRequest updateRequest = new UpdateRequest(SCA_GIRLS, scaGirls.getId().toString());

        JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        String string = JSON.toJSONString(scaGirls, SerializerFeature.WriteDateUseDateFormat);
        // 设置更新文档内容
        updateRequest.doc(string, XContentType.JSON);
        // 执行更新文档
        UpdateResponse response = client.update(updateRequest, RequestOptions.DEFAULT);

        System.out.println(response);
    }

    /**
     * 删除文档信息
     */
    public static void del(Long id) throws IOException {
        // 创建删除请求对象
        DeleteRequest deleteRequest = new DeleteRequest(SCA_GIRLS, id.toString());
        // 执行删除文档
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);

        System.out.println(response);
    }

}
