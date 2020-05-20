package com.procurement.notice.infrastructure.dto.contract

import com.fasterxml.jackson.annotation.JsonProperty

data class TreasuryRejectionResponse(
    @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: String,
    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String
)
