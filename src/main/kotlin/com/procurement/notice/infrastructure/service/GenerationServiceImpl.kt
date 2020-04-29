package com.procurement.notice.infrastructure.service

import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.utils.milliNowUTC
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenerationServiceImpl : GenerationService {

    companion object {
        private const val SEPARATOR = "-"
    }

    override fun generateAmendmentId(): UUID {
        return UUID.randomUUID()
    }

    override fun generateOcid(cpid: String, stage: String): String {
        return cpid + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    override fun generateReleaseId(cpid: String): String {
        return cpid + SEPARATOR + milliNowUTC()
    }
}