package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.fe.create.CreateFeContext
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Operation
import com.procurement.notice.model.ocds.PurposeOfNotice
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderDescription
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.TenderTitle
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Release
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
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode,
        operation: Operation
    ): ResponseDto {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = releaseService.getMs(data)
        val params = releaseService.getParamsForCreateCnPnPin(operation, Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            ocid = cpid
            date = releaseDate
            id = generationService.generateReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            tender.statusDetails = params.statusDetails
            organizationService.processMsParties(this, checkFs)
        }
        val release = releaseService.getRelease(data)
        val ocId = generationService.generateOcid(cpid = cpid, stage = stage)
        release.apply {
            date = releaseDate
            ocid = ocId
            id = generationService.generateReleaseId(ocId)
            tag = params.tag
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            hasPreviousNotice = false
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = params.isACallForCompetition)
        }
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms = ms, checkFs = checkFs, ocId = ocId, processType = params.relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(release = release, cpId = cpid)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = releaseDate.toDate())
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = releaseDate.toDate())
        budgetService.createEiByMs(eiIds = checkFs.ei, msCpId = cpid, dateTime = releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns = budgetBreakdowns, msCpId = cpid, dateTime = releaseDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocId))
    }

    fun createAp(
        cpid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode,
        operation: Operation
    ): ResponseDto {
        val rawMs = releaseService.getMs(data)
        val compiledMs = rawMs.copy(
            ocid = cpid,
            date = releaseDate,
            id = generationService.generateReleaseId(cpid),
            tag = listOf(Tag.COMPILED), // BR-BR-4.76
            initiationType = InitiationType.TENDER,  // BR-4.75
            tender = rawMs.tender.copy(
                statusDetails = TenderStatusDetails.AGGREGATE_PLANNING // BR-4.66
            )
        )
        val rawRelease = releaseService.getRelease(data)
        val ocId = generationService.generateOcid(cpid = cpid, stage = stage)
        val compiledRelease = rawRelease.copy(
            date = releaseDate, // BR-4.256
            ocid = ocId,
            id = generationService.generateReleaseId(ocId),
            tag = listOf(Tag.PLANNING),    // BR-4.48
            initiationType = InitiationType.TENDER,   // BR-4.74
            hasPreviousNotice = false,   // BR-4.50
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = false)  // BR-4.51
        )
        relatedProcessService.addApRecordRelatedProcessToMs(
            ms = compiledMs,
            ocId = ocId,
            processType = RelatedProcessType.AGGREGATE_PLANNING
        )
        relatedProcessService.addMsRelatedProcessToRecord(release = compiledRelease, cpId = cpid)
        releaseService.saveMs(cpId = cpid, ms = compiledMs, publishDate = releaseDate.toDate())
        releaseService.saveRecord(cpId = cpid, stage = stage, release = compiledRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocId))
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
        val msTender = releaseService.getMsTender(data = data)
        val receivedRelease: Release = releaseService.getRelease(data = data)
        val msEntity = releaseService.getMsEntity(cpid = cpid)
        val ms = releaseService.getMs(data = msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        val updatedMS = ms.copy(
            id = generationService.generateReleaseId(ocid = cpid),
            date = releaseDate,
            tag = listOf(Tag.COMPILED),
            parties = releaseService.getPartiesWithActualPersones(msTender.procuringEntity!!, ms.parties),
            tender = msTender.copy(
                statusDetails = params.statusDetails,
                procuringEntity = prevProcuringEntity,
                hasEnquiries = false
            )
        )
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(data = recordEntity.jsonData)
        val updatedRelease = release.copy (
            id = generationService.generateReleaseId(ocid),
            date = releaseDate,
            tag = listOf(Tag.PLANNING_UPDATE),
            tender = release.tender.copy(
                status = TenderStatus.COMPLETE,
                statusDetails = TenderStatusDetails.EMPTY
            )
        )
        releaseService.saveRecord(
            cpId = cpid,
            stage = prevStage,
            release = updatedRelease,
            publishDate = recordEntity.publishDate
        )

        val newRelease = updatedRelease.copy(
            ocid = ocidCn,
            id = generationService.generateReleaseId(ocidCn),
            date = releaseDate,
            tag = listOf(Tag.TENDER),
            tender = receivedRelease.tender.copy(
                        //FR-ER-5.5.2.2.7
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        //FR-ER-5.5.2.2.8
                        description = TenderDescription.valueOf(stage.toUpperCase()).text,
                        hasEnquiries = false
                ),
            initiationType = InitiationType.TENDER,
            hasPreviousNotice = true,
            purposeOfNotice = PurposeOfNotice(true),
            parties = mutableListOf(),
            preQualification = receivedRelease.preQualification
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
        releaseService.saveMs(cpId = cpid, ms = updatedMS, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = newRelease, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocidCn))
    }

    fun createNegotiationCnOnPn(
        cpid: String,
        ocid: String,
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

        //BR-2.4.16.6
        val newOcId = generationService.generateOcid(cpid = cpid, stage = stage)

        val releaseNP = releasePN.copy(
            ocid = newOcId, //BR-2.4.16.6
            id = generationService.generateReleaseId(newOcId), //BR-2.4.16.7
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
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = params.relatedProcessType)

        //BR-2.4.16.2
        relatedProcessService.addRecordRelatedProcessToRecord(release = releaseNP, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)

        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = releaseNP, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
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
        val apRelease = getApReleaseForCreateFe(data, context, apEntity)

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
            stage = context.stage,
            release = apRelease,
            publishDate = apEntity.publishDate
        )

        releaseService.saveMs(cpId = context.cpid, ms = msRelease, publishDate =  msEntity.publishDate)

        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    private fun getApReleaseForCreateFe(
        data: JsonNode,
        context: CreateFeContext,
        recordEntity: ReleaseEntity
    ) : Release{
        val storedAp = releaseService.getRelease(recordEntity.jsonData)
        val receivedTender = releaseService.getRecordTender(data)

        return storedAp.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.11
            tag = listOf(Tag.PLANNING_UPDATE),
            //FR.COM-3.2.14
            tender = receivedTender.copy(
                status = TenderStatus.PLANNED,
                statusDetails = TenderStatusDetails.AGGREGATED,
                //FR-5.0.3
                hasEnquiries = storedAp.tender.hasEnquiries
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
                procuringEntity = storedMs.tender.procuringEntity,
                hasEnquiries = storedMs.tender.hasEnquiries
            ),
            //FR.COM-3.2.20
            parties = releaseService.getPartiesWithActualPersones(
                requestProcuringEntity = receivedTender.procuringEntity!!,
                parties = storedMs.parties
            )
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
            id = generationService.generateReleaseId(context.ocid),
            //FR-5.0.2
            date = context.startDate,
            //FR.COM-3.2.1
            hasPreviousNotice = true,
            //FR.COM-3.2.2
            tag = listOf(Tag.TENDER),
            //FR.COM-3.2.3
            purposeOfNotice = receivedFe.purposeOfNotice?.copy(isACallForCompetition = true),
            //FR.COM-3.2.4
            initiationType = InitiationType.TENDER,
            //FR.COM-3.2.5
            ocid = context.ocidCn,
            //FR.COM-3.2.7
            tender = receivedFe.tender.copy(hasEnquiries = false)
        )
        //FR.COM-3.2.6 1)
        relatedProcessService.addMsRelatedProcessToRecord(release = updatedFe, cpId = context.cpid)
        //FR.COM-3.2.6 2)
        relatedProcessService.addRecordRelatedProcessToRecord(
            release = updatedFe,
            cpId = context.cpid,
            ocId = context.ocid,
            processType = RelatedProcessType.PLANNING
        )

        return updatedFe
    }
}
