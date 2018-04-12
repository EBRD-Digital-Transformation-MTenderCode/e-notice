package com.procurement.notice.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.procurement.notice.config.JsonConfig
import java.io.IOException
import java.util.*


fun toJsonNode(value: String): JsonNode = JsonConfig.JsonMapper.mapper.readTree(value)

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