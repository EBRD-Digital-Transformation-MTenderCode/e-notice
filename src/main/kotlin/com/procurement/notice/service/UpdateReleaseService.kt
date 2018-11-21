package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.contract.dto.UpdateAcDto
import com.procurement.notice.model.tender.dto.UpdateCnDto
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.contract.dto.IssuingAcDto
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UpdateReleaseService(private val releaseService: ReleaseService,
                           private val organizationService: OrganizationService,
                           private val relatedProcessService: RelatedProcessService,
                           private val budgetDao: BudgetDao) {

    fun updateCn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 isAuction: Boolean,
                 data: JsonNode): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val dto = toObject(UpdateCnDto::class.java, data)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            hasEnquiries = ms.tender.hasEnquiries
            procuringEntity = ms.tender.procuringEntity
        }
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
            enquiries = record.tender.enquiries
            hasEnquiries = record.tender.hasEnquiries
            if (!isAuction) {
                auctionPeriod = record.tender.auctionPeriod
                procurementMethodModalities = record.tender.procurementMethodModalities
                electronicAuctions = record.tender.electronicAuctions
            }
        }
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        var rationale: String
        val canceledLots = dto.amendment?.relatedLots
        rationale = if (canceledLots != null) {
            "Changing of Contract Notice due to the need of cancelling lot / lots"
        } else {
            "General change of Contract Notice"
        }
        val newAmendmentId = UUID.randomUUID().toString()
        amendments.add(Amendment(
                id = newAmendmentId,
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = canceledLots,
                rationale = rationale,
                changes = null,
                description = null
        ))
        record.apply {
            /* previous record*/
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = if (amendments.isNotEmpty()) amendments else null
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = setOf(newAmendmentId)))
    }

    fun updatePn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto {
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
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
        }
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender = recordTender
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = null
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    fun updateTenderPeriod(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto {
        val recordTender = releaseService.getRecordTender(data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        amendments.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = null,
                rationale = "Extension of tender period",
                changes = null,
                description = null
        ))
        record.apply {
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = if (amendments.isNotEmpty()) amendments else null
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
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
                  data: JsonNode): ResponseDto{
        val dto = toObject(IssuingAcDto::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordContract = toObject(ContractRecord::class.java, recordEntity.jsonData)
        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
        }
        val contract =  recordContract.contracts?.asSequence()?.first() ?: throw ErrorException(ErrorType.DATA_NOT_FOUND)
        contract.apply {
            date = dto.contract.date
            statusDetails = dto.contract.statusDetails
        }
        releaseService.saveContractRecord(cpId = cpid, stage = stage, record = recordContract, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }


    private fun updatePersonsDocuments(dto: UpdateAcDto) {
        val documentDto = dto.documentsOfContractPersones
        if (documentDto != null) {
            dto.award.suppliers?.asSequence()?.forEach { supplier ->
                supplier.persones?.asSequence()?.forEach { person ->
                    person.businessFunctions.asSequence().forEach { businessFunction ->
                        businessFunction.documents.forEach { doc -> doc.update(documentDto.firstOrNull { it.id == doc.id }) }
                    }
                }
            }
            dto.buyer?.persones?.asSequence()?.forEach { person ->
                person.businessFunctions.asSequence().forEach { businessFunction ->
                    businessFunction.documents.forEach { doc -> doc.update(documentDto.firstOrNull { it.id == doc.id }) }
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

}



