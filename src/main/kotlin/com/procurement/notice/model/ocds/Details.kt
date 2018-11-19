package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Details @JsonCreator constructor(

        val typeOfBuyer: String?,

        val typeOfSupplier: String?,

        val mainEconomicActivity: Set<String>?,

        val mainGeneralActivity: String?,

        val mainSectoralActivity: String?,

        val permits: List<Permits>?,

        val gpaProfile: GpaProfile?,

        val bankAccounts: List<BankAccount>?,

        val legalForm: LegalForm?,

        @get:JsonProperty("isACentralPurchasingBody")
        val isACentralPurchasingBody: Boolean?,

        val nutsCode: String?,

        val scale: String?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Permits @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val url: String,

        val permit: Permit
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Permit @JsonCreator constructor(

        val issuedBy: Issue,

        val issuedThought: Issue,

        val validityPeriod: Period
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Issue @JsonCreator constructor(

        val id: String,

        val name: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaProfile @JsonCreator constructor(

        val gpaAnnex: GpaAnnex,

        val gpaOrganizationType: GpaOrganizationType,

        val gpaThresholds: Set<GpaThreshold>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaAnnex @JsonCreator constructor(

        val id: String,

        val legalName: String,

        val uri: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaOrganizationType @JsonCreator constructor(

        val id: String,

        val legalName: String,

        val uri: String
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GpaThreshold @JsonCreator constructor(

        val mainProcurementCategory: String,

        val value: Value
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BankAccount @JsonCreator constructor(

        val description: String,

        val bankName: String,

        val address: Address,

        val identifier: AccountIdentifier,

        val accountIdentification: AccountIdentifier,

        val additionalAccountIdentifiers: Set<AccountIdentifier>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AccountIdentifier @JsonCreator constructor(

        val id: String,

        val scheme: String
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class LegalForm @JsonCreator constructor(

        val id: String,

        val scheme: String,

        val description: String,

        val uri: String?
)