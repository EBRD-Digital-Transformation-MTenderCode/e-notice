package com.procurement.notice.infrastructure.dto.tender.periodEnd

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class TenderPeriodEndRequestTest :
    AbstractDTOTestBase<TenderPeriodEndRequest>(TenderPeriodEndRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/tender/periodEnd/request/request_tender_period_end_ev_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/tender/periodEnd/request/request_tender_period_end_ev_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/tender/periodEnd/request/request_tender_period_end_ev_required_2.json")
    }

    @Test
    fun required3() {
        testBindingAndMapping("json/dto/tender/periodEnd/request/request_tender_period_end_ev_required_3.json")
    }
}
