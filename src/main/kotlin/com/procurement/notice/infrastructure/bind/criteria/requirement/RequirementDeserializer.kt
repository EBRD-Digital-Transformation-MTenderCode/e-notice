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
import java.time.LocalDateTime

class RequirementDeserializer : JsonDeserializer<List<Requirement>>() {
    companion object {
        fun deserialize(requirements: ArrayNode): List<Requirement> {

            return requirements.map { requirement ->
                val id: String = requirement.get("id").asText()
                val title: String = requirement.get("title").asText()
                val description: String? = requirement.takeIf { it.has("description") }?.get("description")?.asText()
                val status: String? = requirement.takeIf { it.has("status") }?.get("status")?.asText()

                val datePublished: LocalDateTime? = requirement
                    .takeIf { it.has("datePublished") }
                    ?.let { dateNode -> JsonDateTimeDeserializer.deserialize(dateNode.get("datePublished").asText()) }

                val dataType: RequirementDataType = RequirementDataType.fromString(requirement.get("dataType").asText())
                val period: Requirement.Period? = deserializePeriod(requirement)
                val eligibleEvidences = deserializeEligibleEvidences(requirement)

                Requirement(
                    id = id,
                    title = title,
                    description = description,
                    period = period,
                    status = status,
                    datePublished = datePublished,
                    dataType = dataType,
                    value = requirementValue(requirement),
                    eligibleEvidences = eligibleEvidences
                )
            }
        }

        fun deserializePeriod(requirementNode: JsonNode): Requirement.Period? =
            requirementNode
                .takeIf { it.has("period") }
                ?.let {
                    val period = it.get("period")
                    val startDate = JsonDateTimeDeserializer.deserialize(period.get("startDate").asText())
                    val endDate = JsonDateTimeDeserializer.deserialize(period.get("endDate").asText())
                    Requirement.Period(
                        startDate = startDate,
                        endDate = endDate
                    )
                }

        fun deserializeEligibleEvidences(requirementNode: JsonNode): List<Requirement.EligibleEvidence>? =
            requirementNode
            .takeIf { it.has("eligibleEvidences") }
            ?.get("eligibleEvidences")
            ?.map {
                Requirement.EligibleEvidence(
                    id = it.get("id").asText(),
                    title = it.get("title").asText(),
                    type = it.get("type").asText(),
                    description = it.takeIf { it.has("description") }?.get("description")?.asText(),
                    relatedDocument = it.takeIf { it.has("relatedDocument") }
                        ?.get("relatedDocument")
                        ?.let {
                            Requirement.EligibleEvidence.RelatedDocument(id = it.get("id").asText())
                        }
                )
            }

        private fun requirementValue(requirementNode: JsonNode): RequirementValue {
            fun datatypeMismatchException(): Nothing = throw ErrorException(
                ErrorType.INVALID_REQUIREMENT_VALUE,
                message = "Requirement.dataType mismatch with datatype in expectedValue || minValue || maxValue."
            )

            val dataType = RequirementDataType.fromString(requirementNode.get("dataType").asText())
            return when {
                isExpectedValue(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.BOOLEAN ->
                            if (requirementNode.get("expectedValue").isBoolean)
                                ExpectedValue.of(requirementNode.get("expectedValue").booleanValue())
                            else
                                datatypeMismatchException()

                        RequirementDataType.STRING ->
                            if (requirementNode.get("expectedValue").isTextual)
                                ExpectedValue.of(requirementNode.get("expectedValue").textValue())
                            else
                                datatypeMismatchException()

                        RequirementDataType.NUMBER ->
                            if (requirementNode.get("expectedValue").isBigDecimal || requirementNode.get("expectedValue").isBigInteger)
                                ExpectedValue.of(BigDecimal(requirementNode.get("expectedValue").asText()))
                            else
                                datatypeMismatchException()

                        RequirementDataType.INTEGER ->
                            if (requirementNode.get("expectedValue").isBigInteger)
                                ExpectedValue.of(requirementNode.get("expectedValue").longValue())
                            else
                                datatypeMismatchException()
                    }
                }
                isRange(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER ->
                            if ((requirementNode.get("minValue").isBigDecimal || requirementNode.get("minValue").isBigInteger)
                                && (requirementNode.get("maxValue").isBigDecimal || requirementNode.get("maxValue").isBigInteger))
                                RangeValue.of(
                                    BigDecimal(requirementNode.get("minValue").asText()),
                                    BigDecimal(requirementNode.get("maxValue").asText())
                                )
                            else
                                datatypeMismatchException()

                        RequirementDataType.INTEGER ->
                            if (requirementNode.get("minValue").isBigInteger && requirementNode.get("maxValue").isBigInteger)
                                RangeValue.of(
                                    requirementNode.get("minValue").asLong(),
                                    requirementNode.get("maxValue").asLong()
                                )
                            else
                                datatypeMismatchException()

                        RequirementDataType.BOOLEAN,
                        RequirementDataType.STRING ->
                            throw ErrorException(
                                ErrorType.INVALID_REQUIREMENT_VALUE,
                                message = "Boolean or String datatype cannot have a range"
                            )
                    }
                }
                isOnlyMax(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER ->
                            if (requirementNode.get("maxValue").isBigDecimal || requirementNode.get("maxValue").isBigInteger)
                                MaxValue.of(BigDecimal(requirementNode.get("maxValue").asText()))
                            else
                                datatypeMismatchException()
                        RequirementDataType.INTEGER ->
                            if (requirementNode.get("maxValue").isBigInteger)
                                MaxValue.of(requirementNode.get("maxValue").longValue())
                            else
                                datatypeMismatchException()
                        RequirementDataType.BOOLEAN,
                        RequirementDataType.STRING ->
                            throw ErrorException(
                                ErrorType.INVALID_REQUIREMENT_VALUE,
                                message = "Boolean or String datatype cannot have a max value"
                            )
                    }
                }
                isOnlyMin(requirementNode) -> {
                    when (dataType) {
                        RequirementDataType.NUMBER ->
                            if (requirementNode.get("minValue").isBigDecimal || requirementNode.get("minValue").isBigInteger)
                                MinValue.of(BigDecimal(requirementNode.get("minValue").asText()))
                            else
                                datatypeMismatchException()

                        RequirementDataType.INTEGER ->
                            if (requirementNode.get("minValue").isBigInteger)
                                MinValue.of(requirementNode.get("minValue").longValue())
                            else
                                datatypeMismatchException()

                        RequirementDataType.BOOLEAN,
                        RequirementDataType.STRING ->
                            throw ErrorException(
                                ErrorType.INVALID_REQUIREMENT_VALUE,
                                message = "Boolean or String datatype cannot have a min value"
                            )
                    }
                }
                isNotBounded(requirementNode) -> {
                    NoneValue
                }
                else -> {
                    throw ErrorException(
                        ErrorType.INVALID_REQUIREMENT_VALUE,
                        message = "Unknown value in Requirement object"
                    )
                }
            }
        }

        private fun isExpectedValue(requirementNode: JsonNode) = requirementNode.has("expectedValue")
            && !requirementNode.has("minValue") && !requirementNode.has("maxValue")

        private fun isRange(requirementNode: JsonNode) = requirementNode.has("minValue")
            && requirementNode.has("maxValue") && !requirementNode.has("expectedValue")

        private fun isOnlyMax(requirementNode: JsonNode) = requirementNode.has("maxValue")
            && !requirementNode.has("minValue") && !requirementNode.has("expectedValue")

        private fun isOnlyMin(requirementNode: JsonNode) = requirementNode.has("minValue")
            && !requirementNode.has("maxValue") && !requirementNode.has("expectedValue")

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
