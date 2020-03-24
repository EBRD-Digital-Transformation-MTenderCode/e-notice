package com.procurement.notice.application.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result


enum class Stage(@JsonValue override val key: String) : EnumElementProvider.Key {

    AC("AC"),
    EI("EI"),
    EV("EV"),
    FS("FS"),
    NP("NP"),
    PN("PN");

    override fun toString(): String = key

    companion object : EnumElementProvider<Stage>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = orThrow(name)
    }
}

private val regex = "(?<=[A-Za-z0-9]{4}-[A-Za-z0-9]{6}-[A-Z]{2}-[0-9]{13}-)([A-Z]{2})(?=-[0-9]{13})".toRegex()

fun tryOfOcid(ocid: Ocid): Result<Stage, String> {
    val stage = regex.find(ocid.toString())?.value
    return if (stage != null)
        Result.success(Stage.creator(stage))
    else
        Result.failure(regex.pattern)
}
