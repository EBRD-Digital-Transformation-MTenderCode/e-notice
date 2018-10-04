package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.*
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FS @JsonCreator constructor(

        val ocid: String,

        var id: String?,

        var date: LocalDateTime?,

        var tag: List<Tag>?,

        var initiationType: InitiationType?,

        var title: String?,

        val description: String?,

        var tender: Tender?,

        var funder: OrganizationReference?,

        var payer: OrganizationReference?,

        var parties: HashSet<Organization>?,

        var planning: FsPlanning?,

        var relatedProcesses: HashSet<RelatedProcess>?
)
