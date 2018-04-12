package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.point.databinding.JsonDateDeserializer
import java.time.LocalDateTime

@JsonPropertyOrder("id", "source", "date", "value", "payer", "payee", "uri", "amount", "providerOrganization", "receiverOrganization")
data class Transaction(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("source")
        val source: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val date: LocalDateTime?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("payer")
        val payer: OrganizationReference?,

        @JsonProperty("payee")
        val payee: OrganizationReference?,

        @JsonProperty("uri")
        val uri: String?,

        @JsonProperty("amount")
        val amount: Value?,

        @JsonProperty("providerOrganization")
        val providerOrganization: Identifier?,

        @JsonProperty("receiverOrganization")
        val receiverOrganization: Identifier?
)
