package com.procurement.notice.service

import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.CommandType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.Operation.*
import com.procurement.notice.utils.toLocalDateTime
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service

@Service
class CommandService(private val historyDao: HistoryDao,
                     private val budgetService: BudgetService,
                     private val createReleaseService: CreateReleaseService,
                     private val updateReleaseService: UpdateReleaseService,
                     private val tenderService: TenderService,
                     private val tenderServiceEv: TenderServiceEv,
                     private val tenderCancellationService: TenderCancellationService,
                     private val enquiryService: EnquiryService) {


    fun execute(cm: CommandMessage): ResponseDto {

        val cpId = cm.context.cpid
        val ocId = cm.context.ocid
        val stage = cm.context.stage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.startDate.toLocalDateTime()
        val data = cm.data

        when (Operation.fromValue(operationType)) {
            CANCEL_TENDER_EV -> return tenderCancellationService.tenderCancellation(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)
            else -> {
                var historyEntity = historyDao.getHistory(cm.id, cm.command.value())
                if (historyEntity != null) {
                    return toObject(ResponseDto::class.java, historyEntity.jsonData)
                }

                val response = when (cm.command) {
                    CommandType.CREATE_RELEASE -> createRelease(cm)
                }
                historyEntity = historyDao.saveHistory(cm.id, cm.command.value(), response)
                return toObject(ResponseDto::
                class.java, historyEntity.jsonData)
            }
        }
    }

    fun createRelease(cm: CommandMessage): ResponseDto {

        val cpId = cm.context.cpid
        val ocId = cm.context.ocid
        val stage = cm.context.stage
        val prevStage = cm.context.prevStage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.startDate.toLocalDateTime()
        val isAuction = cm.context.isAuction
        val data = cm.data

        when (Operation.fromValue(operationType)) {

            CREATE_EI -> return budgetService.createEi(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            UPDATE_EI -> return budgetService.updateEi(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            CREATE_FS -> return budgetService.createFs(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            UPDATE_FS -> return budgetService.updateFs(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            CREATE_CN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_CN)

            CREATE_PN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_PN)

            CREATE_PIN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_PIN)

            CREATE_PIN_ON_PN -> return createReleaseService.createPinOnPn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            CREATE_CN_ON_PN -> return createReleaseService.createCnOnPn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            CREATE_CN_ON_PIN -> return createReleaseService.createCnOnPin(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            UPDATE_CN -> return updateReleaseService.updateCn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    isAuction = isAuction!!,
                    data = data)

            UPDATE_PN -> return updateReleaseService.updatePn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            UPDATE_TENDER_PERIOD -> return updateReleaseService.updateTenderPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            CREATE_ENQUIRY -> return enquiryService.createEnquiry(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            ADD_ANSWER -> return enquiryService.addAnswer(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            SUSPEND_TENDER -> return tenderService.suspendTender(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            UNSUSPEND_TENDER -> return tenderService.unsuspendTender(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            UNSUCCESSFUL_TENDER -> return tenderService.tenderUnsuccessful(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            TENDER_PERIOD_END -> return tenderService.tenderPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            TENDER_PERIOD_END_EV -> return tenderService.tenderPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            ENQUIRY_PERIOD_END -> return tenderServiceEv.enquiryPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)


            AWARD_BY_BID -> return tenderService.awardByBid(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            AWARD_BY_BID_EV -> return tenderServiceEv.awardByBidEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            STANDSTILL_PERIOD -> return tenderService.standstillPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            STANDSTILL_PERIOD_EV -> return tenderServiceEv.standstillPeriodEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            AWARD_PERIOD_END -> return tenderService.awardPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            AWARD_PERIOD_END_EV -> return tenderServiceEv.awardPeriodEndEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            START_NEW_STAGE -> return tenderService.startNewStage(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            CANCEL_STANDSTILL -> return tenderCancellationService.cancellationStandstillPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            CANCEL_TENDER, CANCEL_PLAN -> return tenderCancellationService.tenderCancellation(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
    }
}