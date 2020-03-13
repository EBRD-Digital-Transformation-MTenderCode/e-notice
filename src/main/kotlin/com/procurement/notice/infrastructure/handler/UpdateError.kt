package com.procurement.notice.infrastructure.handler

sealed class UpdateError(val code: String, val description: String)
