package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Details @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val typeOfBuyer: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val typeOfSupplier: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val mainEconomicActivities: Set<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val mainGeneralActivity: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val mainSectoralActivity: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val permits: List<Permits>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bankAccounts: List<BankAccount>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val legalForm: LegalForm?,

    @get:JsonProperty("isACentralPurchasingBody")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isACentralPurchasingBody: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val nutsCode: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scale: String?
)

data class Permits @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val permitDetails: PermitDetails?
)

data class PermitDetails @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val issuedBy: Issue?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val issuedThought: Issue?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val validityPeriod: Period?
)

data class Issue @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String?
)

data class BankAccount @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val bankName: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val address: Address?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val identifier: AccountIdentifier?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val accountIdentification: AccountIdentifier?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalAccountIdentifiers: Set<AccountIdentifier>?
)

data class AccountIdentifier @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?
)

data class LegalForm @JsonCreator constructor(

    val id: String,

    val scheme: String,

    val description: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)