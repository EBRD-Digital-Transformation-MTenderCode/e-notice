package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SuspendTenderDto(

        @JsonProperty("tender")
        val tender: TenderDto
)