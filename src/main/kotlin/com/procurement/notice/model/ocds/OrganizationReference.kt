package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class OrganizationReference @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var identifier: Identifier?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var address: Address?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var additionalIdentifiers: HashSet<Identifier>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var contactPoint: ContactPoint?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var details: Details?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var buyerProfile: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var persones: HashSet<Person>?
)