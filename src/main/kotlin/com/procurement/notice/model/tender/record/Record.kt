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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var ocid: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var tag: List<Tag>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var initiationType: InitiationType?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: HashSet<Organization>?,

    var tender: RecordTender,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var awards: HashSet<Award>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var bids: Bids?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var contracts: HashSet<Contract>?,

    @get:JsonProperty("hasPreviousNotice")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var hasPreviousNotice: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var purposeOfNotice: PurposeOfNotice?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>?
)
