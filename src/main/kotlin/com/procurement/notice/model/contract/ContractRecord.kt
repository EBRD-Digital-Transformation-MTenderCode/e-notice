package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ContractRecord @JsonCreator constructor(

        var ocid: String?,

        var id: String?,

        var date: LocalDateTime?,

        var tag: List<Tag>?,

        var initiationType: InitiationType? = null,

        var planning: ContractPlanning? = null,

        var tender: ContractTender? = null,

        var awards: HashSet<Award>? = null,

        var contracts: HashSet<Contract>? = null,

        var parties: HashSet<Organization>? = null,

        var relatedProcesses: HashSet<RelatedProcess>? = null
)
