package com.procurement.notice.application.service.record

import com.procurement.notice.application.model.record.create.CreateRecordParams
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.MaybeFail

interface CreateRecordService {
    fun createRecord(params: CreateRecordParams): MaybeFail<Fail>
}