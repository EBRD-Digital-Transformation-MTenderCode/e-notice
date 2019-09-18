package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.ContractPlanning
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.TreasuryBudgetSource

data class UpdateAcDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val planning: ContractPlanning?,

    val award: Award,

    val contract: Contract,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val buyer: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val funders: HashSet<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val payers: HashSet<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val treasuryBudgetSources: List<TreasuryBudgetSource>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val addedEI: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val excludedEI: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val addedFS: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val excludedFS: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documentsOfContractPersones: List<DocumentBF>?
)
