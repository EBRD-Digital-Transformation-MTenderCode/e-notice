package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AwardPeriodEndEvDto @JsonCreator constructor(

        val awardPeriod: Period,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val awards: HashSet<Award>,

        val cans: HashSet<Can>,

        val contracts: HashSet<Contract>,

        val contractTerms: Set<ContractTerm>,

        val documents: HashSet<Document>?
)
