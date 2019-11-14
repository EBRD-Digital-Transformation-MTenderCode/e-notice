package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class BidStatus(@JsonValue val value: String) {
    INVITED("invited"),
    PENDING("pending"),
    VALID("valid"),
    DISQUALIFIED("disqualified"),
    WITHDRAWN("withdrawn"),
    EMPTY("empty");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, BidStatus> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): BidStatus = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = BidStatus::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
