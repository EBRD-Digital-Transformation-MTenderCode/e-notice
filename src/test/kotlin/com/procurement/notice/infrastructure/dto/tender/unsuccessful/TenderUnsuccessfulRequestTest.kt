package com.procurement.notice.infrastructure.dto.tender.unsuccessful

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class TenderUnsuccessfulRequestTest :
    AbstractDTOTestBase<TenderUnsuccessfulRequest>(TenderUnsuccessfulRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/tender/unsuccessful/request/request_tender_unsuccessful_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/tender/unsuccessful/request/request_tender_unsuccessful_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/tender/unsuccessful/request/request_tender_unsuccessful_required_2.json")
    }

    @Test
    fun required3() {
        testBindingAndMapping("json/dto/tender/unsuccessful/request/request_tender_unsuccessful_required_3.json")
    }

    @Test
    fun required4() {
        testBindingAndMapping("json/dto/tender/unsuccessful/request/request_tender_unsuccessful_required_4.json")
    }
}
