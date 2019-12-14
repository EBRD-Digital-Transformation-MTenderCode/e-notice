package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class BreakdownStatus(@JsonValue val value: String) {
    MET("met");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, BreakdownStatus> = values().associateBy { it.value.toUpperCase() }

        @JsonCreator
        @JvmStatic
        fun fromString(value: String): BreakdownStatus = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = BreakdownStatus::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
