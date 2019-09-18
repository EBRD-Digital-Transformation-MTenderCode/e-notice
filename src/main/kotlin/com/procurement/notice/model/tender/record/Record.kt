package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
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
import java.util.*

data class Record @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var ocid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var tag: List<Tag>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: HashSet<Organization>?,

    var tender: RecordTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var awards: HashSet<Award>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var bids: Bids?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var contracts: HashSet<Contract>?,

    @get:JsonProperty("hasPreviousNotice")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var hasPreviousNotice: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var purposeOfNotice: PurposeOfNotice?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>?
)
