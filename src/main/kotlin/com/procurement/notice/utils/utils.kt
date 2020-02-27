package com.procurement.notice.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeFormatter
import com.procurement.notice.infrastructure.bind.jackson.configuration
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

private object JsonMapper {
    val mapper: ObjectMapper = ObjectMapper().apply { configuration() }
}

/*Date utils*/
fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, JsonDateTimeFormatter.formatter)
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}

fun LocalDateTime.toDate(): Date {
    return Date.from(this.toInstant(ZoneOffset.UTC))
}

fun Date.toLocalDateTime(): LocalDateTime =
    this.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()

fun localNowUTC(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
}

fun milliNowUTC(): Long {
    return localNowUTC().toInstant(ZoneOffset.UTC).toEpochMilli()
}

/*Json utils*/
fun <Any> toJson(obj: Any): String {
    try {
        return JsonMapper.mapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: String): T {
    Objects.requireNonNull(json)
    try {
        return JsonMapper.mapper.readValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}

fun <T> toObject(clazz: Class<T>, json: JsonNode): T {
    try {
        return JsonMapper.mapper.treeToValue(json, clazz)
    } catch (e: IOException) {
        throw IllegalArgumentException(e)
    }
}

fun <T : Any> JsonNode.toObject(target: Class<T>): T {
    try {
        return JsonMapper.mapper.treeToValue(this, target)
    } catch (expected: IOException) {
        throw IllegalArgumentException("Error binding JSON to an object of type '${target.canonicalName}'.", expected)
    }
}

fun String.toNode(): JsonNode = try {
    JsonMapper.mapper.readTree(this)
} catch (exception: JsonProcessingException) {
    throw IllegalArgumentException("Error parsing String to JsonNode.", exception)
}
