package com.procurement.notice.service.contract

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationContext
import com.procurement.notice.application.service.contract.clarify.TreasuryClarificationData
import com.procurement.notice.dao.BudgetDao
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.json.loadJson
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.infrastructure.dto.contract.TreasuryClarificationRequest
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

        val context = TreasuryClarificationContext(
            cpid = releaseEntity.cpId,
            stage = "AC",
            releaseDate = releaseDate,
            ocid = releaseEntity.ocId
        )

        val request = toObject(TreasuryClarificationRequest::class.java, loadJson(PATH_CN_JSON))
        val data = fromRequestToData(request)

        contractingService.treasuryClarificationAC(context = context, data = data)

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

        val context = TreasuryClarificationContext(
            cpid = releaseEntity.cpId,
            stage = "AC",
            releaseDate = releaseDate,
            ocid = releaseEntity.ocId
        )

        val request = toObject(TreasuryClarificationRequest::class.java, loadJson(PATH_CN_JSON))
        val data = fromRequestToData(request)

        contractingService.treasuryClarificationAC(context = context, data = data)

        val recordCaptor = argumentCaptor<Record>()
        verify(releaseService).saveRecord(
            cpId = any(),
            stage = any(),
            record = recordCaptor.capture(),
            publishDate = any()
        )
        assertEquals(expectedRecord, recordCaptor.firstValue)
    }

    private fun fromRequestToData(clarificationRequest: TreasuryClarificationRequest) = TreasuryClarificationData(
        contract = clarificationRequest.contract.let { contract ->
            TreasuryClarificationData.Contract(
                id = contract.id,
                documents = contract.documents.map { document ->
                    TreasuryClarificationData.Contract.Document(
                        documentType = document.documentType,
                        relatedConfirmations = document.relatedConfirmations,
                        relatedLots = document.relatedLots,
                        description = document.description,
                        title = document.title,
                        id = document.id,
                        url = document.url,
                        datePublished = document.datePublished
                    )
                },
                title = contract.title,
                description = contract.description,
                period = contract.period.let { period ->
                    TreasuryClarificationData.Contract.Period(
                        startDate = period.startDate,
                        endDate = period.endDate
                    )
                },
                value = contract.value.let { value ->
                    TreasuryClarificationData.Contract.Value(
                        amount = value.amount,
                        currency = value.currency,
                        amountNet = value.amountNet,
                        valueAddedTaxincluded = value.valueAddedTaxincluded
                    )
                },
                awardID = contract.awardID,
                confirmationRequests = contract.confirmationRequests.map { confirmationRequest ->
                    TreasuryClarificationData.Contract.ConfirmationRequest(
                        title = confirmationRequest.title,
                        description = confirmationRequest.description,
                        id = confirmationRequest.id,
                        relatedItem = confirmationRequest.relatedItem,
                        relatesTo = confirmationRequest.relatesTo,
                        requestGroups = confirmationRequest.requestGroups.map { requestGroup ->
                            TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup(
                                id = requestGroup.id,
                                requests = requestGroup.requests.map { request ->
                                    TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup.Request(
                                        id = request.id,
                                        description = request.description,
                                        title = request.title,
                                        relatedPerson = request.relatedPerson?.let { relatedPerson ->
                                            TreasuryClarificationData.Contract.ConfirmationRequest.RequestGroup.Request.RelatedPerson(
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
                confirmationResponses = contract.confirmationResponses.map { confirmationResponse ->
                    TreasuryClarificationData.Contract.ConfirmationResponse(
                        id = confirmationResponse.id,
                        value = confirmationResponse.value.let { value ->
                            TreasuryClarificationData.Contract.ConfirmationResponse.Value(
                                name = value.name,
                                id = value.id,
                                relatedPerson = value.relatedPerson?.let { relatedPerson ->
                                    TreasuryClarificationData.Contract.ConfirmationResponse.Value.RelatedPerson(
                                        id = relatedPerson.id,
                                        name = relatedPerson.name
                                    )
                                },
                                date = value.date,
                                verification = value.verification.map { verification ->
                                    TreasuryClarificationData.Contract.ConfirmationResponse.Value.Verification(
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
                date = contract.date,
                milestones = contract.milestones.map { milestone ->
                    TreasuryClarificationData.Contract.Milestone(
                        id = milestone.id,
                        type = milestone.type,
                        title = milestone.title,
                        description = milestone.description,
                        additionalInformation = milestone.additionalInformation,
                        dateMet = milestone.dateMet,
                        dateModified = milestone.dateModified,
                        dueDate = milestone.dueDate,
                        relatedItems = milestone.relatedItems,
                        relatedParties = milestone.relatedParties.map { relatedParty ->
                            TreasuryClarificationData.Contract.Milestone.RelatedParty(
                                id = relatedParty.id,
                                name = relatedParty.name
                            )
                        },
                        status = milestone.status
                    )
                },
                status = contract.status,
                statusDetails = contract.statusDetails
            )
        },
        cans = clarificationRequest.cans.map { can ->
            TreasuryClarificationData.Can(
                id = can.id,
                statusDetails = can.statusDetails,
                status = can.status
            )
        }
    )
}