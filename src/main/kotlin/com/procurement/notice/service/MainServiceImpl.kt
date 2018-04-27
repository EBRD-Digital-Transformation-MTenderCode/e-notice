package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.tender.dto.Operation
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface MainService {

    fun createRelease(cpId: String,
                      ocId: String?,
                      newStage: String,
                      previousStage: String?,
                      operation: String,
                      phase: String?,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto<*>
}

@Service
class MainServiceImpl(private val budgetService: BudgetService,
                      private val releaseService: ReleaseService,
                      private val tenderService: TenderService,
                      private val tenderServiceEv: TenderServiceEv,
                      private val enquiryService: EnquiryService) : MainService {

    override fun createRelease(cpId: String,
                               ocId: String?,
                               newStage: String,
                               previousStage: String?,
                               operation: String,
                               phase: String?,
                               releaseDate: LocalDateTime,
                               data: JsonNode): ResponseDto<*> {
        when (Operation.fromValue(operation)) {
            Operation.CREATE_EI -> return budgetService.createEi(cpId, newStage, releaseDate, data)
            Operation.UPDATE_EI -> return budgetService.updateEi(cpId, newStage, releaseDate, data)
            Operation.CREATE_FS -> return budgetService.createFs(cpId, newStage, releaseDate, data)
            Operation.UPDATE_FS -> {
                val ocId = ocId ?: throw ErrorException(ErrorType.OCID_ERROR)
                return budgetService.updateFs(cpId, ocId, newStage, releaseDate, data)
            }
            Operation.CREATE_CN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data, Operation.CREATE_CN)
            Operation.CREATE_PN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data, Operation.CREATE_PN)
            Operation.CREATE_PIN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data, Operation.CREATE_PIN)
            Operation.CREATE_PIN_ON_PN -> {
                val previousStage = previousStage ?: throw ErrorException(ErrorType.STAGE_ERROR)
                return releaseService.createPinOnPn(cpId, newStage, previousStage, releaseDate, data)
            }
            Operation.CREATE_CN_ON_PN -> {
                val previousStage = previousStage ?: throw ErrorException(ErrorType.STAGE_ERROR)
                return releaseService.createCnOnPn(cpId, newStage, previousStage, releaseDate, data)
            }
            Operation.CREATE_CN_ON_PIN -> {
                val previousStage = previousStage ?: throw ErrorException(ErrorType.STAGE_ERROR)
                return releaseService.createCnOnPin(cpId, newStage, previousStage, releaseDate, data)
            }
            Operation.UPDATE_CN -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
            Operation.CREATE_ENQUIRY -> return enquiryService.createEnquiry(cpId, newStage, releaseDate, data)
            Operation.ADD_ANSWER -> return enquiryService.addAnswer(cpId, newStage, releaseDate, data)
            Operation.SUSPEND_TENDER -> return tenderService.suspendTender(cpId, newStage, releaseDate, data)
            Operation.UNSUSPEND_TENDER -> return enquiryService.unsuspendTender(cpId, newStage, releaseDate, data)
            Operation.TENDER_PERIOD_END -> return tenderService.tenderPeriodEnd(cpId, newStage, releaseDate, data)
            Operation.TENDER_PERIOD_END_EV -> return tenderServiceEv.tenderPeriodEndEv(cpId, newStage, releaseDate, data)
            Operation.AWARD_BY_BID -> return tenderService.awardByBid(cpId, newStage, releaseDate, data)
            Operation.AWARD_BY_BID_EV -> return tenderServiceEv.awardByBidEv(cpId, newStage, releaseDate, data)
            Operation.STANDSTILL_PERIOD -> return tenderService.standstillPeriod(cpId, newStage, releaseDate, data)
            Operation.STANDSTILL_PERIOD_EV -> return tenderServiceEv.standstillPeriodEv(cpId, newStage, releaseDate, data)
            Operation.AWARD_PERIOD_END -> return tenderService.awardPeriodEnd(cpId, newStage, releaseDate, data)
            Operation.AWARD_PERIOD_END_EV -> return tenderServiceEv.awardPeriodEndEv(cpId, newStage, releaseDate, data)
            Operation.START_NEW_STAGE -> {
                val previousStage = previousStage ?: throw ErrorException(ErrorType.STAGE_ERROR)
                return tenderService.startNewStage(cpId, newStage, previousStage, releaseDate, data)
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
    }


}