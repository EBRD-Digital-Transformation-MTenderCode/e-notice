package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class DesignContest @JsonCreator constructor(

    @get:JsonProperty("hasPrizes")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasPrizes: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val prizes: HashSet<Item>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val paymentsToParticipants: String?,

    @get:JsonProperty("serviceContractAward")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val serviceContractAward: Boolean?,

    @get:JsonProperty("juryDecisionBinding")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val juryDecisionBinding: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val juryMembers: HashSet<OrganizationReference>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val participants: HashSet<OrganizationReference>?
)
