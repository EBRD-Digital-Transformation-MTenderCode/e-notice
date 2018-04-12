package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "name")
data class EnquiryAuthor(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("name")
        val name: String?
)
