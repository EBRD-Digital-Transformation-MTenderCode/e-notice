package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class OrganizationReference @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var identifier: Identifier?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var address: Address?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var additionalIdentifiers: List<Identifier>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var contactPoint: ContactPoint?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var details: Details?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var buyerProfile: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var persones: List<Person>?
)
