package com.crazy.rain.config;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
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

    private static final String HOST = "http://localhost:9001/api-consumer";

    private String secretId;

    private String secretKey;

    public ApexLinkClient() {
    }

    public ApexLinkClient(String secretId, String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    public HttpResponse getUserName(String body) {
        Map<String, String> headers = getHeaders(body, "GET",HOST);
        return HttpRequest.get(HOST + "/test/getUserName")
                .addHeaders(headers)
                .form("username", body)
                .execute();
    }

    private Map<String, String> getHeaders(String body, String method,String host) {
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


}
