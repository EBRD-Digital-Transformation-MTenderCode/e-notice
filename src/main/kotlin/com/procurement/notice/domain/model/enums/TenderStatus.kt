package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.model.ocds.AmendmentRelatesTo

enum class TenderStatus(@JsonValue override val key: String): EnumElementProvider.Key {
    ACTIVE("active"),
    CANCELLED("cancelled"),
    COMPLETE("complete"),
    PLANNED("planned"),
    PLANNING("planning"),
    UNSUCCESSFUL("unsuccessful"),
    WITHDRAWN("withdrawn");

    override fun toString(): String = this.key

    companion object : EnumElementProvider<AmendmentRelatesTo>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = TenderStatus.orThrow(name)
    }
}
