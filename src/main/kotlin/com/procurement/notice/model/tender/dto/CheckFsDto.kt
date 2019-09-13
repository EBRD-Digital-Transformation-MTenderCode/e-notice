package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.OrganizationReference

data class CheckFsDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val ei: HashSet<String>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val buyer: HashSet<OrganizationReference>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val funder: HashSet<OrganizationReference>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val payer: HashSet<OrganizationReference>
)
