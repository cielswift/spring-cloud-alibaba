//package com.ciel.scatquick.el;
//
//import com.alibaba.fastjson.JSON;
//import org.elasticsearch.action.delete.DeleteRequest;
//import org.elasticsearch.action.delete.DeleteResponse;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.io.IOException;
//
//public class IndexService {
//
//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//
//    /**
//     * 增加文档信息
//     */
//    public void addDocument() {
//        try {
//            // 创建索引请求对象
//            IndexRequest indexRequest = new IndexRequest("mydlq-user", "doc", "1");
//            // 创建员工信息
//            UserInfo userInfo = new UserInfo();
//            userInfo.setName("张三");
//            userInfo.setAge(29);
//            userInfo.setSalary(100.00f);
//            userInfo.setAddress("北京市");
//            userInfo.setRemark("来自北京市的张先生");
//            userInfo.setCreateTime(new Date());
//            userInfo.setBirthDate("1990-01-10");
//            // 将对象转换为 byte 数组
//            byte[] json = JSON.toJSONBytes(userInfo);
//            // 设置文档内容
//            indexRequest.source(json, XContentType.JSON);
//            // 执行增加文档
//            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
//            log.info("创建状态：{}", response.status());
//        } catch (Exception e) {
//            log.error("", e);
//        }
//    }
//
//    /**
//     * 获取文档信息
//     */
//    public void getDocument() {
//        try {
//            // 获取请求对象
//            GetRequest getRequest = new GetRequest("mydlq-user", "doc", "1");
//            // 获取文档信息
//            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
//            // 将 JSON 转换成对象
//            if (getResponse.isExists()) {
//                UserInfo userInfo = JSON.parseObject(getResponse.getSourceAsBytes(), UserInfo.class);
//                log.info("员工信息：{}", userInfo);
//            }
//        } catch (IOException e) {
//            log.error("", e);
//        }
//    }
//
//    /**
//     * 更新文档信息
//     */
//    public void updateDocument() {
//        try {
//            // 创建索引请求对象
//            UpdateRequest updateRequest = new UpdateRequest("mydlq-user", "doc", "1");
//            // 设置员工更新信息
//            UserInfo userInfo = new UserInfo();
//            userInfo.setSalary(200.00f);
//            userInfo.setAddress("北京市海淀区");
//            // 将对象转换为 byte 数组
//            byte[] json = JSON.toJSONBytes(userInfo);
//            // 设置更新文档内容
//            updateRequest.doc(json, XContentType.JSON);
//            // 执行更新文档
//            UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    /**
//     * 删除文档信息
//     */
//    public void deleteDocument() {
//        try {
//            // 创建删除请求对象
//            DeleteRequest deleteRequest = new DeleteRequest("mydlq-user", "doc", "1");
//            // 执行删除文档
//            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
//
//        } catch (IOException e) {
//
//        }
//    }
//
//}