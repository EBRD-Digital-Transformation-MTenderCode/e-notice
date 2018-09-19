package com.procurement.notice.service

import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.Operation.*
import com.procurement.notice.utils.toLocalDateTime
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service

interface MainService {

    fun createRelease(cm: CommandMessage): ResponseDto

}

@Service
class MainServiceImpl(private val historyDao: HistoryDao,
                      private val budgetService: BudgetService,
                      private val createReleaseService: CreateReleaseService,
                      private val updateReleaseService: UpdateReleaseService,
                      private val tenderService: TenderService,
                      private val tenderServiceEv: TenderServiceEv,
                      private val tenderCancellationService: TenderCancellationService,
                      private val enquiryService: EnquiryService) : MainService {


    override fun createRelease(cm: CommandMessage): ResponseDto {

        var historyEntity = historyDao.getHistory(cm.context.operationId, cm.command.value())
        if (historyEntity != null) return toObject(ResponseDto::class.java, historyEntity.jsonData)

        val cpId = cm.context.cpid
        val ocId = cm.context.ocid
        val stage = cm.context.stage
        val prevStage = cm.context.prevStage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.startDate.toLocalDateTime()
        val data = cm.data

        when (Operation.fromValue(operationType)) {

            CREATE_EI -> return processResponse(cm, budgetService.createEi(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UPDATE_EI -> return processResponse(cm, budgetService.updateEi(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            CREATE_FS -> return processResponse(cm, budgetService.createFs(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UPDATE_FS -> return processResponse(cm, budgetService.updateFs(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            CREATE_CN -> return processResponse(cm, createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_CN))

            CREATE_PN -> return processResponse(cm, createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_PN))

            CREATE_PIN -> return processResponse(cm, createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = CREATE_PIN))
            CREATE_PIN_ON_PN -> return processResponse(cm, createReleaseService.createPinOnPn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data))
            CREATE_CN_ON_PN -> return processResponse(cm, createReleaseService.createCnOnPn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data))
            CREATE_CN_ON_PIN -> return processResponse(cm, createReleaseService.createCnOnPin(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data))
            UPDATE_CN -> return processResponse(cm, updateReleaseService.updateCn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UPDATE_PN -> return processResponse(cm, updateReleaseService.updatePn(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UPDATE_TENDER_PERIOD -> return processResponse(cm, updateReleaseService.updateTenderPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            CREATE_ENQUIRY -> return processResponse(cm, enquiryService.createEnquiry(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            ADD_ANSWER -> return processResponse(cm, enquiryService.addAnswer(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            SUSPEND_TENDER -> return processResponse(cm, tenderService.suspendTender(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UNSUSPEND_TENDER -> return processResponse(cm, enquiryService.unsuspendTender(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            UNSUCCESSFUL_TENDER -> return processResponse(cm, tenderService.tenderUnsuccessful(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            TENDER_PERIOD_END -> return processResponse(cm, tenderService.tenderPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            TENDER_PERIOD_END_EV -> return processResponse(cm, tenderService.tenderPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            AWARD_BY_BID -> return processResponse(cm, tenderService.awardByBid(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            AWARD_BY_BID_EV -> return processResponse(cm, tenderServiceEv.awardByBidEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            STANDSTILL_PERIOD -> return processResponse(cm, tenderService.standstillPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            STANDSTILL_PERIOD_EV -> return processResponse(cm, tenderServiceEv.standstillPeriodEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            AWARD_PERIOD_END -> return processResponse(cm, tenderService.awardPeriodEnd(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            AWARD_PERIOD_END_EV -> return processResponse(cm, tenderServiceEv.awardPeriodEndEv(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            START_NEW_STAGE -> return processResponse(cm, tenderService.startNewStage(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data))
            CANCEL_STANDSTILL -> return processResponse(cm, tenderCancellationService.cancellationStandstillPeriod(
                    cpid = cpId,
                    ocid = ocId!!,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data))
            CANCEL_TENDER, CANCEL_TENDER_EV, CANCEL_PLAN -> return processResponse(cm,
                    tenderCancellationService.tenderCancellation(
                            cpid = cpId,
                            ocid = ocId!!,
                            stage = stage,
                            releaseDate = releaseDate,
                            data = data))
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
    }

    fun processResponse(cm: CommandMessage, response: ResponseDto): ResponseDto {
        val historyEntity = historyDao.saveHistory(cm.context.operationId, cm.command.value(), response)
        return toObject(ResponseDto::class.java, historyEntity.jsonData)
    }

}