package com.procurement.notice.infrastructure.service

import com.datastax.driver.core.utils.UUIDs
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
        return generateRandomUUID()
    }

    override fun generateOcid(cpid: String, stage: String): String {
        return cpid + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    override fun generateReleaseId(ocid: String): String {
        return ocid + SEPARATOR + milliNowUTC()
    }

    override fun generateRelatedProcessId(): String = generateTimeBasedRandomUUID().toString()


    private fun generateRandomUUID() = UUID.randomUUID()

    private fun generateTimeBasedRandomUUID(): UUID = UUIDs.timeBased();

}