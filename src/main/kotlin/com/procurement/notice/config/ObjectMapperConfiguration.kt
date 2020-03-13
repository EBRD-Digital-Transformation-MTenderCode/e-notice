package com.procurement.notice.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration(objectMapper: ObjectMapper) {

    init {
        objectMapper.configuration()
    }
}
