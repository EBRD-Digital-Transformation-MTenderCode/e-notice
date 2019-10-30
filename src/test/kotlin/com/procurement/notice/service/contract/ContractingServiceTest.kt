package com.procurement.notice.service.contract

import com.fasterxml.jackson.databind.JsonNode
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.json.loadJson
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.service.OrganizationService
import com.procurement.notice.service.RelatedProcessService
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject
import org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

internal class ContractingServiceTest {
    companion object {
        private const val PATH_CN_JSON =
            "json/test/ac/treasury/clarification/request_clarification_treasury.json"
        private const val PATH_JSON_FROM_RELEASE =
            "json/test/ac/treasury/clarification/release_entity_json_data.json"
        private val organizationService: OrganizationService = mock()
        private val relatedProcessService: RelatedProcessService = mock()
        private val budgetDao: BudgetDao = mock()
        private val releaseDao: ReleaseDao = mock()
        private val generationService: GenerationService = mock()
        private val releaseService: ReleaseService = mock()
        private val contractingService = ContractingService(
            budgetDao = budgetDao,
            generationService = generationService,
            organizationService = organizationService,
            relatedProcessService = relatedProcessService,
            releaseDao = releaseDao,
            releaseService = releaseService
        )
    }

    @BeforeEach
    fun init() {
        reset(releaseService, organizationService, relatedProcessService, budgetDao, releaseDao, generationService)
    }

    @Test
    fun treasuryClarificationAC_saveContractRecordSuccess() {

        val releaseEntity = ReleaseEntity(
            cpId = UUID.randomUUID().toString(),
            ocId = UUID.randomUUID().toString(),
            publishDate = Date(),
            releaseDate = Date(),
            releaseId = UUID.randomUUID().toString(),
            stage = "PN",
            status = "active",
            jsonData = loadJson(PATH_JSON_FROM_RELEASE)
        )

        whenever(releaseService.getRecordEntity(any(), any())).thenReturn(releaseEntity)
        whenever(releaseService.getRecord(releaseEntity.jsonData)).thenCallRealMethod()

        val contractRecordId = UUID.randomUUID()
        val recordId = UUID.randomUUID()
        whenever(releaseService.newReleaseId(any()))
            .thenReturn(contractRecordId.toString())
            .thenReturn(recordId.toString())

        val releaseDate = LocalDateTime.now()
        val contractRecordJsonPath = "json/test/ac/treasury/clarification/contract_record_with_updated_contracts.json"
        val contractRecordWithUpdatedContracts = toObject(ContractRecord::class.java, loadJson(contractRecordJsonPath))
        val expectedContractRecord = contractRecordWithUpdatedContracts.copy(
            id = contractRecordId.toString(),
            date = releaseDate,
            tag = listOf(Tag.CONTRACT_UPDATE)
        )

        contractingService.treasuryClarificationAC(
            cpid = UUID.randomUUID().toString(),
            stage = "AC",
            releaseDate = releaseDate,
            data = toObject(JsonNode::class.java, loadJson(PATH_CN_JSON)),
            ocid = UUID.randomUUID().toString()
        )

        val contractRecordCaptor = argumentCaptor<ContractRecord>()
        verify(releaseService).saveContractRecord(
            cpId = any(),
            stage = any(),
            record = contractRecordCaptor.capture(),
            publishDate = any()
        )
        assertEquals(expectedContractRecord, contractRecordCaptor.firstValue)
    }

    @Test
    fun treasuryClarificationAC_saveRecordSuccess() {

        val releaseEntity = ReleaseEntity(
            cpId = UUID.randomUUID().toString(),
            ocId = UUID.randomUUID().toString(),
            publishDate = Date(),
            releaseDate = Date(),
            releaseId = UUID.randomUUID().toString(),
            stage = "PN",
            status = "active",
            jsonData = loadJson(PATH_JSON_FROM_RELEASE)
        )

        whenever(releaseService.getRecordEntity(any(), any())).thenReturn(releaseEntity)
        whenever(releaseService.getRecord(releaseEntity.jsonData)).thenCallRealMethod()

        val contractRecordId = UUID.randomUUID()
        val recordId = UUID.randomUUID()
        whenever(releaseService.newReleaseId(any()))
            .thenReturn(contractRecordId.toString())
            .thenReturn(recordId.toString())

        val releaseDate = LocalDateTime.now()
        val recordJsonPath = "json/test/ac/treasury/clarification/record_updated_with_cans.json"
        val recordUpdatedWithCans = toObject(Record::class.java, loadJson(recordJsonPath))
        val expectedRecord = recordUpdatedWithCans.copy(
            id = recordId.toString(),
            date = releaseDate,
            tag = listOf(Tag.TENDER_UPDATE)
        )

        contractingService.treasuryClarificationAC(
            cpid = UUID.randomUUID().toString(),
            stage = "AC",
            releaseDate = releaseDate,
            data = toObject(JsonNode::class.java, loadJson(PATH_CN_JSON)),
            ocid = UUID.randomUUID().toString()
        )

        val recordCaptor = argumentCaptor<Record>()
        verify(releaseService).saveRecord(
            cpId = any(),
            stage = any(),
            record = recordCaptor.capture(),
            publishDate = any()
        )
        assertEquals(expectedRecord, recordCaptor.firstValue)
    }
}