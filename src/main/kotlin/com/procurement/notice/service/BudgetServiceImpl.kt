package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.entity.BudgetEntity
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
interface BudgetService {

    fun createEi(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun updateEi(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun createFs(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun updateFs(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun createEiByMs(eiIds: HashSet<String>, msCpId: String, dateTime: LocalDateTime)

    fun createFsByMs(budgetBreakdown: List<BudgetBreakdown>, msCpId: String, dateTime: LocalDateTime)
}

@Service
class BudgetServiceImpl(private val budgetDao: BudgetDao,
                        private val organizationService: OrganizationService,
                        private val relatedProcessService: RelatedProcessService) : BudgetService {

    companion object {
        private val SEPARATOR = "-"
        private val FS_SEPARATOR = "-FS-"
    }

    override fun createEi(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val ei = toObject(EI::class.java, data.toString())
        ei.id = getReleaseId(cpid)
        ei.date = releaseDate
        ei.tag = listOf(Tag.COMPILED)
        ei.initiationType = InitiationType.TENDER
        organizationService.processEiParties(ei)
        budgetDao.saveBudget(getEiEntity(ei, stage))
        return getResponseDto(ei.ocid, ei.ocid)
    }

    override fun updateEi(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = budgetDao.getByCpId(cpid) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val updateEi = toObject(EI::class.java, data.toString())
        val ei = toObject(EI::class.java, entity.jsonData)
        ei.id = getReleaseId(cpid)
        ei.date = releaseDate
        updateEiDto(ei, updateEi)
        budgetDao.saveBudget(getEiEntity(ei, stage))
        return getResponseDto(ei.ocid, ei.ocid)
    }

    override fun createFs(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val fs = toObject(FS::class.java, data.toString())
        fs.id = getReleaseId(fs.ocid)
        fs.date = releaseDate
        fs.tag = listOf(Tag.PLANNING)
        fs.initiationType = InitiationType.TENDER
        organizationService.processFsParties(fs)
        relatedProcessService.addEiRelatedProcessToFs(fs, cpid)
        val amount = fs.planning?.budget?.amount?.amount ?: 0.00
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, amount))
        createEiByFs(cpid, fs.ocid)
        return getResponseDto(cpid, fs.ocid)
    }

    override fun updateFs(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = budgetDao.getByCpIdAndOcId(cpid, ocid) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val updateFs = toObject(FS::class.java, data.toString())
        val fs = toObject(FS::class.java, entity.jsonData)
        val updateAmount = updateFs.planning?.budget?.amount?.amount ?: 0.00
        val amount = fs.planning?.budget?.amount?.amount ?: 0.00
        fs.id = getReleaseId(ocid)
        fs.date = releaseDate
        updateFsDto(fs, updateFs)
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, updateAmount))
        if (updateAmount != amount) updateEiAmountByFs(cpid)
        return getResponseDto(cpid, fs.ocid)
    }

    override fun createEiByMs(eiIds: HashSet<String>, msCpId: String, dateTime: LocalDateTime) {
        eiIds.forEach { eiCpId ->
            val entity = budgetDao.getByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val ei = toObject(EI::class.java, entity.jsonData)
            ei.id = getReleaseId(eiCpId)
            ei.date = dateTime
            relatedProcessService.addMsRelatedProcessToEi(ei, msCpId)
            budgetDao.saveBudget(getEiEntity(ei, entity.stage))
        }
    }

    override fun createFsByMs(budgetBreakdown: List<BudgetBreakdown>, msCpId: String, dateTime: LocalDateTime) {
        budgetBreakdown.forEach {
            val eiCpId = getCpIdFromOcId(it.id!!)
            val entity = budgetDao.getByCpIdAndOcId(eiCpId, it.id) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val fs = toObject(FS::class.java, entity.jsonData)
            fs.id = getReleaseId(it.id)
            fs.date = dateTime
            fs.tag = listOf(Tag.PLANNING_UPDATE)
            relatedProcessService.addMsRelatedProcessToFs(fs, msCpId)
            budgetDao.saveBudget(getFsEntity(entity.cpId, fs, entity.stage, entity.amount))
        }
    }

    private fun getCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    private fun updateEiDto(ei: EI, updateEi: EI) {
        ei.title = updateEi.title
        ei.planning = updateEi.planning
        ei.tender = updateEi.tender
    }

    private fun updateFsDto(fs: FS, updateFs: FS) {
        fs.title = updateFs.title
        fs.tender = updateFs.tender
        fs.parties = updateFs.parties
        fs.planning = updateFs.planning
    }

    private fun createEiByFs(eiCpId: String, fsOcId: String) {
        val entity = budgetDao.getByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val ei = toObject(EI::class.java, entity.jsonData)
        val totalAmount = budgetDao.getTotalAmountByCpId(eiCpId)
        if (totalAmount != null) ei.planning?.budget?.amount?.amount = totalAmount.toFixed(2)
        ei.id = getReleaseId(eiCpId)
        ei.date = localNowUTC()
        relatedProcessService.addFsRelatedProcessToEi(ei, fsOcId)
        budgetDao.saveBudget(getEiEntity(ei, entity.stage))
    }

    private fun updateEiAmountByFs(eiCpId: String) {
        val entity = budgetDao.getByCpId(eiCpId) ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        val ei = toObject(EI::class.java, entity.jsonData)
        val totalAmount = budgetDao.getTotalAmountByCpId(eiCpId)
        if (totalAmount != null) ei.planning?.budget?.amount?.amount = totalAmount.toFixed(2)
        ei.id = getReleaseId(eiCpId)
        ei.date = localNowUTC()
        budgetDao.saveBudget(getEiEntity(ei, entity.stage))
    }

    private fun getEiEntity(ei: EI, stage: String): BudgetEntity {
        return BudgetEntity(
                cpId = ei.ocid,
                ocId = ei.ocid,
                releaseDate = ei.date!!.toDate(),
                releaseId = ei.id!!,
                stage = stage,
                amount = ei.planning?.budget?.amount?.amount,
                jsonData = toJson(ei)
        )
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getFsEntity(cpId: String, fs: FS, stage: String, amount: Double?): BudgetEntity {
        return BudgetEntity(
                cpId = cpId,
                ocId = fs.ocid,
                releaseDate = fs.date!!.toDate(),
                releaseId = fs.id!!,
                stage = stage,
                amount = amount,
                jsonData = toJson(fs)
        )
    }

    private fun getResponseDto(cpid: String?, ocid: String?): ResponseDto<JsonNode> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }
}
