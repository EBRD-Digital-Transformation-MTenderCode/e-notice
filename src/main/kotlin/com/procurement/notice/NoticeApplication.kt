package com.procurement.notice

import com.procurement.notice.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
@EnableEurekaClient
class NoticeApplication

fun main(args: Array<String>) {
    runApplication<NoticeApplication>(*args)
}
