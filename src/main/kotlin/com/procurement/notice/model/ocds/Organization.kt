package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Organization @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var name: String?,

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

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var persones: List<Person>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var buyerProfile: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: MutableList<PartyRole>
)
