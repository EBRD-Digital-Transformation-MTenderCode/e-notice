package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "id", "identifier", "address", "additionalIdentifiers", "contactPoint", "details", "buyerProfile")
data class OrganizationReference(

        @JsonProperty("name")
        val name: String?,

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("identifier")
        var identifier: Identifier?,

        @JsonProperty("address")
        var address: Address?,

        @JsonProperty("additionalIdentifiers")
        var additionalIdentifiers: HashSet<Identifier>?,

        @JsonProperty("contactPoint")
        var contactPoint: ContactPoint?,

        @JsonProperty("details")
        var details: Details?,

        @JsonProperty("buyerProfile")
        var buyerProfile: String?
)