package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class Scale(@JsonValue val value: String) {
    MICRO("MICRO"),
    SME("SME"),
    LARGE("LARGE"),
    EMPTY("  ");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, Scale> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): Scale = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = Scale::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
