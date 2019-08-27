package com.procurement.notice.infrastructure.dto.award

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class EvaluateAwardRequestTest : AbstractDTOTestBase<EvaluateAwardRequest>(EvaluateAwardRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/award/evaluate/request_evaluate_award_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/award/evaluate/request_evaluate_award_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/award/evaluate/request_evaluate_award_required_2.json")
    }
}
