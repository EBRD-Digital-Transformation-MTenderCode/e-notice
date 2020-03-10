package com.procurement.notice.domain.fail

import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.ValidationResult

sealed class Fail {

    abstract class Error(val prefix: String) : Fail() {
        abstract val code: String
        abstract val description: String
        val message: String
            get() = "ERROR CODE: '$code', DESCRIPTION: '$description'."

        companion object {
            fun <T, E : Error> E.toResult(): Result<T, E> = Result.failure(this)

            fun <E : Error> E.toValidationResult(): ValidationResult<E> = ValidationResult.error(this)
        }

        class BadRequest(override val description: String) : Fail.Error(prefix = "B-") {
            override val code: String
                get() = prefix + "001"
        }
    }

    sealed class Incident(val level: Level, number: String, val description: String) : Fail() {
        val code: String = "INC-$number"

        sealed class Database(number: String, description: String) :
            Incident(level = Level.ERROR, number = number, description = description) {

            class Access(description: String, val exception: Exception) :
                Database(number = "1.1", description = description)

            class NotFound(description: String) :
                Database(number = "1.2", description = description)
        }

        class InternalError : Incident(
            level = Level.ERROR,
            number = "02",
            description = "Internal error"
        )

//        class ParseFromDatabaseIncident(jsonData: String) : Incident(
//            level = Level.ERROR,
//            number = "3",
//            description = "Could not parse data '$jsonData' stored in database. "
//        )

//        class DatabaseIncident : Incident(
//            level = Level.ERROR,
//            number = "03",
//            description = "Could not process data from database."
//        )

        enum class Level(override val key: String) : EnumElementProvider.Key {
            ERROR("error"),
            WARNING("warning"),
            INFO("info");

            companion object : EnumElementProvider<Level>(info = info())
        }
    }
}
