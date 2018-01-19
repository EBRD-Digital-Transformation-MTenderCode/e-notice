package com.procurement.notice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NoticeApplication {

    public static void main(final String[] args) {
        SpringApplication.run(NoticeApplication.class, args);
    }
}
