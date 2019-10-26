package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class RequirementDataType(@JsonValue val value: String) {

    BOOLEAN("boolean"),
    STRING("string"),
    NUMBER("number"),
    INTEGER("integer");

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val elements: Map<String, RequirementDataType> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): RequirementDataType = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = RequirementDataType::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}