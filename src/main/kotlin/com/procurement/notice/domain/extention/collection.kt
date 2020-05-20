package com.procurement.notice.domain.extention

fun <T> T?.toList(): List<T> = if (this != null) listOf(this) else emptyList()