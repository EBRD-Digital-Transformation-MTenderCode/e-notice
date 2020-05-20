package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Details @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val typeOfBuyer: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val typeOfSupplier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val mainEconomicActivities: Set<MainEconomicActivity>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val mainGeneralActivity: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val mainSectoralActivity: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val permits: List<Permits>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bankAccounts: List<BankAccount>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val legalForm: LegalForm?,

    @get:JsonProperty("isACentralPurchasingBody")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val isACentralPurchasingBody: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val nutsCode: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scale: String?
)

data class Permits @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val permitDetails: PermitDetails?
)

data class PermitDetails @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val issuedBy: Issue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val issuedThought: Issue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val validityPeriod: Period?
)

data class Issue @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?
)

data class BankAccount @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val bankName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val address: Address?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val identifier: AccountIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val accountIdentification: AccountIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalAccountIdentifiers: Set<AccountIdentifier>?
)

data class AccountIdentifier @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?
)

data class LegalForm @JsonCreator constructor(

    val id: String,

    val scheme: String,

    val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
