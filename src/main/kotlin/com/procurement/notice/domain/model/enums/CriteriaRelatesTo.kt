package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class CriteriaRelatesTo(@JsonValue val value: String) {
    TENDERER("tenderer"),
    ITEM("item"),
    LOT("lot"),
    AWARD("award");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, CriteriaRelatesTo> = values().associateBy { it.value.toUpperCase() }

        @JvmStatic
        @JsonCreator
        fun fromString(value: String): CriteriaRelatesTo = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = CriteriaRelatesTo::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
