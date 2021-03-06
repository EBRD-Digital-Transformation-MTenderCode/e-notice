package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.model.Cpid
import com.procurement.notice.application.model.Ocid
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.entity.Record
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.Params
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.model.tender.record.ReleaseTender
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReleaseService(private val releaseDao: ReleaseDao) {

    companion object {
        private const val SEPARATOR = "-"
        private const val MS = "MS"
        private const val TENDER_JSON = "tender"
    }

    fun getMs(data: JsonNode): Ms = toObject(Ms::class.java, data)

    fun getMs(data: String): Ms = toObject(Ms::class.java, data)

    fun getMsTender(data: JsonNode): MsTender = toObject(MsTender::class.java, data.get(TENDER_JSON))

    fun getRecordTender(data: JsonNode): ReleaseTender = toObject(ReleaseTender::class.java, data.get(TENDER_JSON))

    fun getRelease(data: JsonNode): Release = toObject(Release::class.java, data)

    fun getRelease(data: String): Release  = toObject(Release::class.java, data)

    fun getRecord(data: String): Record = toObject(Record::class.java, data)

    fun getMsEntity(cpid: String): ReleaseEntity {
        return releaseDao.getByCpIdAndOcId(cpid, cpid) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
    }

    fun getRecordEntity(cpId: String, ocId: String): ReleaseEntity {
        return releaseDao.getByCpIdAndOcId(cpId, ocId) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
    }

    fun tryGetRecordEntity(cpid: Cpid, ocid: Ocid): Result<ReleaseEntity?, Fail.Incident.Database> {
        return releaseDao.tryGetByCpIdAndOcId(cpid, ocid)
    }

    fun getPartiesWithActualPersones(
        requestProcuringEntity: OrganizationReference,
        parties: Collection<Organization>?
    ): MutableList<Organization> = parties?.map { party ->
        if (party.id == requestProcuringEntity.id) {
            party.copy(
                persones = requestProcuringEntity.persones
            )
        } else
            party
    }?.toMutableList()
        ?: mutableListOf()

    fun newRecordEntity(cpId: String, stage: String, release: Release, publishDate: Date): ReleaseEntity {
        val ocId = release.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = release.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = release.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
            cpId = cpId,
            ocId = ocId,
            releaseId = releaseId,
            releaseDate = releaseDate,
            publishDate = publishDate,
            stage = stage,
            json = toJson(release),
            status = release.tender.status.toString()
        )
    }

    fun newRecordEntity(cpid: Cpid, ocid: Ocid.SingleStage, record: Record, publishDate: Date): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = record.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
            cpId = cpid.toString(),
            ocId = ocId,
            releaseId = releaseId,
            releaseDate = releaseDate,
            publishDate = publishDate,
            stage = ocid.stage.toString(),
            json = toJson(record),
            status = record.tender.status.toString()
        )
    }

    fun newMSEntity(cpId: String, ms: Ms, publishDate: Date): ReleaseEntity {
        val releaseId = ms.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = ms.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpId,
                ocId = cpId,
                releaseId = releaseId,
                releaseDate = releaseDate,
                publishDate = publishDate,
                stage = "",
                json = toJson(ms),
                status = ms.tender.status.toString()
        )
    }

    fun newMSEntity(cpid: Cpid, record: Record, publishDate: Date): ReleaseEntity {
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = record.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpid.toString(),
                ocId = cpid.toString(),
                releaseId = releaseId,
                releaseDate = releaseDate,
                publishDate = publishDate,
                stage = "",
                json = toJson(record),
                status = record.tender.status.toString()
        )
    }

    fun newContractRecordEntity(cpId: String, stage: String, record: ContractRecord, publishDate: Date): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = record.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpId,
                ocId = ocId,
                releaseId = releaseId,
                releaseDate = releaseDate,
                publishDate = publishDate,
                stage = stage,
                json = toJson(record),
                status = ""
        )
    }

    fun newEntity(cpId: String,
                  ocId: String,
                  releaseId: String,
                  releaseDate: Date,
                  publishDate: Date,
                  stage: String,
                  json: String,
                  status: String): ReleaseEntity {
        return ReleaseEntity(
                cpId = cpId,
                ocId = ocId,
                publishDate = publishDate,
                releaseDate = releaseDate,
                releaseId = releaseId,
                stage = stage,
                jsonData = json,
                status = status
        )
    }

    fun saveMs(cpId: String, ms: Ms, publishDate: Date) {
        releaseDao.saveMs(newMSEntity(cpId = cpId, ms = ms, publishDate = publishDate))
    }

    fun saveRecord(cpId: String, stage: String, release: Release, publishDate: Date) {
        releaseDao.saveRecord(newRecordEntity(cpId = cpId, stage = stage, release = release, publishDate = publishDate))
    }

    fun saveRecord(cpid: Cpid, ocid: Ocid, record: Record, publishDate: Date) {
        when (ocid) {
            is Ocid.SingleStage ->
                releaseDao.saveRecord(newRecordEntity(cpid = cpid, ocid = ocid, record = record, publishDate = publishDate))
            is Ocid.MultiStage ->
                releaseDao.saveMs(newMSEntity(cpid = cpid, record = record, publishDate = publishDate))
        }
    }

    fun saveContractRecord(cpId: String, stage: String, record: ContractRecord, publishDate: Date) {
        releaseDao.saveRecord(newContractRecordEntity(cpId = cpId, stage = stage, record = record, publishDate = publishDate))
    }

    fun getParamsForCreateCnPnPin(operation: Operation, stage: Stage): Params {
        val params = Params()
        when (operation) {
            Operation.CREATE_CN -> {
                params.tag = listOf(Tag.TENDER)
                params.isACallForCompetition = true
            }
            Operation.CREATE_PN, Operation.CREATE_PIN -> {
                params.tag = listOf(Tag.PLANNING)
                params.isACallForCompetition = false
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        when (stage) {
            Stage.PS -> {
                params.statusDetails = TenderStatusDetails.PRESELECTION
                params.relatedProcessType = RelatedProcessType.X_PRESELECTION
            }
            Stage.PQ -> {
                params.statusDetails = TenderStatusDetails.PREQUALIFICATION
                params.relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.EV -> {
                params.statusDetails = TenderStatusDetails.EVALUATION
                params.relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            Stage.PN -> {
                params.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                params.relatedProcessType = RelatedProcessType.PLANNING
            }
            Stage.PIN -> {
                params.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                params.relatedProcessType = RelatedProcessType.X_PLANNED
            }

            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    fun getParamsForUpdateCnOnPnPin(stage: Stage, operation: Operation? = null): Params {
        val params = Params()
        when (stage) {
            Stage.PS -> {
                params.statusDetails = TenderStatusDetails.PRESELECTION
                params.relatedProcessType = RelatedProcessType.X_PRESELECTION
            }
            Stage.PQ -> {
                params.statusDetails = TenderStatusDetails.PREQUALIFICATION
                params.relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.EV -> {
                params.statusDetails = TenderStatusDetails.EVALUATION
                params.relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            Stage.NP -> {
                if (operation == Operation.CREATE_NEGOTIATION_CN_ON_PN) {
                    params.statusDetails = TenderStatusDetails.NEGOTIATION
                    params.relatedProcessType = RelatedProcessType.X_NEGOTIATION
                } else
                    throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
            }
            Stage.TP -> {
                params.relatedProcessType = RelatedProcessType.X_TENDERING
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

}
