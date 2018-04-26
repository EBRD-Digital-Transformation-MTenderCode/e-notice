package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.OrganizationReference

@JsonPropertyOrder("ei", "buyer", "funder", "payer")
data class CheckFsDto(

        @JsonProperty("ei")
        val ei: HashSet<String>,

        @JsonProperty("buyer")
        val buyer: HashSet<OrganizationReference>,

        @JsonProperty("funder")
        val funder: HashSet<OrganizationReference>,

        @JsonProperty("payer")
        val payer: HashSet<OrganizationReference>
)
