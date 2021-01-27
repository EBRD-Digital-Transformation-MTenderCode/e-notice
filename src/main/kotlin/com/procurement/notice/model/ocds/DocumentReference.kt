package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty

data class DocumentReference(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String
)