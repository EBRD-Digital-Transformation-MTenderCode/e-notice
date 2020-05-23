package com.procurement.notice.infrastructure.dto.entity.parties

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

class PersonId private constructor(private val value: String) {

    @JsonValue
    override fun toString(): String = value

    companion object {

        @JvmStatic
        @JsonCreator
        fun parse(text: String): PersonId? = if (text.isBlank())
            null
        else
            PersonId(text)

        fun generate(scheme: String, id: String): PersonId = PersonId("$scheme-$id")
    }
}
