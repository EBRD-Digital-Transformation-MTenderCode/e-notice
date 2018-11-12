package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.TreasuryBudgetSource
import com.procurement.notice.model.tender.record.ContractPlanning

data class UpdateAcDto @JsonCreator constructor(

        val awards: Award,

        val contracts: Contract,

        var planning: ContractPlanning?,

        var buyer: OrganizationReference?,

        val funders: HashSet<OrganizationReference>?,

        val payers: HashSet<OrganizationReference>?,

        var treasuryBudgetSources: List<TreasuryBudgetSource>?,

        val addedEI: Set<String>?,

        val excludedEI: Set<String>?,

        val addedFS: Set<String>?,

        val excludedFS: Set<String>?
)