package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "scheme", "relationship", "objectOfProcurementInPIN", "uri")
data class RelatedNotice(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("scheme")
        val scheme: RelatedNoticeScheme?,

        @JsonProperty("relationship")
        val relationship: Relationship?,

        @JsonProperty("objectOfProcurementInPIN")
        val objectOfProcurementInPIN: String?,

        @JsonProperty("uri")
        val uri: String?
)

