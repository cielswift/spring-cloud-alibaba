package com.ciel.scareactive.es.nat;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

public class EsTes {

    public static RestHighLevelClient client = ESConfig.restHighLevelClient();

    public static void main(String[] args) throws IOException {

        createIndex();

    }

    public static void createIndex() throws IOException {

        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();

                builder.startObject("age");
                {
                    builder.field("type", "long");
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
        CreateIndexRequest request = new CreateIndexRequest("ciel");
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
        DeleteIndexRequest request = new DeleteIndexRequest("ciel");
        // 执行删除索引
        AcknowledgedResponse acknowledgedResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        // 判断是否删除成功
        boolean siDeleted = acknowledgedResponse.isAcknowledged();
    }
}
