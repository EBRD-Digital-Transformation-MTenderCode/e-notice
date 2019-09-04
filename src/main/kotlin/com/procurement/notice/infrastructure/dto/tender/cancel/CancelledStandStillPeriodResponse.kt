package com.procurement.notice.infrastructure.dto.tender.cancel

import com.fasterxml.jackson.annotation.JsonProperty

data class CancelledStandStillPeriodResponse(
    @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: String,
    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String,
    @field:JsonProperty("amendmentsIds") @param:JsonProperty("amendmentsIds") val amendmentsIds: List<String>
)