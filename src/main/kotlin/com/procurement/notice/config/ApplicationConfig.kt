package com.procurement.notice.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(WebConfig::class, ServiceConfig::class, DaoConfiguration::class)
class ApplicationConfig
