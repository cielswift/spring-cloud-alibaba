package com.ciel.scagateway.filter.controller;

import com.ciel.scaapi.exception.AlertException;
import com.ciel.scaapi.retu.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin //允许跨域调用

@AllArgsConstructor //在构造函数里注入属性参数
@Slf4j //直接注入日志对象
public class WayController {

    private DiscoveryClient discoveryClient;

    private AutowireCapableBeanFactory beanFactory;

    private WebClient.Builder webb;

    @GetMapping("/mono")
    public Mono<String> mono() throws AlertException {

        if(System.currentTimeMillis()%2==1){
            throw new AlertException("fucks");
        }
        /**
         * 首先是Mono.just()，直接由这个对象构造出一个Mono。
         * 然后Mono.fromRunnable(），用一个线程来构建一个Mono。
         */
        Mono<Object> mono = Mono.fromRunnable(() -> System.out.println("a"));

        Mono.just("fuck").flatMap(x -> Mono.just(x.toUpperCase()))
                .then(Mono.just("ff"));

        return webb.build().get().uri("https://element.eleme.cn/#/zh-CN/component/image")
                .retrieve().bodyToMono(String.class);
    }

    @PostMapping("/upload")
    public Mono<String> addAttach(@RequestPart("file") FilePart filePart,//获取文件参数
                                  @RequestPart("dataId") String dataId){//获取其他参数

        String strFileName = filePart.filename();//获取文件名
        filePart.transferTo(Paths.get("c:/ciel/".concat(strFileName)));//转储文件
        return Mono.just("ok");
    }

    @GetMapping("/downloadFile")
    public Mono<Void> downloadFile(String dataId, ServerHttpResponse response) throws UnsupportedEncodingException {

        File file = new File("C:/ciel/微信图片_20200302214455.png");
        if(!file.exists()) {

            return ServerHttpResponseUtil.writeObjectJson(response, Result.error("文件不存在"));

        }else {
            ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;

            response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, //输出文件名乱码问题处理
                "attachment; filename=" + new String("oth.png".getBytes("UTF-8"), "iso-8859-1"));

            response.getHeaders().setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return zeroCopyResponse.writeWith(file, 0, file.length());
        }
    }

    /**
     * 发射多个数据
     *
     * @return
     */
    @GetMapping("/flux")
    public Flux<Object> flux() {
        return Flux.just(Stream.generate(UUID::randomUUID).limit(10000).collect(Collectors.toList()));
    }


    @GetMapping("/index")
    public Mono<Object> index() {

        log.error("当前线程名称" + Thread.currentThread().getName());

        return Mono.create(monoSink -> {
            log.error("创建 Mono");

            monoSink.success("hello webflux");
        })
                .doOnSubscribe(subscription -> { //当订阅者去订阅发布者的时候，该方法会调用

                    log.error("{}", subscription);
                    log.error("当前线程名称" + Thread.currentThread().getName());
                }).doOnNext(o -> {  //当订阅者收到数据时，改方法会调用

                    log.error("{}", o);
                    log.error("当前线程名称" + Thread.currentThread().getName());
                });
    }

    /**
     * 获取服务实例
     */
    @GetMapping("/instances")
    public Map<String, List<ServiceInstance>> instances() {
        Map<String, List<ServiceInstance>> instances = new HashMap<>(1 << 4);
        List<String> services = discoveryClient.getServices();
        services.forEach(s -> {
            List<ServiceInstance> list = discoveryClient.getInstances(s);
            instances.put(s, list);
        });
        return instances;
    }

}
