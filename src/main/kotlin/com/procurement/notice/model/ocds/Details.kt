package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: String?,

        val typeOfSupplier: String?,

        val mainEconomicActivities: Set<String>?,

        val mainGeneralActivity: String?,

        val mainSectoralActivity: String?,

        val permits: List<Permits>?,

        val bankAccounts: List<BankAccount>?,

        val legalForm: LegalForm?,

        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        val scale: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Permits @JsonCreator constructor(

        val id: String?,

        val scheme: String?,

        val url: String?,

        val permit: Permit?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Permit @JsonCreator constructor(

        val issuedBy: Issue?,

        val issuedThought: Issue?,

        val validityPeriod: Period?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Issue @JsonCreator constructor(

        val id: String?,

        val name: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BankAccount @JsonCreator constructor(

        val description: String?,

        val bankName: String?,

        val address: Address?,

        val identifier: AccountIdentifier?,

        val accountIdentification: AccountIdentifier?,

        val additionalAccountIdentifiers: Set<AccountIdentifier>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountIdentifier @JsonCreator constructor(

        val id: String?,

        val scheme: String?
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class LegalForm @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val description: String,

        val uri: String?
)