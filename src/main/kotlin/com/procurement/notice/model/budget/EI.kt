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

data class EI @JsonCreator constructor(

    val ocid: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var tag: List<Tag>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var tender: Tender?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val buyer: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: List<Organization>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var planning: EiPlanning?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: List<RelatedProcess>?
)
