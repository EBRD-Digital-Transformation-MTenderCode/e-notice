package com.procurement.notice.infrastructure.service.update

import com.procurement.notice.infrastructure.dto.entity.RecordAccountIdentifier
import com.procurement.notice.infrastructure.dto.entity.RecordClassification
import com.procurement.notice.infrastructure.dto.entity.RecordIdentifier
import com.procurement.notice.infrastructure.dto.entity.RecordIssue
import com.procurement.notice.infrastructure.dto.entity.RecordLegalForm
import com.procurement.notice.infrastructure.dto.entity.RecordPeriod
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedOrganization
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedParty
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedProcess
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddress
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddressDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordCountryDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordLocalityDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordRegionDetails
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBidsStatistic
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordBudgetSource
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordConfirmationRequest
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordConfirmationResponse
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordRequest
import com.procurement.notice.infrastructure.dto.entity.parties.RecordBankAccount
import com.procurement.notice.infrastructure.dto.entity.parties.RecordPermitDetails
import com.procurement.notice.infrastructure.dto.entity.parties.RecordPermits
import com.procurement.notice.infrastructure.dto.entity.tender.RecordMilestone
import com.procurement.notice.infrastructure.dto.entity.tender.RecordUnit
import com.procurement.notice.infrastructure.dto.request.RequestAccountIdentifier
import com.procurement.notice.infrastructure.dto.request.RequestClassification
import com.procurement.notice.infrastructure.dto.request.RequestIdentifier
import com.procurement.notice.infrastructure.dto.request.RequestIssue
import com.procurement.notice.infrastructure.dto.request.RequestLegalForm
import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.RequestRelatedOrganization
import com.procurement.notice.infrastructure.dto.request.RequestRelatedParty
import com.procurement.notice.infrastructure.dto.request.RequestRelatedProcess
import com.procurement.notice.infrastructure.dto.request.address.RequestAddress
import com.procurement.notice.infrastructure.dto.request.address.RequestAddressDetails
import com.procurement.notice.infrastructure.dto.request.address.RequestCountryDetails
import com.procurement.notice.infrastructure.dto.request.address.RequestLocalityDetails
import com.procurement.notice.infrastructure.dto.request.address.RequestRegionDetails
import com.procurement.notice.infrastructure.dto.request.awards.RequestBidsStatistic
import com.procurement.notice.infrastructure.dto.request.contracts.RequestBudgetSource
import com.procurement.notice.infrastructure.dto.request.contracts.RequestConfirmationRequest
import com.procurement.notice.infrastructure.dto.request.contracts.RequestConfirmationResponse
import com.procurement.notice.infrastructure.dto.request.contracts.RequestRequest
import com.procurement.notice.infrastructure.dto.request.parties.RequestBankAccount
import com.procurement.notice.infrastructure.dto.request.parties.RequestPermitDetails
import com.procurement.notice.infrastructure.dto.request.parties.RequestPermits
import com.procurement.notice.infrastructure.dto.request.tender.RequestMilestone
import com.procurement.notice.infrastructure.dto.request.tender.RequestUnit
import com.procurement.notice.infrastructure.service.record.createAccountIdentifier
import com.procurement.notice.infrastructure.service.record.createAddress
import com.procurement.notice.infrastructure.service.record.createBankAccount
import com.procurement.notice.infrastructure.service.record.createBidsStatistic
import com.procurement.notice.infrastructure.service.record.createBudgetSource
import com.procurement.notice.infrastructure.service.record.createClassification
import com.procurement.notice.infrastructure.service.record.createConfirmationRequest
import com.procurement.notice.infrastructure.service.record.createConfirmationResponse
import com.procurement.notice.infrastructure.service.record.createContractPeriod
import com.procurement.notice.infrastructure.service.record.createIdentifier
import com.procurement.notice.infrastructure.service.record.createLegalForm
import com.procurement.notice.infrastructure.service.record.createMilestone
import com.procurement.notice.infrastructure.service.record.createPermitDetails
import com.procurement.notice.infrastructure.service.record.createPermits
import com.procurement.notice.infrastructure.service.record.createRelatedParty
import com.procurement.notice.infrastructure.service.record.createRelatedProcess
import com.procurement.notice.infrastructure.service.record.createRequest
import com.procurement.notice.infrastructure.service.record.updateAccountIdentifier
import com.procurement.notice.infrastructure.service.record.updateAccountIdentifierElement
import com.procurement.notice.infrastructure.service.record.updateAddress
import com.procurement.notice.infrastructure.service.record.updateBankAccount
import com.procurement.notice.infrastructure.service.record.updateBidsStatistic
import com.procurement.notice.infrastructure.service.record.updateBudgetSource
import com.procurement.notice.infrastructure.service.record.updateClassification
import com.procurement.notice.infrastructure.service.record.updateConfirmationRequest
import com.procurement.notice.infrastructure.service.record.updateConfirmationResponse
import com.procurement.notice.infrastructure.service.record.updateIdentifier
import com.procurement.notice.infrastructure.service.record.updateLegalForm
import com.procurement.notice.infrastructure.service.record.updateMilestone
import com.procurement.notice.infrastructure.service.record.updatePeriod
import com.procurement.notice.infrastructure.service.record.updatePermitDetails
import com.procurement.notice.infrastructure.service.record.updatePermits
import com.procurement.notice.infrastructure.service.record.updateRelatedParty
import com.procurement.notice.infrastructure.service.record.updateRelatedProcess
import com.procurement.notice.infrastructure.service.record.updateRequest
import com.procurement.notice.infrastructure.service.record.updateStrategy
import com.procurement.notice.infrastructure.service.record.updateTags
import com.procurement.notice.infrastructure.service.record.updateUnit
import com.procurement.notice.json.toJson
import com.procurement.notice.lib.toSetBy
import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.RelatedProcessScheme
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class UpdatedRecordTest {

    @Nested
    inner class Tags {

        @Test
        fun `update Tag`() {
            val prevTags = listOf(Tag.AWARD, Tag.AWARD_CANCELLATION, Tag.COMPILED)
            val newTags = listOf(Tag.AWARD, Tag.CONTRACT)
            val tagsDiff = prevTags - newTags

            val updatedTags = prevTags.updateTags(newTags)

            assertTrue(updatedTags.size == newTags.size)
            assertTrue(updatedTags.containsAll(newTags))
            assertFalse(updatedTags.containsAll(tagsDiff))
        }
    }

    @Nested
    inner class IdentifierTest {

        private val prevIdentifier = RecordIdentifier(
            id = "_requestIdentifier.id-484",
            scheme = "_requestIdentifier.scheme-327",
            uri = "_requestIdentifier.uri-281",
            legalName = "_requestIdentifier.legalName-671"
        )

        private val sampleNewdentifier = RequestIdentifier(
            id = "_requestIdentifier.id-484",
            scheme = "_requestIdentifier.scheme-327",
            uri = "_requestIdentifier.uri-281",
            legalName = "_requestIdentifier.legalName-671"
        )

        @Test
        fun `update identifier - without previous value`() {
            val createdIdentifier = createIdentifier(
                sampleNewdentifier
            )
            assertEquals(sampleNewdentifier.toJson(), createdIdentifier.toJson())
        }

        @Test
        fun `update identifier - full update`() {
            val updatedTags = prevIdentifier.updateIdentifier(sampleNewdentifier)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedTags.toJson(), sampleNewdentifier.toJson())
        }

        @Test
        fun `update identifier - partial updating`() {
            val newIdentifier = sampleNewdentifier.copy(
                uri = null
            )

            val expectedIdentifier = Identifier(
                id = sampleNewdentifier.id,
                legalName = sampleNewdentifier.legalName,
                uri = prevIdentifier.uri,
                scheme = sampleNewdentifier.scheme
            )

            val updatedIdentifier = prevIdentifier.updateIdentifier(newIdentifier)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedIdentifier.toJson(), updatedIdentifier.toJson())
        }
    }

    @Nested
    inner class AddressTest {

        val prevAddress = RecordAddress(
            streetAddress = "address.streetAddress",
            addressDetails = RecordAddressDetails(
                country = RecordCountryDetails(
                    id = "country.id",
                    scheme = "dbAddress?.addressDetails?.country?.scheme",
                    description = "dbAddress?.addressDetails?.country?.description",
                    uri = "dbAddress?.addressDetails?.country?.uri"
                ),
                region = RecordRegionDetails(
                    id = "region.id",
                    scheme = "dbAddress?.addressDetails?.region?.scheme",
                    description = "dbAddress?.addressDetails?.region?.description",
                    uri = "dbAddress?.addressDetails?.region?.uri"
                ),
                locality = RecordLocalityDetails(
                    id = "locality.id",
                    scheme = "locality.scheme",
                    description = "locality.description",
                    uri = "dbAddress?.addressDetails?.locality?.uri"
                )
            ),
            postalCode = "dbAddress?.postalCode"
        )

        val sampleNewAddress = RequestAddress(
            streetAddress = "address.streetAddress-348",
            addressDetails = RequestAddressDetails(
                country = RequestCountryDetails(
                    id = "country.id-361",
                    scheme = "dbAddress?.addressDetails?.country?.scheme-82",
                    description = "dbAddress?.addressDetails?.country?.description-625",
                    uri = "dbAddress?.addressDetails?.country?.uri-720"
                ),
                region = RequestRegionDetails(
                    id = "region.id-560",
                    scheme = "dbAddress?.addressDetails?.region?.scheme-938",
                    description = "dbAddress?.addressDetails?.region?.description-512",
                    uri = "dbAddress?.addressDetails?.region?.uri-428"
                ),
                locality = RequestLocalityDetails(
                    id = "locality.id-799",
                    scheme = "locality.scheme-327",
                    description = "locality.description-829",
                    uri = "dbAddress?.addressDetails?.locality?.uri-484"
                )
            ),
            postalCode = "dbAddress?.postalCode-161"
        )

        @Test
        fun `update Address - without previous value`() {
            val createdAddress = createAddress(sampleNewAddress)
            assertEquals(sampleNewAddress.toJson(), createdAddress.toJson())
        }

        @Test
        fun `update Address - full update`() {
            val updatedAddress = prevAddress.updateAddress(sampleNewAddress)
            assertEquals(updatedAddress.toJson(), sampleNewAddress.toJson())
        }

        @Test
        fun `update Address - partial updating`() {

            val newAddress = sampleNewAddress.copy(
                streetAddress = null,
                addressDetails = sampleNewAddress.addressDetails?.copy(
                    country = sampleNewAddress.addressDetails!!.country.copy(
                        uri = null,
                        description = null
                    ),
                    region = sampleNewAddress.addressDetails!!.region.copy(
                        description = null
                    ),
                    locality = sampleNewAddress.addressDetails!!.locality.copy(
                        uri = null
                    )
                )
            )

            val expectedAddress = Address(
                streetAddress = prevAddress.streetAddress,
                addressDetails = AddressDetails(
                    country = CountryDetails(
                        id = sampleNewAddress.addressDetails!!.country.id,
                        description = prevAddress.addressDetails!!.country.description,
                        scheme = sampleNewAddress.addressDetails!!.country.scheme,
                        uri = prevAddress.addressDetails!!.country.uri
                    ),
                    region = RegionDetails(
                        id = sampleNewAddress.addressDetails!!.region.id,
                        description = prevAddress.addressDetails!!.region.description,
                        scheme = sampleNewAddress.addressDetails!!.region.scheme,
                        uri = sampleNewAddress.addressDetails!!.region.uri
                    ),
                    locality = LocalityDetails(
                        id = sampleNewAddress.addressDetails!!.locality.id,
                        description = sampleNewAddress.addressDetails!!.locality.description,
                        scheme = sampleNewAddress.addressDetails!!.locality.scheme,
                        uri = prevAddress.addressDetails!!.locality?.uri
                    )
                ),
                postalCode = newAddress.postalCode
            )

            val updatedAddress = prevAddress.updateAddress(newAddress)

            assertEquals(expectedAddress.toJson(), updatedAddress.toJson())
        }
    }

    @Nested
    inner class AccountIdentifierTest {

        val prevAccountIdentifier = RecordAccountIdentifier(
            id = "requestAccountIdentifier.id",
            scheme = "requestAccountIdentifier.scheme"
        )

        val sampleNewAccountIdentifier = RequestAccountIdentifier(
            id = "requestAccountIdentifier.id",
            scheme = "requestAccountIdentifier.scheme"
        )

        @Test
        fun `update AccountIdentifier - without previous value`() {
            val createdAccountIdentifier = createAccountIdentifier(sampleNewAccountIdentifier)
            assertEquals(sampleNewAccountIdentifier.toJson(), createdAccountIdentifier.toJson())
        }

        @Test
        fun `update AccountIdentifier - full update`() {
            val updatedAccountIdentifier = prevAccountIdentifier.updateAccountIdentifier(sampleNewAccountIdentifier)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedAccountIdentifier.toJson(), sampleNewAccountIdentifier.toJson())
        }
    }

    @Nested
    inner class AdditionalAccountIdentifierTest {

        private val COMMON_ID = "id-161"

        val prevAdditionalAccountIdentifier = listOf(
            RecordAccountIdentifier(
                id = COMMON_ID,
                scheme = "dbAccountIdentifier.scheme-744"
            ),
            RecordAccountIdentifier(
                id = "dbAccountIdentifier.id-971",
                scheme = "dbAccountIdentifier.scheme-424"
            )
        )

        val sampleNewAdditionalAccountIdentifier = listOf(
            RequestAccountIdentifier(
                id = COMMON_ID,
                scheme = "requestAccountIdentifier.scheme-206"
            ),
            RequestAccountIdentifier(
                id = "requestAccountIdentifier.id-77",
                scheme = "requestAccountIdentifier.scheme-763"
            ),
            RequestAccountIdentifier(
                id = "requestAccountIdentifier.id-468",
                scheme = "requestAccountIdentifier.scheme-615"
            )
        )

        @Test
        fun `update AdditionalAccountIdentifier - without previous value`() {
            val updatedAdditionalAccountIdentifier = updateStrategy(
                receivedElements = sampleNewAdditionalAccountIdentifier,
                keyExtractorForReceivedElement = { it.id },
                availableElements = emptyList(),
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordAccountIdentifier::updateAccountIdentifierElement,
                createBlock = ::createAccountIdentifier
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(sampleNewAdditionalAccountIdentifier.toJson(), updatedAdditionalAccountIdentifier.toJson())
        }

        @Test
        fun `update AdditionalAccountIdentifier - empty collection in request`() {
            val updatedAdditionalAccountIdentifier = updateStrategy(
                receivedElements = emptyList(),
                keyExtractorForReceivedElement = { it.id },
                availableElements = prevAdditionalAccountIdentifier,
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordAccountIdentifier::updateAccountIdentifierElement,
                createBlock = ::createAccountIdentifier
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(prevAdditionalAccountIdentifier.toJson(), updatedAdditionalAccountIdentifier.toJson())
        }

        @Test
        fun `update AdditionalAccountIdentifier - partial updating`() {
            val newAdditionalAccountIdentifier = sampleNewAdditionalAccountIdentifier.map {
                it.copy()
            }.associateBy { it.id }

            val dbIds = prevAdditionalAccountIdentifier.map { it.id }
            val requestIds = sampleNewAdditionalAccountIdentifier.map { it.id }
            val distinctIds = (dbIds + requestIds).toSet()

            val updatedAccountIdentification = updateStrategy(
                receivedElements = newAdditionalAccountIdentifier.values.toList(),
                keyExtractorForReceivedElement = { it.id },
                availableElements = prevAdditionalAccountIdentifier,
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordAccountIdentifier::updateAccountIdentifierElement,
                createBlock = ::createAccountIdentifier
            )
                .doReturn { _ -> throw RuntimeException() }
                .associateBy { it.id }

            assertEquals(distinctIds.size, updatedAccountIdentification.size)
            assertTrue(updatedAccountIdentification.keys.containsAll(requestIds))
            assertTrue(updatedAccountIdentification.keys.containsAll(dbIds))
        }
    }

    @Nested
    inner class BankAccountTest {

        val prevBankAccount = RecordBankAccount(
            identifier = AccountIdentifierTest().prevAccountIdentifier,
            address = AddressTest().prevAddress,
            description = "dbBankAccount.description",
            accountIdentification = AccountIdentifierTest().prevAccountIdentifier,
            additionalAccountIdentifiers = AdditionalAccountIdentifierTest().prevAdditionalAccountIdentifier,
            bankName = "dbBankAccount.bankName"
        )

        val sampleNewBankAccount = RequestBankAccount(
            identifier = AccountIdentifierTest().sampleNewAccountIdentifier,
            address = AddressTest().sampleNewAddress,
            description = "dbBankAccount.description",
            accountIdentification = AccountIdentifierTest().sampleNewAccountIdentifier,
            additionalAccountIdentifiers = AdditionalAccountIdentifierTest().sampleNewAdditionalAccountIdentifier,
            bankName = "dbBankAccount.bankName"
        )

        @Test
        fun `update BankAccount - without previous value`() {
            val createdBankAccount = createBankAccount(sampleNewBankAccount)
            assertEquals(sampleNewBankAccount.toJson(), createdBankAccount.toJson())
        }

        @Test
        fun `update BankAccount - partial updating`() {

            val newBankAccount = sampleNewBankAccount.copy(
                identifier = AccountIdentifierTest().sampleNewAccountIdentifier,
                accountIdentification = AccountIdentifierTest().sampleNewAccountIdentifier,
                additionalAccountIdentifiers = emptyList(),
                address = null,
                description = null,
                bankName = null
            )

            val updatedBankAccount = prevBankAccount.updateBankAccount(newBankAccount)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(prevBankAccount.address!!.toJson(), updatedBankAccount.address!!.toJson())
            assertEquals(prevBankAccount.description!!.toJson(), updatedBankAccount.description!!.toJson())
            assertEquals(prevBankAccount.bankName!!.toJson(), updatedBankAccount.bankName!!.toJson())
            assertEquals(newBankAccount.identifier.toJson(), updatedBankAccount.identifier.toJson())
            assertEquals(
                newBankAccount.accountIdentification!!.toJson(),
                updatedBankAccount.accountIdentification!!.toJson()
            )
            assertEquals(
                prevBankAccount.additionalAccountIdentifiers.toJson(),
                updatedBankAccount.additionalAccountIdentifiers.toJson()
            )
        }
    }

    @Nested
    inner class BankAccountsTest {

        private val COMMON_ID = "id-161"

        private val prevBanckAccounts = listOf(
            RecordBankAccount(
                identifier = AccountIdentifierTest().prevAccountIdentifier.copy(id = COMMON_ID),
                address = AddressTest().prevAddress,
                description = "dbBankAccount.description",
                accountIdentification = AccountIdentifierTest().prevAccountIdentifier,
                additionalAccountIdentifiers = AdditionalAccountIdentifierTest().prevAdditionalAccountIdentifier,
                bankName = "dbBankAccount.bankName"
            )
        )

        private val sampleNewBankAccount = listOf(
            RequestBankAccount(
                identifier = AccountIdentifierTest().sampleNewAccountIdentifier.copy(id = COMMON_ID),
                address = AddressTest().sampleNewAddress,
                description = "dbBankAccount.description-513",
                accountIdentification = AccountIdentifierTest().sampleNewAccountIdentifier,
                additionalAccountIdentifiers = AdditionalAccountIdentifierTest().sampleNewAdditionalAccountIdentifier,
                bankName = "dbBankAccount.bankName-675"
            ),
            RequestBankAccount(
                identifier = AccountIdentifierTest().sampleNewAccountIdentifier,
                address = null,
                description = "dbBankAccount.description-874",
                accountIdentification = null,
                additionalAccountIdentifiers = emptyList(),
                bankName = "dbBankAccount.bankName-976"
            )
        )

        @Test
        fun `update BankAccount - without previous value`() {
            val updatedBankAccount = updateStrategy(
                receivedElements = sampleNewBankAccount,
                keyExtractorForReceivedElement = { it.identifier.id },
                availableElements = emptyList(),
                keyExtractorForAvailableElement = { it.identifier.id },
                updateBlock = RecordBankAccount::updateBankAccount,
                createBlock = ::createBankAccount
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(sampleNewBankAccount.toJson(), updatedBankAccount.toJson())
        }

        @Test
        fun `update BankAccount - empty collection in request`() {
            val updatedBankAccount = updateStrategy(
                receivedElements = emptyList(),
                keyExtractorForReceivedElement = { it.identifier.id },
                availableElements = prevBanckAccounts,
                keyExtractorForAvailableElement = { it.identifier.id },
                updateBlock = RecordBankAccount::updateBankAccount,
                createBlock = ::createBankAccount
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(prevBanckAccounts.toJson(), updatedBankAccount.toJson())
        }

        @Test
        fun `update BankAccount - partial updating`() {
            val dbIds = prevBanckAccounts.map { it.identifier.id }
            val requestIds = sampleNewBankAccount.map { it.identifier.id }
            val distinctIds = (dbIds + requestIds).toSet()

            val updatedBankAccounts = updateStrategy(
                receivedElements = sampleNewBankAccount,
                keyExtractorForReceivedElement = { it.identifier.id },
                availableElements = prevBanckAccounts,
                keyExtractorForAvailableElement = { it.identifier.id },
                updateBlock = RecordBankAccount::updateBankAccount,
                createBlock = ::createBankAccount
            )
                .doReturn { _ -> throw RuntimeException() }
                .map { it.identifier.id }

            assertEquals(distinctIds.size, updatedBankAccounts.size)
            assertTrue(updatedBankAccounts.containsAll(requestIds))
            assertTrue(updatedBankAccounts.containsAll(dbIds))
        }
    }

    @Nested
    inner class LegalFormTest {

        val prevLegalForm = RecordLegalForm(
            id = "dbLegalForm.id",
            description = "dbLegalForm.description",
            uri = "dbLegalForm?.uri",
            scheme = "dbLegalForm.scheme"
        )

        val sampleNewLegalForm = RequestLegalForm(
            id = "dbLegalForm.id",
            description = "dbLegalForm.description-542",
            uri = "dbLegalForm?.uri-649",
            scheme = "dbLegalForm.scheme"
        )

        @Test
        fun `update LegalForm - without previous value`() {
            val createdLegalForm = createLegalForm(sampleNewLegalForm)
            assertEquals(sampleNewLegalForm.toJson(), createdLegalForm.toJson())
        }

        @Test
        fun `update LegalForm - full update`() {
            val updatedLegalForm = prevLegalForm.updateLegalForm(sampleNewLegalForm)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedLegalForm.toJson(), sampleNewLegalForm.toJson())
        }

        @Test
        fun `update LegalForm - partial updating`() {
            val newLegalForm = sampleNewLegalForm.copy(uri = null)

            val expectedValue = RecordLegalForm(
                id = newLegalForm.id,
                description = newLegalForm.description,
                scheme = newLegalForm.scheme,
                uri = prevLegalForm.uri
            )
            val updatedLegalForm = prevLegalForm.updateLegalForm(newLegalForm)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedLegalForm.toJson())
        }

        @Test
        fun `update LegalForm - same id`() {
            val result = prevLegalForm.updateLegalForm(sampleNewLegalForm.copy(id = "ID-1"))
            assertTrue(result.isFail)
        }
    }

    @Nested
    inner class ClassificationTest {

        val prevClassification = RecordClassification(
            id = "dbClassification.id",
            description = "dbClassification.description",
            uri = "dbClassification?.uri",
            scheme = "dbClassification.scheme"
        )

        val sampleNewClassification = RequestClassification(
            id = "dbClassification.id",
            description = "dbClassification.description-542",
            uri = "dbClassification?.uri-649",
            scheme = "dbClassification.scheme"
        )

        @Test
        fun `update Classification - without previous value`() {
            val createdClassification = createClassification(sampleNewClassification)
            assertEquals(sampleNewClassification.toJson(), createdClassification.toJson())
        }

        @Test
        fun `update Classification - full update`() {
            val updatedClassification = prevClassification.updateClassification(sampleNewClassification)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedClassification.toJson(), sampleNewClassification.toJson())
        }

        @Test
        fun `update Classification - partial updating`() {
            val newClassification = sampleNewClassification.copy(uri = null)

            val expectedValue = RecordClassification(
                id = newClassification.id,
                description = newClassification.description,
                scheme = newClassification.scheme,
                uri = prevClassification.uri
            )
            val updatedClassification = prevClassification.updateClassification(newClassification)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(expectedValue.toJson(), updatedClassification.toJson())
        }

        @Test
        fun `update Classification - same id`() {
            val result = prevClassification.updateClassification(sampleNewClassification.copy(id = "ID-1"))
            assertTrue(result.isFail)
        }
    }

    @Nested
    inner class UnitTest {

        val prevUnit = RecordUnit(
            id = "unitId",
            name = "available?.name",
            uri = "available?.uri",
            value = Value(BigDecimal.ZERO, "MDL", null, null)
        )

        val sampleNewUnit = RequestUnit(
            id = "unitId",
            name = "rqUnit.name",
            uri = "rqUnit.uri",
            value = Value(BigDecimal.TEN, "MDL", null, null)
        )

        @Test
        fun `update Unit - partial updating`() {
            val newUnit = sampleNewUnit.copy(uri = null)

            val expectedValue = RecordUnit(
                id = sampleNewUnit.id,
                name = sampleNewUnit.name,
                value = sampleNewUnit.value,
                uri = prevUnit.uri
            )
            val updatedUnit = prevUnit.updateUnit(newUnit)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedUnit.toJson())
        }

        @Test
        fun `update Unit - same id`() {
            val result = prevUnit.updateUnit(sampleNewUnit.copy(id = "ID-1"))
            assertTrue(result.isFail)
        }
    }

    @Nested
    inner class PeriodTest {

        val prevPeriod = RecordPeriod(
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now().plusDays(10),
            durationInDays = 20,
            maxExtentDate = LocalDateTime.now().plusDays(30)
        )

        val sampleNewPeriod = RequestPeriod(
            startDate = LocalDateTime.now().plusDays(20),
            endDate = LocalDateTime.now().plusDays(30),
            durationInDays = 20,
            maxExtentDate = LocalDateTime.now().plusDays(30)
        )

        @Test
        fun `update Period - without previous value`() {
            val createdPeriod = createContractPeriod(sampleNewPeriod)
            assertEquals(sampleNewPeriod.toJson(), createdPeriod.toJson())
        }

        @Test
        fun `update Period - full update`() {
            val updatedPeriod = prevPeriod.updatePeriod(sampleNewPeriod)
            assertEquals(updatedPeriod.toJson(), sampleNewPeriod.toJson())
        }

        @Test
        fun `update Period - partial updating`() {
            val newPeriod = sampleNewPeriod.copy(
                endDate = null,
                maxExtentDate = null
            )

            val expectedValue = RecordPeriod(
                startDate = sampleNewPeriod.startDate,
                durationInDays = sampleNewPeriod.durationInDays,
                endDate = prevPeriod.endDate,
                maxExtentDate = prevPeriod.maxExtentDate
            )
            val updatedPeriod = prevPeriod.updatePeriod(newPeriod)

            assertEquals(expectedValue.toJson(), updatedPeriod.toJson())
        }
    }

    @Nested
    inner class BidsStatisticTest {

        val prevBidsStatistic = RecordBidsStatistic(
            id = "id",
            date = LocalDateTime.now(),
            value = Double.MIN_VALUE,
            notes = "dbBidsStatistic?.notes",
            measure = "dbBidsStatistic?.measure",
            relatedLot = "dbBidsStatistic?.relatedLot"
        )

        val sampleNewBidsStatistic = RequestBidsStatistic(
            id = "id",
            date = LocalDateTime.now(),
            value = Double.MAX_VALUE,
            notes = "rqBidsStatistic.notes",
            measure = "rqBidsStatistic.measure",
            relatedLot = "rqBidsStatistic.relatedLot"
        )

        @Test
        fun `update BidsStatistic - without previous value`() {
            val createdBidsStatistic = createBidsStatistic(sampleNewBidsStatistic)
            assertEquals(sampleNewBidsStatistic.toJson(), createdBidsStatistic.toJson())
        }

        @Test
        fun `update BidsStatistic - full update`() {
            val updatedBidsStatistic = prevBidsStatistic.updateBidsStatistic(sampleNewBidsStatistic)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedBidsStatistic.toJson(), sampleNewBidsStatistic.toJson())
        }

        @Test
        fun `update BidsStatistic - partial updating`() {
            val newBidsStatistic = sampleNewBidsStatistic.copy(
                notes = null,
                measure = null
            )

            val expectedValue = RecordBidsStatistic(
                id = sampleNewBidsStatistic.id,
                date = sampleNewBidsStatistic.date,
                value = sampleNewBidsStatistic.value,
                relatedLot = sampleNewBidsStatistic.relatedLot,
                measure = prevBidsStatistic.measure,
                notes = prevBidsStatistic.notes
            )
            val updatedBidsStatistic = prevBidsStatistic.updateBidsStatistic(newBidsStatistic)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(expectedValue.toJson(), updatedBidsStatistic.toJson())
        }
    }

    @Nested
    inner class BudgetSourceTest {

        val prevBudgetSource = RecordBudgetSource(
            id = "budgetSource.id",
            currency = "dbBudgetSource?.currency",
            amount = BigDecimal.ZERO
        )

        val sampleNewBudgetSource = RequestBudgetSource(
            id = "budgetSource.id",
            currency = "rqBudgetSource?.currency",
            amount = BigDecimal.TEN
        )

        @Test
        fun `update BudgetSource - without previous value`() {
            val createdBudgetSource = createBudgetSource(sampleNewBudgetSource)
            assertEquals(sampleNewBudgetSource.toJson(), createdBudgetSource.toJson())
        }

        @Test
        fun `update BudgetSource - full update`() {
            val updatedBudgetSource = prevBudgetSource.updateBudgetSource(sampleNewBudgetSource)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedBudgetSource.toJson(), sampleNewBudgetSource.toJson())
        }

        @Test
        fun `update BudgetSource - partial updating`() {
            val newBudgetSource = sampleNewBudgetSource.copy(
                currency = null
            )

            val expectedValue = RecordBudgetSource(
                id = sampleNewBudgetSource.id,
                amount = sampleNewBudgetSource.amount,
                currency = prevBudgetSource.currency
            )
            val updatedBudgetSource = prevBudgetSource.updateBudgetSource(newBudgetSource)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedBudgetSource.toJson())
        }
    }

    @Nested
    inner class RequestTest {

        val prevRequest = RecordRequest(
            id = "id",
            relatedPerson = null,
            description = "dbRequest.description",
            title = "dbRequest.title",
            relatedOrganization = RecordRelatedOrganization(
                id = "org-id",
                name = "org-name-db"
            )
        )

        val sampleNewRequest = RequestRequest(
            id = "id",
            relatedPerson = null,
            description = "rqRequest.description",
            title = "rqRequest.title",
            relatedOrganization = RequestRelatedOrganization(
                id = "org-id",
                name = "org-name-rq"
            )
        )

        @Test
        fun `update Request - without previous value`() {
            val createdRequest = createRequest(sampleNewRequest)
            assertEquals(sampleNewRequest.toJson(), createdRequest.toJson())
        }

        @Test
        fun `update Request - full update`() {
            val updatedRequest = prevRequest.updateRequest(sampleNewRequest)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedRequest.toJson(), sampleNewRequest.toJson())
        }

        @Test
        fun `update Request - partial updating`() {
            val newRequest = sampleNewRequest.copy(
                relatedPerson = null
            )

            val expectedValue = RecordRequest(
                id = sampleNewRequest.id,
                description = sampleNewRequest.description,
                title = sampleNewRequest.title,
                relatedPerson = prevRequest.relatedPerson,
                relatedOrganization = sampleNewRequest.relatedOrganization?.let { relatedOrganization ->
                    RecordRelatedOrganization(
                        id = relatedOrganization.id,
                        name = relatedOrganization.name
                    )
                }
            )
            val updatedRequest = prevRequest.updateRequest(newRequest)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedRequest.toJson())
        }
    }

    @Nested
    inner class ConfirmationRequestTest {

        val prevConfirmationRequest = RecordConfirmationRequest(
            id = "ConfirmationRequest.id",
            title = "dbConfirmationRequest?.title",
            description = "dbConfirmationRequest?.description",
            relatesTo = "dbConfirmationRequest?.relatesTo",
            relatedItem = "dbConfirmationRequest.relatedItem",
            type = "dbConfirmationRequest?.type",
            source = "dbConfirmationRequest.source",
            requestGroups = emptyList()
        )

        val sampleNewConfirmationRequest = RequestConfirmationRequest(
            id = "ConfirmationRequest.id",
            title = "dbConfirmationRequest?.title",
            description = "dbConfirmationRequest?.description",
            relatesTo = "dbConfirmationRequest?.relatesTo",
            relatedItem = "dbConfirmationRequest.relatedItem",
            type = "dbConfirmationRequest?.type",
            source = "dbConfirmationRequest.source",
            requestGroups = emptyList()
        )

        @Test
        fun `update ConfirmationRequest - without previous value`() {
            val createdConfirmationRequest = createConfirmationRequest(sampleNewConfirmationRequest)
            assertEquals(sampleNewConfirmationRequest.toJson(), createdConfirmationRequest.toJson())
        }

        @Test
        fun `update ConfirmationRequest - full update`() {
            val updatedConfirmationRequest = prevConfirmationRequest
                .updateConfirmationRequest(sampleNewConfirmationRequest)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedConfirmationRequest.toJson(), sampleNewConfirmationRequest.toJson())
        }

        @Test
        fun `update ConfirmationRequest - partial updating`() {
            val newConfirmationRequest = sampleNewConfirmationRequest.copy(
                description = null,
                type = null,
                relatesTo = null
            )

            val expectedValue = RecordConfirmationRequest(
                id = sampleNewConfirmationRequest.id,
                relatesTo = prevConfirmationRequest.relatesTo,
                type = prevConfirmationRequest.type,
                description = prevConfirmationRequest.description,
                title = sampleNewConfirmationRequest.title,
                requestGroups = emptyList(),
                source = sampleNewConfirmationRequest.source,
                relatedItem = sampleNewConfirmationRequest.relatedItem
            )
            val updatedConfirmationRequest = prevConfirmationRequest.updateConfirmationRequest(newConfirmationRequest)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(expectedValue.toJson(), updatedConfirmationRequest.toJson())
        }
    }

    @Nested
    inner class ConfirmationResponseTest {

        val prevConfirmationResponse = RecordConfirmationResponse(
            id = "ConfirmationResponse.id",
            value = null,
            request = "dbConfirmationResponse?.request"
        )

        val sampleNewConfirmationResponse = RequestConfirmationResponse(
            id = "ConfirmationResponse.id",
            value = null,
            request = "rqConfirmationResponse.request"
        )

        @Test
        fun `update ConfirmationResponse - without previous value`() {
            val createdConfirmationResponse = createConfirmationResponse(sampleNewConfirmationResponse)
            assertEquals(sampleNewConfirmationResponse.toJson(), createdConfirmationResponse.toJson())
        }

        @Test
        fun `update ConfirmationResponse - full update`() {
            val updatedConfirmationResponse = prevConfirmationResponse
                .updateConfirmationResponse(sampleNewConfirmationResponse)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedConfirmationResponse.toJson(), sampleNewConfirmationResponse.toJson())
        }

        @Test
        fun `update ConfirmationResponse - partial updating`() {
            val newConfirmationResponse = sampleNewConfirmationResponse.copy(
                value = null
            )

            val expectedValue = RecordConfirmationResponse(
                id = sampleNewConfirmationResponse.id,
                value = prevConfirmationResponse.value,
                request = sampleNewConfirmationResponse.request
            )
            val updatedConfirmationResponse = prevConfirmationResponse
                .updateConfirmationResponse(newConfirmationResponse)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(expectedValue.toJson(), updatedConfirmationResponse.toJson())
        }
    }

    @Nested
    inner class RelatedPartyTest {

        val prevRelatedParty = RecordRelatedParty(
            id = "dbRelatedParty.id",
            name = "dbRelatedParty?.name"
        )

        val sampleNewRelatedParty = RequestRelatedParty(
            id = "rqRelatedParty.id",
            name = "rqRelatedParty.name"
        )

        @Test
        fun `update RelatedParty - without previous value`() {
            val createdRelatedParty = createRelatedParty(sampleNewRelatedParty)
            assertEquals(sampleNewRelatedParty.toJson(), createdRelatedParty.toJson())
        }

        @Test
        fun `update RelatedParty - full update`() {
            val updatedRelatedParty = prevRelatedParty.updateRelatedParty(sampleNewRelatedParty)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedRelatedParty.toJson(), sampleNewRelatedParty.toJson())
        }

        @Test
        fun `update RelatedParty - partial updating`() {
            val newRelatedParty = sampleNewRelatedParty.copy(
                name = null
            )

            val expectedValue = RecordRelatedParty(
                id = sampleNewRelatedParty.id,
                name = prevRelatedParty.name
            )
            val updatedRelatedParty = prevRelatedParty.updateRelatedParty(newRelatedParty)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedRelatedParty.toJson())
        }
    }

    @Nested
    inner class MilestoneTest {

        val prevMilestone = RecordMilestone(
            id = "Milestone.id",
            title = "dbMilestone?.title",
            description = "dbMilestone?.description",
            type = "dbMilestone?.type",
            status = "dbMilestone?.status",
            dateMet = LocalDateTime.now(),
            dateModified = LocalDateTime.now(),
            additionalInformation = "dbMilestone?.additionalInformation",
            dueDate = LocalDateTime.now(),
            relatedItems = emptyList(),
            relatedParties = emptyList()
        )

        val sampleNewMilestone = RequestMilestone(
            id = "Milestone.id",
            title = "rqMilestone?.title",
            description = "rqMilestone?.description",
            type = "rqMilestone?.type",
            status = "rqMilestone?.status",
            dateMet = LocalDateTime.now(),
            dateModified = LocalDateTime.now(),
            additionalInformation = "rqMilestone?.additionalInformation",
            dueDate = LocalDateTime.now(),
            relatedItems = emptyList(),
            relatedParties = emptyList()
        )

        @Test
        fun `update Milestone - without previous value`() {
            val createdMilestone = createMilestone(sampleNewMilestone)
            assertEquals(sampleNewMilestone.toJson(), createdMilestone.toJson())
        }

        @Test
        fun `update Milestone - full update`() {
            val updatedMilestone = prevMilestone.updateMilestone(sampleNewMilestone)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedMilestone.toJson(), sampleNewMilestone.toJson())
        }

        @Test
        fun `update Milestone - partial updating`() {
            val newMilestone = sampleNewMilestone.copy(
                type = null,
                status = null,
                dateModified = null
            )

            val expectedValue = RecordMilestone(
                id = sampleNewMilestone.id,
                dateModified = prevMilestone.dateModified,
                status = prevMilestone.status,
                type = prevMilestone.type,
                title = sampleNewMilestone.title,
                description = sampleNewMilestone.description,
                relatedParties = emptyList(),
                relatedItems = emptyList(),
                dueDate = sampleNewMilestone.dueDate,
                additionalInformation = sampleNewMilestone.additionalInformation,
                dateMet = sampleNewMilestone.dateMet
            )
            val updatedMilestone = prevMilestone.updateMilestone(newMilestone)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(expectedValue.toJson(), updatedMilestone.toJson())
        }
    }

    @Nested
    inner class PermitDetailsTest {

        val prevPermitDetails = RecordPermitDetails(
            issuedBy = RecordIssue(
                id = "dbPermitDetails?.issuedBy?.id",
                name = "dbPermitDetails?.issuedBy?.name"
            ),
            issuedThought = RecordIssue(
                id = "dbPermitDetails?.issuedBy?.id",
                name = "dbPermitDetails?.issuedBy?.name"
            ),
            validityPeriod = PeriodTest().prevPeriod
        )

        val sampleNewPermitDetails = RequestPermitDetails(
            issuedBy = RequestIssue(
                id = "dbPermitDetails?.issuedBy?.id",
                name = "dbPermitDetails?.issuedBy?.name-382"
            ),
            issuedThought = RequestIssue(
                id = "dbPermitDetails?.issuedBy?.id",
                name = "dbPermitDetails?.issuedBy?.name-860"
            ),
            validityPeriod = PeriodTest().sampleNewPeriod
        )

        @Test
        fun `update PermitDetails - without previous value`() {
            val createdPermitDetails = createPermitDetails(sampleNewPermitDetails)
            assertEquals(sampleNewPermitDetails.toJson(), createdPermitDetails.toJson())
        }

        @Test
        fun `update PermitDetails - full update`() {
            val updatedPermitDetails = prevPermitDetails.updatePermitDetails(sampleNewPermitDetails)
                .doReturn { _ -> throw RuntimeException() }
            assertEquals(updatedPermitDetails.toJson(), sampleNewPermitDetails.toJson())
        }

        @Test
        fun `update PermitDetails - partial updating`() {
            val newPermitDetails = sampleNewPermitDetails.copy(
                issuedBy = null,
                validityPeriod = null
            )

            val updatedPermitDetails = prevPermitDetails.updatePermitDetails(newPermitDetails)
                .doReturn { _ -> throw RuntimeException() }

            assertEquals(prevPermitDetails.issuedBy!!.toJson(), updatedPermitDetails.issuedBy!!.toJson())
            assertEquals(prevPermitDetails.validityPeriod!!.toJson(), updatedPermitDetails.validityPeriod!!.toJson())
            assertEquals(newPermitDetails.issuedThought!!.toJson(), updatedPermitDetails.issuedThought!!.toJson())
        }

        @Test
        fun `update PermitDetails - updating id`() {
            val newPermitDetails = RequestPermitDetails(
                issuedBy = RequestIssue(id = "id1", name = "name1"),
                issuedThought = RequestIssue(id = "id2", name = null),
                validityPeriod = null
            )

            val updatedPermitDetails = prevPermitDetails.updatePermitDetails(newPermitDetails)
                .doReturn { _ -> throw RuntimeException() }

            val expectedIssuedBy = newPermitDetails.issuedBy!!
            val expectedIssueTrough = RequestIssue(id = "id2", name = "dbPermitDetails?.issuedBy?.name")

            assertEquals(expectedIssuedBy.toJson(), updatedPermitDetails.issuedBy!!.toJson())
            assertEquals(expectedIssueTrough.toJson(), updatedPermitDetails.issuedThought!!.toJson())
        }
    }

    @Nested
    inner class PermitsTest {

        private val COMMON_ID = "id-161"

        private val prevPermits = listOf(
            RecordPermits(
                id = COMMON_ID,
                scheme = "dbPermit.scheme-664",
                url = "dbPermit.url-48",
                permitDetails = PermitDetailsTest().prevPermitDetails
            ), RecordPermits(
                id = "dbPermit.id-905",
                scheme = "dbPermit.scheme-873",
                url = "dbPermit.url-991",
                permitDetails = PermitDetailsTest().prevPermitDetails
            )
        )

        private val sampleNewPermits = listOf(
            RequestPermits(
                id = COMMON_ID,
                scheme = "dbPermit.scheme-593",
                url = "dbPermit.url-115",
                permitDetails = PermitDetailsTest().sampleNewPermitDetails
            ),
            RequestPermits(
                id = "dbPermit.id-115",
                scheme = "dbPermit.scheme-705",
                url = "dbPermit.url-615",
                permitDetails = PermitDetailsTest().sampleNewPermitDetails
            )
        )

        @Test
        fun `update Permits - without previous value`() {
            val updatedPermits = updateStrategy(
                receivedElements = sampleNewPermits,
                keyExtractorForReceivedElement = { it.id },
                availableElements = emptyList(),
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordPermits::updatePermits,
                createBlock = ::createPermits
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(sampleNewPermits.toJson(), updatedPermits.toJson())
        }

        @Test
        fun `update Permits - empty collection in request`() {
            val updatedPermits = updateStrategy(
                receivedElements = emptyList(),
                keyExtractorForReceivedElement = { it.id },
                availableElements = prevPermits,
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordPermits::updatePermits,
                createBlock = ::createPermits
            ).doReturn { _ -> throw RuntimeException() }
            assertEquals(prevPermits.toJson(), updatedPermits.toJson())
        }

        @Test
        fun `update Permits - partial updating`() {
            val dbIds = prevPermits.map { it.id }
            val requestIds = sampleNewPermits.map { it.id }
            val distinctIds = (dbIds + requestIds).toSet()

            val updatedPermits = updateStrategy(
                receivedElements = sampleNewPermits,
                keyExtractorForReceivedElement = { it.id },
                availableElements = prevPermits,
                keyExtractorForAvailableElement = { it.id },
                updateBlock = RecordPermits::updatePermits,
                createBlock = ::createPermits
            )
                .doReturn { _ -> throw RuntimeException() }
                .associateBy { it.id }

            val updatedCommonPermit = updatedPermits[COMMON_ID]!!
            val commonPermitFromRequest = sampleNewPermits.find { it.id == COMMON_ID }

            assertEquals(distinctIds.size, updatedPermits.size)
            assertEquals(commonPermitFromRequest!!.toJson(), updatedCommonPermit.toJson())

            assertTrue(updatedPermits.keys.containsAll(requestIds))
            assertTrue(updatedPermits.keys.containsAll(dbIds))
        }
    }

    @Test
    fun `update strategy`() {
        val commonId = "rqRelatedProcess.id-2"

        val existsElements = listOf(
            RecordRelatedProcess(
                id = "rqRelatedProcess.id-1",
                scheme = RelatedProcessScheme.OCID,
                identifier = "available?.identifier-1",
                uri = "available?.uri-1",
                relationship = listOf(RelatedProcessType.PARENT)
            ),
            RecordRelatedProcess(
                id = commonId,
                scheme = RelatedProcessScheme.OCID,
                identifier = "available?.identifier",
                uri = "available?.uri",
                relationship = listOf(RelatedProcessType.PLANNING)
            )
        )

        val newElements = listOf(
            RequestRelatedProcess(
                id = commonId,
                scheme = RelatedProcessScheme.OCID,
                identifier = "available?.identifier-3",
                uri = null,
                relationship = listOf(RelatedProcessType.X_EVALUATION)
            ),
            RequestRelatedProcess(
                id = "rqRelatedProcess.id-4",
                scheme = RelatedProcessScheme.OCID,
                identifier = "available?.identifier-4",
                uri = "available?.uri-4",
                relationship = listOf(RelatedProcessType.X_EXPENDITURE_ITEM)
            )
        )

        val updatedElements = updateStrategy(
            receivedElements = newElements,
            keyExtractorForReceivedElement = { it.id },
            availableElements = existsElements,
            keyExtractorForAvailableElement = { it.id },
            updateBlock = RecordRelatedProcess::updateRelatedProcess,
            createBlock = ::createRelatedProcess
        )
            .doReturn { _ -> throw RuntimeException() }
            .associateBy { it.id }

        val existsElementsIds = existsElements.toSetBy { it.id }
        val newElementsIds = newElements.toSetBy { it.id }
        val distinctIds = (existsElementsIds + newElementsIds).toSet()

        assertEquals(distinctIds.size, updatedElements.size)
        assertTrue(updatedElements.keys.containsAll(newElementsIds))
        assertTrue(updatedElements.keys.containsAll(existsElementsIds))

        val commonElementUpdated = updatedElements.getValue(commonId)
        val commonElementsForUpdate = newElements.find { it.id == commonId }!!
        val commonElementsPrev = existsElements.find { it.id == commonId }!!
        val expectedCommonElement = RecordRelatedProcess(
            id = commonId,
            uri = commonElementsPrev.uri,
            scheme = commonElementsForUpdate.scheme,
            relationship = commonElementsForUpdate.relationship,
            identifier = commonElementsForUpdate.identifier
        )
        assertEquals(expectedCommonElement, commonElementUpdated)
    }
}
