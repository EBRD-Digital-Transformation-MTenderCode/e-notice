package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime

data class Ms @JsonCreator constructor(

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

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var planning: MsPlanning?,

    var tender: MsTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: MutableList<Organization> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: List<RelatedProcess>?
)
