package com.procurement.notice.infrastructure.bind.criteria.requirement

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.procurement.notice.domain.model.enums.RequirementDataType
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.model.ocds.ExpectedValue
import com.procurement.notice.model.ocds.MaxValue
import com.procurement.notice.model.ocds.MinValue
import com.procurement.notice.model.ocds.NoneValue
import com.procurement.notice.model.ocds.RangeValue
import com.procurement.notice.model.ocds.Requirement
import com.procurement.notice.model.ocds.RequirementValue
import java.io.IOException
import java.math.BigDecimal

class RequirementDeserializer : JsonDeserializer<List<Requirement>>() {
    companion object {
        fun deserialize(requirements: ArrayNode): List<Requirement> {

            return requirements.map { requirement ->
                val id: String = requirement.get("id").asText()
                val title: String = requirement.get("title").asText()
                val dataType: RequirementDataType = RequirementDataType.fromString(requirement.get("dataType").asText())
                val description: String? = requirement.takeIf { it.has("description") }?.get("description")?.asText()
                val period: Requirement.Period? = requirement.takeIf { it.has("period") }
                    ?.let {
                        val period = it.get("period")
                        val startDate = JsonDateTimeDeserializer.deserialize(period.get("startDate").asText())
                        val endDate = JsonDateTimeDeserializer.deserialize(period.get("endDate").asText())
                        Requirement.Period(
                            startDate = startDate,
                            endDate = endDate
                        )
                    }

                Requirement(
                    id = id,
                    title = title,
                    description = description,
                    period = period,
                    dataType = dataType,
                    value = requirementValue(requirement)
                )
            }
        }

        private fun requirementValue(requirementNode: JsonNode): RequirementValue {
            val dataType = RequirementDataType.fromString(requirementNode.get("dataType").asText())
            return when {
                isExpectedValue(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.BOOLEAN -> ExpectedValue.of(requirementNode.get("expectedValue").booleanValue())
                        RequirementDataType.STRING -> ExpectedValue.of(requirementNode.get("expectedValue").textValue())
                        RequirementDataType.NUMBER -> ExpectedValue.of(BigDecimal(requirementNode.get("expectedValue").asText()))
                        RequirementDataType.INTEGER -> ExpectedValue.of(requirementNode.get("expectedValue").longValue())
                    }
                }
                isRange(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER -> RangeValue.of(
                            BigDecimal(requirementNode.get("minValue").asText()),
                            BigDecimal(requirementNode.get("maxValue").asText())
                        )
                        RequirementDataType.INTEGER -> RangeValue.of(
                            requirementNode.get("minValue").longValue(),
                            requirementNode.get("maxValue").longValue()
                        )
                        RequirementDataType.BOOLEAN, RequirementDataType.STRING -> throw RuntimeException()
                    }
                }
                isOnlyMax(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER -> MaxValue.of(BigDecimal(requirementNode.get("maxValue").asText()))
                        RequirementDataType.INTEGER -> MaxValue.of(requirementNode.get("maxValue").longValue())
                        RequirementDataType.BOOLEAN, RequirementDataType.STRING -> throw RuntimeException()
                    }
                }
                isOnlyMin(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER -> MinValue.of(BigDecimal(requirementNode.get("minValue").asText()))
                        RequirementDataType.INTEGER -> MinValue.of(requirementNode.get("minValue").longValue())
                        RequirementDataType.BOOLEAN, RequirementDataType.STRING -> throw RuntimeException()
                    }
                }
                isNotBounded(requirementNode) -> {
                    NoneValue
                }
                else -> {
                    throw ErrorException(
                        ErrorType.INVALID_REQUIREMENT_VALUE,
                        message = "Expected value cannot exists with Min/MaxValue"
                    )
                }
            }
        }

        private fun isExpectedValue(requirementNode: JsonNode) = requirementNode.has("expectedValue")
            && (!requirementNode.has("minValue") || !requirementNode.has("maxValue"))

        private fun isRange(requirementNode: JsonNode) =
            requirementNode.has("minValue") && requirementNode.has("maxValue")

        private fun isOnlyMax(requirementNode: JsonNode) = requirementNode.has("maxValue")
        private fun isOnlyMin(requirementNode: JsonNode) = requirementNode.has("minValue")

        private fun isNotBounded(requirementNode: JsonNode) = !requirementNode.has("expectedValue")
            && !requirementNode.has("minValue") && !requirementNode.has("maxValue")
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(
        jsonParser: JsonParser,
        deserializationContext: DeserializationContext
    ): List<Requirement> {
        val requirementNode = jsonParser.readValueAsTree<ArrayNode>()
        return deserialize(requirementNode)
    }
}
