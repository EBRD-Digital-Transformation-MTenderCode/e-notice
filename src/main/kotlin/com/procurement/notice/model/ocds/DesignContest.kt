package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DesignContest @JsonCreator constructor(

        @get:JsonProperty("hasPrizes")
        val hasPrizes: Boolean?,

        val prizes: HashSet<Item>?,

        val paymentsToParticipants: String?,

        @get:JsonProperty("serviceContractAward")
        val serviceContractAward: Boolean?,

        @get:JsonProperty("juryDecisionBinding")
        val juryDecisionBinding: Boolean?,

        val juryMembers: HashSet<OrganizationReference>?,

        val participants: HashSet<OrganizationReference>?
)
