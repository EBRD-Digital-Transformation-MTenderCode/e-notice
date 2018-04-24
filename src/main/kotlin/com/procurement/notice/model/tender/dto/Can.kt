package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Contract

data class Can(

        @JsonProperty("contracts")
        val contracts: HashSet<Contract>
)
