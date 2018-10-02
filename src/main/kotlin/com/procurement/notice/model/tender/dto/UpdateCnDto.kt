package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateCnDto @JsonCreator constructor(

        var amendments: List<Amendment>
)
