package com.procurement.notice.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notice.infrastructure.service.JacksonJsonTransform
import com.procurement.notice.infrastructure.service.Transform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonTransformConfiguration {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun jsonTransform(): Transform = JacksonJsonTransform(objectMapper)

}
