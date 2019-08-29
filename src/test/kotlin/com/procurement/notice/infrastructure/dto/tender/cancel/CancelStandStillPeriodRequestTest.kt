package com.procurement.notice.infrastructure.dto.tender.cancel

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class CancelStandStillPeriodRequestTest :
    AbstractDTOTestBase<CancelStandStillPeriodRequest>(CancelStandStillPeriodRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/tender/cancel/standstill/request/request_cancel_standstill_period_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/tender/cancel/standstill/request/request_cancel_standstill_period_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/tender/cancel/standstill/request/request_cancel_standstill_period_required_2.json")
    }
}
