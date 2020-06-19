package com.demo.filter.ratelimit;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: RemoteAddrKeyResolver
 * @Description: 使用Redis插件方式做请求IP地址信息限流
 * @Author: sutao
 * @Date: 2020/6/19 17:27
 * @Snice: V1.0.0
 */
public class RemoteAddrKeyResolver implements KeyResolver {
    public static final String BEAN_NAME = "remoteAddrKeyResolver";

    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
