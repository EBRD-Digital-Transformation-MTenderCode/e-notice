package com.procurement.notice.infrastructure.dto.award

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class StartAwardPeriodRequestTest : AbstractDTOTestBase<StartAwardPeriodRequest>(StartAwardPeriodRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/award/startperiod/request_start_award_period_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/award/startperiod/request_start_award_period_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/award/startperiod/request_start_award_period_required_2.json")
    }
}
