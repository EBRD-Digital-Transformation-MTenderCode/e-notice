package com.procurement.notice.infrastructure.dto.award

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class AwardConsiderationRequestTest : AbstractDTOTestBase<AwardConsiderationRequest>(AwardConsiderationRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/award/consideration/request_award_consideration_full.json")
    }
}