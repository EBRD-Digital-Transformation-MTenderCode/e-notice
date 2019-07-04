package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.ContractPlanning
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.TreasuryBudgetSource

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class UpdateAcDto @JsonCreator constructor(

        val planning: ContractPlanning?,

        val award: Award,

        val contract: Contract,

        val buyer: OrganizationReference?,

        val funders: HashSet<OrganizationReference>?,

        val payers: HashSet<OrganizationReference>?,

        val treasuryBudgetSources: List<TreasuryBudgetSource>?,

        val addedEI: Set<String>?,

        val excludedEI: Set<String>?,

        val addedFS: Set<String>?,

        val excludedFS: Set<String>?,

        val documentsOfContractPersones: List<DocumentBF>?
)