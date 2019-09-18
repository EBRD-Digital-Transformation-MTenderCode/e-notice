package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatusDetails

data class Params(
    var statusDetails: TenderStatusDetails = TenderStatusDetails.PRESELECTION,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var tag: List<Tag> = listOf(),

    var isACallForCompetition: Boolean = false,
    var relatedProcessType: RelatedProcessType = RelatedProcessType.X_PRESELECTION
)
