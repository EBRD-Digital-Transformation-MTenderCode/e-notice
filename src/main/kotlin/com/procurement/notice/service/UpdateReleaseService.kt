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
import com.procurement.notice.model.ocds.Recurrence
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.ReleaseAmendment
import com.procurement.notice.model.ocds.RenewalV2
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.toValue
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsBudget
import com.procurement.notice.model.tender.ms.MsPlanning
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.ElectronicAuctionModalities
import com.procurement.notice.model.tender.record.ElectronicAuctions
import com.procurement.notice.model.tender.record.ElectronicAuctionsDetails
import com.procurement.notice.model.tender.record.Release
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
        val storedMs = releaseService.getMs(msEntity.jsonData)

        val updatedMs = Ms(
            id = generationService.generateReleaseId(ocid = context.cpid), //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.COMPILED), //FR-MR-5.5.2.3.1
            ocid = storedMs.ocid,
            initiationType = storedMs.initiationType,
            language = storedMs.language,

            //FR-MR-5.5.2.3.6
            planning = data.planning
                .let { planning ->
                    MsPlanning(
                        rationale = planning.rationale,
                        budget = planning.budget
                            .let { budget ->
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
                                                period = budgetBreakdown.period
                                                    .let { period ->
                                                        Period(
                                                            startDate = period.startDate,
                                                            endDate = period.endDate,
                                                            maxExtentDate = null,
                                                            durationInDays = null
                                                        )
                                                    },
                                                sourceParty = budgetBreakdown.sourceParty
                                                    .let { sourceParty ->
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
                                                europeanUnionFunding = budgetBreakdown.europeanUnionFunding
                                                    ?.let { europeanUnionFunding ->
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
            tender = data.tender
                .let { tender ->
                    MsTender(
                        id = storedMs.tender.id, //FR-MR-5.5.2.3.4
                        status = storedMs.tender.status, //FR-MR-5.5.2.3.4
                        statusDetails = storedMs.tender.statusDetails, //FR-MR-5.5.2.3.4
                        title = storedMs.tender.title,
                        description = storedMs.tender.description,
                        procuringEntity = storedMs.tender.procuringEntity, //FR-MR-5.5.2.3.3
                        hasEnquiries = storedMs.tender.hasEnquiries,

                        value = tender.value.toValue(),
                        procurementMethod = tender.procurementMethod.toString(),
                        procurementMethodDetails = tender.procurementMethodDetails,
                        procurementMethodRationale = tender.procurementMethodRationale,
                        procurementMethodAdditionalInfo = tender.procurementMethodAdditionalInfo,
                        mainProcurementCategory = tender.mainProcurementCategory,
                        additionalProcurementCategories = tender.additionalProcurementCategories,
                        eligibilityCriteria = tender.eligibilityCriteria,
                        contractPeriod = tender.contractPeriod
                            .let { contractPeriod ->
                                Period(
                                    startDate = contractPeriod.startDate,
                                    endDate = contractPeriod.endDate,
                                    durationInDays = null,
                                    maxExtentDate = null
                                )
                            },
                        acceleratedProcedure = AcceleratedProcedure(
                            isAcceleratedProcedure = tender.acceleratedProcedure.isAcceleratedProcedure,
                            acceleratedProcedureJustification = null
                        ),
                        classification = tender.classification
                            .let { classification ->
                                Classification(
                                    scheme = classification.scheme,
                                    id = classification.id,
                                    description = classification.description,
                                    uri = null
                                )
                            },
                        designContest = DesignContest(
                            serviceContractAward = tender.designContest.serviceContractAward,
                            hasPrizes = null,
                            prizes = null,
                            paymentsToParticipants = null,
                            juryDecisionBinding = null,
                            juryMembers = null,
                            participants = null
                        ),
                        electronicWorkflows = tender.electronicWorkflows
                            .let { electronicWorkflows ->
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
                        legalBasis = tender.legalBasis,
                        procedureOutsourcing = ProcedureOutsourcing(
                            procedureOutsourced = tender.procedureOutsourcing.procedureOutsourced,
                            outsourcedTo = null
                        ),
                        dynamicPurchasingSystem = DynamicPurchasingSystem(
                            hasDynamicPurchasingSystem = tender.dynamicPurchasingSystem.hasDynamicPurchasingSystem,
                            hasOutsideBuyerAccess = null,
                            noFurtherContracts = null
                        ),
                        framework = Framework(
                            isAFramework = tender.framework.isAFramework,
                            typeOfFramework = null,
                            maxSuppliers = null,
                            exceptionalDurationRationale = null,
                            additionalBuyerCategories = null
                        ),

                        submissionLanguages = null,
                        amendments = null
                    )
                },
            relatedProcesses = storedMs.relatedProcesses,

            //FR-MR-5.5.2.3.7
            parties = storedMs.parties
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
                        )
                    else
                        party
                }
                .toMutableList()
        )

        val storedReleaseEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val storedReleaseEV = releaseService.getRelease(storedReleaseEntity.jsonData)

        val newReleaseId = generationService.generateReleaseId(context.ocid)
        val actualReleaseId = storedReleaseEV.id!!
        val newAmendment: ReleaseAmendment = newAmendment(
            context = context,
            data = data,
            actualReleaseID = actualReleaseId,
            newReleaseID = newReleaseId
        )
        val updatedAmendments: List<ReleaseAmendment> = storedReleaseEV.tender.amendments + newAmendment

        val updatedReleaseEV = Release(
            id = newReleaseId, //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.TENDER_AMENDMENT), //FR-ER-5.5.2.3.1
            relatedProcesses = storedReleaseEV.relatedProcesses, //FR-ER-5.5.2.3.2
            parties = storedReleaseEV.parties, //FR-ER-5.5.2.3.2
            ocid = storedReleaseEV.ocid,
            initiationType = storedReleaseEV.initiationType,
            language = storedReleaseEV.language,

            tender = data.tender
                .let { tender ->
                    ReleaseTender(
                        title = storedReleaseEV.tender.title, //FR-ER-5.5.2.3.3
                        description = storedReleaseEV.tender.description, //FR-ER-5.5.2.3.3
                        hasEnquiries = storedReleaseEV.tender.hasEnquiries, //FR-ER-5.5.2.3.3
                        enquiries = storedReleaseEV.tender.enquiries, //FR-ER-5.5.2.3.3
                        awardCriteria = storedReleaseEV.tender.awardCriteria, //FR-ER-5.5.2.3.3
                        awardCriteriaDetails = storedReleaseEV.tender.awardCriteriaDetails, //FR-ER-5.5.2.3.3
                        criteria = storedReleaseEV.tender.criteria, //FR-ER-5.5.2.3.3
                        conversions = storedReleaseEV.tender.conversions, //FR-ER-5.5.2.3.3
                        otherCriteria = storedReleaseEV.tender.otherCriteria, // FR-ER-5.5.2.3.8
                        secondStage = storedReleaseEV.tender.secondStage, //FR-ER-5.5.2.3.9

                        id = tender.id,
                        status = tender.status,
                        statusDetails = tender.statusDetails,
                        lotGroups = tender.lotGroups
                            .map { lotGroup ->
                                LotGroup(
                                    id = null,
                                    optionToCombine = lotGroup.optionToCombine,
                                    relatedLots = null,
                                    maximumValue = null
                                )
                            },
                        tenderPeriod = tender.tenderPeriod
                            ?.let { tenderPeriod ->
                                Period(
                                    startDate = tenderPeriod.startDate,
                                    endDate = tenderPeriod.endDate,
                                    maxExtentDate = null,
                                    durationInDays = null
                                )
                            }
                            ?: storedReleaseEV.tender.tenderPeriod,
                        enquiryPeriod = tender.enquiryPeriod
                            ?.let { enquiryPeriod ->
                                Period(
                                    startDate = enquiryPeriod.startDate,
                                    endDate = enquiryPeriod.endDate,
                                    maxExtentDate = null,
                                    durationInDays = null
                                )
                            }
                            ?: storedReleaseEV.tender.enquiryPeriod,
                        submissionMethod = tender.submissionMethod.toList(),
                        submissionMethodDetails = tender.submissionMethodDetails,
                        submissionMethodRationale = tender.submissionMethodRationale.toList(),
                        requiresElectronicCatalogue = tender.requiresElectronicCatalogue,
                        procurementMethodModalities = getProcurementMethodModalities(
                            context = context,
                            data = data,
                            previous = storedReleaseEV.tender.procurementMethodModalities
                        ),
                        lots = tender.lots
                            .map { lot ->
                                Lot(
                                    id = lot.id,
                                    internalId = lot.internalId,
                                    title = lot.title,
                                    description = lot.description,
                                    classification = null,
                                    status = lot.status,
                                    statusDetails = lot.statusDetails,
                                    value = lot.value.toValue(),
                                    hasOptions = lot.hasOptions,
                                    options = lot.options
                                        .map { option ->
                                            Option(
                                                hasOptions = null,
                                                optionDetails = null,
                                                description = option.description,
                                                period = option.period
                                                    ?.let { period ->
                                                        Period(
                                                            startDate = period.startDate,
                                                            endDate = period.endDate,
                                                            maxExtentDate = period.maxExtentDate,
                                                            durationInDays = period.durationInDays
                                                        )
                                                    }
                                            )
                                        },
                                    hasRenewal = lot.hasRenewal,
                                    renewal = lot.renewal
                                        ?.let { renewal ->
                                            RenewalV2(
                                                description = renewal.description,
                                                minimumRenewals = renewal.minimumRenewals,
                                                maximumRenewals = renewal.maximumRenewals,
                                                period = renewal.period
                                                    ?.let { period ->
                                                        Period(
                                                            startDate = period.startDate,
                                                            endDate = period.endDate,
                                                            durationInDays = period.durationInDays,
                                                            maxExtentDate = period.maxExtentDate
                                                        )
                                                    }
                                            )
                                        },
                                    hasRecurrence = lot.hasRecurrence,
                                    recurrence = lot.recurrence
                                        ?.let { recurrence ->
                                            Recurrence(
                                                description = recurrence.description,
                                                dates = recurrence.dates
                                                    .map { date ->
                                                        Recurrence.Date(date.startDate)
                                                    }
                                            )
                                        },
                                    contractPeriod = lot.contractPeriod
                                        .let { contractPeriod ->
                                            Period(
                                                startDate = contractPeriod.startDate,
                                                endDate = contractPeriod.endDate,
                                                durationInDays = null,
                                                maxExtentDate = null
                                            )
                                        },
                                    placeOfPerformance = lot.placeOfPerformance
                                        ?.let { placeOfPerformance ->
                                            PlaceOfPerformance(
                                                address = placeOfPerformance.address
                                                    .let { address ->
                                                        Address(
                                                            streetAddress = address.streetAddress,
                                                            postalCode = address.postalCode,
                                                            addressDetails = address.addressDetails
                                                                .let { addressDetails ->
                                                                    AddressDetails(
                                                                        country = addressDetails.country
                                                                            .let { country ->
                                                                                CountryDetails(
                                                                                    scheme = country.scheme,
                                                                                    id = country.id,
                                                                                    description = country.description,
                                                                                    uri = country.uri
                                                                                )
                                                                            },
                                                                        region = addressDetails.region
                                                                            .let { region ->
                                                                                RegionDetails(
                                                                                    scheme = region.scheme,
                                                                                    id = region.id,
                                                                                    description = region.description,
                                                                                    uri = region.uri
                                                                                )
                                                                            },
                                                                        locality = addressDetails.locality
                                                                            .let { locality ->
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
                            },
                        items = tender.items
                            .map { item ->
                                Item(
                                    id = item.id,
                                    internalId = item.internalId,
                                    classification = item.classification
                                        .let { classification ->
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
                                        },
                                    quantity = item.quantity,
                                    unit = item.unit
                                        .let { unit ->
                                            com.procurement.notice.model.ocds.Unit(
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
                            },
                        documents = tender.documents
                            .map { document ->
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
                            },
                        amendments = updatedAmendments, //FR-ER-5.5.2.3.4

                        //FR-ER-5.5.2.3.6
                        auctionPeriod = getAuctionPeriod(
                            context = context,
                            data = data,
                            previous = storedReleaseEV.tender.auctionPeriod
                        ),

                        //FR-ER-5.5.2.3.6
                        electronicAuctions = getElectronicAuctions(
                            context = context,
                            data = data,
                            previous = storedReleaseEV.tender.electronicAuctions
                        ),

                        procurementMethodRationale = null,
                        awardPeriod = null,
                        standstillPeriod = null,
                        value = null,
                        classification = null,
                        targets = emptyList(),
                        procuringEntity = null
                    )
                },
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
                ?: storedReleaseEV.preQualification,
            awards = storedReleaseEV.awards,
            hasPreviousNotice = storedReleaseEV.hasPreviousNotice,
            purposeOfNotice = storedReleaseEV.purposeOfNotice,

            bids = null,
            contracts = emptyList(),
            qualifications = emptyList(),
            submissions = null,
            invitations = emptyList()
        )

        releaseService.saveMs(cpId = context.cpid, ms = updatedMs, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            release = updatedReleaseEV,
            publishDate = storedReleaseEntity.publishDate
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
                            },
                            electronicAuctionProgress = null,
                            electronicAuctionResult = null
                        )
                    }
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
        val receivedMs = releaseService.getMs(data)
        val receivedTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val storedMs = releaseService.getMs(msEntity.jsonData)

        val updatedTenderMs = storedMs.tender
            .let{ tender ->
                MsTender(
                    id = tender.id,
                    status = tender.status,
                    statusDetails = tender.statusDetails,
                    value = tender.value,
                    procurementMethod = tender.procurementMethod,
                    procurementMethodDetails = tender.procurementMethodDetails,
                    mainProcurementCategory = tender.mainProcurementCategory,
                    hasEnquiries = tender.hasEnquiries,
                    eligibilityCriteria = tender.eligibilityCriteria,
                    contractPeriod = tender.contractPeriod,
                    acceleratedProcedure = tender.acceleratedProcedure,
                    classification = tender.classification,
                    designContest = tender.designContest,
                    electronicWorkflows = tender.electronicWorkflows,
                    jointProcurement = tender.jointProcurement,
                    legalBasis = tender.legalBasis,
                    procedureOutsourcing = tender.procedureOutsourcing,
                    dynamicPurchasingSystem = tender.dynamicPurchasingSystem,
                    framework = tender.framework,
                    procuringEntity = tender.procuringEntity,

                    title = receivedMs.tender.title,
                    description = receivedMs.tender.description,
                    procurementMethodRationale = receivedMs.tender.procurementMethodRationale,
                    procurementMethodAdditionalInfo = receivedMs.tender.procurementMethodAdditionalInfo,

                    additionalProcurementCategories = emptyList(),
                    amendments = emptyList(),
                    submissionLanguages = emptyList()
                )
            }

        val updatedMs = Ms(
            id = generationService.generateReleaseId(cpid),
            date = releaseDate,
            initiationType = storedMs.initiationType,
            ocid = storedMs.ocid,
            language = storedMs.language,
            tag = listOf(Tag.COMPILED),
            planning = receivedMs.planning,
            tender = updatedTenderMs,
            parties = storedMs.parties,
            relatedProcesses = storedMs.relatedProcesses
        )

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val storedRelease: Release = releaseService.getRelease(recordEntity.jsonData)
        val updatedTender = storedRelease.tender
            .let{ tender ->
                ReleaseTender(
                    id = tender.id,
                    title = tender.title,
                    description = tender.description,
                    status = tender.status,
                    statusDetails = tender.statusDetails,
                    lotGroups = tender.lotGroups,
                    hasEnquiries = tender.hasEnquiries,
                    submissionMethod = tender.submissionMethod,
                    submissionMethodDetails = tender.submissionMethodDetails,
                    submissionMethodRationale = tender.submissionMethodRationale,
                    requiresElectronicCatalogue = tender.requiresElectronicCatalogue,

                    tenderPeriod = receivedTender.tenderPeriod,
                    lots = receivedTender.lots,
                    items = receivedTender.items,
                    documents = receivedTender.documents,

                    criteria = emptyList(),
                    conversions = emptyList(),
                    otherCriteria = null,
                    enquiryPeriod = null,
                    standstillPeriod = null,
                    awardPeriod = null,
                    auctionPeriod = null,
                    enquiries = mutableListOf(),
                    amendments = emptyList(),
                    awardCriteria = null,
                    awardCriteriaDetails = null,
                    procurementMethodModalities = emptyList(),
                    electronicAuctions = null,
                    secondStage = null,
                    procurementMethodRationale = null,
                    classification = null,
                    value = null,
                    targets = emptyList(),
                    procuringEntity = null
                )
            }

        val updatedRelease = Release(
            id = generationService.generateReleaseId(ocid),
            tag = listOf(Tag.PLANNING_UPDATE),
            hasPreviousNotice = storedRelease.hasPreviousNotice,
            purposeOfNotice = storedRelease.purposeOfNotice,
            ocid = storedRelease.ocid,
            initiationType = storedRelease.initiationType,
            language = storedRelease.language,
            date = releaseDate,
            relatedProcesses = storedRelease.relatedProcesses,
            parties = storedRelease.parties,
            preQualification = null,
            awards = emptyList(),
            bids = null,
            contracts = emptyList(),
            qualifications = emptyList(),
            submissions = null,
            invitations = emptyList(),
            tender = updatedTender
        )

        releaseService.saveMs(cpId = cpid, ms = updatedMs, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = updatedRelease, publishDate = recordEntity.publishDate)
        val amendmentsIds = null
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    fun updateAp(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val receivedMS = releaseService.getMs(data)
        val receivedRawRelease = releaseService.getRecordTender(data)

        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val storedMS = releaseService.getMs(msEntity.jsonData)

        val updatedReceivedMs = receivedMS.copy(
            tender = receivedMS.tender.copy(
                id = storedMS.tender.id,
                status = storedMS.tender.status,
                statusDetails = storedMS.tender.statusDetails,
                procuringEntity = storedMS.tender.procuringEntity,
                hasEnquiries = storedMS.tender.hasEnquiries
            )
        )

        val updatedMs = storedMS.copy(
            id = generationService.generateReleaseId(cpid),
            date = releaseDate,
            planning = updatedReceivedMs.planning,
            tender = updatedReceivedMs.tender,
            parties = mutableListOf()
        )

        /* release */
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val storedRelease = releaseService.getRelease(recordEntity.jsonData)
        val storedItemsById = storedRelease.tender.items.associateBy { it.id }

        val updatedReceivedRawRelease = receivedRawRelease.copy(
            title = storedRelease.tender.title,
            description = storedRelease.tender.description,
            items = receivedRawRelease.items.map {  item ->
                item.copy(
                    deliveryAddress = updateDeliveryAddress(
                        receivedAddress = item.deliveryAddress,
                        storedAddress = storedItemsById[item.id]?.deliveryAddress
                    )
                )
            }
        )
        val updatedRelease = storedRelease.copy(
            id = generationService.generateReleaseId(ocid),
            date = releaseDate,
            tag = listOf(Tag.PLANNING_UPDATE),
            tender = updatedReceivedRawRelease
        )
        releaseService.saveMs(cpId = cpid, ms = updatedMs, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = updatedRelease, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateDeliveryAddress(receivedAddress: Address?, storedAddress: Address?) =
        receivedAddress?.copy(
            streetAddress = receivedAddress.streetAddress ?: storedAddress?.streetAddress,
            postalCode = receivedAddress.postalCode ?: storedAddress?.postalCode,
            addressDetails = receivedAddress.addressDetails?.copy(
                locality = receivedAddress.addressDetails.locality ?: storedAddress?.addressDetails?.locality
            )
        )
            ?: storedAddress

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



