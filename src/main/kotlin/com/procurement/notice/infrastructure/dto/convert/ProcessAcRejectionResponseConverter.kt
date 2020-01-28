package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionResult
import com.procurement.notice.infrastructure.dto.contract.TreasuryRejectionResponse

fun TreasuryRejectionResult.convert(): TreasuryRejectionResponse {
    return TreasuryRejectionResponse(
        cpid = this.cpid,
        ocid = this.ocid
    )
}
