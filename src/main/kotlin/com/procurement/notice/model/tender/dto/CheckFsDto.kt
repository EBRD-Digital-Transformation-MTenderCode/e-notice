package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.OrganizationReference

data class CheckFsDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val ei: List<String>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val buyer: List<OrganizationReference>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val funder: List<OrganizationReference>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val payer: List<OrganizationReference>
)
