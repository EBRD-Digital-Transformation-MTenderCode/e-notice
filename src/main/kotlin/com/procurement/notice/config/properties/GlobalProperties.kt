package com.procurement.notice.config.properties

import com.procurement.notice.infrastructure.dto.ApiVersion2

object GlobalProperties {
    const val serviceId = "2"
    const val serviceName = "e-notice"

    object App {
        val apiVersion = ApiVersion2(major = 1, minor = 0, patch = 0)
    }
}
