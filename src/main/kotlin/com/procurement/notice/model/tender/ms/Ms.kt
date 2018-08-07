package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Ms @JsonCreator constructor(

        var ocid: String?,

        var id: String?,

        var date: LocalDateTime?,

        var tag: List<Tag>?,

        var initiationType: InitiationType?,

        val language: String? = "ro",

        var planning: MsPlanning?,

        var tender: MsTender,

        var parties: HashSet<Organization>?,

        var relatedProcesses: HashSet<RelatedProcess>?
)
