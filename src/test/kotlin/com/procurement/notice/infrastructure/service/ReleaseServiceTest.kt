package com.procurement.notice.infrastructure.service

import com.nhaarman.mockito_kotlin.mock
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.json.loadJson
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Person
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ReleaseServiceTest {

    private val releaseDao: ReleaseDao = mock()
    private val releaseService = ReleaseService(releaseDao)

    private val PERSON_JSON = loadJson("json/service/release/procuring_entity_person.json")
    private val PROCURING_ENTITY_JSON = loadJson("json/service/release/procuring_entity.json")

    @Test
    fun `replace parties with actual persones`() {
        val targetProcuringEntityId = "procuring-entity-id-5"
        val samplePartiesPerson = toObject(Person::class.java, PERSON_JSON)
        val sampleOrganization = toObject(Organization::class.java, PROCURING_ENTITY_JSON)

        val parties = generateSequence(1) { it + 1 }
            .take(10)
            .map {
                sampleOrganization.copy(
                    id = "procuring-entity-id-${it}",
                    persones = hashSetOf(samplePartiesPerson.copy(identifier = samplePartiesPerson.identifier.copy(id = "$it")))
                )
            }.toHashSet()

        val requestPerson = toObject(Person::class.java, PERSON_JSON).apply { this.name = "ACTUAL_PERSON" }
        val requestProcuringEntity = OrganizationReference(
            id = targetProcuringEntityId,
            persones = hashSetOf(requestPerson),
            additionalIdentifiers = null,
            name = null,
            identifier = null,
            contactPoint = null,
            details = null,
            buyerProfile = null,
            address = null,
            title = null
        )

        val previousProcuringEntity = parties.find { it.id == targetProcuringEntityId }
        val updatedParties = releaseService.getPartiesWithActualPersones(requestProcuringEntity, parties)
        val changedProcuringEntity = updatedParties.find { it.id == targetProcuringEntityId }

        assertNotNull(updatedParties)
        assertNotNull(changedProcuringEntity)
        assertTrue(parties.size == updatedParties.size)
        assertEquals(previousProcuringEntity!!.copy(persones = null), changedProcuringEntity!!.copy(persones = null))
        assertEquals(requestProcuringEntity.persones, changedProcuringEntity.persones)
    }
}
