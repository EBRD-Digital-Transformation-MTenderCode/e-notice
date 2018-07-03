package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.OrganizationReference

data class CheckFsDto @JsonCreator constructor(

        val ei: HashSet<String>,

        val buyer: HashSet<OrganizationReference>,

        val funder: HashSet<OrganizationReference>,

        val payer: HashSet<OrganizationReference>
)
