package com.ciel.scaconsumer.controller;

import com.alibaba.fastjson.JSON;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaentity.entity.ScaGirls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class RestTempController {

    @Autowired
    @Qualifier("restTemplateServer")
    protected RestTemplate restTemplateServer;

    @Autowired
    private LoadBalancerClient balancerClient; //ribbon 负载均衡器

    @GetMapping("/get")
    public Result get() {

        ServiceInstance choose = balancerClient.choose("producer10");
        Map<String, String> metadata = choose.getMetadata(); //获取nacos上的元数据

        //选择调用的微服务的名称
        //ServiceInstance 封装了服务的基本信息，如 IP，端口
        ServiceInstance si = balancerClient.choose("producer10");
        String url = "http://" + si.getHost() + ":" + si.getPort() + "/producer10/get?name=xiapeixin202";
        //ResponseEntity:封装了返回值信息
        ResponseEntity<List> exchange =
                restTemplateServer.exchange(url, HttpMethod.GET, null, List.class);
        //路径 方式, 参数, 返回值用什么包装
        List list = exchange.getBody();

        ////////////////////////////////////////////////////////////////////////////////////////
        /** 带参数----------------------------------------------------------------------------*/
        Map<String,String> param = new HashMap<>();
        param.put("id","2");
        param.put("name","夏培鑫");

        List forObject = restTemplateServer.getForObject
                ("http://producer10/producer10/get?id={id}&name={name}",List.class,param);

        ///////////////////////////////////////////////////////////////////////////////////////////////
        /** json ----------------------------------------------------------------------------*/

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
//        headers.setContentType(type); //设置请求类型
//        headers.setAccept(Faster.toList(type)); //设置响应类型

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE); //设置请求类型
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE); //设置响应类型

        ScaGirls scaGirls = new ScaGirls();
        scaGirls.setBirthday(new Date());
        scaGirls.setName("nin");


        HttpEntity<String> formEntity = new HttpEntity<String>(JSON.toJSONString(scaGirls), headers);

        String forObject1 = restTemplateServer.postForObject
                ("http://producer10/producer10/post?id=10", formEntity, String.class);

        return Result.ok().data(list);
    }

    @PostMapping("/file")
    public String fielUpload(@RequestParam("fiel") MultipartFile file){


//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.setConnection("Keep-Alive");
//        headers.setCacheControl("no-cache");
//
//        FileSystemResource resource = new FileSystemResource(new File("C:/ciel/捕获.PNG"));
//
//        HttpEntity<FileSystemResource> httpEntity = new HttpEntity<>(resource);
//
//
//        ResponseEntity<String> responseEntity =
//                restTemplate.postForEntity("http://SPRINGCLOUD-PRODUCER/producer/role/file", httpEntity, String.class);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("file",file.getResource());

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(parts, headers);

        ResponseEntity<Map> responseEntity =
                restTemplateServer.postForEntity("http://SPRINGCLOUD-PRODUCER/producer/role/file",httpEntity,Map.class);

        return "";
    }
}
