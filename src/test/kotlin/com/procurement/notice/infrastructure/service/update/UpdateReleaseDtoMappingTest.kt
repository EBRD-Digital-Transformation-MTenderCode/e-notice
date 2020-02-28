package com.procurement.notice.infrastructure.service.update

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.procurement.notice.infrastructure.bind.jackson.configuration
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toNode
import com.procurement.notice.utils.toObject
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate
import java.io.File

class UpdateReleaseDtoMappingTest {

    private val restTemplate = RestTemplate()
    private val objectMapper = jacksonObjectMapper().apply { configuration() }

    @Test
    fun mapRelease() {
        val releaseNodes = objectMapper.readTree(File("src/test/resources/json/dto/release/releases.json"))
        releaseNodes.forEach { release ->
            val dto = toObject(RequestRelease::class.java, release)
            val actualRelease = toJson(dto).toNode()
            compareByKeys(expected = release, actual = actualRelease)
        }
    }
}

fun compareByKeys(expected: JsonNode, actual: JsonNode) {
    expected.fields().forEach { expectedField ->
        if (!actual.has(expectedField.key)) {
            if (expectedField.value.size() != 0) {
                throw RuntimeException("Cannot find field '${expectedField.key}' in node with fields ${actual.fieldNames().asSequence().toList()}")
            }
        }
        when (expectedField.value) {
            is ObjectNode -> {
                compareByKeys(expectedField.value, actual.get(expectedField.key))
            }
            is ArrayNode  -> {
                val nodes = expectedField.value as ArrayNode
                nodes.forEachIndexed { index, item ->
                    compareByKeys(item, actual.get(expectedField.key)[index])
                }
            }
        }
    }
}


