package com.procurement.notice.service.contract

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.can.CreateProtocolContext
import com.procurement.notice.application.service.can.CreateProtocolData
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.contract.ContractTender
import com.procurement.notice.model.contract.dto.ActivationDto
import com.procurement.notice.model.contract.dto.ConfirmCanDto
import com.procurement.notice.model.contract.dto.CreateAcDto
import com.procurement.notice.model.contract.dto.EndAwardPeriodDto
import com.procurement.notice.model.contract.dto.EndContractingProcessDto
import com.procurement.notice.model.contract.dto.FinalUpdateAcDto
import com.procurement.notice.model.contract.dto.IssuingAcDto
import com.procurement.notice.model.contract.dto.SigningDto
import com.procurement.notice.model.contract.dto.UpdateAcDto
import com.procurement.notice.model.contract.dto.UpdateCanDocumentsDto
import com.procurement.notice.model.contract.dto.VerificationDto
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.service.OrganizationService
import com.procurement.notice.service.RelatedProcessService
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.service.contract.strategy.CancelCANsAndContractStrategy
import com.procurement.notice.service.contract.strategy.CancelCANsStrategy
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ContractingService(private val releaseService: ReleaseService,
                         private val organizationService: OrganizationService,
                         private val relatedProcessService: RelatedProcessService,
                         private val budgetDao: BudgetDao,
                         private val releaseDao: ReleaseDao,
                         generationService: GenerationService) {

    private val cancelCANsStrategy = CancelCANsStrategy(releaseService, generationService)
    private val cancelCANsAndContractStrategy = CancelCANsAndContractStrategy(releaseService, generationService)

    fun createAc(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto {
        val dto = toObject(CreateAcDto::class.java, data.toString())
        val cans = dto.cans
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD_UPDATE)
            if (dto.cans.isNotEmpty()) {
                contracts?.let { updateCanContracts(it, cans) }
            }
        }

        val contract = dto.contract
        val ocIdContract = contract.id!!
        val contractedAward = dto.contractedAward
        contract.agreedMetrics = dto.contractTerm.agreedMetrics

        val contractedTender = ContractTender(
                id = record.tender.id,
                lots = dto.contractedTender.lots,
                classification = dto.contractedTender.classification,
                mainProcurementCategory = dto.contractedTender.mainProcurementCategory,
                procurementMethod = dto.contractedTender.procurementMethod,
                procurementMethodDetails = dto.contractedTender.procurementMethodDetails
        )
        val recordContract = ContractRecord(
                ocid = ocIdContract,
                id = releaseService.newReleaseId(ocIdContract),
                date = releaseDate,
                tag = listOf(Tag.CONTRACT),
                initiationType = record.initiationType,
                tender = contractedTender,
                awards = hashSetOf(contractedAward),
                contracts = hashSetOf(contract))
        organizationService.processContractRecordPartiesFromAwards(recordContract)
        relatedProcessService.addMsRelatedProcessToContract(record = recordContract, cpId = cpid)
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = ocIdContract, processType = RelatedProcessType.X_CONTRACTING)
        relatedProcessService.addRecordRelatedProcessToContractRecord(record = recordContract, ocId = ocid, cpId = cpid, processType = RelatedProcessType.X_EVALUATION)
        relatedProcessService.addContractRelatedProcessToCAN(record = record, ocId = ocIdContract, cpId = cpid, contract = contract, cans = cans)

        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        releaseService.saveContractRecord(cpId = cpid, stage = "AC", record = recordContract, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun updateAC(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto {

        val dto = toObject(UpdateAcDto::class.java, data)
        updatePersonsDocuments(dto)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
            planning = dto.planning
            awards = hashSetOf(dto.award)
            contracts = hashSetOf(dto.contract)
        }
        organizationService.processContractRecordPartiesFromAwards(recordContract)
        organizationService.processContractRecordPartiesFromBudget(record = recordContract, buyer = dto.buyer, funders = dto.funders, payers = dto.payers)
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
        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun issuingAC(cpid: String,
                  ocid: String,
                  stage: String,
                  releaseDate: LocalDateTime,
                  data: JsonNode): ResponseDto {
        val dto = toObject(IssuingAcDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
        }
        val contract = recordContract.contracts?.asSequence()?.first() ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        contract.apply {
            date = dto.contract.date
            statusDetails = dto.contract.statusDetails
        }
        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun finalUpdateAC(cpid: String,
                      ocid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val dto = toObject(FinalUpdateAcDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
            contracts = hashSetOf(dto.contract)
        }
        val contract = recordContract.contracts?.asSequence()?.first() ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        contract.apply {
            date = dto.contract.date
            statusDetails = dto.contract.statusDetails
        }
        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun buyerSigningAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)

        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts = hashSetOf(dto.contract)
        }

        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))

    }

    fun supplierSigningAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts = hashSetOf(dto.contract)
        }

        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun verificationAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(VerificationDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts?.firstOrNull()?.apply {
                statusDetails = dto.contract.statusDetails
            }
        }
        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))

    }

    fun treasuryApprovingAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(SigningDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        dto.contract.apply {
            agreedMetrics = recordContract.contracts?.firstOrNull()?.agreedMetrics
        }

        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            contracts = hashSetOf(dto.contract)
        }

        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun activationAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(ActivationDto::class.java, data)

        val recordContractEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordContractEntity.jsonData)
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.CONTRACT_UPDATE)
            date = releaseDate
            contracts?.firstOrNull()?.apply {
                status = dto.contract.status
                statusDetails = dto.contract.statusDetails
                milestones = dto.contract.milestones
            }
        }

        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = cpid, stage = "EV")
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val recordEv = releaseService.getRecord(recordEvEntity.jsonData)
        recordEv.apply {
            id = releaseService.newReleaseId(recordEvEntity.ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.lots?.let { updateLots(it, dto.lots) }
            contracts?.let { updateCanContracts(it, dto.cans) }
        }

        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordContractEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = "EV", record = recordEv, publishDate = recordEvEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun endAwardPeriod(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(EndAwardPeriodDto::class.java, data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.apply {
                statusDetails = TenderStatusDetails.EXECUTION
            }
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = cpid, stage = "EV")
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val recordEv = releaseService.getRecord(recordEvEntity.jsonData)
        recordEv.apply {
            id = releaseService.newReleaseId(recordEvEntity.ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.apply {
                awardPeriod = dto.awardPeriod
                status = TenderStatus.fromValue(dto.tender.status)
                statusDetails = TenderStatusDetails.fromValue(dto.tender.statusDetails)
                lots?.let { updateLots(it, dto.lots) }
            }
            bids?.details?.let { updateBids(it, dto.bids) }
            awards?.let { updateAwards(it, dto.awards) }
            contracts?.let { updateCanContracts(it, dto.cans) }
        }
        val recordContractEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordContractEntity.jsonData)
        if (dto.contract != null) {
            recordContract.apply {
                id = releaseService.newReleaseId(ocid)
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
            releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordContractEntity.publishDate)
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = "EV", record = recordEv, publishDate = recordEvEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateCanContracts(recordContracts: HashSet<Contract>, dtoCans: HashSet<Can>) {
        for (contract in recordContracts) {
            dtoCans.firstOrNull { it.id == contract.id }?.apply {
                contract.status = this.status!!
                contract.statusDetails = this.statusDetails!!
            }
        }
    }

    private fun updatePersonsDocuments(dto: UpdateAcDto) {
        val documentDto = dto.documentsOfContractPersones
        if (documentDto != null) {
            dto.award.suppliers?.asSequence()?.forEach { supplier ->
                supplier.persones?.asSequence()?.forEach { person ->
                    person.businessFunctions.asSequence().forEach { businessFunction ->
                        businessFunction.documents.forEach { docBf ->
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
                    businessFunction.documents.forEach { docBf ->
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
        val record = releaseService.getRecord(recordEntity.jsonData)

        val updatedBids = updatedBids(context = context, data = data, bids = record.bids)
        val updatedContracts = updateContracts(can = data.can, contracts = record.contracts ?: emptyList())
        val updatedLots = updateLots(lot = data.lot, lots = record.tender.lots ?: emptyList())

        val updatedRecord = record.copy(
            //BR-2.8.4.4
            id = releaseService.newReleaseId(ocid),

            //BR-2.8.4.1
            tag = listOf(Tag.AWARD_UPDATE),

            //BR-2.8.4.3
            date = context.releaseDate,

            //BR-2.8.4.6
            bids = updatedBids,

            //BR-2.8.4.7
            contracts = updatedContracts.toHashSet(),

            //BR-2.8.4.9
            tender = record.tender.copy(
                //BR-2.8.4.8
                lots = updatedLots.toHashSet()
            )
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = context.stage,
            record = updatedRecord,
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
            "EV" -> {
                if (data.bids.isEmpty())
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
                    details = updatedDetails.toHashSet()
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
    private fun updateContracts(can: CreateProtocolData.CAN, contracts: Collection<Contract>): List<Contract> {
        val newContract = Contract(
            id = can.id.toString(),
            awardId = can.awardId.toString(),
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

    fun updateCanDocs(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(UpdateCanDocumentsDto::class.java, data)
        val recordEntity = releaseDao.getByCpIdAndOcId(cpId = cpid, ocId = ocid)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = releaseService.getRecord(recordEntity.jsonData)

        record.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            contracts?.asSequence()
                    ?.firstOrNull { it.id == dto.contract.id }
                    ?.apply {
                        documents = dto.contract.documents.toHashSet()
                    }

        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
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

    fun confirmCan(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(ConfirmCanDto::class.java, data)
        val recordEntity = releaseDao.getByCpIdAndOcId(cpId = cpid, ocId = ocid)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.apply {
                lots?.let { updateLots(it, dto.lots) }
            }
            contracts?.let { updateCanContracts(it, dto.cans) }
        }
        releaseService.saveRecord(cpId = cpid, stage = "EV", record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun endContractingProcess(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(EndContractingProcessDto::class.java, data)

        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.apply {
                status = TenderStatus.fromValue(dto.tender.status)
                statusDetails = TenderStatusDetails.fromValue(dto.tender.statusDetails)
            }
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = cpid, stage = "EV")
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val recordEv = releaseService.getRecord(recordEvEntity.jsonData)
        recordEv.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_UPDATE)
            tender.apply {
                awardPeriod = dto.awardPeriod
                status = TenderStatus.fromValue(dto.tender.status)
                statusDetails = TenderStatusDetails.fromValue(dto.tender.statusDetails)
                lots?.let { updateLots(it, dto.lots) }
            }
            bids?.details?.let { updateBids(it, dto.bids) }
            awards?.let { updateAwards(it, dto.awards) }
            contracts?.let { updateCanContracts(it, dto.cans) }
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = "EV", record = recordEv, publishDate = recordEvEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateAwards(recordAwards: HashSet<Award>, dtoAwards: HashSet<Award>) {
        for (award in recordAwards) {
            dtoAwards.firstOrNull { it.id == award.id }?.apply {
                award.date = this.date
                award.status = this.status
                award.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateBids(recordBids: HashSet<Bid>, dtoBids: HashSet<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateLots(recordLots: HashSet<Lot>, dtoLots: HashSet<Lot>) {
        for (lot in recordLots) {
            dtoLots.firstOrNull { it.id == lot.id }?.apply {
                lot.status = this.status
                lot.statusDetails = this.statusDetails
            }
        }
    }
}
