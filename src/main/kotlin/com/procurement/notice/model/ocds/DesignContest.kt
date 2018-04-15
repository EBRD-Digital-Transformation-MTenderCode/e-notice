package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("hasPrizes", "prizes", "paymentsToParticipants", "serviceContractAward", "juryDecisionBinding", "juryMembers", "participants")
data class DesignContest(

        @JsonProperty("hasPrizes")
        @get:JsonProperty("hasPrizes")
        val hasPrizes: Boolean?,

        @JsonProperty("prizes")
        val prizes: HashSet<Item>?,

        @JsonProperty("paymentsToParticipants")
        val paymentsToParticipants: String?,

        @JsonProperty("serviceContractAward")
        @get:JsonProperty("serviceContractAward")
        val serviceContractAward: Boolean?,

        @JsonProperty("juryDecisionBinding")
        @get:JsonProperty("juryDecisionBinding")
        val juryDecisionBinding: Boolean?,

        @JsonProperty("juryMembers")
        val juryMembers: HashSet<OrganizationReference>?,

        @JsonProperty("participants")
        val participants: HashSet<OrganizationReference>?
)
