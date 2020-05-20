package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class SecondStage(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("minimumCandidates") @param:JsonProperty("minimumCandidates") val minimumCandidates: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumCandidates") @param:JsonProperty("maximumCandidates") val maximumCandidates: Int?
)
