package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.EiForFs
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.budget.FsDto
import com.procurement.notice.model.entity.BudgetEntity
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.utils.localNowUTC
import com.procurement.notice.utils.milliNowUTC
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Service
class BudgetService(private val budgetDao: BudgetDao,
                    private val organizationService: OrganizationService,
                    private val relatedProcessService: RelatedProcessService) {

    companion object {
        private const val SEPARATOR = "-"
        private const val FS_SEPARATOR = "-FS-"
    }

    fun createEi(cpid: String, stage: String, language: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val receivedEi = toObject(EI::class.java, data)
        val ei = receivedEi.copy(
            id = getReleaseId(cpid),
            date = releaseDate,
            tag = listOf(Tag.COMPILED),
            language = language,
            initiationType = InitiationType.TENDER
        )
        organizationService.processEiParties(ei)
        budgetDao.saveBudget(getEiEntity(ei, stage, releaseDate.toDate()))
        return ResponseDto(data = DataResponseDto(cpid = cpid))
    }

    fun updateEi(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val entity = budgetDao.getEiByCpId(cpid) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val receivedEi = toObject(EI::class.java, data)
        val storedEi = toObject(EI::class.java, entity.jsonData)
        storedEi.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            title = receivedEi.title
            planning = receivedEi.planning
            tender = receivedEi.tender
        }
        budgetDao.saveBudget(getEiEntity(storedEi, stage, entity.publishDate))
        val amendmentIds = null
        return ResponseDto(data = DataResponseDto(cpid = cpid, amendmentsIds = amendmentIds))
    }

    fun createFs(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(FsDto::class.java, data)
        val fs = dto.fs
        fs.apply {
            id = getReleaseId(fs.ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING)
            initiationType = InitiationType.TENDER
        }
        organizationService.processFsParties(fs)
        relatedProcessService.addEiRelatedProcessToFs(fs, cpid)
        val amount: BigDecimal = fs.planning?.budget?.amount?.amount ?: BigDecimal.ZERO
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, amount, releaseDate.toDate()))
        dto.ei?.let { createEiByFs(cpid, fs.ocid, dto.ei) }
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = fs.ocid))
    }

    fun updateFs(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val entity = budgetDao.getFsByCpIdAndOcId(cpid, ocid) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val dto = toObject(FsDto::class.java, data)
        val fs = toObject(FS::class.java, entity.jsonData)
        val updateFs = dto.fs
        val updateAmount: BigDecimal = updateFs.planning?.budget?.amount?.amount ?: BigDecimal.valueOf(0.00)
        val amount: BigDecimal = fs.planning?.budget?.amount?.amount ?: BigDecimal.valueOf(0.00)
        fs.apply {
            id = getReleaseId(ocid)
            date = releaseDate
            title = updateFs.title
            tender = updateFs.tender
            planning = updateFs.planning
        }
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, updateAmount, entity.publishDate))
        if (updateAmount != amount && dto.ei != null) updateEiAmountByFs(cpid, dto.ei)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = fs.ocid))
    }

    fun createEiByMs(eiIds: List<String>, msCpId: String, dateTime: LocalDateTime) {
        eiIds.forEach { eiCpId ->
            val entity = budgetDao.getEiByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val ei = toObject(EI::class.java, entity.jsonData)
            ei.id = getReleaseId(eiCpId)
            ei.date = dateTime
            relatedProcessService.addMsRelatedProcessToEi(ei, msCpId)
            budgetDao.saveBudget(getEiEntity(ei, entity.stage, entity.publishDate))
        }
    }

    fun createFsByMs(budgetBreakdowns: List<BudgetBreakdown>, msCpId: String, dateTime: LocalDateTime) {
        budgetBreakdowns.forEach {
            val eiCpId = getCpIdFromOcId(it.id)
            val entity = budgetDao.getFsByCpIdAndOcId(eiCpId, it.id) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val fs = toObject(FS::class.java, entity.jsonData)
            fs.id = getReleaseId(it.id)
            fs.date = dateTime
            fs.tag = listOf(Tag.PLANNING_UPDATE)
            relatedProcessService.addMsRelatedProcessToFs(fs, msCpId)
            budgetDao.saveBudget(getFsEntity(entity.cpId, fs, entity.stage, entity.amount, entity.publishDate))
        }
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    private fun createEiByFs(eiCpId: String, fsOcId: String, eiForFs: EiForFs) {
        val entity = budgetDao.getEiByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val ei = toObject(EI::class.java, entity.jsonData)
        ei.apply {
            id = getReleaseId(eiCpId)
            date = localNowUTC()
            planning?.budget?.amount = eiForFs.planning.budget.amount
        }
        relatedProcessService.addFsRelatedProcessToEi(ei, fsOcId)
        budgetDao.saveBudget(getEiEntity(ei, entity.stage, entity.publishDate))
    }

    private fun updateEiAmountByFs(eiCpId: String, eiForFs: EiForFs) {
        val entity = budgetDao.getEiByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val ei = toObject(EI::class.java, entity.jsonData)
        ei.apply {
            id = getReleaseId(eiCpId)
            date = localNowUTC()
            planning?.budget?.amount = eiForFs.planning.budget.amount
        }
        budgetDao.saveBudget(getEiEntity(ei, entity.stage, entity.publishDate))
    }

    private fun getEiEntity(ei: EI, stage: String, publishDate: Date): BudgetEntity {
        val releaseId = ei.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = ei.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return BudgetEntity(
                cpId = ei.ocid,
                ocId = ei.ocid,
                publishDate = publishDate,
                releaseDate = releaseDate,
                releaseId = releaseId,
                stage = stage,
                jsonData = toJson(ei)
        )
    }

    private fun getFsEntity(cpId: String, fs: FS, stage: String, amount: BigDecimal?, publishDate: Date): BudgetEntity {
        val releaseId = fs.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = fs.date?.toDate() ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return BudgetEntity(
                cpId = cpId,
                ocId = fs.ocid,
                publishDate = publishDate,
                releaseDate = releaseDate,
                releaseId = releaseId,
                stage = stage,
                amount = amount,
                jsonData = toJson(fs)
        )
    }
}
