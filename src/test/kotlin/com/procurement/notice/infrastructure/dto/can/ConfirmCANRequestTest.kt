package com.procurement.notice.infrastructure.dto.can

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class ConfirmCANRequestTest : AbstractDTOTestBase<ConfirmCANRequest>(ConfirmCANRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/can/confirm/request/request_confirm_can_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/can/confirm/request/request_confirm_can_required_1.json")
    }
}
