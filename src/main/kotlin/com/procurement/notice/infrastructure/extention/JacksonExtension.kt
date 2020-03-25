package com.procurement.notice.infrastructure.extention

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.procurement.notice.domain.fail.error.DataValidationErrors
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.failure
import com.procurement.notice.domain.utils.Result.Companion.success
import com.procurement.notice.domain.utils.asFailure
import com.procurement.notice.domain.utils.asSuccess
import com.procurement.notice.domain.utils.bind
import java.math.BigDecimal

fun JsonNode.getOrNull(name: String): JsonNode? = if (this.has(name)) this.get(name) else null

fun JsonNode.tryGetAttribute(name: String): Result<JsonNode, DataValidationErrors> = this.getOrNull(name)
    ?.asSuccess()
    ?: DataValidationErrors.MissingRequiredAttribute(name = name).asFailure()

fun JsonNode.tryGetAttribute(name: String, type: JsonNodeType): Result<JsonNode, DataValidationErrors> =
    tryGetAttribute(name = name)
        .bind { node ->
            if (node.nodeType == type)
                success(node)
            else
                failure(
                    DataValidationErrors.DataTypeMismatch(
                        name = name,
                        expectedType = type.name,
                        actualType = node.nodeType.name
                    )
                )
        }

fun JsonNode.tryGetTextAttribute(name: String): Result<String, DataValidationErrors> =
    tryGetAttribute(name = name, type = JsonNodeType.STRING)
        .map {
            it.asText()
        }

fun JsonNode.tryGetBigDecimalAttribute(name: String): Result<BigDecimal, DataValidationErrors> =
    tryGetAttribute(name = name, type = JsonNodeType.NUMBER)
        .map {
            it.decimalValue()
        }

fun <T> JsonNode.tryGetAttributeAsEnum(name: String, enumProvider: EnumElementProvider<T>):
    Result<T, DataValidationErrors> where T : Enum<T>,
                                                     T : EnumElementProvider.Key = this.tryGetTextAttribute(name)
    .bind { text ->
        enumProvider.orNull(text)
            ?.asSuccess<T, DataValidationErrors>()
            ?: failure(
                DataValidationErrors.UnknownValue(
                    name = name,
                    expectedValues = enumProvider.allowedValues,
                    actualValue = text
                )
            )
    }

fun <E> JsonNode.getOrErrorResult(name: String, error: (String) -> E): Result<JsonNode, E> = this.getOrNull(name)
    ?.let { success(it) }
    ?: failure(error(name))
