package com.procurement.notice.model.ocds

import com.procurement.notice.exception.EnumException

enum class AmendmentRelatesTo(val value: String) {
    LOT("lot"),
    TENDER("tender"),
    CAN("can");

    override fun toString(): String = this.value

    companion object {
        private val elements: Map<String, AmendmentRelatesTo> = values()
            .associateBy { it.value.toUpperCase() }

        fun fromString(value: String): AmendmentRelatesTo =
            elements[value.toUpperCase()] ?: throw EnumException(
                enumType = AmendmentRelatesTo::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value })
    }
}
