package com.procurement.notice.service

import com.procurement.notice.application.model.parseCpid
import com.procurement.notice.application.model.parseOcid
import com.procurement.notice.application.model.record.create.CreateRecordParams
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.record.CreateRecordService
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.MaybeFail
import com.procurement.notice.infrastructure.service.record.createRelease
import com.procurement.notice.infrastructure.service.record.defineRecordTag
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateRecordServiceImpl(
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) : CreateRecordService {
    companion object {
        private val log = LoggerFactory.getLogger(CreateRecordServiceImpl::class.java)
    }

    override fun createRecord(params: CreateRecordParams): MaybeFail<Fail> {

        val data = params.data

        // FR.COM-3.4.5
        val ocid = parseOcid(data.ocid)
            .doReturn { error -> return MaybeFail.error(error) }

        val cpid = parseCpid(data.cpid)
            .doReturn { error -> return MaybeFail.error(error) }

        // FR.COM-3.4.4
        val releaseId = generationService.generateReleaseId(ocid = ocid.toString())

        // FR.COM-3.4.7
        val tag = defineRecordTag(data.tender?.statusDetails)

        // FR.COM-3.4.8
        val initiationType = InitiationType.TENDER

        // FR.COM-3.4.3
        // FR.COM-3.4.1
        val newRelease = createRelease(releaseId, tag, initiationType, params)
            .also {
                log.debug("CREATED RECORD (id: '${releaseId}'): '${toJson(it)}'.")
            }

        // FR.COM-3.4.9
        releaseService.saveRecord(
            cpid = cpid,
            ocid = ocid, // FR.COM-3.4.2
            record = newRelease,
            publishDate = params.date.toDate() // FR.COM-3.4.6
        )

        return MaybeFail.ok()
    }
}
