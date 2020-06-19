package com.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName:
 * @Description: 网关过滤器
 * @Author: sutao
 * @Date: 2020/6/19 17:27
 * @Snice: V1.0.0
 */
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {
    private static final String ELAPSED_TIME_BEGIN = "elapsedTimeBegin";
    private final String TOKENSTR = "AZJTK";
    private final String GATEWAYTOKEN = "GATEWAYTOKEN";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入filter");
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        //地址栏传参的url token 从地址栏中获取
        String token = request.getQueryParams().getFirst(TOKENSTR);
      /*  //如果地址栏中有token信息，
        if(StringUtils.isEmpty(token)){
            //地址栏中为空，则从header中获取
            token = request.getHeaders().getFirst(this.TOKENSTR);
        }else{
            //地址栏中获取到token,讲token传入token
            request.mutate().header(TOKENSTR,token).build();
        }

        exchange.getAttributes().put(ELAPSED_TIME_BEGIN, System.currentTimeMillis());

        ServerHttpResponse response = exchange.getResponse();

        String method = request.getMethodValue();

        if (token == null || token.isEmpty()) {
            log.info("token为空...");
            return response.writeWith(Mono.just(this.sessionErrror(response)));
        }
        //当token不为空的时候去校验token信息是否正确
        ResultEnum resultEnum = jwtService.check(token,path,method);
        //校验不成功，则直接中断
        if(!ResultEnum.OK.equals(resultEnum)){
            log.info("校验不成功,result:{}",resultEnum);
            return response.writeWith(Mono.just(this.sessionErrror(response)));
        }

        //增加令牌 并存入redis 缓存时间30秒
        String urlToken = this.getUrlToken();
        redisUtil.setKey(urlToken,token,30, TimeUnit.SECONDS);*/
       // request.mutate().header(GATEWAYTOKEN,urlToken).build();

        return chain.filter(exchange);
      /*
        //重新组装头部
        ServerWebExchange newExchange = exchange.mutate().request(request).build();
        return chain.filter(newExchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(ELAPSED_TIME_BEGIN);
                    if (startTime != null) {
                       log.info(path + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );*/
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
