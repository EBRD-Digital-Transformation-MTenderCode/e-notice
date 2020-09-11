package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.cn.UpdateCNContext
import com.procurement.notice.application.service.cn.UpdateCNData
import com.procurement.notice.application.service.cn.UpdatedCN
import com.procurement.notice.infrastructure.dto.entity.parties.PersonId
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.AcceleratedProcedure
import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.BusinessFunction
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.DesignContest
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.DynamicPurchasingSystem
import com.procurement.notice.model.ocds.ElectronicWorkflows
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.Framework
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.Item
import com.procurement.notice.model.ocds.JointProcurement
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.LotGroup
import com.procurement.notice.model.ocds.Option
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Person
import com.procurement.notice.model.ocds.PlaceOfPerformance
import com.procurement.notice.model.ocds.ProcedureOutsourcing
import com.procurement.notice.model.ocds.RecurrentProcurement
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.ReleaseAmendment
import com.procurement.notice.model.ocds.Renewal
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Variant
import com.procurement.notice.model.ocds.toValue
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsBudget
import com.procurement.notice.model.tender.ms.MsPlanning
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.ElectronicAuctionModalities
import com.procurement.notice.model.tender.record.ElectronicAuctions
import com.procurement.notice.model.tender.record.ElectronicAuctionsDetails
import com.procurement.notice.model.tender.record.ReleasePreQualification
import com.procurement.notice.model.tender.record.ReleaseTender
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UpdateReleaseService(
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) {

    fun updateCn(context: UpdateCNContext, data: UpdateCNData): UpdatedCN {
        val msEntity = releaseService.getMsEntity(cpid = context.cpid)
        val recordMS = releaseService.getMs(msEntity.jsonData)
        val updatedRecordMS: Ms = recordMS.copy(
            id = generationService.generateReleaseId(ocid = context.cpid), //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.COMPILED), //FR-MR-5.5.2.3.1
            //FR-MR-5.5.2.3.6
            planning = data.planning.let { planning ->
                MsPlanning(
                    rationale = planning.rationale,
                    budget = planning.budget.let { budget ->
                        MsBudget(
                            id = null,
                            description = budget.description,
                            amount = budget.amount.toValue(),
                            isEuropeanUnionFunded = budget.isEuropeanUnionFunded,
                            budgetBreakdown = budget.budgetBreakdowns
                                .mapIfNotEmpty { budgetBreakdown ->
                                    BudgetBreakdown(
                                        id = budgetBreakdown.id,
                                        description = budgetBreakdown.description,
                                        amount = budgetBreakdown.amount.toValue(),
                                        period = budgetBreakdown.period.let { period ->
                                            Period(
                                                startDate = period.startDate,
                                                endDate = period.endDate,
                                                maxExtentDate = null,
                                                durationInDays = null
                                            )
                                        },
                                        sourceParty = budgetBreakdown.sourceParty.let { sourceParty ->
                                            OrganizationReference(
                                                id = sourceParty.id,
                                                name = sourceParty.name,
                                                identifier = null,
                                                additionalIdentifiers = null,
                                                address = null,
                                                contactPoint = null,
                                                buyerProfile = null,
                                                persones = null,
                                                details = null
                                            )
                                        },
                                        europeanUnionFunding = budgetBreakdown.europeanUnionFunding?.let { europeanUnionFunding ->
                                            EuropeanUnionFunding(
                                                projectIdentifier = europeanUnionFunding.projectIdentifier,
                                                projectName = europeanUnionFunding.projectName,
                                                uri = europeanUnionFunding.uri
                                            )
                                        }
                                    )
                                }
                        )
                    }
                )
            },
            tender = data.tender.let { tender ->
                MsTender(
                    id = recordMS.tender.id, //FR-MR-5.5.2.3.4
                    status = recordMS.tender.status, //FR-MR-5.5.2.3.4
                    statusDetails = recordMS.tender.statusDetails, //FR-MR-5.5.2.3.4
                    title = tender.title,
                    description = tender.description,
                    classification = tender.classification.let { classification ->
                        Classification(
                            scheme = classification.scheme,
                            id = classification.id,
                            description = classification.description,
                            uri = null
                        )
                    },
                    acceleratedProcedure = AcceleratedProcedure(
                        isAcceleratedProcedure = tender.acceleratedProcedure.isAcceleratedProcedure,
                        acceleratedProcedureJustification = null
                    ),
                    designContest = DesignContest(
                        serviceContractAward = tender.designContest.serviceContractAward,
                        hasPrizes = null,
                        prizes = null,
                        paymentsToParticipants = null,
                        juryDecisionBinding = null,
                        juryMembers = null,
                        participants = null
                    ),
                    electronicWorkflows = tender.electronicWorkflows.let { electronicWorkflows ->
                        ElectronicWorkflows(
                            useOrdering = electronicWorkflows.useOrdering,
                            usePayment = electronicWorkflows.usePayment,
                            acceptInvoicing = electronicWorkflows.acceptInvoicing
                        )
                    },
                    jointProcurement = JointProcurement(
                        isJointProcurement = tender.jointProcurement.isJointProcurement,
                        country = null
                    ),
                    procedureOutsourcing = ProcedureOutsourcing(
                        procedureOutsourced = tender.procedureOutsourcing.procedureOutsourced,
                        outsourcedTo = null
                    ),
                    framework = Framework(
                        isAFramework = tender.framework.isAFramework,
                        typeOfFramework = null,
                        maxSuppliers = null,
                        exceptionalDurationRationale = null,
                        additionalBuyerCategories = null
                    ),
                    dynamicPurchasingSystem = DynamicPurchasingSystem(
                        hasDynamicPurchasingSystem = tender.dynamicPurchasingSystem.hasDynamicPurchasingSystem,
                        hasOutsideBuyerAccess = null,
                        noFurtherContracts = null
                    ),
                    legalBasis = tender.legalBasis,
                    procurementMethod = tender.procurementMethod.toString(),
                    procurementMethodDetails = tender.procurementMethodDetails,
                    procurementMethodRationale = tender.procurementMethodRationale,
                    procurementMethodAdditionalInfo = tender.procurementMethodAdditionalInfo,
                    mainProcurementCategory = tender.mainProcurementCategory,
                    eligibilityCriteria = tender.eligibilityCriteria,
                    contractPeriod = tender.contractPeriod.let { tenderPeriod ->
                        Period(
                            startDate = tenderPeriod.startDate,
                            endDate = tenderPeriod.endDate,
                            durationInDays = null,
                            maxExtentDate = null
                        )
                    },
                    procuringEntity = recordMS.tender.procuringEntity, //FR-MR-5.5.2.3.3
                    value = tender.value.toValue(),
                    hasEnquiries = recordMS.tender.hasEnquiries,
                    additionalProcurementCategories = null,
                    submissionLanguages = null,
                    amendments = null
                )
            },

            //FR-MR-5.5.2.3.7
            parties = recordMS.parties
                .map { party ->
                    if (party.id == data.tender.procuringEntity.id)
                        party.copy(
                            persones = data.tender.procuringEntity.persons
                                .map { person ->
                                    Person(
                                        id = PersonId.generate(
                                            scheme = person.identifier.scheme,
                                            id = person.identifier.id
                                        ),
                                        title = person.title,
                                        name = person.name,
                                        identifier = person.identifier
                                            .let { identifier ->
                                                Identifier(
                                                    scheme = identifier.scheme,
                                                    id = identifier.id,
                                                    uri = identifier.uri,
                                                    legalName = null
                                                )
                                            },
                                        businessFunctions = person.businessFunctions
                                            .map { businessFunction ->
                                                BusinessFunction(
                                                    id = businessFunction.id,
                                                    type = businessFunction.type,
                                                    jobTitle = businessFunction.jobTitle,
                                                    period = Period(
                                                        startDate = businessFunction.period.startDate,
                                                        endDate = null,
                                                        durationInDays = null,
                                                        maxExtentDate = null
                                                    ),
                                                    documents = businessFunction.documents
                                                        .map { document ->
                                                            DocumentBF(
                                                                id = document.id,
                                                                documentType = document.documentType,
                                                                title = document.title,
                                                                description = document.description,
                                                                datePublished = document.datePublished,
                                                                dateModified = null,
                                                                url = document.url
                                                            )
                                                        }
                                                )
                                            }
                                    )
                                }
                                .toHashSet()
                        )
                    else
                        party
                }
                .toMutableList()
        )

        val recordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val releaseEV = releaseService.getRelease(recordEntity.jsonData)

        val newReleaseID = generationService.generateReleaseId(context.ocid)
        val actualReleaseID = releaseEV.id!!
        val newAmendment: ReleaseAmendment = newAmendment(
            context = context,
            data = data,
            actualReleaseID = actualReleaseID,
            newReleaseID = newReleaseID
        )
        val updatedAmendments: List<ReleaseAmendment> = releaseEV.tender.amendments.plus(newAmendment)

        val updatedReleaseEV = releaseEV.copy(
            id = newReleaseID, //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.TENDER_AMENDMENT), //FR-ER-5.5.2.3.1
            relatedProcesses = releaseEV.relatedProcesses, //FR-ER-5.5.2.3.2
            parties = releaseEV.parties, //FR-ER-5.5.2.3.2
            preQualification = data.preQualification
                ?.let { preQualification ->
                    ReleasePreQualification(
                        period = preQualification.period
                            .let { period ->
                                ReleasePreQualification.Period(
                                    startDate = period.startDate,
                                    endDate = period.endDate
                                )
                            },
                        qualificationPeriod = null
                    )
                }
                ?: releaseEV.preQualification,
            tender = data.tender.let { tender ->
                ReleaseTender(
                    id = tender.id,
                    status = tender.status,
                    statusDetails = tender.statusDetails,
                    title = releaseEV.tender.title, //FR-ER-5.5.2.3.3
                    description = releaseEV.tender.description, //FR-ER-5.5.2.3.3
                    hasEnquiries = releaseEV.tender.hasEnquiries, //FR-ER-5.5.2.3.3
                    enquiries = releaseEV.tender.enquiries, //FR-ER-5.5.2.3.3
                    criteria = releaseEV.tender.criteria, //FR-ER-5.5.2.3.3
                    otherCriteria = releaseEV.tender.otherCriteria, // FR-ER-5.5.2.3.8
                    conversions = releaseEV.tender.conversions, //FR-ER-5.5.2.3.3
                    awardCriteria = releaseEV.tender.awardCriteria, //FR-ER-5.5.2.3.3
                    awardCriteriaDetails = releaseEV.tender.awardCriteriaDetails, //FR-ER-5.5.2.3.3
                    awardPeriod = null,
                    standstillPeriod = null,
                    amendments = updatedAmendments, //FR-ER-5.5.2.3.4
                    //FR-ER-5.5.2.3.6
                    auctionPeriod = getAuctionPeriod(
                        context = context,
                        data = data,
                        previous = releaseEV.tender.auctionPeriod
                    ),
                    //FR-ER-5.5.2.3.6
                    electronicAuctions = getElectronicAuctions(
                        context = context,
                        data = data, previous = releaseEV.tender.electronicAuctions
                    ),
                    procurementMethodModalities = getProcurementMethodModalities(
                        context = context,
                        data = data,
                        previous = releaseEV.tender.procurementMethodModalities
                    ),
                    tenderPeriod = tender.tenderPeriod
                        ?.let { tenderPeriod ->
                            Period(
                                startDate = tenderPeriod.startDate,
                                endDate = tenderPeriod.endDate,
                                maxExtentDate = null,
                                durationInDays = null
                            )
                        }
                        ?: releaseEV.tender.tenderPeriod,
                    enquiryPeriod = tender.enquiryPeriod
                        ?.let { enquiryPeriod ->
                            Period(
                                startDate = enquiryPeriod.startDate,
                                endDate = enquiryPeriod.endDate,
                                maxExtentDate = null,
                                durationInDays = null
                            )
                        }
                        ?: releaseEV.tender.enquiryPeriod,
                    lotGroups = tender.lotGroups.map { lotGroup ->
                        LotGroup(
                            id = null,
                            optionToCombine = lotGroup.optionToCombine,
                            relatedLots = null,
                            maximumValue = null
                        )
                    },
                    lots = tender.lots.map { lot ->
                        Lot(
                            id = lot.id,
                            internalId = lot.internalId,
                            title = lot.title,
                            description = lot.description,
                            status = lot.status,
                            statusDetails = lot.statusDetails,
                            value = lot.value.toValue(),
                            options = lot.options.map { option ->
                                Option(
                                    hasOptions = option.hasOptions,
                                    optionDetails = null
                                )
                            },
                            variants = lot.variants.map { variant ->
                                Variant(
                                    hasVariants = variant.hasVariants,
                                    variantDetails = null
                                )
                            },
                            renewals = lot.renewals.map { renewal ->
                                Renewal(
                                    hasRenewals = renewal.hasRenewals,
                                    renewalConditions = null,
                                    maxNumber = null
                                )
                            },
                            recurrentProcurement = lot.recurrentProcurements.map { recurrentProcurement ->
                                RecurrentProcurement(
                                    isRecurrent = recurrentProcurement.isRecurrent,
                                    description = null,
                                    dates = null
                                )
                            },
                            contractPeriod = lot.contractPeriod.let { contractPeriod ->
                                Period(
                                    startDate = contractPeriod.startDate,
                                    endDate = contractPeriod.endDate,
                                    durationInDays = null,
                                    maxExtentDate = null
                                )
                            },
                            placeOfPerformance = lot.placeOfPerformance?.let { placeOfPerformance ->
                                PlaceOfPerformance(
                                    address = placeOfPerformance.address.let { address ->
                                        Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            addressDetails = address.addressDetails.let { addressDetails ->
                                                AddressDetails(
                                                    country = addressDetails.country.let { country ->
                                                        CountryDetails(
                                                            scheme = country.scheme,
                                                            id = country.id,
                                                            description = country.description,
                                                            uri = country.uri
                                                        )
                                                    },
                                                    region = addressDetails.region.let { region ->
                                                        RegionDetails(
                                                            scheme = region.scheme,
                                                            id = region.id,
                                                            description = region.description,
                                                            uri = region.uri
                                                        )
                                                    },
                                                    locality = addressDetails.locality.let { locality ->
                                                        LocalityDetails(
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
                                    description = placeOfPerformance.description,
                                    nutScode = null
                                )
                            }
                        )
                    }.toList(),
                    items = tender.items.map { item ->
                        Item(
                            id = item.id,
                            internalId = item.internalId,
                            classification = item.classification.let { classification ->
                                Classification(
                                    scheme = classification.scheme,
                                    id = classification.id,
                                    description = classification.description,
                                    uri = null
                                )
                            },
                            additionalClassifications = item.additionalClassifications
                                .map { additionalClassification ->
                                    Classification(
                                        scheme = additionalClassification.scheme,
                                        id = additionalClassification.id,
                                        description = additionalClassification.description,
                                        uri = null
                                    )
                                }.toHashSet(),
                            quantity = item.quantity,
                            unit = item.unit.let { unit ->
                                com.procurement.notice.model.ocds.Unit(
                                    scheme = null,
                                    id = unit.id,
                                    name = unit.name,
                                    value = null,
                                    uri = null
                                )
                            },
                            description = item.description,
                            relatedLot = item.relatedLot,
                            deliveryAddress = null
                        )
                    }.toList(),
                    requiresElectronicCatalogue = tender.requiresElectronicCatalogue,
                    submissionMethod = tender.submissionMethod.toList(),
                    submissionMethodRationale = tender.submissionMethodRationale.toList(),
                    submissionMethodDetails = tender.submissionMethodDetails,
                    documents = tender.documents.map { document ->
                        Document(
                            documentType = document.documentType,
                            id = document.id,
                            title = document.title,
                            description = document.description,
                            relatedLots = document.relatedLots.toList(),
                            datePublished = document.datePublished,
                            url = document.url,
                            dateModified = null,
                            format = null,
                            language = null,
                            relatedConfirmations = null
                        )
                    }.toList(),
                    secondStage = releaseEV.tender.secondStage //FR-ER-5.5.2.3.9
                )
            }
        )

        releaseService.saveMs(cpId = context.cpid, ms = updatedRecordMS, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            release = updatedReleaseEV,
            publishDate = recordEntity.publishDate
        )
        return UpdatedCN(
            cpid = context.cpid,
            ocid = context.ocid,
            amendment = UpdatedCN.Amendment(id = newAmendment.id!!)
        )
    }

    private fun newAmendment(
        context: UpdateCNContext,
        data: UpdateCNData,
        actualReleaseID: String,
        newReleaseID: String
    ): ReleaseAmendment {
        val rationale: String
        val relatedLots: List<String>

        if (data.amendment != null) {
            rationale = "Changing of Contract Notice due to the need of cancelling lot / lots"
            relatedLots = data.amendment.relatedLots
        } else {
            rationale = "General change of Contract Notice"
            relatedLots = emptyList()
        }

        return ReleaseAmendment(
            id = UUID.randomUUID().toString(),
            amendsReleaseID = actualReleaseID,
            releaseID = newReleaseID,
            date = context.releaseDate,
            rationale = rationale,
            relatedLots = relatedLots,
            changes = emptyList(),
            description = null,
            documents = emptyList(),
            relatesTo = null,
            relatedItem = null,
            status = null,
            type = null
        )
    }

    private fun getAuctionPeriod(
        context: UpdateCNContext,
        data: UpdateCNData,
        previous: Period?
    ): Period? = if (context.isAuction && data.isAuctionPeriodChanged)
        Period(
            startDate = data.tender.auctionPeriod!!.startDate,
            endDate = null,
            durationInDays = null,
            maxExtentDate = null
        )
    else
        previous

    private fun getProcurementMethodModalities(
        context: UpdateCNContext,
        data: UpdateCNData,
        previous: List<String>
    ): List<String> = if (context.isAuction && data.isAuctionPeriodChanged)
        data.tender.procurementMethodModalities
    else
        previous

    private fun getElectronicAuctions(
        context: UpdateCNContext,
        data: UpdateCNData,
        previous: ElectronicAuctions?
    ): ElectronicAuctions? =
        if (context.isAuction && data.isAuctionPeriodChanged)
            data.tender.electronicAuctions!!.let { electronicAuctions ->
                ElectronicAuctions(
                    details = electronicAuctions.details.map { detail ->
                        ElectronicAuctionsDetails(
                            id = detail.id,
                            relatedLot = detail.relatedLot,
                            auctionPeriod = detail.auctionPeriod!!.let { auctionPeriod ->
                                Period(
                                    startDate = auctionPeriod.startDate,
                                    endDate = null,
                                    durationInDays = null,
                                    maxExtentDate = null
                                )
                            },
                            electronicAuctionModalities = detail.electronicAuctionModalities.map { electronicAuctionModality ->
                                ElectronicAuctionModalities(
                                    eligibleMinimumDifference = electronicAuctionModality.eligibleMinimumDifference.toValue(),
                                    url = electronicAuctionModality.url
                                )
                            }.toSet(),
                            electronicAuctionProgress = null,
                            electronicAuctionResult = null
                        )
                    }.toSet()
                )
            }
        else
            previous

    fun updatePn(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            procuringEntity = ms.tender.procuringEntity
            hasEnquiries = ms.tender.hasEnquiries
        }
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        recordTender.apply {
            title = release.tender.title
            description = release.tender.description
        }
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender = recordTender
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        val amendmentsIds = null
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    fun updateTenderPeriod(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val recordTender = releaseService.getRecordTender(data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        val actualReleaseID = release.id
        val newReleaseID = generationService.generateReleaseId(ocid)
        val amendments = release.tender.amendments.toMutableList()
        amendments.add(
            ReleaseAmendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = emptyList(),
                rationale = "Extension of tender period",
                changes = emptyList(),
                description = null,
                documents = emptyList(),
                type = null,
                status = null,
                relatedItem = null,
                relatesTo = null
            )
        )
        release.apply {
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = if (amendments.isNotEmpty()) amendments else emptyList()
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        val amendmentsIds = amendments.map { it.id!! }
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }
}



