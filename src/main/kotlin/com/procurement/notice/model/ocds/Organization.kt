package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Organization @JsonCreator constructor(

        var id: String?,

        var name: String?,

        var identifier: Identifier?,

        var address: Address?,

        var additionalIdentifiers: HashSet<Identifier>?,

        var contactPoint: ContactPoint?,

        var details: Details?,

        var persones: HashSet<Person>?,

        var buyerProfile: String?,

        var roles: HashSet<PartyRole>

)