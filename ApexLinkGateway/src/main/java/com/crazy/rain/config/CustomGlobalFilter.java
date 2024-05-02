package com.crazy.rain.config;

import cn.hutool.crypto.SignUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crazy.rain.dubboService.InterfaceDetailsInterface;
import com.crazy.rain.dubboService.UserInterface;
import com.crazy.rain.model.entity.InterfaceInfo;
import com.crazy.rain.model.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private UserInterface userInterface;

    @DubboReference
    private InterfaceDetailsInterface interfaceDetailsInterface;


    private static final Logger log = LoggerFactory.getLogger(CustomGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //记录请求日志
        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getId();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        String path = request.getPath().toString();
        String methodValue = request.getMethodValue();
        log.info("请求id:{},请求地址:{},请求路径:{},请求方式:{}", requestId, remoteAddress, path, methodValue);
        //校验accessKey 是否已分配
        HttpHeaders headers = request.getHeaders();
        String secretId = headers.getFirst("X-secretId");
        String nonce = headers.getFirst("X-nonce");
        String timestamp = headers.getFirst("X-timestamp");
        String signed = headers.getFirst("X-sign");
        String method = headers.getFirst("X-method");
        String host = headers.getFirst("X-host");
        String body = headers.getFirst("X-body");

        if (StringUtils.isAnyBlank(secretId, nonce, timestamp, method, host)) {
            return errorInfo(exchange, "参数不存在", 40000);
        }

        final long timeoutTime = 60 * 5;

        if (timestamp != null && System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > timeoutTime) {
            return errorInfo(exchange, "签名已过期", 40000);
        }

        String randomNumberCache = interfaceDetailsInterface.getRandomNumberCache(nonce + secretId);
        if (StringUtils.isNotBlank(randomNumberCache)) {
            return errorInfo(exchange, "签名已过期", 40000);
        }

        if (StringUtils.isBlank(randomNumberCache)) {
            interfaceDetailsInterface.cacheRandomNumbers(nonce + secretId, nonce);
        }

        if (StringUtils.isAnyBlank(secretId, nonce, timestamp, signed, method)) {
            return errorInfo(exchange, "请求非法", 40000);
        }

        User user = userInterface.verifyUserAccessKey(secretId);

        if (user == null) {
            return errorInfo(exchange, "secretId未分配", 40000);
        }

        Sign sign = SignUtil.sign(SignAlgorithm.SHA256withRSA);

        byte[] data = (body + user.getSecretKey()).getBytes();

        boolean verify = sign.verify(data, signed.getBytes());

        if (verify) {
            return errorInfo(exchange, "签名异常", 40000);
        }

        String targetPath = path.substring(13);

        InterfaceInfo interfaceInfo = interfaceDetailsInterface.verifyIfItExists(targetPath, host, method);

        if (interfaceInfo == null) {
            return errorInfo(exchange, "接口不存在", 40400);
        }

        return handleResponse(exchange, chain, interfaceInfo.getId(), user.getId());
    }


    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 返回response
     *
     * @param exchange
     * @param message  异常信息
     * @param status   data中的status
     * @return
     */
    public static Mono<Void> errorInfo(ServerWebExchange exchange, String message, Integer status) {
        // 自定义返回格式
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("code", status == null ? 50000 : status);
        resultMap.put("message", StringUtils.isBlank(message) ? "服务异常！" : message);
        return Mono.defer(() -> {
            byte[] bytes = new byte[0];
            try {
                String jsonStr = JSONUtil.toJsonStr(resultMap);
                bytes = jsonStr.getBytes();
            } catch (Exception e) {
                log.error("网关响应异常：", e);
            }
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        });
    }

    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long id, Long userId) {
        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            // (这里就理解为它在拼接字符串,它把缓冲区的数据取出来，一点一点拼接好)
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 业务逻辑
                                HttpStatus statusCode = originalResponse.getStatusCode();
                                // 读取响应体的内容并转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建响应日志
                                String data = new String(content, StandardCharsets.UTF_8);//data

                                try {
                                    JSONObject entries = JSONUtil.parseObj(data);
                                    int code = (int) entries.get("code");
                                    if (code != 20000) {
                                        return bufferFactory.wrap(getErrorMessage("服务调用失败", 50000).getBytes());
                                    }
                                } catch (Exception e) {
                                    log.error("数据解析异常");
                                }

                                boolean result = interfaceDetailsInterface.increaseNumberCalls(id, userId);
                                if (!result) {
                                    return bufferFactory.wrap(getErrorMessage("扣费失败", 50000).getBytes());
                                }
                                // 打印日志
                                log.info("响应结果：" + data);
                                // 将处理后的内容重新包装成DataBuffer并返回
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("网关处理响应异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    private String getErrorMessage(String message, int code) {
        Map<String, Object> resultMap = new HashMap<>(8);
        resultMap.put("code", code);
        resultMap.put("message", message);
        return JSONUtil.toJsonStr(resultMap);
    }

}