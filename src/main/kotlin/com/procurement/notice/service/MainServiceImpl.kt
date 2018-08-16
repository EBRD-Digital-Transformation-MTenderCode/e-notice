package com.procurement.notice.service

import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.utils.toLocalDateTime
import org.springframework.stereotype.Service

interface MainService {

    fun createRelease(cm: CommandMessage): ResponseDto

}

@Service
class MainServiceImpl(private val budgetService: BudgetService,
                      private val createReleaseService: CreateReleaseService,
                      private val updateReleaseService: UpdateReleaseService,
                      private val tenderService: TenderService,
                      private val tenderServiceEv: TenderServiceEv,
                      private val enquiryService: EnquiryService) : MainService {


    override fun createRelease(cm: CommandMessage): ResponseDto {

        val cpId = cm.context.cpid
        val ocId = cm.context.ocid
        val stage = cm.context.stage
        val prevStage = cm.context.prevStage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.startDate.toLocalDateTime()
        val data = cm.data

        when (Operation.fromValue(operationType)) {

            Operation.CREATE_EI -> return budgetService.createEi(cpid = cpId, stage = stage, releaseDate = releaseDate, data = data)

            Operation.UPDATE_EI -> return budgetService.updateEi(cpid = cpId, stage = stage, releaseDate = releaseDate, data = data)

            Operation.CREATE_FS -> return budgetService.createFs(cpid = cpId, stage = stage, releaseDate = releaseDate, data = data)

            Operation.UPDATE_FS -> return budgetService.updateFs(cpid = cpId, ocid = ocId, stage = stage, releaseDate = releaseDate, data = data)

            Operation.CREATE_CN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = Operation.CREATE_CN)

            Operation.CREATE_PN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = Operation.CREATE_PN)

            Operation.CREATE_PIN -> return createReleaseService.createCnPnPin(
                    cpid = cpId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data,
                    operation = Operation.CREATE_PIN)

            Operation.CREATE_PIN_ON_PN -> return createReleaseService.createPinOnPn(
                    cpid = cpId,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            Operation.CREATE_CN_ON_PN -> return createReleaseService.createCnOnPn(
                    cpid = cpId,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            Operation.CREATE_CN_ON_PIN -> return createReleaseService.createCnOnPin(
                    cpid = cpId,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            Operation.UPDATE_CN -> return updateReleaseService.updateCn(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.UPDATE_PN -> return updateReleaseService.updatePn(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.UPDATE_TENDER_PERIOD -> return updateReleaseService.updateTenderPeriod(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.CREATE_ENQUIRY -> return enquiryService.createEnquiry(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.ADD_ANSWER -> return enquiryService.addAnswer(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.SUSPEND_TENDER -> return tenderService.suspendTender(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.UNSUSPEND_TENDER -> return enquiryService.unsuspendTender(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.UNSUCCESSFUL_TENDER -> return tenderService.tenderUnsuccessful(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.TENDER_PERIOD_END -> return tenderService.tenderPeriodEnd(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.TENDER_PERIOD_END_EV -> return tenderServiceEv.tenderPeriodEndEv(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.AWARD_BY_BID -> return tenderService.awardByBid(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.AWARD_BY_BID_EV -> return tenderServiceEv.awardByBidEv(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.STANDSTILL_PERIOD -> return tenderService.standstillPeriod(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.STANDSTILL_PERIOD_EV -> return tenderServiceEv.standstillPeriodEv(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.AWARD_PERIOD_END -> return tenderService.awardPeriodEnd(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.AWARD_PERIOD_END_EV -> return tenderServiceEv.awardPeriodEndEv(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    releaseDate = releaseDate,
                    data = data)

            Operation.START_NEW_STAGE -> return tenderService.startNewStage(
                    cpid = cpId,
                    ocid = ocId,
                    stage = stage,
                    prevStage = prevStage!!,
                    releaseDate = releaseDate,
                    data = data)

            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
    }


}