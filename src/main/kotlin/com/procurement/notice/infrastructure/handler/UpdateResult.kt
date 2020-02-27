package com.procurement.notice.infrastructure.handler

sealed class UpdateResult<out T> {
    companion object {
        fun <T> ok(): UpdateResult<T> = Ok
        fun <T> error(value: T): UpdateResult<T> = Error(value)
    }

    abstract val get: T
    abstract val isOk: Boolean
    abstract val isError: Boolean

    fun onError(block: (T) -> Unit): Unit =
        when (this) {
            is Error -> block(get)
            is Ok    -> Unit
        }

    fun <R> map(block: (T) -> R): UpdateResult<R> = flatMap { Error(block(it)) }

    fun <R> flatMap(block: (T) -> UpdateResult<R>): UpdateResult<R> = when (this) {
        is Ok    -> this
        is Error -> block(this.value)
    }

    object Ok : UpdateResult<Nothing>() {
        override val get: Nothing get() = throw NoSuchElementException("ValidationResult does not contain value.")
        override val isOk: Boolean = true
        override val isError: Boolean = !isOk
    }

    data class Error<out T>(val value: T) : UpdateResult<T>() {
        override val get: T = value
        override val isOk: Boolean = false
        override val isError: Boolean = !isOk
    }
}