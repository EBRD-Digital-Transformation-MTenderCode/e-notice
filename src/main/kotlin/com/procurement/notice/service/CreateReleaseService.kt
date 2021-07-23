package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.fe.amend.AmendFeContext
import com.procurement.notice.application.service.fe.create.CreateFeContext
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.lib.toSetBy
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PurposeOfNotice
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderDescription
import com.procurement.notice.model.ocds.TenderTitle
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.model.tender.record.ReleaseTender
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreateReleaseService(
    private val budgetService: BudgetService,
    private val organizationService: OrganizationService,
    private val relatedProcessService: RelatedProcessService,
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) {

    fun createCnPnPin(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode,
        language: String,
        operation: Operation
    ): ResponseDto {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val receivedMs = releaseService.getMs(data)
        val params = releaseService.getParamsForCreateCnPnPin(operation, Stage.valueOf(stage.toUpperCase()))
        val ms = receivedMs.copy(
            ocid = cpid,
            date = releaseDate,
            id = generationService.generateReleaseId(cpid),
            tag = listOf(Tag.COMPILED),
            language = language,
            initiationType = InitiationType.TENDER,
            tender = receivedMs.tender
                .let { tender ->
                    MsTender(
                        id = tender.id,
                        title = tender.title,
                        description = tender.description,
                        status = tender.status,
                        statusDetails = params.statusDetails,
                        value = tender.value,
                        procurementMethod = tender.procurementMethod,
                        procurementMethodDetails = tender.procurementMethodDetails,
                        procurementMethodRationale = tender.procurementMethodRationale,
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
                        procurementMethodAdditionalInfo = tender.procurementMethodAdditionalInfo,
                        dynamicPurchasingSystem = tender.dynamicPurchasingSystem,
                        framework = tender.framework,
                        procuringEntity = tender.procuringEntity
                            ?.let { procuringEntity ->
                                OrganizationReference(
                                    id = procuringEntity.id,
                                    name = procuringEntity.name,
                                    identifier = null,
                                    address = null,
                                    additionalIdentifiers = null,
                                    contactPoint = null,
                                    details = null,
                                    buyerProfile = null,
                                    persones = null
                                )
                            },
                        additionalProcurementCategories = emptyList(),
                        amendments = emptyList(),
                        submissionLanguages = emptyList()
                    )
                }
        )
        organizationService.processMsParties(ms, checkFs)

        val receivedRelease = releaseService.getRelease(data)
        val release = receivedRelease.copy(
            date = releaseDate,
            ocid = ocid,
            id = generationService.generateReleaseId(ocid),
            tag = params.tag,
            language = language,
            initiationType = InitiationType.TENDER,
            hasPreviousNotice = false,
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = params.isACallForCompetition),
            tender = receivedRelease.tender
                .let {tender->
                ReleaseTender(
                    id = tender.id,
                    title = tender.title,
                    description = tender.description,
                    status = tender.status,
                    statusDetails = tender.statusDetails,
                    lotGroups = tender.lotGroups,
                    tenderPeriod = tender.tenderPeriod,
                    hasEnquiries = tender.hasEnquiries,
                    submissionMethod = tender.submissionMethod,
                    submissionMethodDetails = tender.submissionMethodDetails,
                    submissionMethodRationale = tender.submissionMethodRationale,
                    requiresElectronicCatalogue = tender.requiresElectronicCatalogue,
                    lots = tender.lots,
                    items = tender.items,
                    documents = tender.documents,
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
        )
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms = ms, checkFs = checkFs, ocId = ocid, processType = params.relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(release = release, cpId = cpid)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = releaseDate.toDate())
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = releaseDate.toDate())
        budgetService.createEiByMs(eiIds = checkFs.ei, msCpId = cpid, dateTime = releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns = budgetBreakdowns, msCpId = cpid, dateTime = releaseDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun createAp(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode,
        language: String,
        operation: Operation
    ): ResponseDto {
        val rawMs = releaseService.getMs(data)
        val compiledMs = rawMs.copy(
            ocid = cpid,
            date = releaseDate,
            id = generationService.generateReleaseId(cpid),
            tag = listOf(Tag.COMPILED), // BR-BR-4.76
            language = language, //  BR-4.263
            initiationType = InitiationType.TENDER,  // BR-4.75
            parties = mutableListOf(),
            tender = rawMs.tender.copy(
                procuringEntity = rawMs.tender.procuringEntity?.let { clearOrganizationReference(it) },
                statusDetails = TenderStatusDetails.AGGREGATE_PLANNING // BR-4.66
            )
        )
        val rawRelease = releaseService.getRelease(data)
        val compiledRelease = rawRelease.copy(
            date = releaseDate, // BR-4.256
            ocid = ocid,
            id = generationService.generateReleaseId(ocid),
            tag = listOf(Tag.PLANNING),    // BR-4.48
            language = language, // BR-4.267
            initiationType = InitiationType.TENDER,   // BR-4.74
            hasPreviousNotice = false,   // BR-4.50
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = false)  // BR-4.51
        )
        relatedProcessService.addApRecordRelatedProcessToMs(
            ms = compiledMs,
            ocId = ocid,
            processType = RelatedProcessType.AGGREGATE_PLANNING
        )
        relatedProcessService.addMsRelatedProcessToRecord(release = compiledRelease, cpId = cpid)
        releaseService.saveMs(cpId = cpid, ms = compiledMs, publishDate = releaseDate.toDate())
        releaseService.saveRecord(cpId = cpid, stage = stage, release = compiledRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun createPinOnPn(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
            tender.procuringEntity = prevProcuringEntity
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val newOcId = generationService.generateOcid(cpid = cpid, stage = stage)
        val newRelease = release.copy(
                id = generationService.generateReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.PLANNING),
                tender = recordTender.copy(
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        description = TenderDescription.valueOf(stage.toUpperCase()).text
                ),
                initiationType = InitiationType.TENDER,
                hasPreviousNotice = true,
                purposeOfNotice = PurposeOfNotice(false)
        )
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = RelatedProcessType.X_PLANNED)
        relatedProcessService.addRecordRelatedProcessToRecord(release = newRelease, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, release = release, publishDate = recordEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = newRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

    fun createCnOnPn(
        cpid: String,
        ocid: String,
        ocidCn: String,
        stage: String,
        prevStage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val receivedMsTender: MsTender = releaseService.getMsTender(data = data)
        val receivedRelease: Release = releaseService.getRelease(data = data)
        val receivedReleaseTender: ReleaseTender = receivedRelease.tender

        val storedMsEntity = releaseService.getMsEntity(cpid = cpid)
        val storedMs = releaseService.getMs(data = storedMsEntity.jsonData)
        val storedMsTender = storedMs.tender

        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        val updatedMS = Ms(
            id = generationService.generateReleaseId(ocid = cpid),
            date = releaseDate,
            tag = listOf(Tag.COMPILED),
            planning = storedMs.planning,
            parties = releaseService.getPartiesWithActualPersones(receivedMsTender.procuringEntity!!, storedMs.parties),
            relatedProcesses = storedMs.relatedProcesses,
            ocid = storedMs.ocid,
            initiationType = storedMs.initiationType,

            tender = MsTender(
                id = storedMsTender.id,
                title = storedMsTender.title,
                description = storedMsTender.description,
                procuringEntity = storedMsTender.procuringEntity,

                status = receivedMsTender.status,
                statusDetails = TenderStatusDetails.EVALUATION,
                procurementMethod = receivedMsTender.procurementMethod,
                procurementMethodDetails = receivedMsTender.procurementMethodDetails,
                procurementMethodRationale = receivedMsTender.procurementMethodRationale,
                procurementMethodAdditionalInfo = receivedMsTender.procurementMethodAdditionalInfo,
                mainProcurementCategory = receivedMsTender.mainProcurementCategory,
                additionalProcurementCategories = receivedMsTender.additionalProcurementCategories,
                hasEnquiries = receivedMsTender.hasEnquiries,
                eligibilityCriteria = receivedMsTender.eligibilityCriteria,
                contractPeriod = receivedMsTender.contractPeriod,
                acceleratedProcedure = receivedMsTender.acceleratedProcedure,
                classification = receivedMsTender.classification,
                designContest = receivedMsTender.designContest,
                electronicWorkflows = receivedMsTender.electronicWorkflows,
                jointProcurement = receivedMsTender.jointProcurement,
                legalBasis = receivedMsTender.legalBasis,
                procedureOutsourcing = receivedMsTender.procedureOutsourcing,
                dynamicPurchasingSystem = receivedMsTender.dynamicPurchasingSystem,
                framework = receivedMsTender.framework,
                value = receivedMsTender.value,

                amendments = emptyList(),
                submissionLanguages = emptyList()
            ),

            language = null
        )

        val releasePNEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val storedReleasePN = releaseService.getRelease(data = releasePNEntity.jsonData)

        val updatedReleasePN = Release (
            id = generationService.generateReleaseId(ocid),
            date = releaseDate,
            tag = listOf(Tag.PLANNING_UPDATE),

            ocid = storedReleasePN.ocid,
            initiationType = storedReleasePN.initiationType,
            hasPreviousNotice = storedReleasePN.hasPreviousNotice,
            purposeOfNotice = storedReleasePN.purposeOfNotice,
            relatedProcesses = storedReleasePN.relatedProcesses,
            parties = storedReleasePN.parties,
            tender = storedReleasePN.tender.copy(
                status = TenderStatus.COMPLETE,
                statusDetails = TenderStatusDetails.EMPTY
            ),

            language = null,
            awards = emptyList(),
            bids = null,
            contracts = emptyList(),
            qualifications = emptyList(),
            submissions = null,
            invitations = emptyList(),
            preQualification = null
        )

        val newRelease = Release(
            id = generationService.generateReleaseId(ocidCn),
            ocid = ocidCn,
            date = releaseDate,
            tag = listOf(Tag.TENDER),
            initiationType = InitiationType.TENDER,
            hasPreviousNotice = true,
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = true),

            preQualification = receivedRelease.preQualification,
            tender = ReleaseTender(
                //FR-ER-5.5.2.2.7
                title = TenderTitle.valueOf(stage.toUpperCase()).text,
                //FR-ER-5.5.2.2.8
                description = TenderDescription.valueOf(stage.toUpperCase()).text,
                hasEnquiries = false,

                id = receivedReleaseTender.id,
                status = receivedReleaseTender.status,
                statusDetails = receivedReleaseTender.statusDetails,
                lotGroups = receivedReleaseTender.lotGroups,
                tenderPeriod = receivedReleaseTender.tenderPeriod,
                enquiryPeriod = receivedReleaseTender.enquiryPeriod,
                auctionPeriod = receivedReleaseTender.auctionPeriod,
                submissionMethod = receivedReleaseTender.submissionMethod,
                submissionMethodDetails = receivedReleaseTender.submissionMethodDetails,
                submissionMethodRationale = receivedReleaseTender.submissionMethodRationale,
                requiresElectronicCatalogue = receivedReleaseTender.requiresElectronicCatalogue,
                awardCriteria = receivedReleaseTender.awardCriteria,
                awardCriteriaDetails = receivedReleaseTender.awardCriteriaDetails,
                procurementMethodModalities = receivedReleaseTender.procurementMethodModalities,
                electronicAuctions = receivedReleaseTender.electronicAuctions,
                lots = receivedReleaseTender.lots,
                items = receivedReleaseTender.items,
                criteria = receivedReleaseTender.criteria,
                conversions = receivedReleaseTender.conversions,
                documents = receivedReleaseTender.documents,
                otherCriteria = receivedReleaseTender.otherCriteria,

                classification = null, // FR-ER-5.5.2.2.10
                value = null,  // FR-ER-5.5.2.2.10
                standstillPeriod = null,
                awardPeriod = null,
                enquiries = mutableListOf(),
                amendments = emptyList(),
                secondStage = null,
                procurementMethodRationale = null,
                targets = emptyList(),
                procuringEntity = null
            ),

            parties = mutableListOf(),
            language = null,
            awards = emptyList(),
            bids = null,
            contracts = emptyList(),
            relatedProcesses = mutableListOf(),
            qualifications = emptyList(),
            submissions = null,
            invitations = emptyList()
        )
        //FR-MR-5.5.2.2.5
        relatedProcessService.addRecordRelatedProcessToMs(
            ms = updatedMS,
            ocid = ocidCn,
            processType = params.relatedProcessType
        )
        relatedProcessService.addRecordRelatedProcessToRecord(
            release = newRelease,
            ocId = ocid,
            cpId = cpid,
            processType = RelatedProcessType.PLANNING
        )

        releaseService.saveMs(cpId = cpid, ms = updatedMS, publishDate = storedMsEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, release = updatedReleasePN, publishDate = releasePNEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = newRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocidCn))
    }

    fun createNegotiationCnOnPn(
        cpid: String,
        ocid: String,
        ocidCn: String,
        stage: String,
        prevStage: String,
        operation: Operation,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(
            stage = Stage.valueOf(stage.toUpperCase()),
            operation = operation
        )
        ms.apply {
            id = generationService.generateReleaseId(cpid) //BR-2.4.16.24
            date = releaseDate //BR-2.4.16.25
            tag = listOf(Tag.COMPILED) //BR-2.4.16.23
            tender = msTender //BR-2.4.16.19
            tender.statusDetails = params.statusDetails //BR-2.4.16.20
            tender.procuringEntity = prevProcuringEntity //BR-2.4.16.21
            tender.hasEnquiries = false
        }

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val releasePN = releaseService.getRelease(recordEntity.jsonData)
        releasePN.apply {
            id = generationService.generateReleaseId(ocid) //BR-2.4.16.16
            date = releaseDate //BR-2.4.16.15
            tag = listOf(Tag.PLANNING_UPDATE) //BR-2.4.16.13
            tender.status = TenderStatus.COMPLETE //BR-2.4.16.18
            tender.statusDetails = TenderStatusDetails.EMPTY //BR-2.4.16.18
        }
        releaseService.saveRecord(cpId = cpid, stage = prevStage, release = releasePN, publishDate = recordEntity.publishDate)

        val releaseNP = releasePN.copy(
            ocid = ocidCn, //BR-2.4.16.6
            id = generationService.generateReleaseId(ocidCn), //BR-2.4.16.7
            date = releaseDate, //BR-2.4.16.8
            tag = listOf(Tag.TENDER), //BR-2.4.16.3
            tender = recordTender.copy( //BR-2.4.16.11
                title = TenderTitle.valueOf(stage.toUpperCase()).text, //BR-2.4.16.9
                description = TenderDescription.valueOf(stage.toUpperCase()).text, //BR-2.4.16.10
                hasEnquiries = false
            ),
            initiationType = InitiationType.TENDER, //BR-2.4.16.5
            hasPreviousNotice = true, //BR-2.4.16.1
            purposeOfNotice = PurposeOfNotice(true), //BR-2.4.16.4
            parties = mutableListOf() //BR-2.4.16.12
        )
        //BR-2.4.16.26
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = ocidCn, processType = params.relatedProcessType)

        //BR-2.4.16.2
        relatedProcessService.addRecordRelatedProcessToRecord(release = releaseNP, ocId = ocidCn, cpId = cpid, processType = RelatedProcessType.PLANNING)

        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = releaseNP, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocidCn))
    }

    fun createCnOnPin(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        releaseService.saveRecord(cpId = cpid, stage = prevStage, release = release, publishDate = recordEntity.publishDate)
        val newOcId = generationService.generateOcid(cpid = cpid, stage = stage)
        val newRelease = release.copy(
                id = generationService.generateReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.TENDER),
                tender = recordTender.copy(
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        description = TenderDescription.valueOf(stage.toUpperCase()).text,
                        hasEnquiries = false
                ),
                initiationType = InitiationType.TENDER,
                hasPreviousNotice = true,
                purposeOfNotice = PurposeOfNotice(true)
        )
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(release = newRelease, ocId = ocid, cpId = cpid, processType = RelatedProcessType.X_PLANNED)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = newRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

    fun createFe(context: CreateFeContext, data: JsonNode): ResponseDto {
        val feRelease = getFeReleaseForCreateFe(data, context)

        val apEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val apRelease = getApReleaseForCreateFe(context, apEntity)

        val msEntity = releaseService.getMsEntity(cpid = context.cpid)
        val msRelease = getMsReleaseForCreateFe(data, context, msEntity)

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            release = feRelease,
            publishDate = context.releaseDate.toDate()
        )

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.prevStage,
            release = apRelease,
            publishDate = apEntity.publishDate
        )

        releaseService.saveMs(cpId = context.cpid, ms = msRelease, publishDate =  msEntity.publishDate)

        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    private fun getApReleaseForCreateFe(
        context: CreateFeContext,
        recordEntity: ReleaseEntity
    ) : Release{
        val storedAp = releaseService.getRelease(recordEntity.jsonData)

        return storedAp.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.11
            tag = listOf(Tag.PLANNING_UPDATE),
            //FR.COM-3.2.14
            tender = storedAp.tender.copy(
                status = TenderStatus.PLANNED,
                statusDetails = TenderStatusDetails.AGGREGATED
            )
        )
    }

    private fun getMsReleaseForCreateFe(
        data: JsonNode,
        context: CreateFeContext,
        msEntity: ReleaseEntity
    ) : Ms{
        val storedMs = releaseService.getMs(data = msEntity.jsonData)
        val receivedTender = releaseService.getMsTender(data)

        val compiledMs = storedMs.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.15
            tag = listOf(Tag.COMPILED),
            //FR.COM-3.2.18
            tender = receivedTender.copy(
                //FR.COM-3.2.16
                statusDetails = TenderStatusDetails.ESTABLISHMENT,
                //FR.COM-3.2.17
                procuringEntity = null,
                hasEnquiries = storedMs.tender.hasEnquiries
            ),
            parties = mutableListOf()
        )
        //FR.COM-3.2.19
        relatedProcessService.addRecordRelatedProcessToMs(
            ms = compiledMs,
            ocid = context.ocidCn,
            processType = RelatedProcessType.X_ESTABLISHMENT
        )

        return compiledMs
    }

    private fun getFeReleaseForCreateFe(
        data: JsonNode,
        context: CreateFeContext
    ): Release {
        val receivedFe = releaseService.getRelease(data)

        val updatedFe = receivedFe.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocidCn),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.1
            hasPreviousNotice = true,
            //FR.COM-3.2.2
            tag = listOf(Tag.TENDER),
            //FR.COM-3.2.3
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = true),
            //FR.COM-3.2.4
            initiationType = InitiationType.TENDER,
            //FR.COM-3.2.5
            ocid = context.ocidCn,
            //FR.COM-3.2.7
            tender = receivedFe.tender.copy(hasEnquiries = false)
        )

        return updatedFe
    }

    fun amendFe(context: AmendFeContext, data: JsonNode) : ResponseDto{
        val feEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val feRelease = getFeReleaseForAmendFe(data, context, feEntity)

        val msEntity = releaseService.getMsEntity(cpid = context.cpid)
        val msRelease = getMsReleaseForAmendFe(data, context, msEntity)

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            release = feRelease,
            publishDate = feEntity.publishDate
        )

        releaseService.saveMs(cpId = context.cpid, ms = msRelease, publishDate =  msEntity.publishDate)

        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    private fun getFeReleaseForAmendFe(
        data: JsonNode,
        context: AmendFeContext,
        recordEntity: ReleaseEntity
    ): Release {
        val receivedData = releaseService.getRelease(data)
        val receivedTender = receivedData.tender

        val storedFe = releaseService.getRelease(recordEntity.jsonData)
        val storedTender = storedFe.tender

        val updatedDocuments = getUpdatedDocuments(receivedTender.documents, storedTender.documents)

        return storedFe.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.1
            tag = listOf(Tag.TENDER_AMENDMENT),
            //FR.COM-3.2.4
            tender = storedTender.copy(
                title = receivedTender.title,
                description = receivedTender.description,
                procurementMethodRationale = receivedTender.procurementMethodRationale ?: storedTender.procurementMethodRationale,
                documents = updatedDocuments,
                enquiryPeriod = receivedTender.enquiryPeriod ?: storedTender.enquiryPeriod,
                procuringEntity = receivedTender.procuringEntity
            ),
            parties = receivedData.parties,
            //FR.COM-3.2.5
            preQualification = receivedData.preQualification ?: storedFe.preQualification
        )
    }

    private fun getUpdatedDocuments(
        receivedDocuments: List<Document>,
        storedDocuments: List<Document>
    ): List<Document> {
        val receivedDocumentsById = receivedDocuments.associateBy { it.id }
        val updatedStoredDocuments = storedDocuments
            .map { storedDocument ->
                receivedDocumentsById[storedDocument.id]
                    ?.let { receivedDocument ->
                        storedDocument.copy(
                            title = receivedDocument.title ?: storedDocument.title,
                            description = receivedDocument.description ?: storedDocument.description
                        )
                    }
                    ?: storedDocument
            }

        val newDocumentsIds = receivedDocumentsById.keys - storedDocuments.toSetBy { it.id }
        val newDocuments = newDocumentsIds.map { receivedDocumentsById.getValue(it) }

        return updatedStoredDocuments + newDocuments
    }

    private fun getMsReleaseForAmendFe(
        data: JsonNode,
        context: AmendFeContext,
        msEntity: ReleaseEntity
    ) : Ms {
        val storedMs = releaseService.getMs(data = msEntity.jsonData)
        val storedTender = storedMs.tender
        val receivedTender = releaseService.getMsTender(data)

        val compiledMs = storedMs.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.6
            tag = listOf(Tag.COMPILED),
            tender = receivedTender.copy(
                //FR.COM-3.2.7
                status = storedTender.status,
                statusDetails = storedTender.statusDetails,
                id = storedTender.id,
                hasEnquiries = storedTender.hasEnquiries,
                procuringEntity = null
            ),
            parties = mutableListOf()
        )

        return compiledMs
    }

    fun clearOrganizationReference(requestProcuringEntity: OrganizationReference): OrganizationReference =
        OrganizationReference(
            id = requestProcuringEntity.id,
            name = requestProcuringEntity.name,
            identifier = null,
            persones = null,
            details = null,
            buyerProfile = null,
            address = null,
            additionalIdentifiers = null,
            contactPoint = null
        )

}
