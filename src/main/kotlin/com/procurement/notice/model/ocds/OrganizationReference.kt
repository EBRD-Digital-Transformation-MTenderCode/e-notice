package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrganizationReference @JsonCreator constructor(

        val name: String?,

        val id: String?,

        var identifier: Identifier?,

        var address: Address?,

        var additionalIdentifiers: HashSet<Identifier>?,

        var contactPoint: ContactPoint?,

        var details: Details?,

        var buyerProfile: String?
)