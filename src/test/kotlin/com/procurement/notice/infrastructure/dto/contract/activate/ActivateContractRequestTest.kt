package com.procurement.notice.infrastructure.dto.contract.activate

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import com.procurement.notice.infrastructure.dto.contract.ActivateContractRequest
import org.junit.jupiter.api.Test

class ActivateContractRequestTest : AbstractDTOTestBase<ActivateContractRequest>(ActivateContractRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/contract/activate/request/request_activate_contract_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/contract/activate/request/request_activate_contract_required_1.json")
    }
}
