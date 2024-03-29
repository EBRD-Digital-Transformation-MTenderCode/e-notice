package com.procurement.notice.service.contract

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.can.ConfirmCANContext
import com.procurement.notice.application.service.can.ConfirmCANData
import com.procurement.notice.application.service.can.CreateCANContext
import com.procurement.notice.application.service.can.CreateCANData
import com.procurement.notice.application.service.can.CreateProtocolContext
import com.procurement.notice.application.service.can.CreateProtocolData
import com.procurement.notice.application.service.contract.activate.ActivateContractContext
import com.procurement.notice.application.service.contract.activate.ActivateContractData
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationContext
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationData
import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionContext
import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionData
import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionResult
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.contract.ContractTender
import com.procurement.notice.model.contract.dto.CreateAcDto
import com.procurement.notice.model.contract.dto.EndAwardPeriodDto
import com.procurement.notice.model.contract.dto.EndContractingProcessDto
import com.procurement.notice.model.contract.dto.FinalUpdateAcDto
import com.procurement.notice.model.contract.dto.IssuingAcDto
import com.procurement.notice.model.contract.dto.SigningDto
import com.procurement.notice.model.contract.dto.UpdateAcDto
import com.procurement.notice.model.contract.dto.UpdateCanDocumentsDto
import com.procurement.notice.model.contract.dto.VerificationDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.ConfirmationRequest
import com.procurement.notice.model.ocds.ConfirmationResponse
import com.procurement.notice.model.ocds.ConfirmationResponseValue
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.RelatedParty
import com.procurement.notice.model.ocds.RelatedPerson
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Request
import com.procurement.notice.model.ocds.RequestGroup
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.ValueTax
import com.procurement.notice.model.ocds.Verification
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.service.OrganizationService
import com.procurement.notice.service.RelatedProcessService
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.service.contract.strategy.ActivationContractStrategy
import com.procurement.notice.service.contract.strategy.CancelCANsAndContractStrategy
import com.procurement.notice.service.contract.strategy.CancelCANsStrategy
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ContractingService(
    private val releaseService: ReleaseService,
    private val organizationService: OrganizationService,
    private val relatedProcessService: RelatedProcessService,
    private val budgetDao: BudgetDao,
    private val releaseDao: ReleaseDao,
    private val generationService: GenerationService
) {

    private val cancelCANsStrategy = CancelCANsStrategy(releaseService, generationService)
    private val cancelCANsAndContractStrategy = CancelCANsAndContractStrategy(releaseService, generationService)
    private val activationContractStrategy = ActivationContractStrategy(
        releaseService = releaseService,
        releaseDao = releaseDao,
        generationService = generationService
    )

    fun createAc(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(CreateAcDto::class.java, data.toString())
        val cans = dto.cans
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD_UPDATE)
            if (dto.cans.isNotEmpty()) {
                contracts.let { updateCanContracts(it, cans) }
            }
        }

        val contract = dto.contract
        val ocIdContract = contract.id!!
        val contractedAward = dto.contractedAward
        contract.agreedMetrics = dto.contractTerm.agreedMetrics

        val contractedTender = ContractTender(
            id = release.tender.id,
            lots = dto.contractedTender.lots,
            classification = dto.contractedTender.classification,
            mainProcurementCategory = dto.contractedTender.mainProcurementCategory,
            procurementMethod = dto.contractedTender.procurementMethod,
            procurementMethodDetails = dto.contractedTender.procurementMethodDetails
        )
        val recordContract = ContractRecord(
            ocid = ocIdContract,
            id = generationService.generateReleaseId(ocIdContract),
            date = releaseDate,
            tag = listOf(Tag.CONTRACT),
            initiationType = release.initiationType,
            tender = contractedTender,
            awards = listOf(contractedAward),
            contracts = listOf(contract)
        )
        //BR-2.7.1.10
        val processType = when(stage) {
            "EV", "TP" -> RelatedProcessType.X_EVALUATION
            "NP" -> RelatedProcessType.X_NEGOTIATION
            "RQ" -> RelatedProcessType.X_REQUEST_QUOTATION
            else -> throw ErrorException(
                error = ErrorType.INVALID_STAGE,
                message = "Current stage '${stage}', required stage: 'EV', 'TP', 'RQ' or 'NP'."
            )
        }
        organizationService.processContractRecordPartiesFromAwards(recordContract)
        relatedProcessService.addMsRelatedProcessToContract(record = recordContract, cpId = cpid)
        relatedProcessService.addRecordRelatedProcessToMs(
            ms = ms,
            ocid = ocIdContract,
            processType = RelatedProcessType.X_CONTRACTING
        )
        relatedProcessService.addRecordRelatedProcessToContractRecord(
            record = recordContract,
            ocId = ocid,
            cpId = cpid,
            processType = processType
        )
        relatedProcessService.addContractRelatedProcessToCAN(
            release = release,
            ocId = ocIdContract,
            cpId = cpid,
            contract = contract,
            cans = cans
        )

        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = "AC",
            record = recordContract,
            publishDate = releaseDate.toDate()
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun updateAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {

        val dto = toObject(UpdateAcDto::class.java, data)
        updatePersonsDocuments(dto)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
            planning = dto.planning
            awards = listOf(dto.award)
            contracts = listOf(dto.contract)
        }
        organizationService.processContractRecordPartiesFromAwards(recordContract)
        organizationService.processContractRecordPartiesFromBudget(
            record = recordContract,
            buyer = dto.buyer,
            funders = dto.funders,
            payers = dto.payers
        )
        dto.addedFS?.forEach { fsOcid ->
            val entity = budgetDao.getFsByCpIdAndOcId(relatedProcessService.getEiCpIdFromOcId(fsOcid), fsOcid)
                ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val fs = toObject(FS::class.java, entity.jsonData)
            relatedProcessService.addFsRelatedProcessToContract(recordContract, fsOcid)
            relatedProcessService.addContractRelatedProcessToFs(fs = fs, cpid = cpid, ocid = ocid)
            entity.jsonData = toJson(fs)
            budgetDao.saveBudget(entity)
        }
        dto.excludedFS?.forEach { fsOcid ->
            val entity = budgetDao.getFsByCpIdAndOcId(relatedProcessService.getEiCpIdFromOcId(fsOcid), fsOcid)
                ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val fs = toObject(FS::class.java, entity.jsonData)
            relatedProcessService.removeFsRelatedProcessFromContract(recordContract, fsOcid)
            relatedProcessService.removeContractRelatedProcessFromFs(fs, ocid)
            entity.jsonData = toJson(fs)
            budgetDao.saveBudget(entity)
        }
        dto.addedEI?.forEach { eiOcid ->
            val entity = budgetDao.getEiByCpId(eiOcid)
                ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val ei = toObject(EI::class.java, entity.jsonData)
            relatedProcessService.addEiRelatedProcessToContract(recordContract, eiOcid)
            relatedProcessService.addContractRelatedProcessToEi(ei = ei, cpid = cpid, ocid = ocid)
            entity.jsonData = toJson(ei)
            budgetDao.saveBudget(entity)
        }
        dto.excludedEI?.forEach { eiOcid ->
            val entity = budgetDao.getEiByCpId(eiOcid)
                ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
            val ei = toObject(EI::class.java, entity.jsonData)
            relatedProcessService.removeEiRelatedProcessFromContract(recordContract, eiOcid)
            relatedProcessService.removeContractRelatedProcessFromEi(ei, ocid)
            entity.jsonData = toJson(ei)
            budgetDao.saveBudget(entity)
        }
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun issuingAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(IssuingAcDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
        }
        val contract = recordContract.contracts?.asSequence()?.first() ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        contract.apply {
            date = dto.contract.date
            statusDetails = dto.contract.statusDetails
            internalId = dto.contract.internalId
        }
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun finalUpdateAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(FinalUpdateAcDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }
        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
            contracts = listOf(dto.contract)
        }
        val contract = recordContract.contracts?.asSequence()?.first() ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        contract.apply {
            date = dto.contract.date
            statusDetails = dto.contract.statusDetails
        }
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun buyerSigningAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)

        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts = listOf(dto.contract)
        }

        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun supplierSigningAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts = listOf(dto.contract)
        }

        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun verificationAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(VerificationDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts?.firstOrNull()?.apply {
                statusDetails = dto.contract.statusDetails
            }
        }
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun treasuryApprovingAC(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            contracts = listOf(dto.contract)
        }

        releaseService.saveContractRecord(
            cpId = cpid,
            stage = stage,
            record = recordContract,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun treasuryClarificationAC(
        context: TreasuryClarificationContext,
        data: TreasuryClarificationData
    ): ResponseDto {

        fun getUpdatedContractRecord(
            clarificationContext: TreasuryClarificationContext,
            clarificationData: TreasuryClarificationData,
            entity: ReleaseEntity
        ): ContractRecord {
            val contractRecord = toObject(ContractRecord::class.java, entity.jsonData)
            val contract = getContractFromData(clarificationData.contract)

            //BR-2.7.6.7
            val updatedMetrics = contractRecord.contracts?.firstOrNull()?.agreedMetrics
            val updatedContract = contract.copy(
                agreedMetrics = updatedMetrics
            )

            val updatedRecordContract = contractRecord.copy(
                //BR-2.7.6.8
                id = generationService.generateReleaseId(clarificationContext.ocid),
                //BR-2.7.6.2
                date = clarificationContext.releaseDate,
                //BR-2.7.6.6
                contracts = listOf(updatedContract),
                //BR-2.7.6.1
                tag = listOf(Tag.CONTRACT_UPDATE)
            )
            return updatedRecordContract
        }

        fun getUpdatedRecord(
            clarificationContext: TreasuryClarificationContext,
            clarificationData: TreasuryClarificationData,
            entity: ReleaseEntity
        ): Release {
            val release: Release = releaseService.getRelease(entity.jsonData)
            val contracts = release.contracts.toList()
            val updatedContracts = getContractsUpdatedWithCansSection(contracts, clarificationData.cans)

            val updatedRecord = release.copy(
                //BR-2.8.6.4
                id = generationService.generateReleaseId(clarificationContext.ocid),
                //BR-2.8.6.3
                date = clarificationContext.releaseDate,
                //BR-2.8.6.1
                tag = listOf(Tag.TENDER_UPDATE),
                //BR-2.8.6.6
                contracts = updatedContracts.toList()
            )
            return updatedRecord
        }

        val entityForContractRelease =
            releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)

        val updatedRecordContract = getUpdatedContractRecord(
            clarificationContext = context,
            entity = entityForContractRelease,
            clarificationData = data
        )

        val recordStage = when (context.pmd) {
            ProcurementMethod.OT, ProcurementMethod.TEST_OT,
            ProcurementMethod.SV, ProcurementMethod.TEST_SV,
            ProcurementMethod.MV, ProcurementMethod.TEST_MV -> "EV"

            ProcurementMethod.CD, ProcurementMethod.TEST_CD,
            ProcurementMethod.DA, ProcurementMethod.TEST_DA,
            ProcurementMethod.DC, ProcurementMethod.TEST_DC,
            ProcurementMethod.IP, ProcurementMethod.TEST_IP,
            ProcurementMethod.NP, ProcurementMethod.TEST_NP,
            ProcurementMethod.OP, ProcurementMethod.TEST_OP -> "NP"

            ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ -> "RQ"

            ProcurementMethod.CF, ProcurementMethod.TEST_CF,
            ProcurementMethod.FA, ProcurementMethod.TEST_FA,
            ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
            ProcurementMethod.OF, ProcurementMethod.TEST_OF,
            ProcurementMethod.RT, ProcurementMethod.TEST_RT -> throw ErrorException(ErrorType.INVALID_PMD)
        }
        val entityForEvaluationOrNegotiationRelease = releaseDao.getByCpIdAndStage(cpId = context.cpid, stage = recordStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)

        val updatedRelease = getUpdatedRecord(
            clarificationContext = context,
            entity = entityForEvaluationOrNegotiationRelease,
            clarificationData = data
        )

        releaseService.saveContractRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecordContract,
            publishDate = entityForContractRelease.publishDate
        )

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = recordStage,
            release = updatedRelease,
            publishDate = entityForEvaluationOrNegotiationRelease.publishDate
        )

        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    fun treasuryRejectionAC(
        context: TreasuryRejectionContext,
        data: TreasuryRejectionData
    ): TreasuryRejectionResult {

        fun getUpdatedContractRecord(
            rejectionContext: TreasuryRejectionContext,
            rejectionData: TreasuryRejectionData,
            entity: ReleaseEntity
        ): ContractRecord {
            val contractRecord = toObject(ContractRecord::class.java, entity.jsonData)
            val contract = getContractFromData(rejectionData.contract)

            //BR-2.7.6.7
            val updatedMetrics = contractRecord.contracts?.firstOrNull()?.agreedMetrics
            val updatedContract = contract.copy(
                agreedMetrics = updatedMetrics
            )

            val updatedRecordContract = contractRecord.copy(
                //BR-2.7.6.8
                id = generationService.generateReleaseId(rejectionContext.ocid),
                //BR-2.7.6.2
                date = rejectionContext.releaseDate,
                //BR-2.7.6.6
                contracts = listOf(updatedContract),
                //BR-2.7.6.1
                tag = listOf(Tag.CONTRACT_UPDATE)
            )
            return updatedRecordContract
        }

        fun getUpdatedRecord(
            rejectionContext: TreasuryRejectionContext,
            rejectionData: TreasuryRejectionData,
            entity: ReleaseEntity
        ): Release {
            val release: Release = releaseService.getRelease(entity.jsonData)
            val contracts = release.contracts.toList()
            val updatedContracts = updateContractWithCansSection(contracts, rejectionData.cans)

            val updatedRecord = release.copy(
                //BR-2.8.6.4
                id = generationService.generateReleaseId(rejectionContext.ocid),
                //BR-2.8.6.3
                date = rejectionContext.releaseDate,
                //BR-2.8.6.1
                tag = listOf(Tag.TENDER_UPDATE),
                //BR-2.8.6.6
                contracts = updatedContracts.toList()
            )
            return updatedRecord
        }

        val entityForContractRelease =
            releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)

        val updatedRecordContract = getUpdatedContractRecord(
            rejectionContext = context,
            entity = entityForContractRelease,
            rejectionData = data
        )

        val recordStage = when (context.pmd) {
            ProcurementMethod.OT, ProcurementMethod.TEST_OT,
            ProcurementMethod.SV, ProcurementMethod.TEST_SV,
            ProcurementMethod.MV, ProcurementMethod.TEST_MV -> "EV"

            ProcurementMethod.CD, ProcurementMethod.TEST_CD,
            ProcurementMethod.DA, ProcurementMethod.TEST_DA,
            ProcurementMethod.DC, ProcurementMethod.TEST_DC,
            ProcurementMethod.IP, ProcurementMethod.TEST_IP,
            ProcurementMethod.NP, ProcurementMethod.TEST_NP,
            ProcurementMethod.OP, ProcurementMethod.TEST_OP -> "NP"

            ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ -> "RQ"

            ProcurementMethod.CF, ProcurementMethod.TEST_CF,
            ProcurementMethod.FA, ProcurementMethod.TEST_FA,
            ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
            ProcurementMethod.OF, ProcurementMethod.TEST_OF,
            ProcurementMethod.RT, ProcurementMethod.TEST_RT -> throw ErrorException(ErrorType.INVALID_PMD)
        }
        val entityForEvaluationOrNegotiationRelease = releaseDao.getByCpIdAndStage(cpId = context.cpid, stage = recordStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)

        val updatedRelease = getUpdatedRecord(
            rejectionContext = context,
            entity = entityForEvaluationOrNegotiationRelease,
            rejectionData = data
        )

        releaseService.saveContractRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecordContract,
            publishDate = entityForContractRelease.publishDate
        )

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = recordStage,
            release = updatedRelease,
            publishDate = entityForEvaluationOrNegotiationRelease.publishDate
        )

        return TreasuryRejectionResult(cpid = context.cpid, ocid = context.ocid)
    }

    private fun getContractsUpdatedWithCansSection(
        contracts: List<Contract>,
        cans: List<TreasuryClarificationData.Can>
    ): List<Contract> =
        contracts.map { contract ->
            val matchedCan = cans
                .asSequence()
                .filter { can -> can.id == contract.id }
                .firstOrNull()
            if (matchedCan != null) {
                contract.copy(
                    status = matchedCan.status,
                    statusDetails = matchedCan.statusDetails
                )
            } else {
                contract
            }
        }

    private fun updateContractWithCansSection(
        contracts: List<Contract>,
        cans: List<TreasuryRejectionData.Can>
    ): List<Contract> =
        contracts.map { contract ->
            val matchedCan = cans
                .asSequence()
                .filter { can -> can.id == contract.id }
                .firstOrNull()
            if (matchedCan != null) {
                contract.copy(
                    status = matchedCan.status,
                    statusDetails = matchedCan.statusDetails
                )
            } else {
                contract
            }
        }

    private fun getContractFromData(contractData: TreasuryClarificationData.Contract): Contract =
        Contract(
            id = contractData.id,
            documents = contractData.documents.map { document ->
                Document(
                    documentType = document.documentType,
                    relatedConfirmations = document.relatedConfirmations,
                    relatedLots = document.relatedLots?.map { it.toString() },
                    description = document.description,
                    title = document.title,
                    id = document.id,
                    url = document.url,
                    datePublished = document.datePublished,
                    dateModified = null,
                    format = null,
                    language = null
                )
            },
            title = contractData.title,
            description = contractData.description,
            period = contractData.period.let { period ->
                Period(
                    startDate = period.startDate,
                    endDate = period.endDate,
                    durationInDays = null,
                    maxExtentDate = null
                )
            },
            value = contractData.value.let { value ->
                ValueTax(
                    amount = value.amount,
                    currency = value.currency,
                    amountNet = value.amountNet,
                    valueAddedTaxIncluded = value.valueAddedTaxincluded
                )
            },
            agreedMetrics = null,
            awardId = contractData.awardId,
            confirmationRequests = contractData.confirmationRequests.map { confirmationRequest ->
                ConfirmationRequest(
                    title = confirmationRequest.title,
                    description = confirmationRequest.description,
                    id = confirmationRequest.id,
                    relatedItem = confirmationRequest.relatedItem,
                    relatesTo = confirmationRequest.relatesTo,
                    requestGroups = confirmationRequest.requestGroups.map { requestGroup ->
                        RequestGroup(
                            id = requestGroup.id,
                            requests = requestGroup.requests.map { request ->
                                Request(
                                    id = request.id,
                                    description = request.description,
                                    title = request.title,
                                    relatedPerson = request.relatedPerson?.let { relatedPerson ->
                                        RelatedPerson(
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
            confirmationResponses = contractData.confirmationResponses.map { confirmationResponse ->
                ConfirmationResponse(
                    id = confirmationResponse.id,
                    value = confirmationResponse.value.let { value ->
                        ConfirmationResponseValue(
                            name = value.name,
                            id = value.id,
                            relatedPerson = value.relatedPerson?.let { relatedPerson ->
                                RelatedPerson(
                                    id = relatedPerson.id,
                                    name = relatedPerson.name
                                )
                            },
                            date = value.date,
                            verification = value.verification.map { verification ->
                                Verification(
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
            date = contractData.date,
            milestones = contractData.milestones.map { milestone ->
                Milestone(
                    id = milestone.id,
                    type = milestone.type,
                    title = milestone.title,
                    description = milestone.description,
                    additionalInformation = milestone.additionalInformation,
                    dateMet = milestone.dateMet,
                    dateModified = milestone.dateModified,
                    dueDate = milestone.dueDate,
                    relatedItems = milestone.relatedItems?.map { it.toString() },
                    relatedParties = milestone.relatedParties.map { relatedParty ->
                        RelatedParty(
                            id = relatedParty.id,
                            name = relatedParty.name
                        )
                    },
                    status = milestone.status
                )
            },
            status = contractData.status,
            statusDetails = contractData.statusDetails,
            relatedLots = null,
            items = null,
            classification = null,
            amendment = null,
            amendments = null,
            budgetSource = null,
            countryOfOrigin = null,
            dateSigned = null,
            extendsContractId = null,
            isFrameworkOrDynamic = null,
            lotVariant = null,
            relatedProcesses = null,
            requirementResponses = null,
            valueBreakdown = null
        )

    private fun getContractFromData(contractData: TreasuryRejectionData.Contract): Contract =
        Contract(
            id = contractData.id,
            documents = contractData.documents.map { document ->
                Document(
                    documentType = document.documentType,
                    relatedConfirmations = document.relatedConfirmations,
                    relatedLots = document.relatedLots?.map { it.toString() },
                    description = document.description,
                    title = document.title,
                    id = document.id,
                    url = document.url,
                    datePublished = document.datePublished,
                    dateModified = null,
                    format = null,
                    language = null
                )
            },
            title = contractData.title,
            description = contractData.description,
            period = contractData.period.let { period ->
                Period(
                    startDate = period.startDate,
                    endDate = period.endDate,
                    durationInDays = null,
                    maxExtentDate = null
                )
            },
            value = contractData.value.let { value ->
                ValueTax(
                    amount = value.amount,
                    currency = value.currency,
                    amountNet = value.amountNet,
                    valueAddedTaxIncluded = value.valueAddedTaxincluded
                )
            },
            agreedMetrics = null,
            awardId = contractData.awardId,
            confirmationRequests = contractData.confirmationRequests.map { confirmationRequest ->
                ConfirmationRequest(
                    title = confirmationRequest.title,
                    description = confirmationRequest.description,
                    id = confirmationRequest.id,
                    relatedItem = confirmationRequest.relatedItem,
                    relatesTo = confirmationRequest.relatesTo,
                    requestGroups = confirmationRequest.requestGroups.map { requestGroup ->
                        RequestGroup(
                            id = requestGroup.id,
                            requests = requestGroup.requests.map { request ->
                                Request(
                                    id = request.id,
                                    description = request.description,
                                    title = request.title,
                                    relatedPerson = request.relatedPerson?.let { relatedPerson ->
                                        RelatedPerson(
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
            confirmationResponses = contractData.confirmationResponses.map { confirmationResponse ->
                ConfirmationResponse(
                    id = confirmationResponse.id,
                    value = confirmationResponse.value.let { value ->
                        ConfirmationResponseValue(
                            name = value.name,
                            id = value.id,
                            relatedPerson = value.relatedPerson?.let { relatedPerson ->
                                RelatedPerson(
                                    id = relatedPerson.id,
                                    name = relatedPerson.name
                                )
                            },
                            date = value.date,
                            verification = value.verification.map { verification ->
                                Verification(
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
            date = contractData.date,
            milestones = contractData.milestones.map { milestone ->
                Milestone(
                    id = milestone.id,
                    type = milestone.type,
                    title = milestone.title,
                    description = milestone.description,
                    additionalInformation = milestone.additionalInformation,
                    dateMet = milestone.dateMet,
                    dateModified = milestone.dateModified,
                    dueDate = milestone.dueDate,
                    relatedItems = milestone.relatedItems?.map { it.toString() },
                    relatedParties = milestone.relatedParties.map { relatedParty ->
                        RelatedParty(
                            id = relatedParty.id,
                            name = relatedParty.name
                        )
                    },
                    status = milestone.status
                )
            },
            status = contractData.status,
            statusDetails = contractData.statusDetails,
            relatedLots = null,
            items = null,
            classification = null,
            amendment = null,
            amendments = null,
            budgetSource = null,
            countryOfOrigin = null,
            dateSigned = null,
            extendsContractId = null,
            isFrameworkOrDynamic = null,
            lotVariant = null,
            relatedProcesses = null,
            requirementResponses = null,
            valueBreakdown = null
        )

    fun activationAC(context: ActivateContractContext, data: ActivateContractData): ResponseDto =
        activationContractStrategy.activateContract(context, data)

    fun endAwardPeriod(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(EndAwardPeriodDto::class.java, data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.apply {
                statusDetails = TenderStatusDetails.EXECUTION
            }
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = cpid, stage = "EV")
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val releaseEv = releaseService.getRelease(recordEvEntity.jsonData)
        releaseEv.apply {
            id = generationService.generateReleaseId(recordEvEntity.ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.apply {
                awardPeriod = dto.awardPeriod
                status = TenderStatus.creator(dto.tender.status)
                statusDetails = TenderStatusDetails.creator(dto.tender.statusDetails)
                lots.let { updateLots(it, dto.lots) }
            }
            bids?.details?.let { updateBids(it, dto.bids) }
            awards.let { updateAwards(it, dto.awards) }
            contracts.let { updateCanContracts(it, dto.cans) }
        }
        val recordContractEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordContractEntity.jsonData)
        if (dto.contract != null) {
            recordContract.apply {
                id = generationService.generateReleaseId(ocid)
                tag = listOf(Tag.CONTRACT_UPDATE)
                date = releaseDate
                contracts?.firstOrNull()?.apply {
                    status = dto.contract.status
                    statusDetails = dto.contract.statusDetails
                    milestones = dto.contract.milestones
                }
            }
        }
        if (dto.contract != null) {
            releaseService.saveContractRecord(
                cpId = cpid,
                stage = stage,
                record = recordContract,
                publishDate = recordContractEntity.publishDate
            )
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = cpid,
            stage = "EV",
            release = releaseEv,
            publishDate = recordEvEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateCanContracts(recordContracts: List<Contract>, dtoCans: List<Can>) {
        for (contract in recordContracts) {
            dtoCans.firstOrNull { it.id == contract.id }?.apply {
                contract.status = this.status!!
                contract.statusDetails = this.statusDetails
            }
        }
    }

    private fun updatePersonsDocuments(dto: UpdateAcDto) {
        val documentDto = dto.documentsOfContractPersones
        if (documentDto != null) {
            dto.award.suppliers?.asSequence()?.forEach { supplier ->
                supplier.persones?.asSequence()?.forEach { person ->
                    person.businessFunctions.asSequence().forEach { businessFunction ->
                        businessFunction.documents?.forEach { docBf ->
                            documentDto.forEach { docDto ->
                                if (docBf.id == docDto.id && docBf.documentType == docDto.documentType) {
                                    docBf.update(docDto)
                                }
                            }
                        }
                    }
                }
            }
            dto.buyer?.persones?.asSequence()?.forEach { person ->
                person.businessFunctions.asSequence().forEach { businessFunction ->
                    businessFunction.documents?.forEach { docBf ->
                        documentDto.forEach { docDto ->
                            if (docBf.id == docDto.id && docBf.documentType == docDto.documentType) {
                                docBf.update(docDto)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun DocumentBF.update(documentDto: DocumentBF?) {
        if (documentDto != null) {
            this.url = documentDto.url
            this.datePublished = documentDto.datePublished
            this.dateModified = documentDto.dateModified
        }
    }

    fun createProtocol(context: CreateProtocolContext, data: CreateProtocolData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val updatedBids = updatedBids(context = context, data = data, bids = release.bids)
        val updatedContracts = updateContracts(can = data.can, contracts = release.contracts)
        val updatedLots = updateLots(lot = data.lot, lots = release.tender.lots)

        val updatedRecord = release.copy(
            //BR-2.8.4.4
            id = generationService.generateReleaseId(ocid),

            //BR-2.8.4.1
            tag = listOf(Tag.AWARD_UPDATE),

            //BR-2.8.4.3
            date = context.releaseDate,

            //BR-2.8.4.6
            bids = updatedBids,

            //BR-2.8.4.7
            contracts = updatedContracts.toList(),

            //BR-2.8.4.9
            tender = release.tender.copy(
                //BR-2.8.4.8
                lots = updatedLots.toList()
            )
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = context.stage,
            release = updatedRecord,
            publishDate = recordEntity.publishDate
        )
    }

    /**
     * BR-2.8.4.6 Bids
     *
     * 1. eNotice analyzes stage value from the context of Request:
     *   a. IF [stage == "EV"] then:
     *     i.  Rewrite all Bids objects from previous Release to new Release;
     *     ii. forEach bid object from Request system executes:
     *       1. Finds bid.details object in new Release where bid.details.ID == bid.id from Request;
     *       2. Sets bid.details.statusDetails in object (found before) == bid.statusDetails from processed bid of Request;
     *   b. ELSE [stage == "NP"] then:
     *        system does not perform any operation;
     */
    private fun updatedBids(context: CreateProtocolContext, data: CreateProtocolData, bids: Bids?): Bids? {
        return when (context.stage) {
            "EV", "TP", "RQ" -> {
                if (data.bids == null || data.bids.isEmpty())
                    throw ErrorException(error = ErrorType.BIDS_IN_REQUEST_IS_EMPTY)

                val bidsById: Map<UUID, CreateProtocolData.Bid> = data.bids.associateBy { it.id }
                val detailsByBidId: Map<UUID, Bid> = bids!!.details!!.associateBy { UUID.fromString(it.id) }
                val ids: Set<UUID> = detailsByBidId.keys.plus(bidsById.keys)
                val updatedDetails: List<Bid> = ids.map { id ->
                    bidsById[id]?.let { bid ->
                        detailsByBidId.getValue(id)
                            .copy(statusDetails = bid.statusDetails)
                    } ?: detailsByBidId.getValue(id)
                }

                bids.copy(
                    details = updatedDetails
                )
            }
            "NP" -> bids
            else -> throw ErrorException(
                error = ErrorType.INVALID_STAGE,
                message = "Current stage: '${context.stage}', expected stages: [EV, NP]."
            )
        }
    }

    /**
     * BR-2.8.4.7 Contracts
     *
     * eNotice executes next operations:
     * 1. Rewrite all Contracts objects from previous Release to new Release;
     * 2. Saves CAN object from Request as a new Contract object in Contracts arrays in new formed Release following to the next order:
     *   a. can.ID == contracts.ID;
     *   b. can.awardId == contracts.awardId;
     *   c. can.lotId == contracts.relatedLots;
     *   d. can.status == contracts.status;
     *   e. can.statusDetails == contracts.statusDetails;
     *   f. can.date == contracts.date;
     */
    private fun updateContracts(
        can: CreateProtocolData.CAN,
        contracts: Collection<Contract>
    ): List<Contract> {
        val newContract = Contract(
            id = can.id.toString(),
            awardId = can.awardId,
            status = can.status,
            statusDetails = can.statusDetails,
            date = can.date,
            documents = null,
            relatedLots = listOf(can.lotId.toString())
        )
        return contracts.plus(newContract)
    }

    /**
     * BR-2.8.4.8 lots
     *
     * 1. Rewrite all Lot objects from previous Release to new Release;
     * 2. forEach Lot object from Request system executes:
     *   a. Finds lot.ID object in new Release where lot.ID == lot.id from Request;
     *   b. Sets lot.statusDetails in object (found before) == lot.statusDetails from processed lot of Request;
     */
    private fun updateLots(lot: CreateProtocolData.Lot, lots: Collection<Lot>): List<Lot> {
        return lots.map {
            if (lot.id.toString() == it.id)
                it.copy(
                    statusDetails = lot.statusDetails
                )
            else
                it
        }
    }

    fun createCAN(context: CreateCANContext, data: CreateCANData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val updatedContracts = updateContracts(can = data.can, contracts = release.contracts)

        val updatedRelease = release.copy(
            //BR-2.8.4.4
            id = generationService.generateReleaseId(ocid),

            //BR-2.8.4.1
            tag = listOf(Tag.AWARD_UPDATE),

            //BR-2.8.4.3
            date = context.releaseDate,

            //BR-2.8.4.6
            contracts = updatedContracts.toList()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = context.stage,
            release = updatedRelease,
            publishDate = recordEntity.publishDate
        )
    }

    /**
     * BR-2.8.4.6 Contracts
     *
     * eNotice executes next operations:
     * 1. Rewrite all Contracts objects from previous Release to new Release;
     * 2. Saves CAN object from Request as a new Contract object in Contracts arrays in new formed Release following to the next order:
     *   a. can.ID == contracts.ID;
     *   b. can.awardId == contracts.awardId;
     *   c. can.lotId == contracts.relatedLots;
     *   d. can.status == contracts.status;
     *   e. can.statusDetails == contracts.statusDetails;
     *   f. can.date == contracts.date;
     */
    private fun updateContracts(can: CreateCANData.CAN, contracts: Collection<Contract>): List<Contract> {
        val newContract = Contract(
            id = can.id.toString(),
            awardId = can.awardId,
            status = can.status,
            statusDetails = can.statusDetails,
            date = can.date,
            documents = null,
            relatedLots = listOf(can.lotId.toString())
        )
        return contracts.plus(newContract)
    }

    fun updateCanDocs(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(UpdateCanDocumentsDto::class.java, data)
        val recordEntity = releaseDao.getByCpIdAndOcId(cpId = cpid, ocId = ocid)
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val release = releaseService.getRelease(recordEntity.jsonData)

        release.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            contracts.asSequence()
                .firstOrNull { it.id == dto.contract.id }
                ?.apply {
                    documents = dto.contract.documents
                }

        }
        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            release = release,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun cancelCan(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto =
        cancelCANsStrategy.cancelCan(
            cpid = cpid,
            ocid = ocid,
            stage = stage,
            releaseDate = releaseDate,
            data = data
        )

    fun cancelCanAndContract(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto =
        cancelCANsAndContractStrategy.cancelCanAndContract(
            cpid = cpid,
            ocid = ocid,
            stage = stage,
            releaseDate = releaseDate,
            data = data
        )

    fun confirmCan(context: ConfirmCANContext, data: ConfirmCANData): ResponseDto {
        fun Collection<Lot>.updating(data: ConfirmCANData): List<Lot> {
            val lotsByIds = data.lots.associateBy { it.id }
            return this.map { lot ->
                lotsByIds[UUID.fromString(lot.id)]
                    ?.let {
                        lot.copy(
                            status = it.status,
                            statusDetails = it.statusDetails
                        )
                    }
                    ?: lot
            }
        }

        fun Collection<Award>.updating(data: ConfirmCANData): List<Award> {
            val awardsByIds = data.awards.associateBy { it.id }
            return this.map { award ->
                awardsByIds[UUID.fromString(award.id)]
                    ?.let {
                        award.copy(
                            status = it.status,
                            statusDetails = it.statusDetails
                        )
                    }
                    ?: award
            }
        }

        fun Collection<Bid>.updating(data: ConfirmCANData): List<Bid> {
            val bidsByIds = data.bids?.associateBy { it.id }
            return if (bidsByIds == null)
                this.toList()
            else
                this.map { bid ->
                    bidsByIds[UUID.fromString(bid.id)]
                        ?.let {
                            bid.copy(
                                status = it.status,
                                statusDetails = it.statusDetails
                            )
                        }
                        ?: bid
                }
        }

        fun Collection<Contract>.updating(data: ConfirmCANData): List<Contract> {
            val cansByIds = data.cans.associateBy { it.id }
            return this.map { contract ->
                cansByIds[UUID.fromString(contract.id)]
                    ?.let {
                        contract.copy(
                            status = it.status,
                            statusDetails = it.statusDetails
                        )
                    }
                    ?: contract
            }
        }

        val recordEntity = releaseDao.getByCpIdAndOcId(cpId = context.cpid, ocId = context.ocid)
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val updatedRelease = release.copy(
            id = generationService.generateReleaseId(context.ocid),
            date = context.releaseDate,
            tag = listOf(Tag.TENDER_UPDATE),
            tender = release.tender.let { tender ->
                tender.copy(
                    lots = tender.lots.updating(data).toList()
                )
            },
            awards = release.awards.updating(data).toList(),
            bids = release.bids?.let { bids ->
                bids.copy(details = bids.details?.updating(data))
            },
            contracts = release.contracts.updating(data).toList()
        )
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = "EV",
            release = updatedRelease,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    fun endContractingProcess(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(EndContractingProcessDto::class.java, data)

        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.apply {
                status = TenderStatus.creator(dto.tender.status)
                statusDetails = TenderStatusDetails.creator(dto.tender.statusDetails)
            }
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = cpid, stage = stage)
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val releaseEv = releaseService.getRelease(recordEvEntity.jsonData)
        releaseEv.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.apply {
                awardPeriod = dto.awardPeriod
                status = TenderStatus.creator(dto.tender.status)
                statusDetails = TenderStatusDetails.creator(dto.tender.statusDetails)
                lots.let { updateLots(it, dto.lots) }
            }
            bids?.details?.let { updateBids(it, dto.bids) }
            awards.let { updateAwards(it, dto.awards) }
            contracts.let { updateCanContracts(it, dto.cans) }
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = cpid,
            stage = "EV",
            release = releaseEv,
            publishDate = recordEvEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateAwards(recordAwards: List<Award>, dtoAwards: List<Award>) {
        for (award in recordAwards) {
            dtoAwards.firstOrNull { it.id == award.id }?.apply {
                award.status = this.status
                award.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateBids(recordBids: List<Bid>, dtoBids: List<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateLots(recordLots: List<Lot>, dtoLots: List<Lot>) {
        for (lot in recordLots) {
            dtoLots.firstOrNull { it.id == lot.id }?.apply {
                lot.status = this.status
                lot.statusDetails = this.statusDetails
            }
        }
    }
}
