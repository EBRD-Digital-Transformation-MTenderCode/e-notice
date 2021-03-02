package com.procurement.notice.infrastructure.dto.cn.update

import com.procurement.notice.infrastructure.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class UpdateCNRequestTest : AbstractDTOTestBase<UpdateCNRequest>(UpdateCNRequest::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/dto/cn/update/request_cn_update_full.json")
    }

    @Test
    fun required1() {
        testBindingAndMapping("json/dto/cn/update/request_cn_update_required_1.json")
    }

    @Test
    fun required2() {
        testBindingAndMapping("json/dto/cn/update/request_cn_update_required_2.json")
    }

    @Test
    fun required3() {
        testBindingAndMapping("json/dto/cn/update/request_cn_update_required_3.json")
    }
}
