package com.procurement.notice.application.service

import java.util.*

interface GenerationService {
    fun generateAmendmentId(): UUID

    fun generateOcid(cpid: String, stage: String): String

    fun generateReleaseId(ocid: String): String

    fun generateRelatedProcessId(): String
}

