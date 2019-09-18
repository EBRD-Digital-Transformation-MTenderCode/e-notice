package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class DesignContest @JsonCreator constructor(

    @get:JsonProperty("hasPrizes")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasPrizes: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val prizes: HashSet<Item>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val paymentsToParticipants: String?,

    @get:JsonProperty("serviceContractAward")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val serviceContractAward: Boolean?,

    @get:JsonProperty("juryDecisionBinding")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val juryDecisionBinding: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val juryMembers: HashSet<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val participants: HashSet<OrganizationReference>?
)
