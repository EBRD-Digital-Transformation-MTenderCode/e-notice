package com.procurement.notice.application.service.cn

data class UpdatedCN(
    val cpid: String,
    val ocid: String,
    val amendment: Amendment?
) {
    data class Amendment(
        val id: String
    )
}
