package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class AmendmentType(@JsonValue val value: String){

    CANCELLATION("cancellation"),
    TENDER_CHANGE("tenderChange");

    override fun toString(): String = this.value

    companion object {
        private val elements: Map<String, AmendmentType> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): AmendmentType =
            elements[value.toUpperCase()] ?: throw EnumException(
                enumType = AmendmentType::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value })
    }
}
