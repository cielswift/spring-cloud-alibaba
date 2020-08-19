package com.ciel.scareactive.es.nat;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESConfig {

    private static String address = "106.12.213.120:9200";
    /** 连接超时时间 */
    private static int connectTimeout = 3000;
    /** Socket 连接超时时间 */
    private static int socketTimeout = 3000;
    /** 获取连接的超时时间 */
    private static int connectionRequestTimeout = 5000;
    /** 最大连接数 */
    private static int maxConnectNum = 8;
    /** 最大路由连接数 */
    private static int maxConnectPerRoute = 8;


    public static RestHighLevelClient restHighLevelClient() {
        String[] hostList = address.split(",");

        HttpHost[] httpHost = new HttpHost[hostList.length];

        for (int i = 0; i < hostList.length; i++) {
            String host = hostList[i].split(":")[0];
            String port = hostList[i].split(":")[1];
            httpHost[i] = new HttpHost(host, Integer.parseInt(port), "http");
        }

        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
            return requestConfigBuilder;
        });

        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);
    }
}
