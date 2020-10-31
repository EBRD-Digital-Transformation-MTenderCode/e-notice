package com.procurement.notice.infrastructure.handler.record.update

sealed class UpdateError(val code: String, val description: String)
