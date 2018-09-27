package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.*
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Record @JsonCreator constructor(

        var ocid: String?,

        var id: String?,

        var date: LocalDateTime?,

        var tag: List<Tag>?,

        var initiationType: InitiationType?,

        var parties: HashSet<Organization>?,

        var tender: RecordTender,

        var awards: HashSet<Award>?,

        var bids: Bids?,

        var contracts: HashSet<Contract>?,

        @get:JsonProperty("hasPreviousNotice")
        var hasPreviousNotice: Boolean?,

        var purposeOfNotice: PurposeOfNotice?,

        var relatedProcesses: HashSet<RelatedProcess>?
)
