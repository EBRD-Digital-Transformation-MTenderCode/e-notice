package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

data class Organization @JsonCreator constructor(

        var id: String?,

        var name: String?,

        var identifier: Identifier?,

        var additionalIdentifiers: HashSet<Identifier>?,

        var address: Address?,

        var contactPoint: ContactPoint?,

        var roles: HashSet<PartyRole>,

        var details: Details?,

        var buyerProfile: String?
)