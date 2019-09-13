package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime

data class Ms @JsonCreator constructor(

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var planning: MsPlanning?,

    var tender: MsTender,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var parties: HashSet<Organization>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>?
)
