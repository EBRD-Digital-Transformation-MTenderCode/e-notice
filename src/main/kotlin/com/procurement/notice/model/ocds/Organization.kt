package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "id", "identifier", "additionalIdentifiers", "address", "contactPoint", "roles", "details", "buyerProfile")
data class Organization(

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("name")
        var name: String?,

        @JsonProperty("identifier")
        var identifier: Identifier?,

        @JsonProperty("additionalIdentifiers")
        var additionalIdentifiers: HashSet<Identifier>?,

        @JsonProperty("address")
        var address: Address?,

        @JsonProperty("contactPoint")
        var contactPoint: ContactPoint?,

        @JsonProperty("roles")
        var roles: HashSet<PartyRole>,

        @JsonProperty("details")
        var details: Details?,

        @JsonProperty("buyerProfile")
        var buyerProfile: String?
)