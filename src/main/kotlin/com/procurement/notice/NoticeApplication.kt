package com.procurement.notice

import com.procurement.notice.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfig::class])
class NoticeApplication

fun main(args: Array<String>) {
    runApplication<NoticeApplication>(*args)
}
