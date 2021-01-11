package com.procurement.notice.infrastructure.dto.tender.criterion.requirement

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementDeserializer
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementSerializer
import com.procurement.notice.json.testingBindingAndMapping
import com.procurement.notice.model.ocds.Requirement
import org.junit.jupiter.api.Test


class EligibleEvidencesTest {

    @Test
    fun fully() {
        testingBindingAndMapping<RequirementDTO>("json/dto/tender/criterion/requirement/eligible_evidences_full.json")
    }

    @Test
    fun required_1() {
        testingBindingAndMapping<RequirementDTO>("json/dto/tender/criterion/requirement/eligible_evidences_required_1.json")
    }

    @Test
    fun required_2() {
        testingBindingAndMapping<RequirementDTO>("json/dto/tender/criterion/requirement/eligible_evidences_required_2.json")
    }

    private data class RequirementDTO(
        @JsonDeserialize(using = RequirementDeserializer::class)
        @JsonSerialize(using = RequirementSerializer::class)
        @field:JsonProperty("requirements") @param:JsonProperty("requirements") val requirements: List<Requirement> = emptyList()
    )

}