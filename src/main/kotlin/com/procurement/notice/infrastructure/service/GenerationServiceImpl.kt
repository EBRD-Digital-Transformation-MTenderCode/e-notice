package com.procurement.notice.infrastructure.service

import com.procurement.notice.application.service.GenerationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class GenerationServiceImpl : GenerationService {
    override fun generateAmendmentId(): UUID {
        return UUID.randomUUID()
    }
}