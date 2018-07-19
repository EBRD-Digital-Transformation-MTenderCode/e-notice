package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.OrganizationReference

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CheckFsDto @JsonCreator constructor(

        val ei: HashSet<String>,

        val buyer: HashSet<OrganizationReference>,

        val funder: HashSet<OrganizationReference>,

        val payer: HashSet<OrganizationReference>
)
