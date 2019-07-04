package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Tender
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
