package com.procurement.notice.domain.fail

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.ValidationResult

sealed class Fail(prefix: String, number: String) {
    val code: String = "$prefix$number/${GlobalProperties.service.id}"
    abstract val description: String
    val message: String
        get() = "ERROR CODE: '$code', DESCRIPTION: '$description'."

    abstract fun logging(logger: Logger)

    abstract class Error(prefix: String, number: String) : Fail(prefix = prefix, number = number) {
        class BadRequest(
            override val description: String,
            val json: String? = null,
            val exception: Exception? = null) : Error(
            prefix = "RQ-",
            number = "1"
        ) {
            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }
        }
    }

    sealed class Incident(val level: Level, number: String, override val description: String) :
        Fail(prefix = "INC-", number = number) {

        override fun logging(logger: Logger) {
            when (level) {
                Level.ERROR -> logger.error(message)
                Level.WARNING -> logger.warn(message)
                Level.INFO -> logger.info(message)
            }
        }

        sealed class Transform(number: String, description: String, val exception: Exception) :
            Incident(level = Level.ERROR, number = number, description = description) {

            override fun logging(logger: Logger) {
                logger.error(message = message, exception = exception)
            }

            class Parsing(description: String, exception: Exception) :
                Transform(number = "1.1", description = description, exception = exception)

            class Mapping(description: String, exception: Exception) :
                Transform(number = "1.2", description = description, exception = exception)

            class Deserialization(description: String, exception: Exception) :
                Transform(number = "1.3", description = description, exception = exception)

            class Serialization(description: String, exception: Exception) :
                Transform(number = "1.4", description = description, exception = exception)
        }

        class NetworkError(description: String) :
            Incident(level = Level.ERROR, number = "2", description = description)

        class BadResponse(description: String, val exception: Exception? = null, val body: String) :
            Incident(level = Level.ERROR, number = "3", description = description)

        class ResponseError(description: String) :
            Incident(level = Level.ERROR, number = "4", description = description)

        sealed class Database(number: String, description: String) :
            Incident(level = Level.ERROR, number = number, description = description) {

            class Access(description: String, val exception: Exception) :
                Database(number = "5.1", description = description) {

                override fun logging(logger: Logger) {
                    logger.error(message = message, exception = exception)
                }
            }

            class NotFound(description: String) :
                Database(number = "5.2", description = description)

            class InvalidData(val data: String, val exception: Exception? = null) : Database(
                number = "5.3",
                description = "Could not parse data stored in database. "
            ) {
                override fun logging(logger: Logger) {
                    logger.error(message, mdc = mapOf("data" to data), exception = exception)
                }
            }
        }

        class InternalError : Incident(
            level = Level.ERROR,
            number = "6",
            description = "Internal error"
        )

        enum class Level(@JsonValue override val key: String) : EnumElementProvider.Key {
            ERROR("error"),
            WARNING("warning"),
            INFO("info");

            companion object : EnumElementProvider<Level>(info = info())
        }
    }

}

fun <T, E : Fail.Error> E.toResult(): Result<T, E> = Result.failure(this)

fun <E : Fail.Error> E.toValidationResult(): ValidationResult<E> = ValidationResult.error(this)