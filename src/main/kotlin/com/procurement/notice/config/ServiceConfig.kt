package com.procurement.notice.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.procurement.notice.service",
        "com.procurement.notice.infrastructure.handler",
        "com.procurement.notice.application.service",
        "com.procurement.notice.infrastructure.service"
    ]
)
class ServiceConfig
