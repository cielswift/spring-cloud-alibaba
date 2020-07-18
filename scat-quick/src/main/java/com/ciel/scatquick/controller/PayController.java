package com.ciel.scatquick.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ciel.scaapi.retu.Result;
import com.ciel.scaapi.util.Faster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pay")
public class PayController {

    public static final Map<String, String> PARAM = new HashMap<>();

    static{
        PARAM.put("URL","https://openapi.alipaydev.com/gateway.do");
        PARAM.put("APPID","2016101300679220");
        PARAM.put("PRIVATE","MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjHQLazWr60FXuzmsXC20FKZ4k5h2FlRDX43pdCvD6VRTfZ66ddukw1T9rueftG0GARPFb4Sd+dhQmEJGZDJJ205KStR0+sH0eufvmzEKOLAmYUWvBElLKOEV4wThFG/7dgWy8o+09k5t1VMF8XIR9pdANg7QIRM8kCn+wa1LmQ6b9eM376sSmuP178xu4sSesh/Xc14dq1m7I+gce/d2yaJCL6cdJNi7oOrRwNDsT7DQzV3HqWVLxy85xeFlZIXjqq67BSquAYUSbkPGT7po18gvhZiSpTas2MdHkIRSVfUVLH+DhfOL+px4nnDPoi1uHDS/P4qGifTkSJ5TCN1J1AgMBAAECggEAU7nIhVd72lKmSXZbMfEDfOrn6jjUXoUtqCNz496B431YwXQPBXTMLezIKRu8GNEsxhKdcXQZOaiSnxdSJSV26NehHP0qjcs57lO5IbcnUqL4Kd9hWDF3naVUw68qmw7LvBd7ITljxX/HBGsFBjjXKUe7i7RW7Qek6/MXqA7u9pJhF7Cr3Ru6kLidsy1t35mnxh1Ucd5eLOyuczI3ENrLuw8Oc5X3yjHw6z7LOVhVm0VvjhihmGuMQSzOJnpNPJjTVe/qqcrcCyvCO5qI7cIjp77dzROKzhqippddHLD1ApN7Qd4x+kr5eb1QxNzBhcgslrbyhdzbhUZwDlFWd/i0AQKBgQDaxP0mJwf1PFvtGZ+ZCdEhBFhLrYd3kPJgKur+bNXvomv3qMVQbsAwpRVwJvOWo2EQ7HpL5v67LCAXANhiKQjPrNtuuUpAlVLRuOJnJJ4O+WKw+WcaNYjF/E4Nudp+iDNq0yiVH3vNQgp26Xvft2+wqQHxvP1m+L6E7ZKVIR1XoQKBgQC+30fR2wm3B5eU5AmYIqwZpzvxa5+SG+x+ZNOZFGbn3nhaxQi8/Fubx67th/4bzDSbH1n0Y1KFaU7Rp3lmfBbV0KGM+G9rDhBbFhbLJd6/o8sJPHzCPHLiapQL2c6jyKxQDEImIV2IZgzGZYjPow/14T5fRxMAFJMDIPrA2ib6VQKBgBp/dbIjVCFuGCxVyDD/MWdwYUl5UUk4M0NWr3P1tsv5vl4XAR9G6tx5gmk9Lo/2ZhFosz9yUTRWmeRvY8Yv9jNBWqrEoqvD6m4gmLupgcOKjVumOcjA5zoj30hmVIKy+Jdd0DKPx60K7L2OdkSYsVySe/vNyluokVsEc5bAlg9BAoGAU5HDCjjS2hYy4fet07RqkZjM//jVFs2vD+/pTT7Hh4G6RFO8bbI4Ec6t4kYGlfkklz50zjGmcc8XuWeQGbT+8oK0GNq1PbQcqSTUugFMs69tOBAJt6lry4JKa53jgYZdG3cihztYjl3P/hV6fx6v4EHMYz7lLMXVUCtiXomlQzkCgYBtkC0zIKQson4lxP9pDHazMWY/PhWZM9MMnaaLmE2IGrJcMkZscPCq5UJ9xVCI+kcDWCQE0i0jrszfPNPpopvdkGSM40F/lTe4z3ugLjTd72jgJIgnosn4PV3EanoG9K3uooobQ6swN2HmQcy5XcqbE+Ayc/r9ELA8hutg+BwpxQ==");
        PARAM.put("FORMAT","json");
        PARAM.put("CHARSET","UTF-8");
        PARAM.put("PUBLIC","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAllfkzNHG0bshipf7qnYZ7oSrTeKCHUMibEI04xN15ut+GCCGbiH+UaJbY/xM/NXoi2GSzUdNKHAk84LwSbqLARY49j7rvIAgkJN3csSAbrHZ9J57BjQg3lX7uGqCbk2mDiLFX58r0d1FQrRJfzc3GoktPI7itxD9CedhEp7KPQlW9CAok/eeYgHM64DuUC0IokfVdQ1Qhko22HajyJPZKaUjglBpX18zDtw/+511VOmSNU+3wipw3BUhKXOQdGtaxOrroa3k1zLPOHhyJFSsS583UvjDrih+hGNm2Y05ghc/TEw+9Awvc9o+y0fQU4U1rGWVFh4ce7SekZkxpUvDzQIDAQAB");
        PARAM.put("SIGN","RSA2");
    }


    public static AlipayClient alipayClient =
            new DefaultAlipayClient(PARAM.get("URL")
                    ,PARAM.get("APPID"),
                    PARAM.get("PRIVATE"),
                    PARAM.get("FORMAT"),
                    PARAM.get("CHARSET"),
                    PARAM.get("PUBLIC"),
                    PARAM.get("SIGN"));

    @GetMapping("/pay")
    public Result pay(@RequestParam String name,
                      @RequestParam String money) throws AlipayApiException {

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("http://wxz1bosi8vp44b1ra3lambc.ngrok.wendal.cn/quick/pay/notify");

        HashMap<String, String> context = new HashMap<>();
        context.put("out_trade_no",String.valueOf(System.currentTimeMillis()));
        context.put("total_amount","439862");
        context.put("subject","naizi");

        request.setBizContent(Faster.toJson(context));
        AlipayTradePrecreateResponse response = alipayClient.execute(request);

        HashMap hashMap = Faster.parseJson(response.getBody(), HashMap.class);
        JSONObject bo = (JSONObject)hashMap.get("alipay_trade_precreate_response");

        Map<String, String> map = new HashMap<>();
        map.put("sign",hashMap.get("sign").toString());
        bo.entrySet().forEach(t -> {
            map.put(t.getKey(),t.getValue().toString());
        });
        boolean v1 = AlipaySignature.rsaCheckV1(map, PARAM.get("PUBLIC"), PARAM.get("CHARSET"), PARAM.get("SIGN"));
        System.out.println("验签:"+v1);

        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return Result.ok().data(Faster.parseJson(response.getBody(),Map.class));
    }


    @GetMapping("/ser")
    public Result pay(String no) throws AlipayApiException {

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        HashMap<String, String> context = new HashMap<>();
        context.put("out_trade_no",no);

        request.setBizContent(Faster.toJson(context));

        AlipayTradeQueryResponse response = alipayClient.execute(request);

        HashMap hashMap = Faster.parseJson(response.getBody(), HashMap.class);
        JSONObject bo = (JSONObject)hashMap.get("alipay_trade_query_response");

        Map<String, String> map = new HashMap<>();
        map.put("sign",hashMap.get("sign").toString());
        bo.entrySet().forEach(t -> {
            map.put(t.getKey(),t.getValue().toString());
        });
        boolean v1 = AlipaySignature.rsaCheckV1(map, PARAM.get("PUBLIC"), PARAM.get("CHARSET"), PARAM.get("SIGN"));
        System.out.println("验签:"+v1);


        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        return Result.ok().data(Faster.parseJson(response.getBody(),Map.class));
    }

    @RequestMapping("/notify")
    public void notify(HttpServletRequest request,
                       HttpServletResponse response) throws IOException, AlipayApiException {

        Map<String, String> map = request.getParameterMap()
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, t -> t.getValue()[0]));

        boolean v1 = AlipaySignature.rsaCheckV1(map, PARAM.get("PUBLIC"), PARAM.get("CHARSET"), PARAM.get("SIGN"));

        System.out.println("验签:"+v1);
        System.out.println(map);

        response.getWriter().println("success");
    }

}
