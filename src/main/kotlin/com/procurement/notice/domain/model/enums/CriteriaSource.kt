package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class CriteriaSource(@JsonValue val value: String) {
    TENDERER("tenderer"),
    BUYER("buyer"),
    PROCURING_ENTITY("procuringEntity");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, CriteriaSource> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): CriteriaSource = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = CriteriaSource::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
