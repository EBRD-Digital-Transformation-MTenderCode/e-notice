package com.procurement.notice.infrastructure.dto.award.auction

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class StartAwardPeriodAuctionRequestTest : AbstractDTOTestBase<StartAwardPeriodAuctionRequest>(StartAwardPeriodAuctionRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/award/startperiod/auction/request_start_award_period_auction_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/award/startperiod/auction/request_start_award_period_auction_required_1.json")
    }
}
