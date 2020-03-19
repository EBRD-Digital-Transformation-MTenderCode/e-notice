package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.PurposeOfNotice
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime

data class Release (

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("ocid") @param:JsonProperty("ocid") var ocid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("tag") @param:JsonProperty("tag") var tag: List<Tag> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("initiationType") @param:JsonProperty("initiationType") var initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("parties") @param:JsonProperty("parties") var parties: MutableList<Organization> = mutableListOf(),

    @field:JsonProperty("tender") @param:JsonProperty("tender") var tender: ReleaseTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("awards") @param:JsonProperty("awards") var awards: List<Award> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bids") @param:JsonProperty("bids") var bids: Bids?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("contracts") @param:JsonProperty("contracts") var contracts: List<Contract> = emptyList(),

    @get:JsonProperty("hasPreviousNotice")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var hasPreviousNotice: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("purposeOfNotice") @param:JsonProperty("purposeOfNotice") var purposeOfNotice: PurposeOfNotice?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") var relatedProcesses: MutableList<RelatedProcess> = mutableListOf()
)
