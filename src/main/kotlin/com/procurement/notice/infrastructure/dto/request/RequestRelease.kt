package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.awards.RequestAward
import com.procurement.notice.infrastructure.dto.request.bids.RequestBids
import com.procurement.notice.infrastructure.dto.request.contracts.RequestContract
import com.procurement.notice.infrastructure.dto.request.invitation.RequestInvitation
import com.procurement.notice.infrastructure.dto.request.parties.RequestOrganization
import com.procurement.notice.infrastructure.dto.request.planning.RequestPlanning
import com.procurement.notice.infrastructure.dto.request.qualification.RequestQualification
import com.procurement.notice.infrastructure.dto.request.submissions.RequestSubmission
import com.procurement.notice.infrastructure.dto.request.tender.RequestTender
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Tag

data class RequestRelease(

    @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: String,

    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("tag") @param:JsonProperty("tag") val tag: List<Tag> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("initiationType") @param:JsonProperty("initiationType") val initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("language") @param:JsonProperty("language") val language: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: RequestPlanning?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("parties") @param:JsonProperty("parties") val parties: MutableList<RequestOrganization> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: RequestTender?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<RequestAward> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: RequestBids?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("contracts") @param:JsonProperty("contracts") val contracts: List<RequestContract> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("submissions") @param:JsonProperty("submissions") val submissions: RequestSubmission?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("qualifications") @param:JsonProperty("qualifications") val qualifications: List<RequestQualification> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<RequestAgreedMetric> = emptyList(),

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasPreviousNotice") @param:JsonProperty("hasPreviousNotice") val hasPreviousNotice: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("purposeOfNotice") @param:JsonProperty("purposeOfNotice") val purposeOfNotice: RequestPurposeOfNotice?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: MutableList<RequestRelatedProcess> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("preQualification") @param:JsonProperty("preQualification") val preQualification: RequestPreQualification?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("invitations") @param:JsonProperty("invitations") val invitations: List<RequestInvitation> = emptyList()
)
