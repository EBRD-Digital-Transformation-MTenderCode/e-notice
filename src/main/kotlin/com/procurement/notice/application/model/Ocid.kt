package com.procurement.notice.application.model

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.utils.EnumElementProvider.Companion.keysAsStrings

sealed class Ocid(private val value: String) {

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

    class MultiStage private constructor(value: String) : Ocid(value = value) {

        companion object {
            private val regex = "^[a-z]{4}-[a-z0-9]{6}-[A-Z]{2}-[0-9]{13}\$".toRegex()

            val pattern: String
                get() = regex.pattern

            fun tryCreateOrNull(value: String): Ocid? = if (value.matches(regex)) MultiStage(value = value) else null

            fun generate(cpid: Cpid): Ocid = MultiStage(cpid.toString())
        }
    }

    class SingleStage private constructor(value: String, val stage: Stage) : Ocid(value = value) {

        companion object {
            private const val STAGE_POSITION = 4
            private val STAGES: String
                get() = Stage.allowedElements.keysAsStrings()
                    .joinToString(separator = "|", prefix = "(", postfix = ")") { it.toUpperCase() }

            private val regex = "^[a-z]{4}-[a-z0-9]{6}-[A-Z]{2}-[0-9]{13}-$STAGES-[0-9]{13}\$".toRegex()

            val pattern: String
                get() = regex.pattern

            fun tryCreateOrNull(value: String): Ocid? =
                if (value.matches(regex)) {
                    val stage = Stage.orNull(value.split("-")[STAGE_POSITION])!!
                    SingleStage(value = value, stage = stage)
                } else
                    null
        }
    }
}
