package com.procurement.notice.service

import com.procurement.notice.application.service.auction.AuctionPeriodEndContext
import com.procurement.notice.application.service.auction.AuctionService
import com.procurement.notice.application.service.award.AwardService
import com.procurement.notice.application.service.award.CreateAwardContext
import com.procurement.notice.application.service.award.CreateAwardData
import com.procurement.notice.application.service.award.EndAwardPeriodContext
import com.procurement.notice.application.service.award.EndAwardPeriodData
import com.procurement.notice.application.service.award.EvaluateAwardContext
import com.procurement.notice.application.service.award.StartAwardPeriodContext
import com.procurement.notice.application.service.award.StartAwardPeriodData
import com.procurement.notice.application.service.award.auction.AwardConsiderationContext
import com.procurement.notice.application.service.award.auction.StartAwardPeriodAuctionContext
import com.procurement.notice.application.service.can.ConfirmCANContext
import com.procurement.notice.application.service.can.ConfirmCANData
import com.procurement.notice.application.service.can.CreateCANContext
import com.procurement.notice.application.service.can.CreateCANData
import com.procurement.notice.application.service.can.CreateProtocolContext
import com.procurement.notice.application.service.can.CreateProtocolData
import com.procurement.notice.application.service.cn.UpdateCNContext
import com.procurement.notice.application.service.cn.UpdatedCN
import com.procurement.notice.application.service.contract.activate.ActivateContractContext
import com.procurement.notice.application.service.contract.activate.ActivateContractData
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationContext
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationData
import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionContext
import com.procurement.notice.application.service.fe.amend.AmendFeContext
import com.procurement.notice.application.service.fe.create.CreateFeContext
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodContext
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodData
import com.procurement.notice.application.service.tender.cancel.CancelledStandStillPeriodData
import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndContext
import com.procurement.notice.application.service.tender.unsuccessful.TenderUnsuccessfulContext
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.auction.periodEnd.request.AuctionPeriodEndRequest
import com.procurement.notice.infrastructure.dto.award.AwardConsiderationRequest
import com.procurement.notice.infrastructure.dto.award.CreateAwardRequest
import com.procurement.notice.infrastructure.dto.award.EndAwardPeriodRequest
import com.procurement.notice.infrastructure.dto.award.EvaluateAwardRequest
import com.procurement.notice.infrastructure.dto.award.StartAwardPeriodRequest
import com.procurement.notice.infrastructure.dto.award.auction.StartAwardPeriodAuctionRequest
import com.procurement.notice.infrastructure.dto.can.ConfirmCANRequest
import com.procurement.notice.infrastructure.dto.can.CreateCANRequest
import com.procurement.notice.infrastructure.dto.can.CreateProtocolRequest
import com.procurement.notice.infrastructure.dto.cn.update.UpdateCNRequest
import com.procurement.notice.infrastructure.dto.contract.ActivateContractRequest
import com.procurement.notice.infrastructure.dto.contract.TreasuryClarificationRequest
import com.procurement.notice.infrastructure.dto.contract.TreasuryRejectionRequest
import com.procurement.notice.infrastructure.dto.convert.convert
import com.procurement.notice.infrastructure.dto.tender.cancel.CancelStandStillPeriodRequest
import com.procurement.notice.infrastructure.dto.tender.periodEnd.TenderPeriodEndRequest
import com.procurement.notice.infrastructure.dto.tender.unsuccessful.TenderUnsuccessfulRequest
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.CommandType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.bpe.cpid
import com.procurement.notice.model.bpe.isAuction
import com.procurement.notice.model.bpe.language
import com.procurement.notice.model.bpe.ocid
import com.procurement.notice.model.bpe.ocidCn
import com.procurement.notice.model.bpe.pmd
import com.procurement.notice.model.bpe.prevStage
import com.procurement.notice.model.bpe.stage
import com.procurement.notice.model.bpe.startDate
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.Operation.ACTIVATION_AC
import com.procurement.notice.model.ocds.Operation.ADD_ANSWER
import com.procurement.notice.model.ocds.Operation.AMEND_FE
import com.procurement.notice.model.ocds.Operation.AUCTION_PERIOD_END
import com.procurement.notice.model.ocds.Operation.AWARD_BY_BID
import com.procurement.notice.model.ocds.Operation.AWARD_BY_BID_EV
import com.procurement.notice.model.ocds.Operation.AWARD_PERIOD_END
import com.procurement.notice.model.ocds.Operation.BUYER_SIGNING_AC
import com.procurement.notice.model.ocds.Operation.CANCEL_CAN
import com.procurement.notice.model.ocds.Operation.CANCEL_CAN_CONTRACT
import com.procurement.notice.model.ocds.Operation.CANCEL_PLAN
import com.procurement.notice.model.ocds.Operation.CANCEL_STANDSTILL_PERIOD
import com.procurement.notice.model.ocds.Operation.CANCEL_TENDER
import com.procurement.notice.model.ocds.Operation.CANCEL_TENDER_EV
import com.procurement.notice.model.ocds.Operation.CONFIRM_CAN
import com.procurement.notice.model.ocds.Operation.CREATE_AC
import com.procurement.notice.model.ocds.Operation.CREATE_AP
import com.procurement.notice.model.ocds.Operation.CREATE_AWARD
import com.procurement.notice.model.ocds.Operation.CREATE_CAN
import com.procurement.notice.model.ocds.Operation.CREATE_CN
import com.procurement.notice.model.ocds.Operation.CREATE_CN_ON_PIN
import com.procurement.notice.model.ocds.Operation.CREATE_CN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_EI
import com.procurement.notice.model.ocds.Operation.CREATE_ENQUIRY
import com.procurement.notice.model.ocds.Operation.CREATE_FE
import com.procurement.notice.model.ocds.Operation.CREATE_FS
import com.procurement.notice.model.ocds.Operation.CREATE_NEGOTIATION_CN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_PIN
import com.procurement.notice.model.ocds.Operation.CREATE_PIN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_PN
import com.procurement.notice.model.ocds.Operation.CREATE_PROTOCOL
import com.procurement.notice.model.ocds.Operation.DO_AWARD_CONSIDERATION
import com.procurement.notice.model.ocds.Operation.END_AWARD_PERIOD
import com.procurement.notice.model.ocds.Operation.END_CONTRACT_PROCESS
import com.procurement.notice.model.ocds.Operation.ENQUIRY_PERIOD_END
import com.procurement.notice.model.ocds.Operation.FINAL_UPDATE
import com.procurement.notice.model.ocds.Operation.ISSUING_AC
import com.procurement.notice.model.ocds.Operation.PROCESS_AC_CLARIFICATION
import com.procurement.notice.model.ocds.Operation.PROCESS_AC_REJECTION
import com.procurement.notice.model.ocds.Operation.STANDSTILL_PERIOD
import com.procurement.notice.model.ocds.Operation.START_AWARD_PERIOD
import com.procurement.notice.model.ocds.Operation.START_NEW_STAGE
import com.procurement.notice.model.ocds.Operation.SUPPLIER_SIGNING_AC
import com.procurement.notice.model.ocds.Operation.SUSPEND_TENDER
import com.procurement.notice.model.ocds.Operation.TENDER_PERIOD_END
import com.procurement.notice.model.ocds.Operation.TENDER_PERIOD_END_AUCTION
import com.procurement.notice.model.ocds.Operation.TENDER_PERIOD_END_EV
import com.procurement.notice.model.ocds.Operation.TREASURY_APPROVING_AC
import com.procurement.notice.model.ocds.Operation.UNSUCCESSFUL_TENDER
import com.procurement.notice.model.ocds.Operation.UNSUSPEND_TENDER
import com.procurement.notice.model.ocds.Operation.UPDATE_AC
import com.procurement.notice.model.ocds.Operation.UPDATE_AP
import com.procurement.notice.model.ocds.Operation.UPDATE_BID_DOCS
import com.procurement.notice.model.ocds.Operation.UPDATE_CAN_DOCS
import com.procurement.notice.model.ocds.Operation.UPDATE_CN
import com.procurement.notice.model.ocds.Operation.UPDATE_EI
import com.procurement.notice.model.ocds.Operation.UPDATE_FS
import com.procurement.notice.model.ocds.Operation.UPDATE_PN
import com.procurement.notice.model.ocds.Operation.UPDATE_TENDER_PERIOD
import com.procurement.notice.model.ocds.Operation.VERIFICATION_AC
import com.procurement.notice.service.contract.ContractingService
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toLocalDateTime
import com.procurement.notice.utils.toObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CommandService(
    private val historyDao: HistoryDao,
    private val budgetService: BudgetService,
    private val createReleaseService: CreateReleaseService,
    private val updateReleaseService: UpdateReleaseService,
    private val tenderService: TenderService,
    private val tenderServiceEv: TenderServiceEv,
    private val tenderCancellationService: TenderCancellationService,
    private val enquiryService: EnquiryService,
    private val contractingService: ContractingService,
    private val awardService: AwardService,
    private val auctionService: AuctionService
) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandService::class.java)
    }

    fun execute(cm: CommandMessage): ResponseDto {
        var historyEntity = historyDao.getHistory(cm.id, cm.command.value())
        if (historyEntity != null) {
            return toObject(ResponseDto::class.java, historyEntity.jsonData)
        }
        val response = when (cm.command) {
            CommandType.CREATE_RELEASE -> createRelease(cm)
        }
        historyEntity = historyDao.saveHistory(cm.id, cm.command.value(), response)
        return toObject(ResponseDto::class.java, historyEntity.jsonData)
    }

    fun createRelease(cm: CommandMessage): ResponseDto {
        val cpId = cm.context.cpid
        val ocId = cm.context.ocid
        val ocidCn = cm.context.ocidCn
        val stage = cm.context.stage
        val prevStage = cm.context.prevStage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.timeStamp.toLocalDateTime()
        val data = cm.data

        return when (Operation.fromValue(operationType)) {

            CREATE_EI -> budgetService.createEi(
                cpid = cpId,
                stage = stage,
                language = cm.language,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_EI -> budgetService.updateEi(
                cpid = cpId,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_FS -> budgetService.createFs(
                cpid = cpId,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_FS -> budgetService.updateFs(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_CN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                language = cm.language,
                operation = CREATE_CN
            )

            CREATE_PN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                language = cm.language,
                operation = CREATE_PN
            )

            CREATE_AP -> createReleaseService.createAp(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                language = cm.language,
                operation = CREATE_AP
            )

            CREATE_PIN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                language = cm.language,
                operation = CREATE_PIN
            )

            CREATE_PIN_ON_PN -> createReleaseService.createPinOnPn(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_CN_ON_PN -> createReleaseService.createCnOnPn(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                ocidCn = ocidCn
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocidCn' attribute in context."
                    ),
                language = cm.language,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_NEGOTIATION_CN_ON_PN -> createReleaseService.createNegotiationCnOnPn(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                ocidCn = ocidCn
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocidCn' attribute in context."
                    ),
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                operation = CREATE_NEGOTIATION_CN_ON_PN,
                data = data
            )

            CREATE_CN_ON_PIN -> createReleaseService.createCnOnPin(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_CN -> {
                val context = UpdateCNContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    isAuction = cm.isAuction
                )
                val request = toObject(UpdateCNRequest::class.java, cm.data)
                val result: UpdatedCN = updateReleaseService.updateCn(context = context, data = request.convert())

                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid,
                        amendmentsIds = result.amendment?.let { amendment ->
                            listOf(amendment.id)
                        }
                    )
                )
            }

            UPDATE_PN -> updateReleaseService.updatePn(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_AP -> updateReleaseService.updateAp(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_TENDER_PERIOD -> updateReleaseService.updateTenderPeriod(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_ENQUIRY -> enquiryService.createEnquiry(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ADD_ANSWER -> enquiryService.addAnswer(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            SUSPEND_TENDER -> tenderService.suspendTender(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UNSUSPEND_TENDER -> tenderService.unsuspendTender(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                pmd = cm.pmd,
                releaseDate = releaseDate,
                data = data
            )

            UNSUCCESSFUL_TENDER -> {
                val context = TenderUnsuccessfulContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate
                )
                val request = toObject(TenderUnsuccessfulRequest::class.java, cm.data)
                val result = tenderService.tenderUnsuccessful(context = context, data = request.convert())
                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid
                    )
                )
            }

            TENDER_PERIOD_END -> {
                val context = TenderPeriodEndContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    pmd = cm.pmd
                )
                val request = toObject(TenderPeriodEndRequest::class.java, cm.data)
                val result = tenderService.tenderPeriodEnd(context = context, data = request.convert())
                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid
                    )
                )
            }
            TENDER_PERIOD_END_AUCTION -> {
                val context = StartAwardPeriodAuctionContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    startDate = cm.startDate
                )
                val request = toObject(StartAwardPeriodAuctionRequest::class.java, cm.data)
                val result = tenderService.tenderPeriodEndAuction(
                    data = request.convert(),
                    context = context
                )

                if (log.isDebugEnabled)
                    log.debug("Tender period end with auction. Result: ${toJson(result)}")

                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid
                    )
                )
            }

            AUCTION_PERIOD_END -> {
                val context = AuctionPeriodEndContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    pmd = cm.pmd
                )
                val request = toObject(AuctionPeriodEndRequest::class.java, cm.data)
                auctionService.periodEnd(context = context, data = request.convert())

                ResponseDto(
                    data = DataResponseDto(
                        cpid = context.cpid,
                        ocid = context.ocid
                    )
                )
            }

            TENDER_PERIOD_END_EV -> {
                val context = TenderPeriodEndContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    pmd = cm.pmd
                )
                val request = toObject(TenderPeriodEndRequest::class.java, cm.data)
                val result = tenderService.tenderPeriodEnd(context = context, data = request.convert())
                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid
                    )
                )
            }

            ENQUIRY_PERIOD_END -> tenderServiceEv.enquiryPeriodEnd(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_BY_BID -> tenderService.awardByBid(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_BY_BID_EV -> tenderServiceEv.awardByBidEv(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            STANDSTILL_PERIOD -> tenderService.standstillPeriod(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_PERIOD_END -> tenderService.awardPeriodEnd(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            START_NEW_STAGE -> tenderService.startNewStage(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_STANDSTILL_PERIOD -> {
                val context = CancelStandStillPeriodContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate
                )
                val request = toObject(CancelStandStillPeriodRequest::class.java, cm.data)
                val cancelStandStillPeriodData = CancelStandStillPeriodData(
                    standstillPeriod = request.standstillPeriod.let { standstillPeriod ->
                        CancelStandStillPeriodData.StandstillPeriod(
                            startDate = standstillPeriod.startDate,
                            endDate = standstillPeriod.endDate
                        )
                    },
                    amendments = request.amendments.map { amendment ->
                        CancelStandStillPeriodData.Amendment(
                            rationale = amendment.rationale,
                            description = amendment.description,
                            documents = amendment.documents?.map { document ->
                                CancelStandStillPeriodData.Amendment.Document(
                                    documentType = document.documentType,
                                    id = document.id,
                                    title = document.title,
                                    description = document.description,
                                    datePublished = document.datePublished,
                                    url = document.url
                                )
                            }
                        )
                    },
                    tender = request.tender.let { tender ->
                        CancelStandStillPeriodData.Tender(
                            statusDetails = tender.statusDetails
                        )
                    }
                )

                val result: CancelledStandStillPeriodData = tenderCancellationService.cancellationStandstillPeriod(
                    context = context,
                    data = cancelStandStillPeriodData
                )
                ResponseDto(
                    data = DataResponseDto(
                        cpid = result.cpid,
                        ocid = result.ocid,
                        amendmentsIds = result.amendmentsIds.toList()
                    )
                )
            }

            CANCEL_TENDER, CANCEL_TENDER_EV, CANCEL_PLAN -> tenderCancellationService.tenderCancellation(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_BID_DOCS -> tenderServiceEv.updateBidDocs(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_AC -> contractingService.createAc(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_AC -> contractingService.updateAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ISSUING_AC -> contractingService.issuingAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            FINAL_UPDATE -> contractingService.finalUpdateAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            BUYER_SIGNING_AC -> contractingService.buyerSigningAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            SUPPLIER_SIGNING_AC -> contractingService.supplierSigningAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            VERIFICATION_AC -> contractingService.verificationAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            TREASURY_APPROVING_AC -> contractingService.treasuryApprovingAC(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            PROCESS_AC_CLARIFICATION -> {
                val context = TreasuryClarificationContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    pmd = cm.pmd,
                    releaseDate = releaseDate
                )
                val clarificationRequest = toObject(TreasuryClarificationRequest::class.java, data)
                val clarificationData = TreasuryClarificationData(
                    contract = clarificationRequest.contract.let { contract ->
                        TreasuryClarificationData.Contract(
                            id = contract.id,
                            documents = contract.documents.map { document ->
                                TreasuryClarificationData.Contract.Document(
                                    documentType = document.documentType,
                                    relatedConfirmations = document.relatedConfirmations,
                                    relatedLots = document.relatedLots,
                                    description = document.description,
                                    title = document.title,
                                    id = document.id,
                                    url = document.url,
                                    datePublished = document.datePublished
                                )
                            },
                            title = contract.title,
                            description = contract.description,
                            period = contract.period.let { period ->
                                TreasuryClarificationData.Contract.Period(
                                    startDate = period.startDate,
                                    endDate = period.endDate
                                )
                            },
                            value = contract.value.let { value ->
                                TreasuryClarificationData.Contract.Value(
                                    amount = value.amount,
                                    currency = value.currency,
                                    amountNet = value.amountNet,
                                    valueAddedTaxincluded = value.valueAddedTaxincluded
                                )
                            },
                            awardId = contract.awardId,
                            confirmationRequests = contract.confirmationRequests.map { confirmationRequest ->
                                TreasuryClarificationData.Contract.ConfirmationRequest(
                                    title = confirmationRequest.title,
                                    description = confirmationRequest.description,
                                    id = confirmationRequest.id,
                                    relatedItem = confirmationRequest.relatedItem,
                                    relatesTo = confirmationRequest.relatesTo,
                                    requestGroups = confirmationRequest.requestGroups.map { requestGroup ->
                                        TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup(
                                            id = requestGroup.id,
                                            requests = requestGroup.requests.map { request ->
                                                TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup.Request(
                                                    id = request.id,
                                                    description = request.description,
                                                    title = request.title,
                                                    relatedPerson = request.relatedPerson?.let { relatedPerson ->
                                                        TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup.Request.RelatedPerson(
                                                            id = relatedPerson.id,
                                                            name = relatedPerson.name
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    },
                                    source = confirmationRequest.source,
                                    type = confirmationRequest.type
                                )
                            },
                            confirmationResponses = contract.confirmationResponses.map { confirmationResponse ->
                                TreasuryClarificationData.Contract.ConfirmationResponse(
                                    id = confirmationResponse.id,
                                    value = confirmationResponse.value.let { value ->
                                        TreasuryClarificationData.Contract.ConfirmationResponse.Value(
                                            name = value.name,
                                            id = value.id,
                                            relatedPerson = value.relatedPerson?.let { relatedPerson ->
                                                TreasuryClarificationData.Contract.ConfirmationResponse.Value.RelatedPerson(
                                                    id = relatedPerson.id,
                                                    name = relatedPerson.name
                                                )
                                            },
                                            date = value.date,
                                            verification = value.verification.map { verification ->
                                                TreasuryClarificationData.Contract.ConfirmationResponse.Value.Verification(
                                                    type = verification.type,
                                                    value = verification.value,
                                                    rationale = verification.rationale
                                                )
                                            }
                                        )
                                    },
                                    request = confirmationResponse.request
                                )
                            },
                            date = contract.date,
                            milestones = contract.milestones.map { milestone ->
                                TreasuryClarificationData.Contract.Milestone(
                                    id = milestone.id,
                                    type = milestone.type,
                                    title = milestone.title,
                                    description = milestone.description,
                                    additionalInformation = milestone.additionalInformation,
                                    dateMet = milestone.dateMet,
                                    dateModified = milestone.dateModified,
                                    dueDate = milestone.dueDate,
                                    relatedItems = milestone.relatedItems,
                                    relatedParties = milestone.relatedParties.map { relatedParty ->
                                        TreasuryClarificationData.Contract.Milestone.RelatedParty(
                                            id = relatedParty.id,
                                            name = relatedParty.name
                                        )
                                    },
                                    status = milestone.status
                                )
                            },
                            status = contract.status,
                            statusDetails = contract.statusDetails
                        )
                    },
                    cans = clarificationRequest.cans.map { can ->
                        TreasuryClarificationData.Can(
                            id = can.id,
                            statusDetails = can.statusDetails,
                            status = can.status
                        )
                    }
                )
                contractingService.treasuryClarificationAC(context, clarificationData)
            }

            PROCESS_AC_REJECTION -> {
                val context = TreasuryRejectionContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    pmd = cm.pmd,
                    releaseDate = releaseDate
                )
                val rejectionRequest = toObject(TreasuryRejectionRequest::class.java, data)
                val rejectionData = rejectionRequest.convert()
                val result = contractingService.treasuryRejectionAC(context, rejectionData)
                val response = result.convert()
                return ResponseDto(data = response)
            }

            ACTIVATION_AC -> {
                val context = ActivateContractContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    pmd = cm.pmd,
                    releaseDate = releaseDate
                )
                val request = toObject(ActivateContractRequest::class.java, cm.data)
                val activateContractData = ActivateContractData(
                    contract = request.contract.let { contract ->
                        ActivateContractData.Contract(
                            id = contract.id,
                            status = contract.status,
                            statusDetails = contract.statusDetails,
                            milestones = contract.milestones.map { milestone ->
                                ActivateContractData.Contract.Milestone(
                                    id = milestone.id,
                                    relatedItems = milestone.relatedItems?.toList(),
                                    status = milestone.status,
                                    additionalInformation = milestone.additionalInformation,
                                    dueDate = milestone.dueDate,
                                    title = milestone.title,
                                    type = milestone.type,
                                    description = milestone.description,
                                    dateModified = milestone.dateModified,
                                    dateMet = milestone.dateMet,
                                    relatedParties = milestone.relatedParties.map { relatedParty ->
                                        ActivateContractData.Contract.Milestone.RelatedParty(
                                            id = relatedParty.id,
                                            name = relatedParty.name
                                        )
                                    }
                                )
                            }
                        )
                    },
                    cans = request.cans.map { can ->
                        ActivateContractData.CAN(
                            id = can.id,
                            status = can.status,
                            statusDetails = can.statusDetails
                        )
                    },
                    lots = request.lots.map { lot ->
                        ActivateContractData.Lot(
                            id = lot.id,
                            status = lot.status,
                            statusDetails = lot.statusDetails
                        )
                    },
                    awards = request.awards.map { award ->
                        ActivateContractData.Award(
                            id = award.id,
                            status = award.status,
                            statusDetails = award.statusDetails
                        )
                    },
                    bids = request.bids?.map { bid ->
                        ActivateContractData.Bid(
                            id = bid.id,
                            status = bid.status,
                            statusDetails = bid.statusDetails
                        )
                    }
                )
                contractingService.activationAC(context = context, data = activateContractData)
            }

            CREATE_PROTOCOL -> {
                val createProtocolContext = CreateProtocolContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    startDate = cm.startDate
                )
                val request = toObject(CreateProtocolRequest::class.java, cm.data)
                val createProtocolData = CreateProtocolData(
                    can = request.can.let { can ->
                        CreateProtocolData.CAN(
                            id = can.id,
                            lotId = can.lotId,
                            awardId = can.awardId,
                            date = can.date,
                            status = can.status,
                            statusDetails = can.statusDetails
                        )
                    },
                    bids = request.bids?.map { bid ->
                        CreateProtocolData.Bid(
                            id = bid.id,
                            statusDetails = bid.statusDetails
                        )
                    },
                    lot = request.lot.let { lot ->
                        CreateProtocolData.Lot(
                            id = lot.id,
                            statusDetails = lot.statusDetails
                        )
                    }
                )

                contractingService.createProtocol(context = createProtocolContext, data = createProtocolData)

                ResponseDto(
                    data = DataResponseDto(
                        cpid = createProtocolContext.cpid,
                        ocid = createProtocolContext.ocid
                    )
                )
            }

            CREATE_CAN -> {
                val createCANContext = CreateCANContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate
                )
                val request = toObject(CreateCANRequest::class.java, cm.data)
                val createCANData = CreateCANData(
                    can = request.can.let { can ->
                        CreateCANData.CAN(
                            id = can.id,
                            lotId = can.lotId,
                            awardId = can.awardId,
                            date = can.date,
                            status = can.status,
                            statusDetails = can.statusDetails
                        )
                    }
                )

                contractingService.createCAN(context = createCANContext, data = createCANData)

                ResponseDto(
                    data = DataResponseDto(
                        cpid = createCANContext.cpid,
                        ocid = createCANContext.ocid
                    )
                )
            }

            UPDATE_CAN_DOCS -> contractingService.updateCanDocs(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_CAN -> contractingService.cancelCan(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_CAN_CONTRACT -> contractingService.cancelCanAndContract(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            END_AWARD_PERIOD -> {
                val endAwardPeriodContext = EndAwardPeriodContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    pmd = cm.pmd,
                    releaseDate = releaseDate
                )
                val request = toObject(EndAwardPeriodRequest::class.java, cm.data)
                val endAwardPeriodData = EndAwardPeriodData(
                    contract = request.contract?.let { contract ->
                        EndAwardPeriodData.Contract(
                            id = contract.id,
                            status = contract.status,
                            statusDetails = contract.statusDetails,
                            milestones = contract.milestones.map { milestone ->
                                EndAwardPeriodData.Contract.Milestone(
                                    id = milestone.id,
                                    relatedItems = milestone.relatedItems?.toList(),
                                    status = milestone.status,
                                    additionalInformation = milestone.additionalInformation,
                                    dueDate = milestone.dueDate,
                                    title = milestone.title,
                                    type = milestone.type,
                                    description = milestone.description,
                                    dateModified = milestone.dateModified,
                                    dateMet = milestone.dateMet,
                                    relatedParties = milestone.relatedParties.map { relatedParty ->
                                        EndAwardPeriodData.Contract.Milestone.RelatedParty(
                                            id = relatedParty.id,
                                            name = relatedParty.name
                                        )
                                    }

                                )
                            }
                        )
                    },
                    cans = request.cans.map { can ->
                        EndAwardPeriodData.CAN(
                            id = can.id,
                            status = can.status,
                            statusDetails = can.statusDetails
                        )
                    },
                    tender = request.tender.let { tender ->
                        EndAwardPeriodData.Tender(
                            status = tender.status,
                            statusDetails = tender.statusDetails
                        )
                    },
                    lots = request.lots.map { lot ->
                        EndAwardPeriodData.Lot(
                            id = lot.id,
                            status = lot.status,
                            statusDetails = lot.statusDetails
                        )
                    },
                    awards = request.awards.map { award ->
                        EndAwardPeriodData.Award(
                            id = award.id,
                            status = award.status,
                            statusDetails = award.statusDetails
                        )
                    },
                    awardPeriod = request.awardPeriod.let { awardPeriod ->
                        EndAwardPeriodData.AwardPeriod(
                            startDate = awardPeriod.startDate,
                            endDate = awardPeriod.endDate
                        )
                    },
                    bids = request.bids?.map { bid ->
                        EndAwardPeriodData.Bid(
                            id = bid.id,
                            status = bid.status,
                            statusDetails = bid.statusDetails
                        )
                    }
                )

                awardService.endAwardPeriod(context = endAwardPeriodContext, data = endAwardPeriodData)
                ResponseDto(
                    data = DataResponseDto(
                        cpid = endAwardPeriodContext.cpid,
                        ocid = endAwardPeriodContext.ocid
                    )
                )
            }

            CONFIRM_CAN -> {
                val createAwardContext = ConfirmCANContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate
                )
                val request = toObject(ConfirmCANRequest::class.java, cm.data)
                val confirmCANData = ConfirmCANData(
                    cans = request.cans.map { can ->
                        ConfirmCANData.CAN(
                            id = can.id,
                            status = can.status,
                            statusDetails = can.statusDetails
                        )
                    },
                    lots = request.lots.map { lot ->
                        ConfirmCANData.Lot(
                            id = lot.id,
                            status = lot.status,
                            statusDetails = lot.statusDetails
                        )
                    },
                    awards = request.awards.map { award ->
                        ConfirmCANData.Award(
                            id = award.id,
                            status = award.status,
                            statusDetails = award.statusDetails
                        )
                    },
                    bids = request.bids?.map { bid ->
                        ConfirmCANData.Bid(
                            id = bid.id,
                            status = bid.status,
                            statusDetails = bid.statusDetails
                        )
                    }
                )
                contractingService.confirmCan(context = createAwardContext, data = confirmCANData)
            }
            END_CONTRACT_PROCESS -> contractingService.endContractingProcess(
                cpid = cpId,
                ocid = ocId
                    ?: throw ErrorException(
                        error = ErrorType.CONTEXT,
                        message = "Missing the 'ocid' attribute in context."
                    ),
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_AWARD -> {
                val createAwardContext = CreateAwardContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    startDate = cm.startDate
                )

                val request = toObject(CreateAwardRequest::class.java, cm.data)
                val createAwardData = CreateAwardData(
                    award = request.award.let { award ->
                        CreateAwardData.Award(
                            id = award.id,
                            date = award.date,
                            status = award.status,
                            statusDetails = award.statusDetails,
                            relatedLots = award.relatedLots.toList(),
                            description = award.description,
                            value = award.value.let { value ->
                                CreateAwardData.Award.Value(
                                    amount = value.amount,
                                    currency = value.currency
                                )
                            },
                            suppliers = award.suppliers.map { supplier ->
                                CreateAwardData.Award.Supplier(
                                    id = supplier.id,
                                    name = supplier.name,
                                    identifier = supplier.identifier.let { identifier ->
                                        CreateAwardData.Award.Supplier.Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            legalName = identifier.legalName,
                                            uri = identifier.uri
                                        )
                                    },
                                    additionalIdentifiers = supplier.additionalIdentifiers?.map { additionalIdentifier ->
                                        CreateAwardData.Award.Supplier.AdditionalIdentifier(
                                            scheme = additionalIdentifier.scheme,
                                            id = additionalIdentifier.id,
                                            legalName = additionalIdentifier.legalName,
                                            uri = additionalIdentifier.uri
                                        )
                                    },
                                    address = supplier.address.let { address ->
                                        CreateAwardData.Award.Supplier.Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            addressDetails = address.addressDetails.let { addressDetails ->
                                                CreateAwardData.Award.Supplier.Address.AddressDetails(
                                                    country = addressDetails.country.let { country ->
                                                        CreateAwardData.Award.Supplier.Address.AddressDetails.Country(
                                                            scheme = country.scheme,
                                                            id = country.id,
                                                            description = country.description,
                                                            uri = country.uri
                                                        )
                                                    },
                                                    region = addressDetails.region.let { region ->
                                                        CreateAwardData.Award.Supplier.Address.AddressDetails.Region(
                                                            scheme = region.scheme,
                                                            id = region.id,
                                                            description = region.description,
                                                            uri = region.uri
                                                        )
                                                    },
                                                    locality = addressDetails.locality.let { locality ->
                                                        CreateAwardData.Award.Supplier.Address.AddressDetails.Locality(
                                                            scheme = locality.scheme,
                                                            id = locality.id,
                                                            description = locality.description,
                                                            uri = locality.uri
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    },
                                    contactPoint = supplier.contactPoint.let { contactPoint ->
                                        CreateAwardData.Award.Supplier.ContactPoint(
                                            name = contactPoint.name,
                                            email = contactPoint.email,
                                            telephone = contactPoint.telephone,
                                            faxNumber = contactPoint.faxNumber,
                                            url = contactPoint.url
                                        )
                                    },
                                    details = supplier.details.let { details ->
                                        CreateAwardData.Award.Supplier.Details(
                                            scale = details.scale
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                awardService.createAward(context = createAwardContext, data = createAwardData)
                ResponseDto(
                    data = DataResponseDto(
                        cpid = createAwardContext.cpid,
                        ocid = createAwardContext.ocid
                    )
                )
            }
            START_AWARD_PERIOD -> {
                val startAwardPeriodContext = StartAwardPeriodContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate,
                    startDate = cm.startDate
                )

                val request = toObject(StartAwardPeriodRequest::class.java, cm.data)
                val startAwardPeriodData = StartAwardPeriodData(
                    award = request.award.let { award ->
                        StartAwardPeriodData.Award(
                            id = award.id,
                            date = award.date,
                            status = award.status,
                            statusDetails = award.statusDetails,
                            relatedLots = award.relatedLots.toList(),
                            description = award.description,
                            value = award.value.let { value ->
                                StartAwardPeriodData.Award.Value(
                                    amount = value.amount,
                                    currency = value.currency
                                )
                            },
                            suppliers = award.suppliers.map { supplier ->
                                StartAwardPeriodData.Award.Supplier(
                                    id = supplier.id,
                                    name = supplier.name,
                                    identifier = supplier.identifier.let { identifier ->
                                        StartAwardPeriodData.Award.Supplier.Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            legalName = identifier.legalName,
                                            uri = identifier.uri
                                        )
                                    },
                                    additionalIdentifiers = supplier.additionalIdentifiers?.map { additionalIdentifier ->
                                        StartAwardPeriodData.Award.Supplier.AdditionalIdentifier(
                                            scheme = additionalIdentifier.scheme,
                                            id = additionalIdentifier.id,
                                            legalName = additionalIdentifier.legalName,
                                            uri = additionalIdentifier.uri
                                        )
                                    },
                                    address = supplier.address.let { address ->
                                        StartAwardPeriodData.Award.Supplier.Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            addressDetails = address.addressDetails.let { addressDetails ->
                                                StartAwardPeriodData.Award.Supplier.Address.AddressDetails(
                                                    country = addressDetails.country.let { country ->
                                                        StartAwardPeriodData.Award.Supplier.Address.AddressDetails.Country(
                                                            scheme = country.scheme,
                                                            id = country.id,
                                                            description = country.description,
                                                            uri = country.uri
                                                        )
                                                    },
                                                    region = addressDetails.region.let { region ->
                                                        StartAwardPeriodData.Award.Supplier.Address.AddressDetails.Region(
                                                            scheme = region.scheme,
                                                            id = region.id,
                                                            description = region.description,
                                                            uri = region.uri
                                                        )
                                                    },
                                                    locality = addressDetails.locality.let { locality ->
                                                        StartAwardPeriodData.Award.Supplier.Address.AddressDetails.Locality(
                                                            scheme = locality.scheme,
                                                            id = locality.id,
                                                            description = locality.description,
                                                            uri = locality.uri
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    },
                                    contactPoint = supplier.contactPoint.let { contactPoint ->
                                        StartAwardPeriodData.Award.Supplier.ContactPoint(
                                            name = contactPoint.name,
                                            email = contactPoint.email,
                                            telephone = contactPoint.telephone,
                                            faxNumber = contactPoint.faxNumber,
                                            url = contactPoint.url
                                        )
                                    },
                                    details = supplier.details.let { details ->
                                        StartAwardPeriodData.Award.Supplier.Details(
                                            scale = details.scale
                                        )
                                    }
                                )
                            }
                        )
                    },
                    awardPeriod = request.awardPeriod.let { awardPeriod ->
                        StartAwardPeriodData.AwardPeriod(
                            startDate = awardPeriod.startDate
                        )
                    },
                    tender = StartAwardPeriodData.Tender(
                        statusDetails = request.tenderStatusDetails
                    )
                )

                awardService.startAwardPeriod(context = startAwardPeriodContext, data = startAwardPeriodData)
                ResponseDto(
                    data = DataResponseDto(
                        cpid = startAwardPeriodContext.cpid,
                        ocid = startAwardPeriodContext.ocid
                    )
                )
            }
            Operation.EVALUATE_AWARD -> {
                val evaluateAwardContext = EvaluateAwardContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    stage = cm.stage,
                    releaseDate = releaseDate
                )
                val request = toObject(EvaluateAwardRequest::class.java, cm.data)
                awardService.evaluate(context = evaluateAwardContext, data = request.convert())
                ResponseDto(
                    data = DataResponseDto(
                        cpid = evaluateAwardContext.cpid,
                        ocid = evaluateAwardContext.ocid
                    )
                )
            }
            DO_AWARD_CONSIDERATION -> {
                val context = AwardConsiderationContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    releaseDate = releaseDate,
                    stage = cm.stage
                )

                val request = toObject(AwardConsiderationRequest::class.java, cm.data)
                awardService.consider(context = context, data = request.convert())
                ResponseDto(
                    data = DataResponseDto(
                        cpid = context.cpid,
                        ocid = context.ocid
                    )
                )
            }
            CREATE_FE -> {
                val context = CreateFeContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    ocidCn = cm.ocidCn,
                    releaseDate = releaseDate,
                    startDate = cm.startDate,
                    stage = cm.stage,
                    prevStage = cm.prevStage
                )

                createReleaseService.createFe(context = context, data = cm.data)
                ResponseDto(
                    data = DataResponseDto(
                        cpid = context.cpid,
                        ocid = context.ocidCn
                    )
                )
            }
            AMEND_FE -> {
                val context = AmendFeContext(
                    cpid = cm.cpid,
                    ocid = cm.ocid,
                    releaseDate = releaseDate,
                    startDate = cm.startDate,
                    stage = cm.stage
                )

                createReleaseService.amendFe(context = context, data = cm.data)
                ResponseDto(
                    data = DataResponseDto(
                        cpid = context.cpid,
                        ocid = context.ocid
                    )
                )
            }
        }
    }
}
