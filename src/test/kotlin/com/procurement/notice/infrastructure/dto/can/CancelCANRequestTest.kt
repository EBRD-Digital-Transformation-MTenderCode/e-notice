package com.procurement.notice.infrastructure.dto.can

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class CancelCANRequestTest : AbstractDTOTestBase<CancelCANRequest>(CancelCANRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/can/cancel/without_contract/request/request_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/can/cancel/without_contract/request/request_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/can/cancel/without_contract/request/request_required_2.json")
    }
}
