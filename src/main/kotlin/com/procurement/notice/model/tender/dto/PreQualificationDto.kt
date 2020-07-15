package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Period

class PreQualificationDto(@field: JsonProperty("period") @param:JsonProperty("period") val period: Period)