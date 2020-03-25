package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.domain.utils.EnumElementProvider

enum class AmendmentRelatesTo(override val key: String): EnumElementProvider.Key, Action {
    LOT("lot"),
    TENDER("tender"),
    CAN("can");

    override fun toString(): String = key

    companion object : EnumElementProvider<AmendmentRelatesTo>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = AmendmentRelatesTo.orThrow(name)
    }

}
