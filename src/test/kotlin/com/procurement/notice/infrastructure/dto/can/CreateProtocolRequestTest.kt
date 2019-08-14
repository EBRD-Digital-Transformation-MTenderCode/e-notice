package com.procurement.notice.infrastructure.dto.can

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class CreateProtocolRequestTest : AbstractDTOTestBase<CreateProtocolRequest>(CreateProtocolRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/can/create/request/request_create_protocol_full.json")
    }
}
