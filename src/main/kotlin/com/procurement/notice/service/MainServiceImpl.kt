package com.procurement.notice.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
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
            Operation.UPDATE_FS -> return budgetService.updateFs(cpId, ocId!!, newStage, releaseDate, data)
            Operation.CREATE_CN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data)
            Operation.CREATE_PN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data)
            Operation.CREATE_PIN -> return releaseService.createCnPnPin(cpId, newStage, releaseDate, data)
            Operation.UPDATE_CN -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
            Operation.CREATE_ENQUIRY -> return enquiryService.createEnquiry(cpId, newStage, releaseDate, data)
            Operation.ADD_ANSWER -> return enquiryService.addAnswer(cpId, newStage, releaseDate, data)
            Operation.UNSUSPEND_TENDER -> return enquiryService.unsuspendTender(cpId, newStage, releaseDate, data)
            Operation.TENDER_PERIOD_END -> return releaseService.tenderPeriodEnd(cpId, newStage, releaseDate, data)
            Operation.SUSPEND_TENDER -> return releaseService.suspendTender(cpId, newStage, releaseDate, data)
            Operation.AWARD_BY_BID -> return releaseService.awardByBid(cpId, newStage, releaseDate, data)
            Operation.AWARD_PERIOD_END -> return releaseService.awardPeriodEnd(cpId, newStage, releaseDate, data)
            Operation.STANDSTILL_PERIOD_END -> return releaseService.standstillPeriodEnd(cpId, newStage, releaseDate, data)
            Operation.START_NEW_STAGE -> return releaseService.startNewStage(cpId, newStage, previousStage!!, releaseDate, data)
            Operation.CREATE_PIN_ON_PN -> return releaseService.createPinOnPn(cpId, newStage, previousStage!!, releaseDate, data)
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
    }

    enum class Operation(val value: String) {
        CREATE_EI("createEI"),
        UPDATE_EI("updateEI"),
        CREATE_FS("createFS"),
        UPDATE_FS("updateFS"),
        CREATE_CN("createCN"),
        CREATE_PN("createPN"),
        CREATE_PIN("createPIN"),
        UPDATE_CN("updateCN"),
        CREATE_ENQUIRY("createEnquiry"),
        ADD_ANSWER("addAnswer"),
        UNSUSPEND_TENDER("unsuspendTender"),
        TENDER_PERIOD_END("tenderPeriodEnd"),
        SUSPEND_TENDER("suspendTender"),
        AWARD_BY_BID("awardByBid"),
        AWARD_PERIOD_END("awardPeriodEnd"),
        STANDSTILL_PERIOD_END("standstillPeriodEnd"),
        START_NEW_STAGE("startNewStage"),
        CREATE_PIN_ON_PN("createPINonPN");

        companion object {
            private val CONSTANTS = HashMap<String, Operation>()

            init {
                for (c in Operation.values()) {
                    CONSTANTS[c.value] = c
                }
            }

            @JsonCreator
            fun fromValue(value: String): Operation {
                return CONSTANTS[value] ?: throw IllegalArgumentException(value)
            }
        }
    }
}