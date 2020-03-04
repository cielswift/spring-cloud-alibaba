package com.ciel.scaapi.retu.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

public class Pay {

    public static AlipayClient alipayClient =
            new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                    "2016101300679220",
                    "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjHQLazWr60FXuzmsXC20FKZ4k5h2FlRDX43pdCvD6VRTfZ66ddukw1T9rueftG0GARPFb4Sd+dhQmEJGZDJJ205KStR0+sH0eufvmzEKOLAmYUWvBElLKOEV4wThFG/7dgWy8o+09k5t1VMF8XIR9pdANg7QIRM8kCn+wa1LmQ6b9eM376sSmuP178xu4sSesh/Xc14dq1m7I+gce/d2yaJCL6cdJNi7oOrRwNDsT7DQzV3HqWVLxy85xeFlZIXjqq67BSquAYUSbkPGT7po18gvhZiSpTas2MdHkIRSVfUVLH+DhfOL+px4nnDPoi1uHDS/P4qGifTkSJ5TCN1J1AgMBAAECggEAU7nIhVd72lKmSXZbMfEDfOrn6jjUXoUtqCNz496B431YwXQPBXTMLezIKRu8GNEsxhKdcXQZOaiSnxdSJSV26NehHP0qjcs57lO5IbcnUqL4Kd9hWDF3naVUw68qmw7LvBd7ITljxX/HBGsFBjjXKUe7i7RW7Qek6/MXqA7u9pJhF7Cr3Ru6kLidsy1t35mnxh1Ucd5eLOyuczI3ENrLuw8Oc5X3yjHw6z7LOVhVm0VvjhihmGuMQSzOJnpNPJjTVe/qqcrcCyvCO5qI7cIjp77dzROKzhqippddHLD1ApN7Qd4x+kr5eb1QxNzBhcgslrbyhdzbhUZwDlFWd/i0AQKBgQDaxP0mJwf1PFvtGZ+ZCdEhBFhLrYd3kPJgKur+bNXvomv3qMVQbsAwpRVwJvOWo2EQ7HpL5v67LCAXANhiKQjPrNtuuUpAlVLRuOJnJJ4O+WKw+WcaNYjF/E4Nudp+iDNq0yiVH3vNQgp26Xvft2+wqQHxvP1m+L6E7ZKVIR1XoQKBgQC+30fR2wm3B5eU5AmYIqwZpzvxa5+SG+x+ZNOZFGbn3nhaxQi8/Fubx67th/4bzDSbH1n0Y1KFaU7Rp3lmfBbV0KGM+G9rDhBbFhbLJd6/o8sJPHzCPHLiapQL2c6jyKxQDEImIV2IZgzGZYjPow/14T5fRxMAFJMDIPrA2ib6VQKBgBp/dbIjVCFuGCxVyDD/MWdwYUl5UUk4M0NWr3P1tsv5vl4XAR9G6tx5gmk9Lo/2ZhFosz9yUTRWmeRvY8Yv9jNBWqrEoqvD6m4gmLupgcOKjVumOcjA5zoj30hmVIKy+Jdd0DKPx60K7L2OdkSYsVySe/vNyluokVsEc5bAlg9BAoGAU5HDCjjS2hYy4fet07RqkZjM//jVFs2vD+/pTT7Hh4G6RFO8bbI4Ec6t4kYGlfkklz50zjGmcc8XuWeQGbT+8oK0GNq1PbQcqSTUugFMs69tOBAJt6lry4JKa53jgYZdG3cihztYjl3P/hV6fx6v4EHMYz7lLMXVUCtiXomlQzkCgYBtkC0zIKQson4lxP9pDHazMWY/PhWZM9MMnaaLmE2IGrJcMkZscPCq5UJ9xVCI+kcDWCQE0i0jrszfPNPpopvdkGSM40F/lTe4z3ugLjTd72jgJIgnosn4PV3EanoG9K3uooobQ6swN2HmQcy5XcqbE+Ayc/r9ELA8hutg+BwpxQ==",
                    "json", "UTF-8",
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAllfkzNHG0bshipf7qnYZ7oSrTeKCHUMibEI04xN15ut+GCCGbiH+UaJbY/xM/NXoi2GSzUdNKHAk84LwSbqLARY49j7rvIAgkJN3csSAbrHZ9J57BjQg3lX7uGqCbk2mDiLFX58r0d1FQrRJfzc3GoktPI7itxD9CedhEp7KPQlW9CAok/eeYgHM64DuUC0IokfVdQ1Qhko22HajyJPZKaUjglBpX18zDtw/+511VOmSNU+3wipw3BUhKXOQdGtaxOrroa3k1zLPOHhyJFSsS583UvjDrih+hGNm2Y05ghc/TEw+9Awvc9o+y0fQU4U1rGWVFh4ce7SekZkxpUvDzQIDAQAB",
                    "RSA2");

    public static void main(String[] args) throws AlipayApiException {

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();//创建API对应的request类
        request.setBizContent("{" +
                "    \"out_trade_no\":\"20197220010101002\"," +//商户订单号
                "    \"total_amount\":\"88.88\"," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"store_id\":\"NJ_001\"," +
                "    \"timeout_express\":\"90m\"}");//订单允许的最晚付款时间
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        System.out.print(response.getBody());
//根据response中的结果继续业务逻辑处理

    }
}
