package com.procurement.notice.infrastructure.io

fun Class<*>.getResourcePath(fileName: String): String = this.classLoader.getResource(fileName)?.path
    ?: throw IllegalStateException("File '$fileName' is not found.")
