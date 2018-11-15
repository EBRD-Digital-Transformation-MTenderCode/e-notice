package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.*
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
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
