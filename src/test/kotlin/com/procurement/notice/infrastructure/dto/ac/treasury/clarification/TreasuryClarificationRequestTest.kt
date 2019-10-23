package com.procurement.notice.infrastructure.dto.ac.treasury.clarification

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import com.procurement.notice.model.contract.dto.TreasuryClarificationRequest
import org.junit.jupiter.api.Test

class TreasuryClarificationRequestTest : AbstractDTOTestBase<TreasuryClarificationRequest>(TreasuryClarificationRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/ac/treasury/clarification/request_clarification_treasury_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/ac/treasury/clarification/request_clarification_treasury_required_1.json")
    }
}