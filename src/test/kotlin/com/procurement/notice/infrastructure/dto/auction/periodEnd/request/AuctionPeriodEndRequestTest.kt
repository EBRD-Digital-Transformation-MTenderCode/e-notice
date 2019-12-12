package com.procurement.notice.infrastructure.dto.auction.periodEnd.request

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class AuctionPeriodEndRequestTest :
    AbstractDTOTestBase<AuctionPeriodEndRequest>(AuctionPeriodEndRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/auction/periodEnd/request/request_auction_period_end_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/auction/periodEnd/request/request_auction_period_end_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/auction/periodEnd/request/request_auction_period_end_required_2.json")
    }

    @Test
    fun required3() {
        testBindingAndMapping("json/dto/auction/periodEnd/request/request_auction_period_end_required_3.json")
    }
}
