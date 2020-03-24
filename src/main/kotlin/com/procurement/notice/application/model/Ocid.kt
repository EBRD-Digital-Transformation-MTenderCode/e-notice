package com.procurement.notice.application.model

import com.fasterxml.jackson.annotation.JsonValue

class Ocid private constructor(private val value: String, val stage: Stage) {

    override fun equals(other: Any?): Boolean {
        return if (this !== other)
            other is Ocid
                && this.value == other.value
        else
            true
    }

    override fun hashCode(): Int = value.hashCode()

    @JsonValue
    override fun toString(): String = value

    companion object {
        private val STAGES: String
            get() = Stage.values()
                .joinToString(separator = "|", prefix = "(", postfix = ")") { it.key.toUpperCase() }

        private val regex = "^[a-z]{4}-[a-z0-9]{6}-[A-Z]{2}-[0-9]{13}-$STAGES-[0-9]{13}\$".toRegex()

        val pattern: String
            get() = regex.pattern

        fun tryCreateOrNull(value: String): Ocid? =
            if (value.matches(regex)) {
                val stage = Stage.orNull(value.split("-")[4])
                Ocid(value = value, stage = stage!!)
            } else
                null
    }
}
