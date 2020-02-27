package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestOrganizationReference

data class RequestDesignContest(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasPrizes") @param:JsonProperty("hasPrizes") val hasPrizes: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("prizes") @param:JsonProperty("prizes") val prizes: List<RequestItem> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("paymentsToParticipants") @param:JsonProperty("paymentsToParticipants") val paymentsToParticipants: String?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("serviceContractAward") @param:JsonProperty("serviceContractAward") val serviceContractAward: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("juryDecisionBinding") @param:JsonProperty("juryDecisionBinding") val juryDecisionBinding: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("juryMembers") @param:JsonProperty("juryMembers") val juryMembers: List<RequestOrganizationReference> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("participants") @param:JsonProperty("participants") val participants: List<RequestOrganizationReference> = emptyList()
)
