package com.procurement.notice.application.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider

enum class Stage(@JsonValue override val key: String) : EnumElementProvider.Key {

    AC("AC"),
    AP("AP"),
    EI("EI"),
    EV("EV"),
    FE("FE"),
    FS("FS"),
    NP("NP"),
    PC("PC"),
    PN("PN"),
    TP("TP");

    override fun toString(): String = key

    companion object : EnumElementProvider<Stage>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = orThrow(name)
    }
}

