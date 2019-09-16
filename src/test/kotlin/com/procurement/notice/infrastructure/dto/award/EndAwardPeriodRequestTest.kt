package com.procurement.notice.infrastructure.dto.award

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class EndAwardPeriodRequestTest : AbstractDTOTestBase<EndAwardPeriodRequest>(EndAwardPeriodRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/award/endperiod/request_end_award_period_fully.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/award/endperiod/request_end_award_period_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/award/endperiod/request_end_award_period_required_2.json")
    }
}
