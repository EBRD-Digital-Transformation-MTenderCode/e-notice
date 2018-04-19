package com.procurement.notice.model.tender.record

import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatusDetails


data class Params(
        var statusDetails: TenderStatusDetails? = null,
        var tag: List<Tag>? = null,
        var isACallForCompetition: Boolean? = null,
        var relatedProcessType: RelatedProcessType? = null
)