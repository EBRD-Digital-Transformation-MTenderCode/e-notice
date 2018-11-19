package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.contract.ContractPlanning

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

        val documentsOfContractPersones: Set<DocumentBF>?
)