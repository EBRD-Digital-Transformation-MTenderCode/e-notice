package com.procurement.notice.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.annotation.PostConstruct


@Configuration
class JsonConfig(private val mapper: ObjectMapper) {

    @PostConstruct
    fun init() {
        JsonMapper.init(mapper)
        DateFormatter.init()
    }

    object JsonMapper {
        lateinit var mapper: ObjectMapper
        fun init(objectMapper: ObjectMapper) {
            val module = SimpleModule()
            module.addSerializer(LocalDateTime::class.java, JsonDateSerializer())
            module.addDeserializer(LocalDateTime::class.java, JsonDateDeserializer())
            objectMapper.registerModule(module)
            objectMapper.registerKotlinModule()
            objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
            objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            objectMapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
            mapper = objectMapper
        }
    }

    object DateFormatter {
        lateinit var formatter: DateTimeFormatter
        fun init() {
            formatter = DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .append(DateTimeFormatter.ISO_LOCAL_DATE)
                    .appendLiteral('T')
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .appendLiteral(':')
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .optionalStart()
                    .appendLiteral(':')
                    .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                    .appendLiteral('Z')
                    .toFormatter()
        }
    }
}
