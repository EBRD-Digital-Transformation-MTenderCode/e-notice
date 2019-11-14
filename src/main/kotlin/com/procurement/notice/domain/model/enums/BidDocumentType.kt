package com.procurement.notice.domain.model.enums

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException

enum class BidDocumentType(@JsonValue val value: String) {
    SUBMISSION_DOCUMENTS("submissionDocuments"),
    ELIGIBILITY_DOCUMENTS("x_eligibilityDocuments"),
    ILLUSTRATION("illustration"),
    COMMERCIAL_OFFER("x_commercialOffer"),
    QUALIFICATION_DOCUMENTS("x_qualificationDocuments"),
    TECHNICAL_PROPOSAL("technicalProposal"),
    SELECTION_DOCUMENTS("selectionDocuments"),
    TECHNICAL_DOCUMENTS("x_technicalDocuments");

    override fun toString(): String = value

    companion object {
        private val elements: Map<String, BidDocumentType> = values().associateBy { it.value.toUpperCase() }

        fun fromString(value: String): BidDocumentType = elements[value.toUpperCase()]
            ?: throw EnumException(
                enumType = BidDocumentType::class.java.canonicalName,
                value = value,
                values = values().joinToString { it.value }
            )
    }
}
