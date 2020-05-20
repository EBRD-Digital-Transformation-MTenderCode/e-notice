package com.procurement.notice.config

import com.procurement.notice.application.service.Logger
import com.procurement.notice.infrastructure.service.CustomLogger
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoggerConfiguration {
    @Bean
    fun logger(): Logger = CustomLogger()
}
