package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.awards.RequestAward
import com.procurement.notice.infrastructure.dto.request.bids.RequestBids
import com.procurement.notice.infrastructure.dto.request.contracts.RequestContract
import com.procurement.notice.infrastructure.dto.request.parties.RequestOrganization
import com.procurement.notice.infrastructure.dto.request.planning.RequestPlanning
import com.procurement.notice.infrastructure.dto.request.tender.RequestTender
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime

data class RequestRelease(

    @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: String,

    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonProperty("tag") @param:JsonProperty("tag") val tag: List<Tag>,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("initiationType") @param:JsonProperty("initiationType") val initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: RequestPlanning?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("parties") @param:JsonProperty("parties") val parties: MutableList<RequestOrganization> = mutableListOf(),

    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: RequestTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<RequestAward> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: RequestBids?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("contracts") @param:JsonProperty("contracts") val contracts: List<RequestContract> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<RequestAgreedMetric> = emptyList(),

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasPreviousNotice") @param:JsonProperty("hasPreviousNotice") val hasPreviousNotice: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("purposeOfNotice") @param:JsonProperty("purposeOfNotice") val purposeOfNotice: RequestPurposeOfNotice?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: MutableList<RequestRelatedProcess> = mutableListOf()
)
