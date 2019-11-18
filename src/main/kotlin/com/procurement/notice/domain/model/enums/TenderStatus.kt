package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class TenderStatus(@JsonValue val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete");

    override fun toString(): String = this.value

    companion object {
        private val elements: Map<String, TenderStatus> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): TenderStatus = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = TenderStatus::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
