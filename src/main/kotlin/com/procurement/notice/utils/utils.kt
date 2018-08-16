package com.procurement.notice.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.procurement.notice.config.JsonConfig
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, JsonConfig.DateFormatter.formatter)
}


/*Date utils*/
fun LocalDateTime.toDate(): Date {
    return Date.from(this.toInstant(ZoneOffset.UTC))
}

fun localNowUTC(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
}

fun milliNowUTC(): Long {
    return localNowUTC().toInstant(ZoneOffset.UTC).toEpochMilli()
}

/*Json utils*/
fun createObjectNode(): ObjectNode = JsonConfig.JsonMapper.mapper.createObjectNode()

fun <Any> toJson(obj: Any): String {
    try {
        return JsonConfig.JsonMapper.mapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: String): T {
    Objects.requireNonNull(json)
    try {
        return JsonConfig.JsonMapper.mapper.readValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: JsonNode): T {
    try {
        return JsonConfig.JsonMapper.mapper.treeToValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}