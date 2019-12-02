package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class AwardStatus(@JsonValue val value: String) {
    ACTIVE("active"),
    PENDING("pending"),
    UNSUCCESSFUL("unsuccessful");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, AwardStatus> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): AwardStatus = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = AwardStatus::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
