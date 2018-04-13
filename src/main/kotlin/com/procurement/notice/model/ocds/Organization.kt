package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "id", "identifier", "additionalIdentifiers", "address", "contactPoint", "roles", "details", "buyerProfile")
data class Organization(

        @JsonProperty("id") val id: String?,

        @JsonProperty("name")
        val name: String?,

        @JsonProperty("identifier")
        val identifier: Identifier?,

        @JsonProperty("additionalIdentifiers")
        val additionalIdentifiers: HashSet<Identifier>?,

        @JsonProperty("address")
        val address: Address?,

        @JsonProperty("contactPoint")
        val contactPoint: ContactPoint?,

        @JsonProperty("roles")
        val roles: HashSet<PartyRole>,

        @JsonProperty("details")
        val details: Details?,

        @JsonProperty("buyerProfile")
        val buyerProfile: String?
)