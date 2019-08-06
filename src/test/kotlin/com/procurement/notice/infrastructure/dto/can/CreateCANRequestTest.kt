package com.procurement.notice.infrastructure.dto.can

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class CreateCANRequestTest : AbstractDTOTestBase<CreateCANRequest>(CreateCANRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/can/create/request/request_create_can_full.json")
    }
}
