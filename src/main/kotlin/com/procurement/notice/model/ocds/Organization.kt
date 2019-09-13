package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class Organization @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var name: String?,

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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var persones: HashSet<Person>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var buyerProfile: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: HashSet<PartyRole>

)