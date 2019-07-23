package com.procurement.notice.service

import com.procurement.notice.application.service.award.AwardService
import com.procurement.notice.application.service.award.CreateAwardContext
import com.procurement.notice.application.service.award.CreateAwardData
import com.procurement.notice.application.service.award.StartAwardPeriodContext
import com.procurement.notice.application.service.award.StartAwardPeriodData
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.infrastructure.dto.award.CreateAwardRequest
import com.procurement.notice.infrastructure.dto.award.StartAwardPeriodRequest
import com.procurement.notice.model.bpe.CommandMessage
import com.procurement.notice.model.bpe.CommandType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.bpe.cpid
import com.procurement.notice.model.bpe.ocid
import com.procurement.notice.model.bpe.stage
import com.procurement.notice.model.bpe.startDate
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.Operation.ACTIVATION_AC
import com.procurement.notice.model.ocds.Operation.ADD_ANSWER
import com.procurement.notice.model.ocds.Operation.AUCTION_PERIOD_END
import com.procurement.notice.model.ocds.Operation.AWARD_BY_BID
import com.procurement.notice.model.ocds.Operation.AWARD_BY_BID_EV
import com.procurement.notice.model.ocds.Operation.AWARD_PERIOD_END
import com.procurement.notice.model.ocds.Operation.BUYER_SIGNING_AC
import com.procurement.notice.model.ocds.Operation.CANCEL_CAN
import com.procurement.notice.model.ocds.Operation.CANCEL_CAN_CONTRACT
import com.procurement.notice.model.ocds.Operation.CANCEL_PLAN
import com.procurement.notice.model.ocds.Operation.CANCEL_STANDSTILL
import com.procurement.notice.model.ocds.Operation.CANCEL_TENDER
import com.procurement.notice.model.ocds.Operation.CANCEL_TENDER_EV
import com.procurement.notice.model.ocds.Operation.CONFIRM_CAN
import com.procurement.notice.model.ocds.Operation.CREATE_AC
import com.procurement.notice.model.ocds.Operation.CREATE_AWARD
import com.procurement.notice.model.ocds.Operation.CREATE_CAN
import com.procurement.notice.model.ocds.Operation.CREATE_CN
import com.procurement.notice.model.ocds.Operation.CREATE_CN_ON_PIN
import com.procurement.notice.model.ocds.Operation.CREATE_CN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_EI
import com.procurement.notice.model.ocds.Operation.CREATE_ENQUIRY
import com.procurement.notice.model.ocds.Operation.CREATE_FS
import com.procurement.notice.model.ocds.Operation.CREATE_NEGOTIATION_CN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_PIN
import com.procurement.notice.model.ocds.Operation.CREATE_PIN_ON_PN
import com.procurement.notice.model.ocds.Operation.CREATE_PN
import com.procurement.notice.model.ocds.Operation.END_AWARD_PERIOD
import com.procurement.notice.model.ocds.Operation.END_CONTRACT_PROCESS
import com.procurement.notice.model.ocds.Operation.ENQUIRY_PERIOD_END
import com.procurement.notice.model.ocds.Operation.FINAL_UPDATE
import com.procurement.notice.model.ocds.Operation.ISSUING_AC
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
import com.procurement.notice.model.ocds.Operation.UPDATE_BID_DOCS
import com.procurement.notice.model.ocds.Operation.UPDATE_CAN_DOCS
import com.procurement.notice.model.ocds.Operation.UPDATE_CN
import com.procurement.notice.model.ocds.Operation.UPDATE_EI
import com.procurement.notice.model.ocds.Operation.UPDATE_FS
import com.procurement.notice.model.ocds.Operation.UPDATE_PN
import com.procurement.notice.model.ocds.Operation.UPDATE_TENDER_PERIOD
import com.procurement.notice.model.ocds.Operation.VERIFICATION_AC
import com.procurement.notice.service.contract.ContractingService
import com.procurement.notice.utils.toLocalDateTime
import com.procurement.notice.utils.toObject
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
    private val awardService: AwardService
) {

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
        val stage = cm.context.stage
        val prevStage = cm.context.prevStage
        val operationType = cm.context.operationType
        val releaseDate = cm.context.timeStamp.toLocalDateTime()
        val isAuction = cm.context.isAuction
        val data = cm.data

        return when (Operation.fromValue(operationType)) {

            CREATE_EI -> budgetService.createEi(
                cpid = cpId,
                stage = stage,
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
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_CN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                operation = CREATE_CN
            )

            CREATE_PN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                operation = CREATE_PN
            )

            CREATE_PIN -> createReleaseService.createCnPnPin(
                cpid = cpId,
                stage = stage,
                releaseDate = releaseDate,
                data = data,
                operation = CREATE_PIN
            )

            CREATE_PIN_ON_PN -> createReleaseService.createPinOnPn(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_CN_ON_PN -> createReleaseService.createCnOnPn(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_NEGOTIATION_CN_ON_PN -> createReleaseService.createNegotiationCnOnPn(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                operation = CREATE_NEGOTIATION_CN_ON_PN,
                data = data
            )

            CREATE_CN_ON_PIN -> createReleaseService.createCnOnPin(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_CN -> updateReleaseService.updateCn(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                isAuction = isAuction!!,
                data = data
            )

            UPDATE_PN -> updateReleaseService.updatePn(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_TENDER_PERIOD -> updateReleaseService.updateTenderPeriod(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_ENQUIRY -> enquiryService.createEnquiry(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ADD_ANSWER -> enquiryService.addAnswer(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            SUSPEND_TENDER -> tenderService.suspendTender(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UNSUSPEND_TENDER -> tenderService.unsuspendTender(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UNSUCCESSFUL_TENDER -> tenderService.tenderUnsuccessful(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            TENDER_PERIOD_END -> tenderService.tenderPeriodEnd(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            TENDER_PERIOD_END_AUCTION -> tenderService.tenderPeriodEndAuction(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AUCTION_PERIOD_END -> tenderService.auctionPeriodEnd(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            TENDER_PERIOD_END_EV -> tenderService.tenderPeriodEnd(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ENQUIRY_PERIOD_END -> tenderServiceEv.enquiryPeriodEnd(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_BY_BID -> tenderService.awardByBid(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_BY_BID_EV -> tenderServiceEv.awardByBidEv(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            STANDSTILL_PERIOD -> tenderService.standstillPeriod(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            AWARD_PERIOD_END -> tenderService.awardPeriodEnd(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            START_NEW_STAGE -> tenderService.startNewStage(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                prevStage = prevStage!!,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_STANDSTILL -> tenderCancellationService.cancellationStandstillPeriod(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_TENDER, CANCEL_TENDER_EV, CANCEL_PLAN -> tenderCancellationService.tenderCancellation(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_BID_DOCS -> tenderServiceEv.updateBidDocs(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_AC -> contractingService.createAc(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_AC -> contractingService.updateAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ISSUING_AC -> contractingService.issuingAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            FINAL_UPDATE -> contractingService.finalUpdateAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            BUYER_SIGNING_AC -> contractingService.buyerSigningAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            SUPPLIER_SIGNING_AC -> contractingService.supplierSigningAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            VERIFICATION_AC -> contractingService.verificationAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            TREASURY_APPROVING_AC -> contractingService.treasuryApprovingAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            ACTIVATION_AC -> contractingService.activationAC(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CREATE_CAN -> contractingService.createCan(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            UPDATE_CAN_DOCS -> contractingService.updateCanDocs(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_CAN -> contractingService.cancelCan(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CANCEL_CAN_CONTRACT -> contractingService.cancelCanAndContract(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            END_AWARD_PERIOD -> contractingService.endAwardPeriod(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            CONFIRM_CAN -> contractingService.confirmCan(
                cpid = cpId,
                ocid = ocId!!,
                stage = stage,
                releaseDate = releaseDate,
                data = data
            )

            END_CONTRACT_PROCESS -> contractingService.endContractingProcess(
                cpid = cpId,
                ocid = ocId!!,
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
                    },
                    lots = request.lots?.map { lot ->
                        CreateAwardData.Lot(
                            id = lot.id,
                            title = lot.title,
                            description = lot.description,
                            status = lot.status,
                            statusDetails = lot.statusDetails,
                            value = lot.value.let { value ->
                                CreateAwardData.Lot.Value(
                                    amount = value.amount,
                                    currency = value.currency
                                )
                            },
                            options = lot.options.map { option ->
                                CreateAwardData.Lot.Option(
                                    hasOptions = option.hasOptions
                                )
                            },
                            variants = lot.variants.map { variant ->
                                CreateAwardData.Lot.Variant(
                                    hasVariants = variant.hasVariants
                                )
                            },
                            renewals = lot.renewals.map { renewal ->
                                CreateAwardData.Lot.Renewal(
                                    hasRenewals = renewal.hasRenewals
                                )
                            },
                            recurrentProcurement = lot.recurrentProcurement.map { recurrentProcurement ->
                                CreateAwardData.Lot.RecurrentProcurement(
                                    isRecurrent = recurrentProcurement.isRecurrent
                                )
                            },
                            contractPeriod = lot.contractPeriod.let { contractPeriod ->
                                CreateAwardData.Lot.ContractPeriod(
                                    startDate = contractPeriod.startDate,
                                    endDate = contractPeriod.endDate
                                )
                            },
                            placeOfPerformance = lot.placeOfPerformance.let { placeOfPerformance ->
                                CreateAwardData.Lot.PlaceOfPerformance(
                                    description = placeOfPerformance.description,
                                    address = placeOfPerformance.address.let { address ->
                                        CreateAwardData.Lot.PlaceOfPerformance.Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            addressDetails = address.addressDetails.let { addressDetails ->
                                                CreateAwardData.Lot.PlaceOfPerformance.Address.AddressDetails(
                                                    country = addressDetails.country.let { country ->
                                                        CreateAwardData.Lot.PlaceOfPerformance.Address.AddressDetails.Country(
                                                            scheme = country.scheme,
                                                            id = country.id,
                                                            description = country.description,
                                                            uri = country.uri
                                                        )
                                                    },
                                                    region = addressDetails.region.let { region ->
                                                        CreateAwardData.Lot.PlaceOfPerformance.Address.AddressDetails.Region(
                                                            scheme = region.scheme,
                                                            id = region.id,
                                                            description = region.description,
                                                            uri = region.uri
                                                        )
                                                    },
                                                    locality = addressDetails.locality.let { locality ->
                                                        CreateAwardData.Lot.PlaceOfPerformance.Address.AddressDetails.Locality(
                                                            scheme = locality.scheme,
                                                            id = locality.id,
                                                            description = locality.description,
                                                            uri = locality.uri
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    }
                )
                awardService.createAward(context = createAwardContext, data = createAwardData)
                ResponseDto(data = DataResponseDto())
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
                        statusDetails = request.statusDetails
                    )
                )

                awardService.startAwardPeriod(context = startAwardPeriodContext, data = startAwardPeriodData)
                ResponseDto(data = DataResponseDto())
            }
        }
    }
}
