package com.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class AdminApplication {

	public static void main(String[] args) {
		log.info("用户中心开始启动......");
		SpringApplication.run(AdminApplication.class, args);
		log.info("用户中心启动成功......");
	}

}
