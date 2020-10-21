package com.procurement.notice.infrastructure.service.update

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import com.procurement.notice.infrastructure.handler.record.update.UpdateRecordRequest
import org.junit.jupiter.api.Test

class UpdateRecordRequestTest : AbstractDTOTestBase<UpdateRecordRequest>(
    UpdateRecordRequest::class.java
) {
    @Test
    fun fully() {
        testBindingAndMapping(pathToJsonFile = "json/dto/record/update/request_update_record_full.json")
    }
}
