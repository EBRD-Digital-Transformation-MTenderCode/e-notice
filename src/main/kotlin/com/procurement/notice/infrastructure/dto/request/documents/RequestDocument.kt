package com.procurement.notice.infrastructure.dto.request.documents

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class RequestDocument(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("url") @param:JsonProperty("url") val url: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dateModified") @param:JsonProperty("dateModified") val dateModified: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("format") @param:JsonProperty("format") val format: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("language") @param:JsonProperty("language") val language: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedConfirmations") @param:JsonProperty("relatedConfirmations") val relatedConfirmations: List<String> = emptyList()
)
