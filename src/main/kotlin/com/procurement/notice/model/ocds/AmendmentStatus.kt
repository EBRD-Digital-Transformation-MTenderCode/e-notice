package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class AmendmentStatus(@JsonValue val value: String) {

    PENDING("pending"),
    ACTIVE("active"),
    WITHDRAWN("withdrawn"),
    CANCELLED("cancelled");

    override fun toString(): String = this.value

    companion object {
        private val elements: Map<String, AmendmentStatus> = values()
            .associateBy { it.value.toUpperCase() }

        fun fromString(value: String): AmendmentStatus =
            elements[value.toUpperCase()] ?: throw EnumException(
                enumType = AmendmentStatus::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value })
    }
}
