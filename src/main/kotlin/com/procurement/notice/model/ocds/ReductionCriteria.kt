package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider

enum class ReductionCriteria(@JsonValue override val key: String) : EnumElementProvider.Key {

    REDUCTION_CRITERIA("reductionCriteria"),
    NONE("none");

    override fun toString(): String = key

    companion object : EnumElementProvider<ReductionCriteria>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = orThrow(name)
    }
}
