package com.procurement.notice.domain.utils

sealed class MaybeFail<out T> {
    companion object {
        fun <T> ok(): MaybeFail<T> = Ok
        fun <T> error(value: T): MaybeFail<T> = Error(value)
    }

    abstract val get: T
    abstract val isOk: Boolean
    abstract val isError: Boolean

    fun onError(block: (T) -> Unit): Unit =
        when (this) {
            is Error -> block(get)
            is Ok -> Unit
        }

    fun <R> map(block: (T) -> R): MaybeFail<R> = flatMap { Error(block(it)) }

    fun <R> flatMap(block: (T) -> MaybeFail<R>): MaybeFail<R> = when (this) {
        is Ok -> this
        is Error -> block(this.value)
    }

    object Ok : MaybeFail<Nothing>() {
        override val get: Nothing get() = throw NoSuchElementException("ProceduralResult does not contain value.")
        override val isOk: Boolean = true
        override val isError: Boolean = !isOk
    }

    data class Error<out T>(val value: T) : MaybeFail<T>() {
        override val get: T = value
        override val isOk: Boolean = false
        override val isError: Boolean = !isOk
    }
}