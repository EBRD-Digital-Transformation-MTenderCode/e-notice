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

data class FS @JsonCreator constructor(

    val ocid: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var tag: List<Tag>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var initiationType: InitiationType?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var tender: Tender?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var funder: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var payer: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: HashSet<Organization>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var planning: FsPlanning?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>?
)
