package com.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: FallbackController
 * @Description: 服务熔断处理
 * @Author: daidegang
 * @Date: 2019/8/22 17:53
 * @Snice: V1.0.0
 */
@RestController
public class FallbackController {
    @GetMapping("/fallback")
    public String fallback() {
        return "Hello World!\nfrom gateway";
    }
}
