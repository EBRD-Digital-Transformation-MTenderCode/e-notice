package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.ContractRecord
import com.procurement.notice.model.tender.record.Params
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service

interface ReleaseService {

    fun getMs(data: JsonNode): Ms

    fun getMs(data: String): Ms

    fun getMsTender(data: JsonNode): MsTender

    fun getRecordTender(data: JsonNode): RecordTender

    fun getRecord(data: JsonNode): Record

    fun getRecord(data: String): Record

    fun getRecordEntity(cpId: String, ocId: String): ReleaseEntity

    fun getMsEntity(cpid: String): ReleaseEntity

    fun newReleaseId(ocId: String): String

    fun newOcId(cpId: String, stage: String): String

    fun newRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity

    fun newContractRecordEntity(cpId: String, stage: String, record: ContractRecord): ReleaseEntity

    fun newMSEntity(cpId: String, ms: Ms): ReleaseEntity

    fun newEntity(cpId: String,
                  ocId: String,
                  releaseId: String,
                  stage: String,
                  json: String,
                  status: String): ReleaseEntity

    fun saveMs(cpId: String, ms: Ms)

    fun saveRecord(cpId: String, stage: String, record: Record)

    fun saveContractRecord(cpId: String, stage: String, record: ContractRecord)

    fun responseDto(cpid: String, ocid: String, amendments: Set<String>? = null): ResponseDto

    fun getParamsForCreateCnPnPin(operation: Operation, stage: Stage): Params

    fun getParamsForUpdateCnOnPnPin(stage: Stage): Params

}


@Service
class ReleaseServiceImpl(private val relatedProcessService: RelatedProcessService,
                         private val releaseDao: ReleaseDao) : ReleaseService {

    companion object {
        private const val SEPARATOR = "-"
        private const val MS = "MS"
        private const val TENDER_JSON = "tender"
    }

    override fun getMs(data: JsonNode): Ms = toObject(Ms::class.java, data)

    override fun getMs(data: String): Ms = toObject(Ms::class.java, data)

    override fun getMsTender(data: JsonNode): MsTender = toObject(MsTender::class.java, data.get(TENDER_JSON))

    override fun getRecordTender(data: JsonNode): RecordTender = toObject(RecordTender::class.java, data.get(TENDER_JSON))

    override fun getRecord(data: JsonNode): Record = toObject(Record::class.java, data)

    override fun getRecord(data: String): Record = toObject(Record::class.java, data)


    override fun getMsEntity(cpid: String): ReleaseEntity {
        return releaseDao.getByCpIdAndOcId(cpid, cpid) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
    }

    override fun getRecordEntity(cpId: String, ocId: String): ReleaseEntity {
        return releaseDao.getByCpIdAndOcId(cpId, ocId) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
    }

    override fun newRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpId,
                ocId = ocId,
                releaseId = releaseId,
                stage = stage,
                json = toJson(record),
                status = record.tender.status.toString()
        )
    }

    override fun newMSEntity(cpId: String, ms: Ms): ReleaseEntity {
        val releaseId = ms.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpId,
                ocId = cpId,
                releaseId = releaseId,
                stage = MS,
                json = toJson(ms),
                status = ms.tender.status.toString()
        )
    }

    override fun newContractRecordEntity(cpId: String, stage: String, record: ContractRecord): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return newEntity(
                cpId = cpId,
                ocId = ocId,
                releaseId = releaseId,
                stage = stage,
                json = toJson(record),
                status = ""
        )
    }

    override fun newEntity(cpId: String,
                           ocId: String,
                           releaseId: String,
                           stage: String,
                           json: String,
                           status: String): ReleaseEntity {
        return ReleaseEntity(
                cpId = cpId,
                ocId = ocId,
                releaseDate = localNowUTC().toDate(),
                releaseId = releaseId,
                stage = stage,
                jsonData = json,
                status = status
        )
    }

    override fun newOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    override fun newReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }


    override fun saveMs(cpId: String, ms: Ms) {
        releaseDao.saveRelease(newMSEntity(cpId = cpId, ms = ms))

    }

    override fun saveRecord(cpId: String, stage: String, record: Record) {
        releaseDao.saveRelease(newRecordEntity(cpId = cpId, stage = stage, record = record))
    }

    override fun saveContractRecord(cpId: String, stage: String, record: ContractRecord) {
        releaseDao.saveRelease(newContractRecordEntity(cpId = cpId, stage = stage, record = record))
    }

    override fun getParamsForCreateCnPnPin(operation: Operation, stage: Stage): Params {
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
            Stage.PN -> {
                params.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                params.relatedProcessType = RelatedProcessType.PLANNING
            }
            Stage.PIN -> {
                params.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                params.relatedProcessType = RelatedProcessType.PRIOR
            }
            Stage.EV -> {
                params.statusDetails = TenderStatusDetails.EVALUATION
                params.relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    override fun getParamsForUpdateCnOnPnPin(stage: Stage): Params {
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
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    override fun responseDto(cpid: String, ocid: String, amendments: Set<String>?): ResponseDto {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        jsonForResponse.put("url", relatedProcessService.getTenderUri(cpid, ocid))
        if (amendments!=null) jsonForResponse.put("amendments", toJson(amendments))
        return ResponseDto(data = jsonForResponse)
    }

}
