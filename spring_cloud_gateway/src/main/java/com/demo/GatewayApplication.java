package com.demo;

import com.demo.filter.TokenFilter;
import com.demo.filter.factory.ElapsedGatewayFilterFactory;
import com.demo.filter.ratelimit.RateLimitFilterByIp;
import com.demo.filter.ratelimit.RemoteAddrKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class GatewayApplication {

	public static void main(String[] args) {
		log.info("网关中心开始启动......");
		SpringApplication.run(GatewayApplication.class, args);
		log.info("网关中心启动成功......");
	}

	@Bean
	public TokenFilter tokenFilter() {
		return new TokenFilter();
	}

	@Bean
	public RouteLocator customerRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/test/rateLimit")
						.filters(f -> f.filter(new RateLimitFilterByIp(100,1, Duration.ofSeconds(1))))
						.uri("http://localhost:8002/hello/rateLimit")
						.id("spring_cloud_admin")
				).build();
	}

	@Bean
	public ElapsedGatewayFilterFactory elapsedGatewayFilterFactory() {
		return new ElapsedGatewayFilterFactory();
	}

	@Bean(name = RemoteAddrKeyResolver.BEAN_NAME)
	public RemoteAddrKeyResolver remoteAddrKeyResolver() {
		return new RemoteAddrKeyResolver();
	}

	private static final String ALL = "*";
	private static final String MAX_AGE = "18000L";

	@Bean
	public WebFilter corsFilter() {
		return (ServerWebExchange ctx, WebFilterChain chain) -> {
			ServerHttpRequest request = ctx.getRequest();
			if (!CorsUtils.isCorsRequest(request)) {
				return chain.filter(ctx);
			}
			HttpHeaders requestHeaders = request.getHeaders();
			ServerHttpResponse response = ctx.getResponse();
			HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
			HttpHeaders headers = response.getHeaders();

			headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
			if (requestMethod != null) {
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
			}
			// headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
			headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
			headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
			headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
			if (request.getMethod() == HttpMethod.OPTIONS) {
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
				response.setStatusCode(HttpStatus.OK);
				return Mono.empty();
			}
			return chain.filter(ctx);
		};
	}

}
