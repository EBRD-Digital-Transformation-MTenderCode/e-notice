package com.procurement.notice.infrastructure.bind.criteria.requirement

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.model.ocds.ExpectedValue
import com.procurement.notice.model.ocds.MaxValue
import com.procurement.notice.model.ocds.MinValue
import com.procurement.notice.model.ocds.NoneValue
import com.procurement.notice.model.ocds.RangeValue
import com.procurement.notice.model.ocds.Requirement
import java.io.IOException
import java.math.BigDecimal

class RequirementSerializer : JsonSerializer<List<Requirement>>() {
    companion object {
        fun serialize(requirements: List<Requirement>): ArrayNode {
            fun BigDecimal.jsonFormat() = BigDecimal("%.3f".format(this))
            val serializedRequirements = JsonNodeFactory.withExactBigDecimals(true).arrayNode()

            requirements.map { requirement ->
                val requirementNode = JsonNodeFactory.withExactBigDecimals(true).objectNode()

                requirementNode.put("id", requirement.id)
                requirementNode.put("title", requirement.title)
                requirement.dataType?.let { requirementNode.put("dataType", it.value) }

                requirement.status?.let { requirementNode.put("status", it) }
                requirement.datePublished?.let { requirementNode.put("datePublished", JsonDateTimeSerializer.serialize(it)) }

                requirement.description?.let { requirementNode.put("description", it) }
                requirement.period?.let {
                    requirementNode.putObject("period")
                        .put("startDate", JsonDateTimeSerializer.serialize(it.startDate))
                        .put("endDate", JsonDateTimeSerializer.serialize(it.endDate))
                }

                requirement.eligibleEvidences
                    ?.map { it.serialize() }
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { requirementNode.putArray("eligibleEvidences").addAll(it) }

                when (requirement.value) {

                    is ExpectedValue.AsString -> {
                        requirementNode.put("expectedValue", requirement.value.value)
                    }
                    is ExpectedValue.AsBoolean -> {
                        requirementNode.put("expectedValue", requirement.value.value)
                    }
                    is ExpectedValue.AsNumber -> {
                        requirementNode.put("expectedValue", requirement.value.value.jsonFormat())
                    }
                    is ExpectedValue.AsInteger -> {
                        requirementNode.put("expectedValue", requirement.value.value)
                    }

                    is RangeValue.AsNumber -> {
                        requirementNode.put("minValue", requirement.value.minValue.jsonFormat())
                        requirementNode.put("maxValue", requirement.value.maxValue.jsonFormat())
                    }
                    is RangeValue.AsInteger -> {
                        requirementNode.put("minValue", requirement.value.minValue)
                        requirementNode.put("maxValue", requirement.value.maxValue)
                    }

                    is MinValue.AsNumber -> {
                        requirementNode.put("minValue", requirement.value.value.jsonFormat())
                    }
                    is MinValue.AsInteger -> {
                        requirementNode.put("minValue", requirement.value.value)
                    }

                    is MaxValue.AsNumber -> {
                        requirementNode.put("maxValue", requirement.value.value.jsonFormat())
                    }
                    is MaxValue.AsInteger -> {
                        requirementNode.put("maxValue", requirement.value.value)
                    }
                    is NoneValue -> Unit
                }

                requirementNode
            }.also { it.forEach { requirement -> serializedRequirements.add(requirement) } }

            return serializedRequirements
        }

        fun Requirement.EligibleEvidence.serialize(): JsonNode =
            JsonNodeFactory.instance.objectNode()
                .apply {
                    put("id", this@serialize.id)
                    put("title", this@serialize.title)
                    put("type", this@serialize.type)

                    this@serialize.description?.let { put("description", it) }

                    this@serialize.relatedDocument
                        ?.let {
                            val relatedDocumentNode = JsonNodeFactory.instance.objectNode()
                                .apply { put("id", it.id) }

                            set("relatedDocument", relatedDocumentNode)
                        }
                }
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
        requirements: List<Requirement>,
        jsonGenerator: JsonGenerator,
        provider: SerializerProvider
    ) =
        jsonGenerator.writeTree(serialize(requirements))
}