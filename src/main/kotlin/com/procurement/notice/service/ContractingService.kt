package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.contract.dto.FinalUpdateAcDto
import com.procurement.notice.model.contract.dto.IssuingAcDto
import com.procurement.notice.model.contract.dto.UpdateAcDto
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContractingService(private val releaseService: ReleaseService,
                         private val organizationService: OrganizationService,
                         private val relatedProcessService: RelatedProcessService,
                         private val budgetDao: BudgetDao) {

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

        recordContract.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.CONTRACT_UPDATE)
            contracts = hashSetOf(dto.contract)
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

    fun buyerSigningAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        TODO()
    }

    fun supplierSigningAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        TODO()
    }

    fun verificationAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        TODO()
    }

    fun treasuryApprovingAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        TODO()
    }

    fun activationAC(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        TODO()
    }

}



