package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.contract.ContractTenderLot
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.ContractTerm

data class CreateAcDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val cans: HashSet<Can>,

    val contract: Contract,

    val contractedAward: Award,

    val contractTerm: ContractTerm,

    val contractedTender: CreateAcTender
)

data class CreateAcTender @JsonCreator constructor(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    var classification: Classification,

    val procurementMethod: String,

    val procurementMethodDetails: String,

    val mainProcurementCategory: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lots: HashSet<ContractTenderLot>

)
