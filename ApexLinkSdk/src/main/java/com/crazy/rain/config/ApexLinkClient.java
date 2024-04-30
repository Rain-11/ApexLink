package com.crazy.rain.config;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ApexLinkClient
 * @Description: api开放平台client
 * @author: CrazyRain
 * @date: 2024/4/28 上午7:44
 */
@Data
public class ApexLinkClient {


    private String secretId;

    private String secretKey;

    public ApexLinkClient(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }


    private Map<String, String> getHeaders(String body, String method, String host) {
        HashMap<String, String> stringObjectsHashMap = new HashMap<>();
        stringObjectsHashMap.put("X-secretId", secretId);
        stringObjectsHashMap.put("X-nonce", RandomUtil.randomNumbers(4));
        stringObjectsHashMap.put("X-timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        Sign sign = SignUtil.sign(SignAlgorithm.SHA256withRSA);
        byte[] signed = sign.sign((body + secretKey).getBytes());
        stringObjectsHashMap.put("X-sign", Arrays.toString(signed));
        stringObjectsHashMap.put("X-method", method);
        stringObjectsHashMap.put("X-host", host);
        return stringObjectsHashMap;
    }


    public HttpResponse invokeHandler(String uri, String body, String host, String method) {
        if (method.isBlank()) {
            throw new RuntimeException("请求方式为空");
        }
        switch (method) {
            case "GET":
                return get(uri, body, host);
            case "POST":
                return post(uri, body, host);
            case "PUT":
                return put(uri, body, host);
            case "DELETE":
                return delete(uri, body, host);
        }
        throw new RuntimeException("请求处理异常");
    }


    public HttpResponse get(String uri, String body, String host) {
        Map<String, String> headers = getHeaders(body, "GET", host);
        HashMap params = new HashMap();
        if (body != null && !body.isEmpty()) {
            params = JSONUtil.parse(body).toBean(HashMap.class);
        }
        return HttpRequest.get(host + uri)
                .addHeaders(headers)
                .form(params)
                .execute();
    }

    public HttpResponse post(String uri, String body, String host) {
        Map<String, String> headers = getHeaders(body, "POST", host);
        return HttpRequest.post(host + uri)
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(body))
                .execute();
    }

    public HttpResponse delete(String uri, String body, String host) {
        Map<String, String> headers = getHeaders(body, "DELETE", host);
        return HttpRequest.delete(host + uri)
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(body))
                .execute();
    }

    public HttpResponse put(String uri, String body, String host) {
        Map<String, String> headers = getHeaders(body, "PUT", host);
        return HttpRequest.put(host + uri)
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(body))
                .execute();
    }

}
