package com.procurement.notice.infrastructure.service.record

import com.procurement.notice.application.model.record.UpdateRecordParams
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.failure
import com.procurement.notice.domain.utils.asSuccess
import com.procurement.notice.infrastructure.dto.enObservationtity.awards.RecordAward
import com.procurement.notice.infrastructure.dto.entity.Record
import com.procurement.notice.infrastructure.dto.entity.RecordAccountIdentifier
import com.procurement.notice.infrastructure.dto.entity.RecordAgreedMetric
import com.procurement.notice.infrastructure.dto.entity.RecordAmendment
import com.procurement.notice.infrastructure.dto.entity.RecordBudgetAllocation
import com.procurement.notice.infrastructure.dto.entity.RecordBudgetBreakdown
import com.procurement.notice.infrastructure.dto.entity.RecordBusinessFunction
import com.procurement.notice.infrastructure.dto.entity.RecordChange
import com.procurement.notice.infrastructure.dto.entity.RecordClassification
import com.procurement.notice.infrastructure.dto.entity.RecordConfirmationResponseValue
import com.procurement.notice.infrastructure.dto.entity.RecordContactPoint
import com.procurement.notice.infrastructure.dto.entity.RecordEuropeanUnionFunding
import com.procurement.notice.infrastructure.dto.entity.RecordIdentifier
import com.procurement.notice.infrastructure.dto.entity.RecordIssue
import com.procurement.notice.infrastructure.dto.entity.RecordLegalForm
import com.procurement.notice.infrastructure.dto.entity.RecordLegalProceedings
import com.procurement.notice.infrastructure.dto.entity.RecordObservation
import com.procurement.notice.infrastructure.dto.entity.RecordObservationUnit
import com.procurement.notice.infrastructure.dto.entity.RecordOrganizationReference
import com.procurement.notice.infrastructure.dto.entity.RecordPeriod
import com.procurement.notice.infrastructure.dto.entity.RecordPreQualification
import com.procurement.notice.infrastructure.dto.entity.RecordPurposeOfNotice
import com.procurement.notice.infrastructure.dto.entity.RecordRecurrentProcurement
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedParty
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedPerson
import com.procurement.notice.infrastructure.dto.entity.RecordRelatedProcess
import com.procurement.notice.infrastructure.dto.entity.RecordRequirementGroup
import com.procurement.notice.infrastructure.dto.entity.RecordVerification
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddress
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddressDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordCountryDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordLocalityDetails
import com.procurement.notice.infrastructure.dto.entity.address.RecordRegionDetails
import com.procurement.notice.infrastructure.dto.entity.auction.RecordElectronicAuctionModalities
import com.procurement.notice.infrastructure.dto.entity.auction.RecordElectronicAuctionProgress
import com.procurement.notice.infrastructure.dto.entity.auction.RecordElectronicAuctionProgressBreakdown
import com.procurement.notice.infrastructure.dto.entity.auction.RecordElectronicAuctionResult
import com.procurement.notice.infrastructure.dto.entity.auction.RecordElectronicAuctionsDetails
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBid
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBidsStatistic
import com.procurement.notice.infrastructure.dto.entity.awards.RecordRequirementReference
import com.procurement.notice.infrastructure.dto.entity.awards.RecordRequirementResponse
import com.procurement.notice.infrastructure.dto.entity.awards.RecordResponder
import com.procurement.notice.infrastructure.dto.entity.awards.RecordReviewProceedings
import com.procurement.notice.infrastructure.dto.entity.bids.RecordBids
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordBudgetSource
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordConfirmationRequest
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordConfirmationResponse
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordContract
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordRequest
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordRequestGroup
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordValueBreakdown
import com.procurement.notice.infrastructure.dto.entity.contracts.RecordValueTax
import com.procurement.notice.infrastructure.dto.entity.documents.RecordDocument
import com.procurement.notice.infrastructure.dto.entity.documents.RecordDocumentBF
import com.procurement.notice.infrastructure.dto.entity.parties.RecordBankAccount
import com.procurement.notice.infrastructure.dto.entity.parties.RecordDetails
import com.procurement.notice.infrastructure.dto.entity.parties.RecordOrganization
import com.procurement.notice.infrastructure.dto.entity.parties.RecordPermitDetails
import com.procurement.notice.infrastructure.dto.entity.parties.RecordPermits
import com.procurement.notice.infrastructure.dto.entity.parties.RecordPerson
import com.procurement.notice.infrastructure.dto.entity.planning.RecordExecutionPeriod
import com.procurement.notice.infrastructure.dto.entity.planning.RecordImplementation
import com.procurement.notice.infrastructure.dto.entity.planning.RecordPlanning
import com.procurement.notice.infrastructure.dto.entity.planning.RecordPlanningBudget
import com.procurement.notice.infrastructure.dto.entity.planning.RecordPlanningBudgetSource
import com.procurement.notice.infrastructure.dto.entity.planning.RecordTransaction
import com.procurement.notice.infrastructure.dto.entity.qualification.RecordQualification
import com.procurement.notice.infrastructure.dto.entity.submission.RecordCandidate
import com.procurement.notice.infrastructure.dto.entity.submission.RecordSubmission
import com.procurement.notice.infrastructure.dto.entity.submission.RecordSubmissionDetail
import com.procurement.notice.infrastructure.dto.entity.tender.RecordAcceleratedProcedure
import com.procurement.notice.infrastructure.dto.entity.tender.RecordCoefficient
import com.procurement.notice.infrastructure.dto.entity.tender.RecordConversion
import com.procurement.notice.infrastructure.dto.entity.tender.RecordCriteria
import com.procurement.notice.infrastructure.dto.entity.tender.RecordDesignContest
import com.procurement.notice.infrastructure.dto.entity.tender.RecordDynamicPurchasingSystem
import com.procurement.notice.infrastructure.dto.entity.tender.RecordElectronicAuctions
import com.procurement.notice.infrastructure.dto.entity.tender.RecordElectronicWorkflows
import com.procurement.notice.infrastructure.dto.entity.tender.RecordFramework
import com.procurement.notice.infrastructure.dto.entity.tender.RecordItem
import com.procurement.notice.infrastructure.dto.entity.tender.RecordJointProcurement
import com.procurement.notice.infrastructure.dto.entity.tender.RecordLot
import com.procurement.notice.infrastructure.dto.entity.tender.RecordLotDetails
import com.procurement.notice.infrastructure.dto.entity.tender.RecordLotGroup
import com.procurement.notice.infrastructure.dto.entity.tender.RecordMilestone
import com.procurement.notice.infrastructure.dto.entity.tender.RecordObjectives
import com.procurement.notice.infrastructure.dto.entity.tender.RecordOption
import com.procurement.notice.infrastructure.dto.entity.tender.RecordPlaceOfPerformance
import com.procurement.notice.infrastructure.dto.entity.tender.RecordProcedureOutsourcing
import com.procurement.notice.infrastructure.dto.entity.tender.RecordRecordEnquiry
import com.procurement.notice.infrastructure.dto.entity.tender.RecordRenewal
import com.procurement.notice.infrastructure.dto.entity.tender.RecordTender
import com.procurement.notice.infrastructure.dto.entity.tender.RecordUnit
import com.procurement.notice.infrastructure.dto.entity.tender.RecordVariant
import com.procurement.notice.infrastructure.dto.request.RequestAccountIdentifier
import com.procurement.notice.infrastructure.dto.request.RequestAgreedMetric
import com.procurement.notice.infrastructure.dto.request.RequestAmendment
import com.procurement.notice.infrastructure.dto.request.RequestBudgetAllocation
import com.procurement.notice.infrastructure.dto.request.RequestBudgetBreakdown
import com.procurement.notice.infrastructure.dto.request.RequestBusinessFunction
import com.procurement.notice.infrastructure.dto.request.RequestChange
import com.procurement.notice.infrastructure.dto.request.RequestClassification
import com.procurement.notice.infrastructure.dto.request.RequestConfirmationResponseValue
import com.procurement.notice.infrastructure.dto.request.RequestContactPoint
import com.procurement.notice.infrastructure.dto.request.RequestEuropeanUnionFunding
import com.procurement.notice.infrastructure.dto.request.RequestIdentifier
import com.procurement.notice.infrastructure.dto.request.RequestLegalForm
import com.procurement.notice.infrastructure.dto.request.RequestLegalProceedings
import com.procurement.notice.infrastructure.dto.request.RequestObservation
import com.procurement.notice.infrastructure.dto.request.RequestObservationUnit
import com.procurement.notice.infrastructure.dto.request.RequestOrganizationReference
import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.RequestPreQualification
import com.procurement.notice.infrastructure.dto.request.RequestPurposeOfNotice
import com.procurement.notice.infrastructure.dto.request.RequestRecurrentProcurement
import com.procurement.notice.infrastructure.dto.request.RequestRelatedParty
import com.procurement.notice.infrastructure.dto.request.RequestRelatedPerson
import com.procurement.notice.infrastructure.dto.request.RequestRelatedProcess
import com.procurement.notice.infrastructure.dto.request.RequestRequirementGroup
import com.procurement.notice.infrastructure.dto.request.RequestVerification
import com.procurement.notice.infrastructure.dto.request.address.RequestAddress
import com.procurement.notice.infrastructure.dto.request.auction.RequestElectronicAuctionModalities
import com.procurement.notice.infrastructure.dto.request.auction.RequestElectronicAuctionProgress
import com.procurement.notice.infrastructure.dto.request.auction.RequestElectronicAuctionProgressBreakdown
import com.procurement.notice.infrastructure.dto.request.auction.RequestElectronicAuctionResult
import com.procurement.notice.infrastructure.dto.request.auction.RequestElectronicAuctionsDetails
import com.procurement.notice.infrastructure.dto.request.awards.RequestAward
import com.procurement.notice.infrastructure.dto.request.awards.RequestBid
import com.procurement.notice.infrastructure.dto.request.awards.RequestBidsStatistic
import com.procurement.notice.infrastructure.dto.request.awards.RequestRequirementReference
import com.procurement.notice.infrastructure.dto.request.awards.RequestRequirementResponse
import com.procurement.notice.infrastructure.dto.request.awards.RequestResponder
import com.procurement.notice.infrastructure.dto.request.awards.RequestReviewProceedings
import com.procurement.notice.infrastructure.dto.request.bids.RequestBids
import com.procurement.notice.infrastructure.dto.request.contracts.RequestBudgetSource
import com.procurement.notice.infrastructure.dto.request.contracts.RequestConfirmationRequest
import com.procurement.notice.infrastructure.dto.request.contracts.RequestConfirmationResponse
import com.procurement.notice.infrastructure.dto.request.contracts.RequestContract
import com.procurement.notice.infrastructure.dto.request.contracts.RequestRequest
import com.procurement.notice.infrastructure.dto.request.contracts.RequestRequestGroup
import com.procurement.notice.infrastructure.dto.request.contracts.RequestValueBreakdown
import com.procurement.notice.infrastructure.dto.request.contracts.RequestValueTax
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocument
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocumentBF
import com.procurement.notice.infrastructure.dto.request.parties.RequestBankAccount
import com.procurement.notice.infrastructure.dto.request.parties.RequestDetails
import com.procurement.notice.infrastructure.dto.request.parties.RequestOrganization
import com.procurement.notice.infrastructure.dto.request.parties.RequestPermitDetails
import com.procurement.notice.infrastructure.dto.request.parties.RequestPermits
import com.procurement.notice.infrastructure.dto.request.parties.RequestPerson
import com.procurement.notice.infrastructure.dto.request.planning.RequestExecutionPeriod
import com.procurement.notice.infrastructure.dto.request.planning.RequestImplementation
import com.procurement.notice.infrastructure.dto.request.planning.RequestPlanning
import com.procurement.notice.infrastructure.dto.request.planning.RequestPlanningBudget
import com.procurement.notice.infrastructure.dto.request.planning.RequestPlanningBudgetSource
import com.procurement.notice.infrastructure.dto.request.planning.RequestTransaction
import com.procurement.notice.infrastructure.dto.request.qualification.RequestQualification
import com.procurement.notice.infrastructure.dto.request.submissions.RequestCandidate
import com.procurement.notice.infrastructure.dto.request.submissions.RequestSubmissionDetail
import com.procurement.notice.infrastructure.dto.request.tender.RequestAcceleratedProcedure
import com.procurement.notice.infrastructure.dto.request.tender.RequestCoefficient
import com.procurement.notice.infrastructure.dto.request.tender.RequestConversion
import com.procurement.notice.infrastructure.dto.request.tender.RequestCriteria
import com.procurement.notice.infrastructure.dto.request.tender.RequestDesignContest
import com.procurement.notice.infrastructure.dto.request.tender.RequestDynamicPurchasingSystem
import com.procurement.notice.infrastructure.dto.request.tender.RequestElectronicAuctions
import com.procurement.notice.infrastructure.dto.request.tender.RequestElectronicWorkflows
import com.procurement.notice.infrastructure.dto.request.tender.RequestFramework
import com.procurement.notice.infrastructure.dto.request.tender.RequestItem
import com.procurement.notice.infrastructure.dto.request.tender.RequestJointProcurement
import com.procurement.notice.infrastructure.dto.request.tender.RequestLot
import com.procurement.notice.infrastructure.dto.request.tender.RequestLotDetails
import com.procurement.notice.infrastructure.dto.request.tender.RequestLotGroup
import com.procurement.notice.infrastructure.dto.request.tender.RequestMilestone
import com.procurement.notice.infrastructure.dto.request.tender.RequestObjectives
import com.procurement.notice.infrastructure.dto.request.tender.RequestOption
import com.procurement.notice.infrastructure.dto.request.tender.RequestPlaceOfPerformance
import com.procurement.notice.infrastructure.dto.request.tender.RequestProcedureOutsourcing
import com.procurement.notice.infrastructure.dto.request.tender.RequestRecordEnquiry
import com.procurement.notice.infrastructure.dto.request.tender.RequestRenewal
import com.procurement.notice.infrastructure.dto.request.tender.RequestTender
import com.procurement.notice.infrastructure.dto.request.tender.RequestUnit
import com.procurement.notice.infrastructure.dto.request.tender.RequestVariant
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.lib.toSetBy
import com.procurement.notice.model.ocds.RecordParticipationFee
import com.procurement.notice.model.ocds.RequestParticipationFee
import com.procurement.notice.model.ocds.Requirement
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value

typealias UpdateRecordResult<T> = Result<T, Fail.Error.BadRequest>

fun RecordLot.updateLot(received: RequestLot): UpdateRecordResult<RecordLot> {
    val value = received.value
        ?.let {
            this.value
                ?.updateValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValue(it)
        }
        ?: this.value

    val contractPeriod = received.contractPeriod
        ?.let {
            this.contractPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.contractPeriod

    val placeOfPerformance = received.placeOfPerformance
        ?.let {
            this.placeOfPerformance?.updatePlaceOfPerformance(it) ?: createPlaceOfPerformance(it)
        }
        ?: this.placeOfPerformance

    return this
        .copy(
            value = value,
            title = received.title ?: this.title,
            status = received.status ?: this.status,
            description = received.description ?: this.description,
            statusDetails = received.statusDetails ?: this.statusDetails,
            recurrentProcurement = this.recurrentProcurement.updateRecurrentProcurement(received.recurrentProcurement),
            contractPeriod = contractPeriod,
            internalId = received.internalId ?: this.internalId,
            options = this.options.updateOptions(received.options),
            placeOfPerformance = placeOfPerformance,
            renewals = this.renewals.updateRenewals(received.renewals),
            variants = this.variants.updateVariants(received.variants)
        )
        .asSuccess()
}

fun List<RecordVariant>.updateVariants(received: List<RequestVariant>): List<RecordVariant> =
    received.mapIfNotEmpty { requestVariant ->
        RecordVariant(
            hasVariants = requestVariant.hasVariants,
            variantDetails = requestVariant.variantDetails
        )
    } ?: this

fun List<RecordRenewal>.updateRenewals(received: List<RequestRenewal>): List<RecordRenewal> =
    received.mapIfNotEmpty { requestRenewal ->
        RecordRenewal(
            hasRenewals = requestRenewal.hasRenewals,
            renewalConditions = requestRenewal.renewalConditions,
            maxNumber = requestRenewal.maxNumber
        )
    } ?: this

fun RecordPlaceOfPerformance.updatePlaceOfPerformance(received: RequestPlaceOfPerformance): RecordPlaceOfPerformance {
    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    return this.copy(
        address = address,
        description = received.description ?: this.description,
        nutScode = received.nutScode ?: this.nutScode
    )
}

fun RecordAddress.updateAddress(received: RequestAddress): RecordAddress =
    this.copy(
        streetAddress = received.streetAddress ?: this.streetAddress,
        addressDetails = received.addressDetails
            ?.let { addressDetails ->
                RecordAddressDetails(
                    country = addressDetails.country
                        .let { country ->
                            RecordCountryDetails(
                                id = country.id,
                                scheme = country.scheme,
                                description = country.description
                                    ?: this.addressDetails?.country?.description,
                                uri = country.uri ?: this.addressDetails?.country?.uri
                            )
                        },
                    region = addressDetails.region
                        .let { region ->
                            RecordRegionDetails(
                                id = region.id,
                                scheme = region.scheme,
                                description = region.description
                                    ?: this.addressDetails?.region?.description,
                                uri = region.uri ?: this.addressDetails?.region?.uri
                            )
                        },
                    locality = addressDetails.locality
                        .let { locality ->
                            RecordLocalityDetails(
                                id = locality.id,
                                scheme = locality.scheme,
                                description = locality.description,
                                uri = locality.uri
                                    ?: this.addressDetails?.locality?.uri
                            )
                        }
                )
            } ?: this.addressDetails,
        postalCode = received.postalCode ?: this.postalCode
    )

fun List<RecordOption>.updateOptions(received: List<RequestOption>): List<RecordOption> =
    received.mapIfNotEmpty { requestOption ->
        RecordOption(
            hasOptions = requestOption.hasOptions,
            optionDetails = requestOption.optionDetails
        )
    } ?: this

fun RecordPeriod.updateContractPeriod(received: RequestPeriod): RecordPeriod =
    this.copy(
        startDate = received.startDate ?: this.startDate,
        endDate = received.endDate ?: this.endDate,
        durationInDays = received.durationInDays ?: this.durationInDays,
        maxExtentDate = received.maxExtentDate ?: this.maxExtentDate
    )

fun List<RecordRecurrentProcurement>.updateRecurrentProcurement(received: List<RequestRecurrentProcurement>): List<RecordRecurrentProcurement> =
    received.mapIfNotEmpty { recurrentProcurement ->
        RecordRecurrentProcurement(
            isRecurrent = recurrentProcurement.isRecurrent,
            description = recurrentProcurement.description,
            dates = recurrentProcurement.dates
                .map { date -> createPeriod(date) }
        )
    } ?: this

fun RecordItem.updateItem(received: RequestItem): UpdateRecordResult<RecordItem> {
    val classification = received.classification
        ?.let {
            this.classification
                ?.updateClassification(it)
                ?.doReturn { e -> return failure(e) }
                ?: createClassification(it)
        }
        ?: this.classification

    val unit = received.unit
        ?.let {
            this.unit
                ?.updateUnit(it)
                ?.doReturn { e -> return failure(e) }
                ?: createUnit(it)
        }
        ?: this.unit

    val additionalClassifications = updateStrategy(
        receivedElements = received.additionalClassifications,
        keyExtractorForReceivedElement = requestClassificationKeyExtractor,
        availableElements = this.additionalClassifications.toList(),
        keyExtractorForAvailableElement = recordClassificationKeyExtractor,
        updateBlock = RecordClassification::updateClassification,
        createBlock = ::createClassification
    )
        .doReturn { e -> return failure(e) }

    val deliveryAddress = received.deliveryAddress
        ?.let {
            this.deliveryAddress?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.deliveryAddress

    return this
        .copy(
            id = received.id,
            description = received.description ?: this.description,
            internalId = received.internalId ?: this.internalId,
            unit = unit,
            quantity = received.quantity ?: this.quantity,
            classification = classification,
            additionalClassifications = additionalClassifications,
            deliveryAddress = deliveryAddress,
            relatedLot = received.relatedLot ?: this.relatedLot
        )
        .asSuccess()
}

val recordClassificationKeyExtractor: (RecordClassification) -> String = { it.id }
val requestClassificationKeyExtractor: (RequestClassification) -> String = { it.id }

fun RecordClassification.updateClassification(received: RequestClassification): UpdateRecordResult<RecordClassification> {
    val classificationIdent = if (received.id == this.id && received.scheme == this.scheme)
        received.id to received.scheme
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'classification'. Ids mismatching: " +
                    "Classification from request (id = '${received.id}', scheme = '${received.scheme}'), " +
                    "Classification from release (id = '${this.id}', scheme = '${this.scheme}'). "
            )
        )

    return this
        .copy(
            id = classificationIdent.first,
            scheme = classificationIdent.second,
            uri = received.uri ?: this.uri,
            description = received.description ?: this.description
        )
        .asSuccess()
}

fun RecordUnit.updateUnit(received: RequestUnit): UpdateRecordResult<RecordUnit> {
    val unitIdent = if (received.id == this.id && received.scheme == this.scheme)
        received.id to received.scheme
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'unit'. Ids mismatching: " +
                    "Unit from request (id = '${received.id}', scheme = '${received.scheme}'), " +
                    "Unit from release (id = '${this.id}, scheme = '${this.scheme}''). "
            )
        )

    val value = received.value
        ?.let {
            this.value
                ?.updateValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValue(it)
        }
        ?: this.value

    return this
        .copy(
            id = unitIdent.first,
            scheme = unitIdent.second,
            name = received.name ?: this.name,
            uri = received.uri ?: this.uri,
            value = value
        )
        .asSuccess()
}

fun RecordCriteria.updateCriteria(received: RequestCriteria): UpdateRecordResult<RecordCriteria> {
    val requirementGroups = updateStrategy(
        receivedElements = received.requirementGroups,
        keyExtractorForReceivedElement = requestRequirementGroupsKeyExtractor,
        availableElements = this.requirementGroups,
        keyExtractorForAvailableElement = recordRequirementGroupsKeyExtractor,
        updateBlock = RecordRequirementGroup::updateRequirementGroup,
        createBlock = ::createRequirementGroup
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            description = received.description ?: this.description,
            title = received.title,
            relatesTo = received.relatesTo ?: this.relatesTo,
            relatedItem = received.relatedItem ?: this.relatedItem,
            source = received.source ?: this.source,
            requirementGroups = requirementGroups
        )
        .asSuccess()
}

val recordRequirementGroupsKeyExtractor: (RecordRequirementGroup) -> String = { it.id }
val requestRequirementGroupsKeyExtractor: (RequestRequirementGroup) -> String = { it.id }

fun RecordRequirementGroup.updateRequirementGroup(received: RequestRequirementGroup): UpdateRecordResult<RecordRequirementGroup> {
    val requirements = updateStrategy(
        receivedElements = received.requirements,
        keyExtractorForReceivedElement = recordRequirementKeyExtractor,
        availableElements = this.requirements,
        keyExtractorForAvailableElement = recordRequirementKeyExtractor,
        updateBlock = Requirement::updateRequirement,
        createBlock = ::createRequirement
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            description = received.description,
            requirements = requirements
        )
        .asSuccess()
}

val recordRequirementKeyExtractor: (Requirement) -> String = { it.id }

fun Requirement.updateRequirement(received: Requirement): UpdateRecordResult<Requirement> =
    Requirement(
        id = received.id,
        description = received.description ?: this.description,
        title = received.title,
        value = received.value,
        period = received.period ?: this.period,
        dataType = received.dataType
    )
        .asSuccess()

fun RecordConversion.updateConversion(received: RequestConversion): UpdateRecordResult<RecordConversion> {
    val coefficients = updateStrategy(
        receivedElements = received.coefficients,
        keyExtractorForReceivedElement = requestCoefficientsKeyExtractor,
        availableElements = this.coefficients,
        keyExtractorForAvailableElement = recordCoefficientsKeyExtractor,
        updateBlock = RecordCoefficient::updateCoefficient,
        createBlock = ::createCoefficient
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            description = received.description ?: this.description,
            relatedItem = received.relatedItem,
            relatesTo = received.relatesTo,
            rationale = received.rationale,
            coefficients = coefficients
        )
        .asSuccess()
}

val recordCoefficientsKeyExtractor: (RecordCoefficient) -> String = { it.id }
val requestCoefficientsKeyExtractor: (RequestCoefficient) -> String = { it.id }

fun RecordCoefficient.updateCoefficient(received: RequestCoefficient): UpdateRecordResult<RecordCoefficient> =
    this.copy(
        id = received.id,
        value = received.value,
        coefficient = received.coefficient
    )
        .asSuccess()

fun List<RecordLotGroup>.updateLotGroups(received: List<RequestLotGroup>): UpdateRecordResult<List<RecordLotGroup>> {
    val result = received.mapIfNotEmpty {
        RecordLotGroup(
            id = it.id,
            relatedLots = it.relatedLots,
            maximumValue = it.maximumValue,
            optionToCombine = it.optionToCombine
        )
    } ?: this
    return result.asSuccess()
}

fun RecordPeriod.updatePeriod(received: RequestPeriod): UpdateRecordResult<RecordPeriod> =
    this.copy(
        startDate = received.startDate ?: this.startDate,
        endDate = received.endDate ?: this.endDate,
        durationInDays = received.durationInDays ?: this.durationInDays,
        maxExtentDate = received.maxExtentDate ?: this.maxExtentDate
    )
        .asSuccess()

fun RecordRecordEnquiry.updateRecordEnquiry(received: RequestRecordEnquiry): UpdateRecordResult<RecordRecordEnquiry> {
    val author = received.author
        ?.let {
            this.author
                ?.updateAuthor(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAuthor(it)
        }
        ?: this.author

    return this.copy(
        id = received.id,
        relatedItem = received.relatedItem ?: this.relatedItem,
        description = received.description ?: this.description,
        title = received.title ?: this.title,
        relatedLot = received.relatedLot ?: this.relatedLot,
        date = received.date ?: this.date,
        answer = received.answer ?: this.answer,
        author = author,
        dateAnswered = received.dateAnswered ?: this.dateAnswered
    )
        .asSuccess()
}

fun RecordOrganizationReference.updateAuthor(received: RequestOrganizationReference): UpdateRecordResult<RecordOrganizationReference> {
    val authorId = if (received.id == this.id)
        received.id
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'author'. Ids mismatching: " +
                    "Author from request (id = '${received.id}'), " +
                    "Author from release (id = '${this.id}'). "
            )
        )

    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    val details = received.details
        ?.let {
            this.details
                ?.updateDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDetails(it)
        }
        ?: this.details

    val identifier = received.identifier
        ?.let {
            this.identifier
                ?.updateIdentifier(it)
                ?.doReturn { e -> return failure(e) }
                ?: createIdentifier(it)
        }
        ?: this.identifier

    val contactPoint = received.contactPoint
        ?.let {
            this.contactPoint
                ?.updateContactPoint(it)
                ?.doReturn { e -> return failure(e) }
                ?: createContactPoint(it)
        }
        ?: this.contactPoint

    val additionalIdentifiers = updateStrategy(
        receivedElements = received.additionalIdentifiers,
        keyExtractorForReceivedElement = requestIdentifierKeyExtractor,
        availableElements = this.additionalIdentifiers,
        keyExtractorForAvailableElement = recordIdentifierKeyExtractor,
        updateBlock = RecordIdentifier::updateIdentifierElement,
        createBlock = ::createIdentifier
    )
        .doReturn { e -> return failure(e) }

    val persones = updateStrategy(
        receivedElements = received.persones,
        keyExtractorForReceivedElement = requestPersonKeyExtractor,
        availableElements = this.persones,
        keyExtractorForAvailableElement = recordPersonKeyExtractor,
        updateBlock = RecordPerson::updatePerson,
        createBlock = ::createPerson
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = authorId,
            name = received.name ?: this.name,
            address = address,
            details = details,
            identifier = identifier,
            contactPoint = contactPoint,
            additionalIdentifiers = additionalIdentifiers,
            buyerProfile = received.buyerProfile ?: this.buyerProfile,
            persones = persones
        )
        .asSuccess()
}

val recordIdentifierKeyExtractor: (RecordIdentifier) -> String = { it.id }
val requestIdentifierKeyExtractor: (RequestIdentifier) -> String = { it.id }

val recordPersonKeyExtractor: (RecordPerson) -> String = { it.identifier.id + it.identifier.scheme }
val requestPersonKeyExtractor: (RequestPerson) -> String = { it.identifier.id + it.identifier.scheme }

fun RecordPerson.updatePerson(received: RequestPerson): UpdateRecordResult<RecordPerson> {
    val identifier = this.identifier.updateIdentifier(received.identifier)
        .doReturn { e -> return failure(e) }

    val businessFunctions = updateStrategy(
        receivedElements = received.businessFunctions,
        keyExtractorForReceivedElement = requestBusinessFunctionKeyExtractor,
        availableElements = this.businessFunctions,
        keyExtractorForAvailableElement = recordBusinessFunctionKeyExtractor,
        updateBlock = RecordBusinessFunction::updateBusinessFunction,
        createBlock = ::createBusinessFunction
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            title = received.title,
            name = received.name,
            identifier = identifier,
            businessFunctions = businessFunctions
        )
        .asSuccess()
}

val recordBusinessFunctionKeyExtractor: (RecordBusinessFunction) -> String = { it.id }
val requestBusinessFunctionKeyExtractor: (RequestBusinessFunction) -> String = { it.id }

fun RecordBusinessFunction.updateBusinessFunction(received: RequestBusinessFunction): UpdateRecordResult<RecordBusinessFunction> {
    val period = this.period.updatePeriod(received.period)
        .doReturn { e -> return failure(e) }
    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentBFKeyExtractor,
        availableElements = this.documents,
        keyExtractorForAvailableElement = recordDocumentBFKeyExtractor,
        updateBlock = RecordDocumentBF::updateDocumentBF,
        createBlock = ::createDocumentBF
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            period = period,
            documents = documents,
            type = received.type,
            jobTitle = received.jobTitle
        )
        .asSuccess()
}

val recordDocumentBFKeyExtractor: (RecordDocumentBF) -> String = { it.id }
val requestDocumentBFKeyExtractor: (RequestDocumentBF) -> String = { it.id }

fun RecordDocumentBF.updateDocumentBF(received: RequestDocumentBF): UpdateRecordResult<RecordDocumentBF> =
    this.copy(
        id = received.id,
        title = received.title ?: this.title,
        url = received.url ?: this.url,
        description = received.description ?: this.description,
        dateModified = received.dateModified ?: this.dateModified,
        datePublished = received.datePublished ?: this.datePublished,
        documentType = received.documentType
    )
        .asSuccess()

fun RecordContactPoint.updateContactPoint(received: RequestContactPoint): UpdateRecordResult<RecordContactPoint> =
    this.copy(
        name = received.name ?: this.name,
        url = received.url ?: this.url,
        faxNumber = received.faxNumber ?: this.faxNumber,
        telephone = received.telephone ?: this.telephone,
        email = received.email ?: this.email
    )
        .asSuccess()

fun RecordIdentifier.updateIdentifier(received: RequestIdentifier): UpdateRecordResult<RecordIdentifier> {
    val identifierIdent = if (received.id == this.id && received.scheme == this.scheme)
        received.id to received.scheme
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'identifier'. Ids mismatching: " +
                    "Identifier from request (id = '${received.id}, scheme = '${received.scheme}''), " +
                    "Identifier from release (id = '${this.id}, scheme = '${this.scheme}''). "
            )
        )
    return this
        .copy(
            id = identifierIdent.first,
            scheme = identifierIdent.second,
            uri = received.uri ?: this.uri,
            legalName = received.legalName ?: this.legalName
        )
        .asSuccess()
}

fun RecordIdentifier.updateIdentifierElement(received: RequestIdentifier): UpdateRecordResult<RecordIdentifier> =
    this.copy(
        id = received.id,
        scheme = received.scheme,
        uri = received.uri ?: this.uri,
        legalName = received.legalName ?: this.legalName
    )
        .asSuccess()

fun RecordDetails.updateDetails(received: RequestDetails): UpdateRecordResult<RecordDetails> {
    val bankAccounts = updateStrategy(
        receivedElements = received.bankAccounts,
        keyExtractorForReceivedElement = requestBankAccountKeyExtractor,
        availableElements = this.bankAccounts,
        keyExtractorForAvailableElement = recordBankAccountKeyExtractor,
        updateBlock = RecordBankAccount::updateBankAccount,
        createBlock = ::createBankAccount
    )
        .doReturn { e -> return failure(e) }

    val legalForm = received.legalForm
        ?.let {
            this.legalForm
                ?.updateLegalForm(it)
                ?.doReturn { e -> return failure(e) }
                ?: createLegalForm(it)
        }
        ?: this.legalForm

    val permits = updateStrategy(
        receivedElements = received.permits,
        keyExtractorForReceivedElement = requestPermitsKeyExtractor,
        availableElements = this.permits,
        keyExtractorForAvailableElement = recordPermitsKeyExtractor,
        updateBlock = RecordPermits::updatePermits,
        createBlock = ::createPermits
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            typeOfBuyer = received.typeOfBuyer ?: this.typeOfBuyer,
            bankAccounts = bankAccounts,
            isACentralPurchasingBody = received.isACentralPurchasingBody ?: this.isACentralPurchasingBody,
            legalForm = legalForm,
            mainEconomicActivities = mainEconomicActivities,
            mainGeneralActivity = received.mainGeneralActivity ?: this.mainGeneralActivity,
            mainSectoralActivity = received.mainSectoralActivity ?: this.mainSectoralActivity,
            nutsCode = received.nutsCode ?: this.nutsCode,
            permits = permits,
            scale = received.scale ?: this.scale,
            typeOfSupplier = received.typeOfSupplier ?: this.typeOfSupplier
        )
        .asSuccess()
}

val recordBankAccountKeyExtractor: (RecordBankAccount) -> String = { it.identifier.id }
val requestBankAccountKeyExtractor: (RequestBankAccount) -> String = { it.identifier.id }

val recordPermitsKeyExtractor: (RecordPermits) -> String = { it.id }
val requestPermitsKeyExtractor: (RequestPermits) -> String = { it.id }

fun RecordPermits.updatePermits(received: RequestPermits): UpdateRecordResult<RecordPermits> {
    val permitDetails = received.permitDetails
        ?.let {
            this.permitDetails
                ?.updatePermitDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPermitDetails(it)
        }
        ?: this.permitDetails

    return this
        .copy(
            id = received.id,
            scheme = received.scheme,
            url = received.url ?: this.url,
            permitDetails = permitDetails
        )
        .asSuccess()
}

fun RecordPermitDetails.updatePermitDetails(received: RequestPermitDetails): UpdateRecordResult<RecordPermitDetails> {
    val validityPeriod = received.validityPeriod
        ?.let {
            this.validityPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.validityPeriod

    return this
        .copy(
            issuedBy = received.issuedBy?.let { requestIssue ->
                val issuedById = if (requestIssue.id == this.issuedBy?.id)
                    requestIssue.id
                else
                    return failure(
                        Fail.Error.BadRequest(
                            description = "Cannot update 'issuedBy'. Ids mismatching: " +
                                "issuedBy from request (id = '${requestIssue.id}'), " +
                                "issuedBy from release (id = '${this.issuedBy?.id}'). "
                        )
                    )
                RecordIssue(
                    id = issuedById,
                    name = requestIssue.name ?: this.issuedBy?.name
                )
            } ?: this.issuedBy,
            issuedThought = received.issuedThought?.let { requestIssue ->
                val issuedByThought = if (requestIssue.id == this.issuedThought?.id)
                    requestIssue.id
                else
                    return failure(
                        Fail.Error.BadRequest(
                            description = "Cannot update 'issuedThought'. Ids mismatching: " +
                                "issuedThought from request (id = '${requestIssue.id}'), " +
                                "issuedThought from release (id = '${this.issuedThought?.id}'). "
                        )
                    )
                RecordIssue(
                    id = issuedByThought,
                    name = requestIssue.name ?: this.issuedThought?.name
                )
            } ?: this.issuedThought,
            validityPeriod = validityPeriod
        )
        .asSuccess()
}

fun RecordLegalForm.updateLegalForm(received: RequestLegalForm): UpdateRecordResult<RecordLegalForm> {
    val legalFormIdent = if (received.id == this.id && received.scheme == this.scheme)
        received.id to received.scheme
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'legalForm'. Ids mismatching: " +
                    "LegalForm from request (id = '${received.id}', scheme = '${received.scheme}'), " +
                    "LegalForm from release (id = '${this.id}', scheme = '${this.scheme}'). "
            )
        )
    return this
        .copy(
            id = legalFormIdent.first,
            description = received.description,
            uri = received.uri ?: this.uri,
            scheme = legalFormIdent.second
        )
        .asSuccess()
}

fun RecordBankAccount.updateBankAccount(received: RequestBankAccount): UpdateRecordResult<RecordBankAccount> {
    val identifier = this.identifier.updateAccountIdentifier(received.identifier)
        .doReturn { e -> return failure(e) }

    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    val accountIdentification = received.accountIdentification
        ?.let {
            this.accountIdentification
                ?.updateAccountIdentifier(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAccountIdentifier(it)
        }
        ?: this.accountIdentification

    val additionalAccountIdentifiers = updateStrategy(
        receivedElements = received.additionalAccountIdentifiers,
        keyExtractorForReceivedElement = requestAccountIdentifierKeyExtractor,
        availableElements = this.additionalAccountIdentifiers.toList(),
        keyExtractorForAvailableElement = recordAccountIdentifierKeyExtractor,
        updateBlock = RecordAccountIdentifier::updateAccountIdentifierElement,
        createBlock = ::createAccountIdentifier
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            identifier = identifier,
            address = address,
            description = received.description ?: this.description,
            accountIdentification = accountIdentification,
            additionalAccountIdentifiers = additionalAccountIdentifiers,
            bankName = received.bankName ?: this.bankName
        )
        .asSuccess()
}

val recordAccountIdentifierKeyExtractor: (RecordAccountIdentifier) -> String = { it.id }
val requestAccountIdentifierKeyExtractor: (RequestAccountIdentifier) -> String = { it.id }

fun RecordAccountIdentifier.updateAccountIdentifier(received: RequestAccountIdentifier): UpdateRecordResult<RecordAccountIdentifier> {
    val accountIdentifierIdent = if (received.id == this.id && received.scheme == this.scheme)
        received.id to received.scheme
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'accountIdentifier'. Ids mismatching: " +
                    "AccountIdentifier from request (id = '${received.id}', scheme = '${received.scheme}'), " +
                    "AccountIdentifier from release (id = '${this.id}', scheme = '${this.scheme}'). "
            )
        )
    return this
        .copy(
            id = accountIdentifierIdent.first,
            scheme = accountIdentifierIdent.second
        )
        .asSuccess()
}

fun RecordAccountIdentifier.updateAccountIdentifierElement(received: RequestAccountIdentifier): UpdateRecordResult<RecordAccountIdentifier> =
    this.copy(
        id = received.id,
        scheme = received.scheme
    )
        .asSuccess()

fun List<RecordChange>.updateChanges(received: List<RequestChange>): UpdateRecordResult<List<RecordChange>> {
    val result = received.mapIfNotEmpty { rqChange ->
        RecordChange(
            property = rqChange.property,
            formerValue = rqChange.formerValue
        )
    } ?: this
    return result.asSuccess()
}

fun RecordDocument.updateDocument(received: RequestDocument): UpdateRecordResult<RecordDocument> =
    this.copy(
        id = received.id,
        relatedLots = this.relatedLots.update(received.relatedLots),
        description = received.description ?: this.description,
        url = received.url ?: this.url,
        documentType = received.documentType ?: this.documentType,
        datePublished = received.datePublished ?: this.datePublished,
        language = received.language ?: this.language,
        dateModified = received.dateModified ?: this.dateModified,
        format = received.format ?: this.format,
        relatedConfirmations = this.relatedConfirmations.update(received.relatedConfirmations),
        title = received.title ?: this.title
    )
        .asSuccess()

fun RecordElectronicAuctions.updateElectronicAuctions(received: RequestElectronicAuctions): UpdateRecordResult<RecordElectronicAuctions> =
    this.copy(
        details = updateStrategy(
            receivedElements = received.details,
            keyExtractorForReceivedElement = requestElectronicAuctionsDetailsKeyExtractor,
            availableElements = this.details.toList(),
            keyExtractorForAvailableElement = recordElectronicAuctionsDetailsKeyExtractor,
            updateBlock = RecordElectronicAuctionsDetails::updateElectronicAuctionsDetails,
            createBlock = ::createElectronicAuctionsDetails
        )
            .doReturn { e -> return failure(e) }
    )
        .asSuccess()

val recordElectronicAuctionsDetailsKeyExtractor: (RecordElectronicAuctionsDetails) -> String = { it.id }
val requestElectronicAuctionsDetailsKeyExtractor: (RequestElectronicAuctionsDetails) -> String = { it.id }

fun RecordElectronicAuctionsDetails.updateElectronicAuctionsDetails(received: RequestElectronicAuctionsDetails): UpdateRecordResult<RecordElectronicAuctionsDetails> {
    val electronicAuctionModalities = this.electronicAuctionModalities.updateElectronicAuctionModalities(received.electronicAuctionModalities)
        .doReturn { e -> return failure(e) }

    val electronicAuctionProgress = updateStrategy(
        receivedElements = received.electronicAuctionProgress,
        keyExtractorForReceivedElement = requestElectronicAuctionProgressKeyExtractor,
        availableElements = this.electronicAuctionProgress.toList(),
        keyExtractorForAvailableElement = recordElectronicAuctionProgressKeyExtractor,
        updateBlock = RecordElectronicAuctionProgress::updateElectronicAuctionProgress,
        createBlock = ::createElectronicAuctionProgress
    )
        .doReturn { e -> return failure(e) }

    val electronicAuctionResult = this.electronicAuctionResult.updateElectronicAuctionResult(received.electronicAuctionResult)
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            relatedLot = received.relatedLot ?: this.relatedLot,
            auctionPeriod = received.auctionPeriod ?: this.auctionPeriod,
            electronicAuctionModalities = electronicAuctionModalities,
            electronicAuctionProgress = electronicAuctionProgress,
            electronicAuctionResult = electronicAuctionResult
        )
        .asSuccess()
}

val recordElectronicAuctionProgressKeyExtractor: (RecordElectronicAuctionProgress) -> String = { it.id }
val requestElectronicAuctionProgressKeyExtractor: (RequestElectronicAuctionProgress) -> String = { it.id }

fun List<RecordElectronicAuctionResult>.updateElectronicAuctionResult(received: List<RequestElectronicAuctionResult>): UpdateRecordResult<List<RecordElectronicAuctionResult>> {
    val result = received.mapIfNotEmpty { rqElectronicAuctionResult ->
        RecordElectronicAuctionResult(
            relatedBid = rqElectronicAuctionResult.relatedBid,
            value = rqElectronicAuctionResult.value
        )
    } ?: this
    return result.asSuccess()
}

fun List<RecordElectronicAuctionModalities>.updateElectronicAuctionModalities(received: List<RequestElectronicAuctionModalities>): UpdateRecordResult<List<RecordElectronicAuctionModalities>> {
    val result = received.mapIfNotEmpty { rqElectronicAuctionModality ->
        RecordElectronicAuctionModalities(
            url = rqElectronicAuctionModality.url,
            eligibleMinimumDifference = rqElectronicAuctionModality.eligibleMinimumDifference
        )
    } ?: this
    return result.asSuccess()
}

fun RecordElectronicAuctionProgress.updateElectronicAuctionProgress(
    received: RequestElectronicAuctionProgress
): UpdateRecordResult<RecordElectronicAuctionProgress> {
    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    val breakdown = this.breakdown.updateElectronicAuctionProgressBreakdown(received.breakdown)
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            period = period,
            breakdown = breakdown
        )
        .asSuccess()
}

fun List<RecordElectronicAuctionProgressBreakdown>.updateElectronicAuctionProgressBreakdown(
    received: List<RequestElectronicAuctionProgressBreakdown>
): UpdateRecordResult<List<RecordElectronicAuctionProgressBreakdown>> {
    val result = received.mapIfNotEmpty { rqBreakdown ->
        RecordElectronicAuctionProgressBreakdown(
            relatedBid = rqBreakdown.relatedBid,
            value = rqBreakdown.value,
            status = rqBreakdown.status,
            dateMet = rqBreakdown.dateMet
        )
    } ?: this
    return result.asSuccess()
}

fun List<String>.update(received: List<String>): List<String> = received.mapIfNotEmpty { it } ?: this

fun RecordTender.updateReleaseTender(received: RequestTender): UpdateRecordResult<RecordTender> {
    val auctionPeriod = received.auctionPeriod
        ?.let {
            this.auctionPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.auctionPeriod

    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents,
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val amendments = updateStrategy(
        receivedElements = received.amendments,
        keyExtractorForReceivedElement = requestAmendmentKeyExtractor,
        availableElements = this.amendments,
        keyExtractorForAvailableElement = recordAmendmentKeyExtractor,
        updateBlock = RecordAmendment::updateAmendment,
        createBlock = ::createAmendment
    )
        .doReturn { e -> return failure(e) }

    val awardPeriod = received.awardPeriod
        ?.let {
            this.awardPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.awardPeriod

    val conversions = updateStrategy(
        receivedElements = received.conversions,
        keyExtractorForReceivedElement = requestConversionKeyExtractor,
        availableElements = this.conversions,
        keyExtractorForAvailableElement = recordConversionKeyExtractor,
        updateBlock = RecordConversion::updateConversion,
        createBlock = ::createConversion
    )
        .doReturn { e -> return failure(e) }

    val criteria = updateStrategy(
        receivedElements = received.criteria,
        keyExtractorForReceivedElement = requestCriteriaKeyExtractor,
        availableElements = this.criteria,
        keyExtractorForAvailableElement = recordCriteriaKeyExtractor,
        updateBlock = RecordCriteria::updateCriteria,
        createBlock = ::createCriteria
    )
        .doReturn { e -> return failure(e) }

    val electronicAuctions = received.electronicAuctions
        ?.let {
            this.electronicAuctions
                ?.updateElectronicAuctions(it)
                ?.doReturn { e -> return failure(e) }
                ?: createElectronicAuctions(it)
        }
        ?: this.electronicAuctions

    val enquiries = updateStrategy(
        receivedElements = received.enquiries,
        keyExtractorForReceivedElement = requestRecordEnquiryKeyExtractor,
        availableElements = this.enquiries,
        keyExtractorForAvailableElement = recordRecordEnquiryKeyExtractor,
        updateBlock = RecordRecordEnquiry::updateRecordEnquiry,
        createBlock = ::createRecordEnquiry
    )
        .doReturn { e -> return failure(e) }
        .toMutableList()

    val enquiryPeriod = received.enquiryPeriod
        ?.let {
            this.enquiryPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.enquiryPeriod

    val items = updateStrategy(
        receivedElements = received.items,
        keyExtractorForReceivedElement = requestItemKeyExtractor,
        availableElements = this.items,
        keyExtractorForAvailableElement = recordItemKeyExtractor,
        updateBlock = RecordItem::updateItem,
        createBlock = ::createItem
    )
        .doReturn { e -> return failure(e) }

    val lotGroups = this.lotGroups.updateLotGroups(received.lotGroups)
        .doReturn { e -> return failure(e) }

    val lots = updateStrategy(
        receivedElements = received.lots,
        keyExtractorForReceivedElement = requestLotKeyExtractor,
        availableElements = this.lots,
        keyExtractorForAvailableElement = recordLotKeyExtractor,
        updateBlock = RecordLot::updateLot,
        createBlock = ::createLot
    )
        .doReturn { e -> return failure(e) }

    val standstillPeriod = received.standstillPeriod
        ?.let {
            this.standstillPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.standstillPeriod

    val tenderPeriod = received.tenderPeriod
        ?.let {
            this.tenderPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.tenderPeriod

    val value = received.value
        ?.let {
            this.value
                ?.updateValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValue(it)
        }
        ?: this.value

    val milestones = updateStrategy(
        receivedElements = received.milestones,
        keyExtractorForReceivedElement = requestMilestoneKeyExtractor,
        availableElements = this.milestones,
        keyExtractorForAvailableElement = recordMilestoneKeyExtractor,
        updateBlock = RecordMilestone::updateMilestone,
        createBlock = ::createMilestone
    )
        .doReturn { e -> return failure(e) }

    val classification = received.classification
        ?.let {
            this.classification
                ?.updateClassification(it)
                ?.doReturn { e -> return failure(e) }
                ?: createClassification(it)
        }
        ?: this.classification

    val amendment = received.amendment
        ?.let {
            this.amendment
                ?.updateAmendment(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAmendment(it)
        }
        ?: this.amendment

    val contractPeriod = received.contractPeriod
        ?.let {
            this.contractPeriod?.updateContractPeriod(it) ?: createContractPeriod(it)
        }
        ?: this.contractPeriod

    val minValue = received.minValue
        ?.let {
            this.minValue
                ?.updateValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValue(it)
        }
        ?: this.minValue

    val tenderers = updateStrategy(
        receivedElements = received.tenderers,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.tenderers,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateOrganizationReference,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }

    val acceleratedProcedure = received.acceleratedProcedure
        ?.let {
            this.acceleratedProcedure
                ?.updateAcceleratedProcedure(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAcceleratedProcedure(it)
        }
        ?: this.acceleratedProcedure

    val designContest = received.designContest
        ?.let {
            this.designContest
                ?.updateDesignContest(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDesignContest(it)
        }
        ?: this.designContest

    val dynamicPurchasingSystem = received.dynamicPurchasingSystem
        ?.let {
            this.dynamicPurchasingSystem
                ?.updateDynamicPurchasingSystem(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDynamicPurchasingSystem(it)
        }
        ?: this.dynamicPurchasingSystem

    val electronicWorkflows = received.electronicWorkflows
        ?.let {
            this.electronicWorkflows
                ?.updateElectronicWorkflows(it)
                ?.doReturn { e -> return failure(e) }
                ?: createElectronicWorkflows(it)
        }
        ?: this.electronicWorkflows

    val framework = received.framework
        ?.let {
            this.framework
                ?.updateFramework(it)
                ?.doReturn { e -> return failure(e) }
                ?: createFramework(it)
        }
        ?: this.framework

    val jointProcurement = received.jointProcurement
        ?.let {
            this.jointProcurement
                ?.updateJointProcurement(it)
                ?.doReturn { e -> return failure(e) }
                ?: createJointProcurement(it)
        }
        ?: this.jointProcurement

    val lotDetails = received.lotDetails
        ?.let {
            this.lotDetails
                ?.updateLotDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createLotDetails(it)
        }
        ?: this.lotDetails

    val objectives = received.objectives
        ?.let {
            this.objectives
                ?.updateObjectives(it)
                ?.doReturn { e -> return failure(e) }
                ?: createObjectives(it)
        }
        ?: this.objectives

    val participationFees = this.participationFees.updateParticipationFee(received.participationFees)
        .doReturn { e -> return failure(e) }

    val procedureOutsourcing = received.procedureOutsourcing
        ?.let {
            this.procedureOutsourcing
                ?.updateProcedureOutsourcing(it)
                ?.doReturn { e -> return failure(e) }
                ?: createProcedureOutsourcing(it)
        }
        ?: this.procedureOutsourcing

    val procuringEntity = received.procuringEntity
        ?.let {
            this.procuringEntity
                ?.updateOrganizationReference(it)
                ?.doReturn { e -> return failure(e) }
                ?: createOrganizationReference(it)
        }
        ?: this.procuringEntity

    val reviewParties = updateStrategy(
        receivedElements = received.reviewParties,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.reviewParties,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateOrganizationReference,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }

    val reviewPeriod = received.reviewPeriod
        ?.let {
            this.reviewPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.reviewPeriod

    return this
        .copy(
            id = this.id,
            status = received.status ?: this.status,
            auctionPeriod = auctionPeriod,
            title = received.title ?: this.title,
            description = received.description ?: this.description,
            documents = documents,
            statusDetails = received.statusDetails ?: this.statusDetails,
            amendments = amendments,
            submissionMethodRationale = this.submissionMethodRationale.update(received.submissionMethodRationale),
            submissionMethodDetails = received.submissionMethodDetails ?: this.submissionMethodDetails,
            awardCriteria = received.awardCriteria ?: this.awardCriteria,
            awardCriteriaDetails = received.awardCriteriaDetails ?: this.awardCriteriaDetails,
            awardPeriod = awardPeriod,
            conversions = conversions,
            criteria = criteria,
            electronicAuctions = electronicAuctions,
            enquiries = enquiries,
            enquiryPeriod = enquiryPeriod,
            hasEnquiries = received.hasEnquiries ?: this.hasEnquiries,
            items = items,
            lotGroups = lotGroups,
            lots = lots,
            procurementMethodModalities = this.procurementMethodModalities.update(received.procurementMethodModalities),
            requiresElectronicCatalogue = received.requiresElectronicCatalogue ?: this.requiresElectronicCatalogue,
            standstillPeriod = standstillPeriod,
            submissionMethod = this.submissionMethod.update(received.submissionMethod),
            tenderPeriod = tenderPeriod,
            value = value,
            milestones = milestones,
            classification = classification,
            amendment = amendment,
            contractPeriod = contractPeriod,
            procurementMethodDetails = received.procurementMethodDetails ?: this.procurementMethodDetails,
            legalBasis = received.legalBasis ?: this.legalBasis,
            mainProcurementCategory = received.mainProcurementCategory ?: this.mainProcurementCategory,
            eligibilityCriteria = received.eligibilityCriteria ?: this.eligibilityCriteria,
            minValue = minValue,
            numberOfTenderers = received.numberOfTenderers ?: this.numberOfTenderers,
            procurementMethod = received.procurementMethod ?: this.procurementMethod,
            procurementMethodRationale = received.procurementMethodRationale ?: this.procurementMethodRationale,
            additionalProcurementCategories = this.additionalProcurementCategories.update(received.additionalProcurementCategories),
            procurementMethodAdditionalInfo = received.procurementMethodAdditionalInfo ?: this.procurementMethodAdditionalInfo,
            submissionLanguages = this.submissionLanguages.update(received.submissionLanguages),
            tenderers = tenderers,
            acceleratedProcedure = acceleratedProcedure,
            designContest = designContest,
            dynamicPurchasingSystem = dynamicPurchasingSystem,
            electronicWorkflows = electronicWorkflows,
            framework = framework,
            jointProcurement = jointProcurement,
            lotDetails = lotDetails,
            objectives = objectives,
            participationFees = participationFees,
            procedureOutsourcing = procedureOutsourcing,
            procuringEntity = procuringEntity,
            reviewParties = reviewParties,
            reviewPeriod = reviewPeriod,
            secondStage = received.secondStage ?: this.secondStage
        )
        .asSuccess()
}

val recordDocumentKeyExtractor: (RecordDocument) -> String = { it.id }
val requestDocumentKeyExtractor: (RequestDocument) -> String = { it.id }

val recordAmendmentKeyExtractor: (RecordAmendment) -> String = { it.id }
val requestAmendmentKeyExtractor: (RequestAmendment) -> String = { it.id }

val recordConversionKeyExtractor: (RecordConversion) -> String = { it.id }
val requestConversionKeyExtractor: (RequestConversion) -> String = { it.id }

val recordCriteriaKeyExtractor: (RecordCriteria) -> String = { it.id }
val requestCriteriaKeyExtractor: (RequestCriteria) -> String = { it.id }

val recordRecordEnquiryKeyExtractor: (RecordRecordEnquiry) -> String = { it.id }
val requestRecordEnquiryKeyExtractor: (RequestRecordEnquiry) -> String = { it.id }

val recordItemKeyExtractor: (RecordItem) -> String = { it.id }
val requestItemKeyExtractor: (RequestItem) -> String = { it.id }

val recordLotKeyExtractor: (RecordLot) -> String = { it.id }
val requestLotKeyExtractor: (RequestLot) -> String = { it.id }

val recordMilestoneKeyExtractor: (RecordMilestone) -> String = { it.id }
val requestMilestoneKeyExtractor: (RequestMilestone) -> String = { it.id }

val recordOrganizationReferenceKeyExtractor: (RecordOrganizationReference) -> String = { it.id }
val requestOrganizationReferenceKeyExtractor: (RequestOrganizationReference) -> String = { it.id }

fun RecordProcedureOutsourcing.updateProcedureOutsourcing(
    received: RequestProcedureOutsourcing
): UpdateRecordResult<RecordProcedureOutsourcing> {
    val outsourcedTo = received.outsourcedTo
        ?.let {
            this.outsourcedTo
                ?.updateOrganization(it)
                ?.doReturn { e -> return failure(e) }
                ?: createOrganization(it)
        }
        ?: this.outsourcedTo

    return this
        .copy(
            procedureOutsourced = received.procedureOutsourced ?: this.procedureOutsourced,
            outsourcedTo = outsourcedTo
        )
        .asSuccess()
}

fun List<RecordParticipationFee>.updateParticipationFee(received: List<RequestParticipationFee>): UpdateRecordResult<List<RecordParticipationFee>> {
    val result = received.mapIfNotEmpty {
        RecordParticipationFee(
            type = it.type,
            value = it.value,
            description = it.description,
            methodOfPayment = it.methodOfPayment
        )
    } ?: this
    return result.asSuccess()
}

fun RecordObjectives.updateObjectives(received: RequestObjectives): UpdateRecordResult<RecordObjectives> =
    this.copy(
        types = this.types.update(received.types),
        additionalInformation = received.additionalInformation ?: this.additionalInformation
    )
        .asSuccess()

fun RecordLotDetails.updateLotDetails(received: RequestLotDetails): UpdateRecordResult<RecordLotDetails> =
    this.copy(
        maximumLotsAwardedPerSupplier = received.maximumLotsAwardedPerSupplier
            ?: this.maximumLotsAwardedPerSupplier,
        maximumLotsBidPerSupplier = received.maximumLotsBidPerSupplier ?: this.maximumLotsBidPerSupplier
    )
        .asSuccess()

fun RecordJointProcurement.updateJointProcurement(
    received: RequestJointProcurement
): UpdateRecordResult<RecordJointProcurement> =
    this.copy(
        isJointProcurement = received.isJointProcurement ?: this.isJointProcurement,
        country = received.country ?: this.country
    )
        .asSuccess()

fun RecordFramework.updateFramework(received: RequestFramework): UpdateRecordResult<RecordFramework> =
    this.copy(
        isAFramework = received.isAFramework ?: this.isAFramework,
        additionalBuyerCategories = this.additionalBuyerCategories.update(received.additionalBuyerCategories),
        exceptionalDurationRationale = received.exceptionalDurationRationale
            ?: this.exceptionalDurationRationale,
        maxSuppliers = received.maxSuppliers ?: this.maxSuppliers,
        typeOfFramework = received.typeOfFramework ?: this.typeOfFramework
    )
        .asSuccess()

fun RecordElectronicWorkflows.updateElectronicWorkflows(
    received: RequestElectronicWorkflows
): UpdateRecordResult<RecordElectronicWorkflows> =
    this.copy(
        useOrdering = received.useOrdering ?: this.useOrdering,
        acceptInvoicing = received.acceptInvoicing ?: this.acceptInvoicing,
        usePayment = received.usePayment ?: this.usePayment
    )
        .asSuccess()

fun RecordDynamicPurchasingSystem.updateDynamicPurchasingSystem(
    received: RequestDynamicPurchasingSystem
): UpdateRecordResult<RecordDynamicPurchasingSystem> =
    this.copy(
        hasDynamicPurchasingSystem = received.hasDynamicPurchasingSystem ?: this.hasDynamicPurchasingSystem,
        hasOutsideBuyerAccess = received.hasOutsideBuyerAccess ?: this.hasOutsideBuyerAccess,
        noFurtherContracts = received.noFurtherContracts ?: this.noFurtherContracts
    )
        .asSuccess()

fun RecordDesignContest.updateDesignContest(received: RequestDesignContest): UpdateRecordResult<RecordDesignContest> {
    val juryMembers = updateStrategy(
        receivedElements = received.juryMembers,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.juryMembers,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateOrganizationReference,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }

    val participants = updateStrategy(
        receivedElements = received.participants,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.participants,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateOrganizationReference,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }

    val prizes = updateStrategy(
        receivedElements = received.prizes,
        keyExtractorForReceivedElement = requestItemKeyExtractor,
        availableElements = this.prizes,
        keyExtractorForAvailableElement = recordItemKeyExtractor,
        updateBlock = RecordItem::updateItem,
        createBlock = ::createItem
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            hasPrizes = received.hasPrizes ?: this.hasPrizes,
            juryDecisionBinding = received.juryDecisionBinding ?: this.juryDecisionBinding,
            juryMembers = juryMembers,
            participants = participants,
            paymentsToParticipants = received.paymentsToParticipants ?: this.paymentsToParticipants,
            prizes = prizes,
            serviceContractAward = received.serviceContractAward ?: this.serviceContractAward
        )
        .asSuccess()
}

fun RecordAcceleratedProcedure.updateAcceleratedProcedure(
    received: RequestAcceleratedProcedure
): UpdateRecordResult<RecordAcceleratedProcedure> =
    this.copy(
        isAcceleratedProcedure = received.isAcceleratedProcedure ?: this.isAcceleratedProcedure,
        acceleratedProcedureJustification = received.acceleratedProcedureJustification
            ?: this.acceleratedProcedureJustification
    )
        .asSuccess()

fun Value.updateValue(received: Value): UpdateRecordResult<Value> =
    this.copy(
        amount = received.amount ?: this.amount,
        currency = received.currency ?: this.currency,
        amountNet = received.amountNet ?: this.amountNet,
        valueAddedTaxIncluded = received.valueAddedTaxIncluded ?: this.valueAddedTaxIncluded
    )
        .asSuccess()

fun RecordOrganization.updateOrganization(received: RequestOrganization): UpdateRecordResult<RecordOrganization> {
    val details = received.details
        ?.let {
            this.details
                ?.updateDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDetails(it)
        }
        ?: this.details

    val identifier = received.identifier
        ?.let {
            this.identifier
                ?.updateIdentifier(it)
                ?.doReturn { e -> return failure(e) }
                ?: createIdentifier(it)
        }
        ?: this.identifier

    val persones = updateStrategy(
        receivedElements = received.persones,
        keyExtractorForReceivedElement = requestPersonKeyExtractor,
        availableElements = this.persones.toList(),
        keyExtractorForAvailableElement = recordPersonKeyExtractor,
        updateBlock = RecordPerson::updatePerson,
        createBlock = ::createPerson
    )
        .doReturn { e -> return failure(e) }

    val additionalIdentifiers = updateStrategy(
        receivedElements = received.additionalIdentifiers,
        keyExtractorForReceivedElement = requestIdentifierKeyExtractor,
        availableElements = this.additionalIdentifiers.toList(),
        keyExtractorForAvailableElement = recordIdentifierKeyExtractor,
        updateBlock = RecordIdentifier::updateIdentifierElement,
        createBlock = ::createIdentifier
    )
        .doReturn { e -> return failure(e) }

    val contactPoint = received.contactPoint
        ?.let {
            this.contactPoint
                ?.updateContactPoint(it)
                ?.doReturn { e -> return failure(e) }
                ?: createContactPoint(it)
        }
        ?: this.contactPoint

    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    return this
        .copy(
            id = received.id,
            name = received.name ?: this.name,
            details = details,
            identifier = identifier,
            persones = persones,
            buyerProfile = received.buyerProfile ?: this.buyerProfile,
            additionalIdentifiers = additionalIdentifiers,
            contactPoint = contactPoint,
            address = address,
            roles = this.roles.union(received.roles).toList()
        )
        .asSuccess()
}

val recordCandidateKeyExtractor: (RecordCandidate) -> String = { it.id }
val requestCandidateKeyExtractor: (RequestCandidate) -> String = { it.id }

fun RecordSubmissionDetail.updateSubmissionDetail(received: RequestSubmissionDetail): UpdateRecordResult<RecordSubmissionDetail> {
    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents,
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val candidates = updateStrategy(
        receivedElements = received.candidates,
        keyExtractorForReceivedElement = requestCandidateKeyExtractor,
        availableElements = this.candidates,
        keyExtractorForAvailableElement = recordCandidateKeyExtractor,
        updateBlock = RecordCandidate::updateCandidate,
        createBlock = ::createCandidate
    )
        .doReturn { e -> return failure(e) }

    val requirementResponses = updateStrategy(
        receivedElements = received.requirementResponses,
        keyExtractorForReceivedElement = requestRequirementResponseKeyExtractor,
        availableElements = this.requirementResponses,
        keyExtractorForAvailableElement = recordRequirementResponseKeyExtractor,
        updateBlock = RecordRequirementResponse::updateRequirementResponse,
        createBlock = ::createRequirementResponse
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            date = received.date ?: this.date,
            status = received.status ?: this.status,
            documents = documents,
            candidates = candidates,
            requirementResponses = requirementResponses
        )
        .asSuccess()
}

fun RecordQualification.updateQualification(received: RequestQualification): UpdateRecordResult<RecordQualification> =
    this.copy(
        id = received.id,
        date = received.date ?: this.date,
        status = received.status ?: this.status,
        statusDetails = received.statusDetails ?: this.statusDetails,
        relatedSubmission = received.relatedSubmission ?: this.relatedSubmission,
        scoring = received.scoring ?: this.scoring
    )
        .asSuccess()


fun RecordAward.updateAward(received: RequestAward): UpdateRecordResult<RecordAward> {
    val contractPeriod = received.contractPeriod
        ?.let {
            this.contractPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.contractPeriod

    val reviewProceedings = received.reviewProceedings
        ?.let {
            this.reviewProceedings
                ?.updateReviewProceedings(it)
                ?.doReturn { e -> return failure(e) }
                ?: createReviewProceedings(it)
        }
        ?: this.reviewProceedings

    val items = updateStrategy(
        receivedElements = received.items,
        keyExtractorForReceivedElement = requestItemKeyExtractor,
        availableElements = this.items,
        keyExtractorForAvailableElement = recordItemKeyExtractor,
        updateBlock = RecordItem::updateItem,
        createBlock = ::createItem
    )
        .doReturn { e -> return failure(e) }

    val amendments = updateStrategy(
        receivedElements = received.amendments,
        keyExtractorForReceivedElement = requestAmendmentKeyExtractor,
        availableElements = this.amendments,
        keyExtractorForAvailableElement = recordAmendmentKeyExtractor,
        updateBlock = RecordAmendment::updateAmendment,
        createBlock = ::createAmendment
    )
        .doReturn { e -> return failure(e) }

    val amendment = received.amendment
        ?.let {
            this.amendment
                ?.updateAmendment(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAmendment(it)
        }
        ?: this.amendment

    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents,
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val requirementResponses = updateStrategy(
        receivedElements = received.requirementResponses,
        keyExtractorForReceivedElement = requestRequirementResponseKeyExtractor,
        availableElements = this.requirementResponses,
        keyExtractorForAvailableElement = recordRequirementResponseKeyExtractor,
        updateBlock = RecordRequirementResponse::updateRequirementResponse,
        createBlock = ::createRequirementResponse
    )
        .doReturn { e -> return failure(e) }

    val suppliers = updateStrategy(
        receivedElements = received.suppliers,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.suppliers,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateSupplier,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }


    return this
        .copy(
            id = received.id,
            status = received.status ?: this.status,
            statusDetails = received.statusDetails ?: this.statusDetails,
            title = received.title ?: this.title,
            description = received.description ?: this.description,
            value = received.value ?: this.value,
            relatedBid = received.relatedBid ?: this.relatedBid,
            relatedLots = this.relatedLots.update(received.relatedLots),
            date = received.date ?: this.date,
            contractPeriod = contractPeriod,
            weightedValue = received.weightedValue ?: this.weightedValue,
            reviewProceedings = reviewProceedings,
            items = items,
            amendments = amendments,
            amendment = amendment,
            documents = documents,
            requirementResponses = requirementResponses,
            suppliers = suppliers
        )
        .asSuccess()
}

val recordRequirementResponseKeyExtractor: (RecordRequirementResponse) -> String = { it.id }
val requestRequirementResponseKeyExtractor: (RequestRequirementResponse) -> String = { it.id }

fun RecordAmendment.updateAmendment(received: RequestAmendment): UpdateRecordResult<RecordAmendment> {
    val amendmentId = if (received.id == this.id)
        received.id
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'amendment'. Ids mismatching: " +
                    "Amendment from request (id = '${received.id}'), " +
                    "Amendment from release (id = '${received.id}'). "
            )
        )

    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents,
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val changes = this.changes.updateChanges(received.changes)
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = amendmentId,
            date = received.date ?: this.date,
            description = received.description ?: this.description,
            rationale = received.rationale ?: this.rationale,
            documents = documents,
            relatedLots = this.relatedLots.update(received.relatedLots),
            releaseID = received.releaseID ?: this.releaseID,
            changes = changes,
            amendsReleaseID = received.amendsReleaseID ?: this.amendsReleaseID,
            type = received.type ?: this.type,
            status = received.status ?: this.status,
            relatedItem = received.relatedItem ?: this.relatedItem,
            relatesTo = received.relatesTo ?: this.relatesTo
        )
        .asSuccess()
}

fun RecordReviewProceedings.updateReviewProceedings(received: RequestReviewProceedings): UpdateRecordResult<RecordReviewProceedings> {
    val legalProcedures = updateStrategy(
        receivedElements = received.legalProcedures,
        keyExtractorForReceivedElement = requestLegalProceedingsKeyExtractor,
        availableElements = this.legalProcedures.toList(),
        keyExtractorForAvailableElement = recordLegalProceedingsKeyExtractor,
        updateBlock = RecordLegalProceedings::updateLegalProceeding,
        createBlock = ::createLegalProceeding
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            buyerProcedureReview = received.buyerProcedureReview ?: this.buyerProcedureReview,
            legalProcedures = legalProcedures,
            reviewBodyChallenge = received.reviewBodyChallenge ?: this.reviewBodyChallenge
        )
        .asSuccess()
}

val recordLegalProceedingsKeyExtractor: (RecordLegalProceedings) -> String = { it.id }
val requestLegalProceedingsKeyExtractor: (RequestLegalProceedings) -> String = { it.id }

fun RecordLegalProceedings.updateLegalProceeding(received: RequestLegalProceedings): UpdateRecordResult<RecordLegalProceedings> =
    this.copy(
        id = received.id,
        title = received.title ?: this.title,
        uri = received.uri ?: this.uri
    )
        .asSuccess()

fun RecordRequirementResponse.updateRequirementResponse(received: RequestRequirementResponse): UpdateRecordResult<RecordRequirementResponse> {
    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    val relatedTenderer = received.relatedTenderer
        ?.let {
            this.relatedTenderer
                ?.updateOrganizationReference(it)
                ?.doReturn { e -> return failure(e) }
                ?: createOrganizationReference(it)
        }
        ?: this.relatedTenderer

    val requirement = received.requirement
        ?.let {
            this.requirement
                ?.updateRequirement(it)
                ?.doReturn { e -> return failure(e) }
                ?: createRequirement(it)
        }
        ?: this.requirement

    val responder = received.responder
        ?.let {
            this.responder
                ?.updateResponder(it)
                ?.doReturn { e -> return failure(e) }
                ?: createResponder(it)
        }
        ?: this.responder

    return this
        .copy(
            id = received.id,
            title = received.title ?: this.title,
            value = received.value,
            description = received.description ?: this.description,
            period = period,
            relatedTenderer = relatedTenderer,
            requirement = requirement,
            responder = responder
        )
        .asSuccess()
}

fun RecordCandidate.updateCandidate(received: RequestCandidate): UpdateRecordResult<RecordCandidate> =
    this.copy(
        id = received.id,
        name = received.name ?: this.name
    )
        .asSuccess()

fun RecordRequirementReference.updateRequirement(received: RequestRequirementReference): UpdateRecordResult<RecordRequirementReference> =
    this.copy(
        id = received.id,
        title = received.title ?: this.title
    )
        .asSuccess()

fun RecordResponder.updateResponder(received: RequestResponder): UpdateRecordResult<RecordResponder> =
    RecordResponder(
        name = received.name,
        id = received.id
    )
        .asSuccess()

fun RecordOrganizationReference.updateOrganizationReference(received: RequestOrganizationReference): UpdateRecordResult<RecordOrganizationReference> {
    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    val contactPoint = received.contactPoint
        ?.let {
            this.contactPoint
                ?.updateContactPoint(it)
                ?.doReturn { e -> return failure(e) }
                ?: createContactPoint(it)
        }
        ?: this.contactPoint

    val additionalIdentifiers = updateStrategy(
        receivedElements = received.additionalIdentifiers,
        keyExtractorForReceivedElement = requestIdentifierKeyExtractor,
        availableElements = this.additionalIdentifiers.toList(),
        keyExtractorForAvailableElement = recordIdentifierKeyExtractor,
        updateBlock = RecordIdentifier::updateIdentifierElement,
        createBlock = ::createIdentifier
    )
        .doReturn { e -> return failure(e) }

    val persones = updateStrategy(
        receivedElements = received.persones,
        keyExtractorForReceivedElement = requestPersonKeyExtractor,
        availableElements = this.persones.toList(),
        keyExtractorForAvailableElement = recordPersonKeyExtractor,
        updateBlock = RecordPerson::updatePerson,
        createBlock = ::createPerson
    )
        .doReturn { e -> return failure(e) }

    val identifier = received.identifier
        ?.let {
            this.identifier
                ?.updateIdentifier(it)
                ?.doReturn { e -> return failure(e) }
                ?: createIdentifier(it)
        }
        ?: this.identifier

    val details = received.details
        ?.let {
            this.details
                ?.updateDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDetails(it)
        }
        ?: this.details


    return this
        .copy(
            id = this.id,
            address = address,
            contactPoint = contactPoint,
            additionalIdentifiers = additionalIdentifiers,
            persones = persones,
            identifier = identifier,
            name = received.name ?: this.name,
            details = details,
            buyerProfile = received.buyerProfile ?: this.buyerProfile
        )
        .asSuccess()
}

fun RecordOrganizationReference.updateSupplier(received: RequestOrganizationReference): UpdateRecordResult<RecordOrganizationReference> {
    val address = received.address
        ?.let {
            this.address?.updateAddress(it) ?: createAddress(it)
        }
        ?: this.address

    val contactPoint = received.contactPoint
        ?.let {
            this.contactPoint
                ?.updateContactPoint(it)
                ?.doReturn { e -> return failure(e) }
                ?: createContactPoint(it)
        }
        ?: this.contactPoint

    val additionalIdentifiers = updateStrategy(
        receivedElements = received.additionalIdentifiers,
        keyExtractorForReceivedElement = requestIdentifierKeyExtractor,
        availableElements = this.additionalIdentifiers.toList(),
        keyExtractorForAvailableElement = recordIdentifierKeyExtractor,
        updateBlock = RecordIdentifier::updateIdentifierElement,
        createBlock = ::createIdentifier
    )
        .doReturn { e -> return failure(e) }

    val persones = updateStrategy(
        receivedElements = received.persones,
        keyExtractorForReceivedElement = requestPersonKeyExtractor,
        availableElements = this.persones.toList(),
        keyExtractorForAvailableElement = recordPersonKeyExtractor,
        updateBlock = RecordPerson::updatePerson,
        createBlock = ::createPerson
    )
        .doReturn { e -> return failure(e) }

    val identifier = received.identifier
        ?.let {
            this.identifier
                ?.updateIdentifier(it)
                ?.doReturn { e -> return failure(e) }
                ?: createIdentifier(it)
        }
        ?: this.identifier

    val details = received.details
        ?.let {
            this.details
                ?.updateDetails(it)
                ?.doReturn { e -> return failure(e) }
                ?: createDetails(it)
        }
        ?: this.details

    return this
        .copy(
            id = this.id,
            address = address,
            contactPoint = contactPoint,
            additionalIdentifiers = additionalIdentifiers,
            persones = persones,
            identifier = identifier,
            name = received.name ?: this.name,
            details = details,
            buyerProfile = received.buyerProfile ?: this.buyerProfile
        )
        .asSuccess()
}

fun RecordBid.updateBid(received: RequestBid): UpdateRecordResult<RecordBid> {
    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents.toList(),
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val requirementResponses = updateStrategy(
        receivedElements = received.requirementResponses,
        keyExtractorForReceivedElement = requestRequirementResponseKeyExtractor,
        availableElements = this.requirementResponses.toList(),
        keyExtractorForAvailableElement = recordRequirementResponseKeyExtractor,
        updateBlock = RecordRequirementResponse::updateRequirementResponse,
        createBlock = ::createRequirementResponse
    )
        .doReturn { e -> return failure(e) }

    val tenderers = updateStrategy(
        receivedElements = received.tenderers,
        keyExtractorForReceivedElement = requestOrganizationReferenceKeyExtractor,
        availableElements = this.tenderers,
        keyExtractorForAvailableElement = recordOrganizationReferenceKeyExtractor,
        updateBlock = RecordOrganizationReference::updateOrganizationReference,
        createBlock = ::createOrganizationReference
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            value = received.value ?: this.value,
            relatedLots = this.relatedLots.update(received.relatedLots),
            documents = documents,
            date = received.date ?: this.date,
            requirementResponses = requirementResponses,
            statusDetails = received.statusDetails ?: this.statusDetails,
            status = received.status ?: this.status,
            tenderers = tenderers
        )
        .asSuccess()
}

fun RecordContract.updateContract(received: RequestContract): UpdateRecordResult<RecordContract> {
    val requirementResponses = updateStrategy(
        receivedElements = received.requirementResponses,
        keyExtractorForReceivedElement = requestRequirementResponseKeyExtractor,
        availableElements = this.requirementResponses.toList(),
        keyExtractorForAvailableElement = recordRequirementResponseKeyExtractor,
        updateBlock = RecordRequirementResponse::updateRequirementResponse,
        createBlock = ::createRequirementResponse
    )
        .doReturn { e -> return failure(e) }

    val documents = updateStrategy(
        receivedElements = received.documents,
        keyExtractorForReceivedElement = requestDocumentKeyExtractor,
        availableElements = this.documents.toList(),
        keyExtractorForAvailableElement = recordDocumentKeyExtractor,
        updateBlock = RecordDocument::updateDocument,
        createBlock = ::createDocument
    )
        .doReturn { e -> return failure(e) }

    val value = received.value
        ?.let {
            this.value
                ?.updateValueTax(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValueTax(it)
        }
        ?: this.value

    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    val amendment = received.amendment
        ?.let {
            this.amendment
                ?.updateAmendment(it)
                ?.doReturn { e -> return failure(e) }
                ?: createAmendment(it)
        }
        ?: this.amendment

    val amendments = updateStrategy(
        receivedElements = received.amendments,
        keyExtractorForReceivedElement = requestAmendmentKeyExtractor,
        availableElements = this.amendments,
        keyExtractorForAvailableElement = recordAmendmentKeyExtractor,
        updateBlock = RecordAmendment::updateAmendment,
        createBlock = ::createAmendment
    )
        .doReturn { e -> return failure(e) }

    val items = updateStrategy(
        receivedElements = received.items,
        keyExtractorForReceivedElement = requestItemKeyExtractor,
        availableElements = this.items.toList(),
        keyExtractorForAvailableElement = recordItemKeyExtractor,
        updateBlock = RecordItem::updateItem,
        createBlock = ::createItem
    )
        .doReturn { e -> return failure(e) }

    val classification = received.classification
        ?.let {
            this.classification
                ?.updateClassification(it)
                ?.doReturn { e -> return failure(e) }
                ?: createClassification(it)
        }
        ?: this.classification

    val agreedMetrics = updateStrategy(
        receivedElements = received.agreedMetrics,
        keyExtractorForReceivedElement = requestAgreedMetricKeyExtractor,
        availableElements = this.agreedMetrics.toList(),
        keyExtractorForAvailableElement = recordAgreedMetricKeyExtractor,
        updateBlock = RecordAgreedMetric::updateAgreedMetric,
        createBlock = ::createAgreedMetric
    )
        .doReturn { e -> return failure(e) }

    val budgetSource = updateStrategy(
        receivedElements = received.budgetSource,
        keyExtractorForReceivedElement = requestBudgetSourceKeyExtractor,
        availableElements = this.budgetSource,
        keyExtractorForAvailableElement = recordBudgetSourceKeyExtractor,
        updateBlock = RecordBudgetSource::updateBudgetSource,
        createBlock = ::createBudgetSource
    )
        .doReturn { e -> return failure(e) }

    val confirmationRequests = updateStrategy(
        receivedElements = received.confirmationRequests,
        keyExtractorForReceivedElement = requestConfirmationRequestKeyExtractor,
        availableElements = this.confirmationRequests,
        keyExtractorForAvailableElement = recordConfirmationRequestKeyExtractor,
        updateBlock = RecordConfirmationRequest::updateConfirmationRequest,
        createBlock = ::createConfirmationRequest
    )
        .doReturn { e -> return failure(e) }

    val confirmationResponses = updateStrategy(
        receivedElements = received.confirmationResponses,
        keyExtractorForReceivedElement = requestConfirmationResponseKeyExtractor,
        availableElements = this.confirmationResponses.toList(),
        keyExtractorForAvailableElement = recordConfirmationResponseKeyExtractor,
        updateBlock = RecordConfirmationResponse::updateConfirmationResponse,
        createBlock = ::createConfirmationResponse
    )
        .doReturn { e -> return failure(e) }

    val milestones = updateStrategy(
        receivedElements = received.milestones,
        keyExtractorForReceivedElement = requestMilestoneKeyExtractor,
        availableElements = this.milestones,
        keyExtractorForAvailableElement = recordMilestoneKeyExtractor,
        updateBlock = RecordMilestone::updateMilestone,
        createBlock = ::createMilestone
    )
        .doReturn { e -> return failure(e) }

    val relatedProcesses = updateStrategy(
        receivedElements = received.relatedProcesses,
        keyExtractorForReceivedElement = requestRelatedProcessKeyExtractor,
        availableElements = this.relatedProcesses.toList(),
        keyExtractorForAvailableElement = recordRelatedProcessKeyExtractor,
        updateBlock = RecordRelatedProcess::updateRelatedProcess,
        createBlock = ::createRelatedProcess
    )
        .doReturn { e -> return failure(e) }

    val valueBreakdown = updateStrategy(
        receivedElements = received.valueBreakdown,
        keyExtractorForReceivedElement = requestValueBreakdownKeyExtractor,
        availableElements = this.valueBreakdown.toList(),
        keyExtractorForAvailableElement = recordValueBreakdownKeyExtractor,
        updateBlock = RecordValueBreakdown::updateValueBreakdown,
        createBlock = ::createValueBreakdown
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            status = received.status,
            statusDetails = received.statusDetails,
            requirementResponses = requirementResponses,
            date = received.date ?: this.date,
            documents = documents,
            relatedLots = this.relatedLots.update(received.relatedLots),
            value = value,
            title = received.title ?: this.title,
            period = period,
            description = received.description ?: this.description,
            amendment = amendment,
            amendments = amendments,
            items = items,
            classification = classification,
            agreedMetrics = agreedMetrics,
            awardId = received.awardId ?: this.awardId,
            budgetSource = budgetSource,
            confirmationRequests = confirmationRequests,
            confirmationResponses = confirmationResponses,
            countryOfOrigin = received.countryOfOrigin ?: this.countryOfOrigin,
            dateSigned = received.dateSigned ?: this.dateSigned,
            extendsContractId = received.extendsContractId ?: this.extendsContractId,
            isFrameworkOrDynamic = received.isFrameworkOrDynamic ?: this.isFrameworkOrDynamic,
            lotVariant = this.lotVariant.update(lotVariant),
            milestones = milestones,
            relatedProcesses = relatedProcesses,
            valueBreakdown = valueBreakdown
        )
        .asSuccess()
}

val recordAgreedMetricKeyExtractor: (RecordAgreedMetric) -> String = { it.id }
val requestAgreedMetricKeyExtractor: (RequestAgreedMetric) -> String = { it.id }

val recordBudgetSourceKeyExtractor: (RecordBudgetSource) -> String = { it.id }
val requestBudgetSourceKeyExtractor: (RequestBudgetSource) -> String = { it.id }

val recordConfirmationRequestKeyExtractor: (RecordConfirmationRequest) -> String = { it.id }
val requestConfirmationRequestKeyExtractor: (RequestConfirmationRequest) -> String = { it.id }

val recordConfirmationResponseKeyExtractor: (RecordConfirmationResponse) -> String = { it.id }
val requestConfirmationResponseKeyExtractor: (RequestConfirmationResponse) -> String = { it.id }

val recordRelatedProcessKeyExtractor: (RecordRelatedProcess) -> String = { it.id }
val requestRelatedProcessKeyExtractor: (RequestRelatedProcess) -> String = { it.id }

val recordValueBreakdownKeyExtractor: (RecordValueBreakdown) -> String = { it.id }
val requestValueBreakdownKeyExtractor: (RequestValueBreakdown) -> String = { it.id }

fun RecordValueBreakdown.updateValueBreakdown(received: RequestValueBreakdown): UpdateRecordResult<RecordValueBreakdown> =
    this.copy(
        id = received.id,
        type = this.type.update(received.type),
        description = received.description ?: this.description,
        amount = received.amount ?: this.amount,
        estimationMethod = received.estimationMethod ?: this.estimationMethod
    )
        .asSuccess()

fun RecordRelatedProcess.updateRelatedProcess(received: RequestRelatedProcess): UpdateRecordResult<RecordRelatedProcess> =
    this.copy(
        id = received.id,
        scheme = received.scheme,
        identifier = received.identifier ?: this.identifier,
        uri = received.uri ?: this.uri,
        relationship = received.relationship
            .mapIfNotEmpty { it }
            ?: this.relationship
    )
        .asSuccess()

fun RecordMilestone.updateMilestone(received: RequestMilestone): UpdateRecordResult<RecordMilestone> {
    val relatedParties = updateStrategy(
        receivedElements = received.relatedParties,
        keyExtractorForReceivedElement = requestRelatedPartyKeyExtractor,
        availableElements = this.relatedParties,
        keyExtractorForAvailableElement = recordRelatedPartyKeyExtractor,
        updateBlock = RecordRelatedParty::updateRelatedParty,
        createBlock = ::createRelatedParty
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            title = received.title ?: this.title,
            description = received.description ?: this.description,
            type = received.type ?: this.type,
            status = received.status ?: this.status,
            dateMet = received.dateMet ?: this.dateMet,
            dateModified = received.dateModified ?: this.dateModified,
            additionalInformation = received.additionalInformation ?: this.additionalInformation,
            dueDate = received.dueDate ?: this.dueDate,
            relatedItems = this.relatedItems.update(received.relatedItems),
            relatedParties = relatedParties
        )
        .asSuccess()
}

val recordRelatedPartyKeyExtractor: (RecordRelatedParty) -> String = { it.id }
val requestRelatedPartyKeyExtractor: (RequestRelatedParty) -> String = { it.id }

fun RecordRelatedParty.updateRelatedParty(received: RequestRelatedParty): UpdateRecordResult<RecordRelatedParty> =
    this.copy(
        id = received.id,
        name = received.name ?: this.name
    )
        .asSuccess()

fun RecordConfirmationResponse.updateConfirmationResponse(received: RequestConfirmationResponse): UpdateRecordResult<RecordConfirmationResponse> {
    val value = received.value
        ?.let {
            this.value
                ?.updateConfirmationResponseValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createConfirmationResponseValue(it)
        }
        ?: this.value

    return this
        .copy(
            id = received.id,
            value = value,
            request = received.request ?: this.request
        )
        .asSuccess()
}

fun RecordConfirmationResponseValue.updateConfirmationResponseValue(
    received: RequestConfirmationResponseValue
): UpdateRecordResult<RecordConfirmationResponseValue> {
    val relatedPerson = received.relatedPerson
        ?.let {
            this.relatedPerson
                ?.updateRelatedPerson(it)
                ?.doReturn { e -> return failure(e) }
                ?: createRelatedPerson(it)
        }
        ?: this.relatedPerson

    val verification = this.verification.updateVerification(received.verification)
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = this.id,
            name = received.name ?: this.name,
            date = received.date ?: this.date,
            relatedPerson = relatedPerson,
            verification = verification
        )
        .asSuccess()
}

fun List<RecordVerification>.updateVerification(received: List<RequestVerification>): UpdateRecordResult<List<RecordVerification>> {
    val result = received.mapIfNotEmpty { rqVerification ->
        RecordVerification(
            type = rqVerification.type,
            value = rqVerification.value,
            rationale = rqVerification.rationale
        )
    } ?: this
    return result.asSuccess()
}

fun RecordRelatedPerson.updateRelatedPerson(received: RequestRelatedPerson): UpdateRecordResult<RecordRelatedPerson> {
    val personId = if (received.id == this.id)
        received.id
    else
        return failure(
            Fail.Error.BadRequest(
                description = "Cannot update 'relatedPerson'. Ids mismatching: " +
                    "RelatedPerson from request (id = '${received.id}'), " +
                    "RelatedPerson from release (id = '${this.id}'). "
            )
        )
    return this
        .copy(
            id = personId,
            name = received.name
        )
        .asSuccess()
}

fun RecordConfirmationRequest.updateConfirmationRequest(received: RequestConfirmationRequest): UpdateRecordResult<RecordConfirmationRequest> {
    val requestGroups = updateStrategy(
        receivedElements = received.requestGroups,
        keyExtractorForReceivedElement = requestRequestGroupKeyExtractor,
        availableElements = this.requestGroups.toList(),
        keyExtractorForAvailableElement = recordRequestGroupKeyExtractor,
        updateBlock = RecordRequestGroup::updateRequestGroup,
        createBlock = ::createRequestGroup
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            title = received.title ?: this.title,
            description = received.description ?: this.description,
            relatesTo = received.relatesTo ?: this.relatesTo,
            relatedItem = received.relatedItem,
            type = received.type ?: this.type,
            source = received.source,
            requestGroups = requestGroups
        )
        .asSuccess()
}

val recordRequestGroupKeyExtractor: (RecordRequestGroup) -> String = { it.id }
val requestRequestGroupKeyExtractor: (RequestRequestGroup) -> String = { it.id }

fun RecordRequestGroup.updateRequestGroup(received: RequestRequestGroup): UpdateRecordResult<RecordRequestGroup> {
    val requests = updateStrategy(
        receivedElements = received.requests,
        keyExtractorForReceivedElement = requestRequestKeyExtractor,
        availableElements = this.requests.toList(),
        keyExtractorForAvailableElement = recordRequestKeyExtractor,
        updateBlock = RecordRequest::updateRequest,
        createBlock = ::createRequest
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            requests = requests
        )
        .asSuccess()
}

val recordRequestKeyExtractor: (RecordRequest) -> String = { it.id }
val requestRequestKeyExtractor: (RequestRequest) -> String = { it.id }

fun RecordRequest.updateRequest(received: RequestRequest): UpdateRecordResult<RecordRequest> {
    val relatedPerson = received.relatedPerson
        ?.let {
            this.relatedPerson
                ?.updateRelatedPerson(it)
                ?.doReturn { e -> return failure(e) }
                ?: createRelatedPerson(it)
        }
        ?: this.relatedPerson

    return this
        .copy(
            id = received.id,
            relatedPerson = relatedPerson,
            description = received.description,
            title = received.title
        )
        .asSuccess()
}

fun RecordBudgetSource.updateBudgetSource(received: RequestBudgetSource): UpdateRecordResult<RecordBudgetSource> =
    this.copy(
        id = received.id,
        currency = received.currency ?: this.currency,
        amount = received.amount ?: this.amount
    )
        .asSuccess()

fun RecordAgreedMetric.updateAgreedMetric(received: RequestAgreedMetric): UpdateRecordResult<RecordAgreedMetric> {
    val observations = updateStrategy(
        receivedElements = received.observations,
        keyExtractorForReceivedElement = requestObservationKeyExtractor,
        availableElements = this.observations.toList(),
        keyExtractorForAvailableElement = recordObservationKeyExtractor,
        updateBlock = RecordObservation::updateObservation,
        createBlock = ::createObservation
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            id = received.id,
            description = received.description ?: this.description,
            title = received.title ?: this.title,
            observations = observations
        )
        .asSuccess()
}

val recordObservationKeyExtractor: (RecordObservation) -> String = { it.id }
val requestObservationKeyExtractor: (RequestObservation) -> String = { it.id }

fun RecordObservationUnit.updateObservationUnit(received: RequestObservationUnit): UpdateRecordResult<RecordObservationUnit> =
    this.copy(
        id = this.id,
        name = received.name ?: this.name,
        scheme = received.scheme
    )
        .asSuccess()

fun RecordObservation.updateObservation(received: RequestObservation): UpdateRecordResult<RecordObservation> {
    val unit = received.unit
        ?.let {
            this.unit
                ?.updateObservationUnit(it)
                ?.doReturn { e -> return failure(e) }
                ?: createObservationUnit(it)
        }
        ?: this.unit

    return this
        .copy(
            id = received.id,
            unit = unit,
            measure = received.measure ?: this.measure,
            notes = received.notes ?: this.notes
        )
        .asSuccess()
}

fun RecordValueTax.updateValueTax(received: RequestValueTax): UpdateRecordResult<RecordValueTax> =
    this.copy(
        amount = received.amount ?: this.amount,
        valueAddedTaxIncluded = received.valueAddedTaxIncluded ?: this.valueAddedTaxIncluded,
        amountNet = received.amountNet ?: this.amountNet,
        currency = received.currency ?: this.currency
    )
        .asSuccess()

fun Record.updateRelease(releaseId: String, params: UpdateRecordParams): UpdateRecordResult<Record> {
    val receivedRelease = params.data
    val relatedProcesses = updateStrategy(
        receivedElements = receivedRelease.relatedProcesses.toList(),
        keyExtractorForReceivedElement = requestRelatedProcessKeyExtractor,
        availableElements = this.relatedProcesses.toList(),
        keyExtractorForAvailableElement = recordRelatedProcessKeyExtractor,
        updateBlock = RecordRelatedProcess::updateRelatedProcess,
        createBlock = ::createRelatedProcess
    )
        .doReturn { e -> return failure(e) }
        .toMutableList()

    val bids = receivedRelease.bids
        ?.let {
            this.bids
                ?.updateBidsObject(it)
                ?.doReturn { e -> return failure(e) }
                ?: createBidsObject(it)
        }
        ?: this.bids

    val awards = updateStrategy(
        receivedElements = receivedRelease.awards,
        keyExtractorForReceivedElement = requestAwardKeyExtractor,
        availableElements = this.awards,
        keyExtractorForAvailableElement = recordAwardKeyExtractor,
        updateBlock = RecordAward::updateAward,
        createBlock = ::createAward
    )
        .doReturn { e -> return failure(e) }

    val contracts = updateStrategy(
        receivedElements = receivedRelease.contracts,
        keyExtractorForReceivedElement = requestContractKeyExtractor,
        availableElements = this.contracts,
        keyExtractorForAvailableElement = recordContractKeyExtractor,
        updateBlock = RecordContract::updateContract,
        createBlock = ::createContract
    )
        .doReturn { e -> return failure(e) }

    val parties = updateStrategy(
        receivedElements = receivedRelease.parties.toList(),
        keyExtractorForReceivedElement = requestOrganizationKeyExtractor,
        availableElements = this.parties.toList(),
        keyExtractorForAvailableElement = recordOrganizationKeyExtractor,
        updateBlock = RecordOrganization::updateOrganization,
        createBlock = ::createOrganization
    )
        .doReturn { e -> return failure(e) }
        .toMutableList()

    val purposeOfNotice = receivedRelease.purposeOfNotice
        ?.let {
            this.purposeOfNotice
                ?.updatePurposeOfNotice(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPurposeOfNotice(it)
        }
        ?: this.purposeOfNotice

    val tender = receivedRelease.tender
        ?.let {
            this.tender
                .updateReleaseTender(it)
                .doReturn { e -> return failure(e) }
        }
        ?: this.tender

    val agreedMetrics = updateStrategy(
        receivedElements = receivedRelease.agreedMetrics,
        keyExtractorForReceivedElement = requestAgreedMetricKeyExtractor,
        availableElements = this.agreedMetrics,
        keyExtractorForAvailableElement = recordAgreedMetricKeyExtractor,
        updateBlock = RecordAgreedMetric::updateAgreedMetric,
        createBlock = ::createAgreedMetric
    )
        .doReturn { e -> return failure(e) }

    val planning = receivedRelease.planning
        ?.let {
            this.planning
                ?.updatePlanning(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPlanning(it)
        }
        ?: this.planning

    val details = updateStrategy(
        receivedElements = receivedRelease.submissions?.details.orEmpty(),
        keyExtractorForReceivedElement = requestSubmissionDetailsKeyExtractor,
        availableElements = this.submissions?.details.orEmpty(),
        keyExtractorForAvailableElement = recordSubmissionDetailsKeyExtractor,
        updateBlock = RecordSubmissionDetail::updateSubmissionDetail,
        createBlock = ::createSubmissionDetail
    )
        .doReturn { e -> return failure(e) }

    val submissions = this.submissions?.copy(details = details)
        ?: if (details.isNotEmpty()) RecordSubmission(details = details) else null

    val qualifications = updateStrategy(
        receivedElements = receivedRelease.qualifications,
        keyExtractorForReceivedElement = requestQualificationKeyExtractor,
        availableElements = this.qualifications,
        keyExtractorForAvailableElement = recordQualificationKeyExtractor,
        updateBlock = RecordQualification::updateQualification,
        createBlock = ::createQualification
    )
        .doReturn { e -> return failure(e) }

    val preQualification = receivedRelease.preQualification
        ?.let {
            this.preQualification
                ?.updatePreQualification(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPreQualification(it)
        }
        ?: this.preQualification

    return this
        .copy(
            id = releaseId,
            ocid = this.ocid,
            date = params.date,
            relatedProcesses = relatedProcesses,
            bids = bids,
            awards = awards,
            contracts = contracts,
            hasPreviousNotice = receivedRelease.hasPreviousNotice ?: this.hasPreviousNotice,
            initiationType = receivedRelease.initiationType ?: this.initiationType,
            parties = parties,
            purposeOfNotice = purposeOfNotice,
            tag = this.tag.updateTags(receivedRelease.tag),
            tender = tender,
            agreedMetrics = agreedMetrics,
            cpid = this.cpid,
            planning = planning,
            submissions = submissions,
            qualifications = qualifications,
            preQualification = preQualification
        )
        .asSuccess()
}

val recordQualificationKeyExtractor: (RecordQualification) -> String = { it.id }
val requestQualificationKeyExtractor: (RequestQualification) -> String = { it.id }

val recordOrganizationKeyExtractor: (RecordOrganization) -> String = { it.id }
val requestOrganizationKeyExtractor: (RequestOrganization) -> String = { it.id }

val recordSubmissionDetailsKeyExtractor: (RecordSubmissionDetail) -> String = { it.id }
val requestSubmissionDetailsKeyExtractor: (RequestSubmissionDetail) -> String = { it.id }

val recordAwardKeyExtractor: (RecordAward) -> String = { it.id }
val requestAwardKeyExtractor: (RequestAward) -> String = { it.id }

val recordContractKeyExtractor: (RecordContract) -> String = { it.id }
val requestContractKeyExtractor: (RequestContract) -> String = { it.id }

fun List<Tag>.updateTags(received: List<Tag>): List<Tag> = received.mapIfNotEmpty { it } ?: this

fun RecordPlanning.updatePlanning(received: RequestPlanning): UpdateRecordResult<RecordPlanning> {
    val implementation = received.implementation
        ?.let {
            this.implementation
                ?.updateImplementation(it)
                ?.doReturn { e -> return failure(e) }
                ?: createImplementation(it)
        }
        ?: this.implementation

    val budget = received.budget
        ?.let {
            this.budget
                ?.updateBudget(it)
                ?.doReturn { e -> return failure(e) }
                ?: createBudget(it)
        }
        ?: this.budget

    return this
        .copy(
            implementation = implementation,
            rationale = received.rationale ?: this.rationale,
            budget = budget
        )
        .asSuccess()
}

fun RecordImplementation.updateImplementation(received: RequestImplementation): UpdateRecordResult<RecordImplementation> =
    this.copy(
        transactions = updateStrategy(
            receivedElements = received.transactions,
            keyExtractorForReceivedElement = requestTransactionKeyExtractor,
            availableElements = this.transactions,
            keyExtractorForAvailableElement = recordTransactionKeyExtractor,
            updateBlock = RecordTransaction::updateTransaction,
            createBlock = ::createTransaction
        )
            .doReturn { e -> return failure(e) }
    )
        .asSuccess()

val recordTransactionKeyExtractor: (RecordTransaction) -> String = { it.id }
val requestTransactionKeyExtractor: (RequestTransaction) -> String = { it.id }

fun RecordTransaction.updateTransaction(received: RequestTransaction): UpdateRecordResult<RecordTransaction> {
    val value = received.value
        ?.let {
            this.value
                ?.updateValue(it)
                ?.doReturn { e -> return failure(e) }
                ?: createValue(it)
        }
        ?: this.value

    val executionPeriod = received.executionPeriod
        ?.let {
            this.executionPeriod
                ?.updateExecutionPeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createExecutionPeriod(it)
        }
        ?: this.executionPeriod

    return this
        .copy(
            id = received.id,
            type = received.type ?: this.type,
            value = value,
            executionPeriod = executionPeriod,
            relatedContractMilestone = received.relatedContractMilestone ?: this.relatedContractMilestone
        )
        .asSuccess()
}

fun RecordExecutionPeriod.updateExecutionPeriod(received: RequestExecutionPeriod): UpdateRecordResult<RecordExecutionPeriod> =
    this.copy(
        durationInDays = received.durationInDays ?: this.durationInDays
    )
        .asSuccess()

fun RecordPlanningBudget.updateBudget(received: RequestPlanningBudget): UpdateRecordResult<RecordPlanningBudget> {
    val budgetSource = updateStrategy(
        receivedElements = received.budgetSource,
        keyExtractorForReceivedElement = requestPlanningBudgetSourceKeyExtractor,
        availableElements = this.budgetSource,
        keyExtractorForAvailableElement = recordPlanningBudgetSourceKeyExtractor,
        updateBlock = RecordPlanningBudgetSource::updatePlanningBudgetSource,
        createBlock = ::createPlanningBudgetSource
    )
        .doReturn { e -> return failure(e) }

    val budgetAllocation = updateStrategy(
        receivedElements = received.budgetAllocation,
        keyExtractorForReceivedElement = requestBudgetAllocationKeyExtractor,
        availableElements = this.budgetAllocation,
        keyExtractorForAvailableElement = recordBudgetAllocationKeyExtractor,
        updateBlock = RecordBudgetAllocation::updateBudgetAllocation,
        createBlock = ::createBudgetAllocation
    )
        .doReturn { e -> return failure(e) }

    val budgetBreakdown = updateStrategy(
        receivedElements = received.budgetBreakdown,
        keyExtractorForReceivedElement = requestBudgetBreakdownKeyExtractor,
        availableElements = this.budgetBreakdown,
        keyExtractorForAvailableElement = recordBudgetBreakdownKeyExtractor,
        updateBlock = RecordBudgetBreakdown::updateBudgetBreakdown,
        createBlock = ::createBudgetBreakdown
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            description = received.description ?: this.description,
            amount = received.amount ?: this.amount,
            isEuropeanUnionFunded = received.isEuropeanUnionFunded ?: this.isEuropeanUnionFunded,
            budgetSource = budgetSource,
            budgetAllocation = budgetAllocation,
            budgetBreakdown = budgetBreakdown
        )
        .asSuccess()
}

val recordPlanningBudgetSourceKeyExtractor: (RecordPlanningBudgetSource) -> String = { it.budgetBreakdownID }
val requestPlanningBudgetSourceKeyExtractor: (RequestPlanningBudgetSource) -> String = { it.budgetBreakdownID }

val recordBudgetAllocationKeyExtractor: (RecordBudgetAllocation) -> String = { it.budgetBreakdownID }
val requestBudgetAllocationKeyExtractor: (RequestBudgetAllocation) -> String = { it.budgetBreakdownID }

val recordBudgetBreakdownKeyExtractor: (RecordBudgetBreakdown) -> String = { it.id }
val requestBudgetBreakdownKeyExtractor: (RequestBudgetBreakdown) -> String = { it.id }

fun RecordBudgetBreakdown.updateBudgetBreakdown(received: RequestBudgetBreakdown): UpdateRecordResult<RecordBudgetBreakdown> {
    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    val europeanUnionFunding = received.europeanUnionFunding
        ?.let {
            this.europeanUnionFunding
                ?.updateEuropeanUnionFunding(it)
                ?.doReturn { e -> return failure(e) }
                ?: createEuropeanUnionFunding(it)
        }
        ?: this.europeanUnionFunding

    val sourceParty = received.sourceParty
        ?.let {
            this.sourceParty
                ?.updateOrganizationReference(it)
                ?.doReturn { e -> return failure(e) }
                ?: createOrganizationReference(it)
        }
        ?: this.sourceParty

    return this
        .copy(
            id = received.id,
            amount = received.amount ?: this.amount,
            period = period,
            description = received.description ?: this.description,
            europeanUnionFunding = europeanUnionFunding,
            sourceParty = sourceParty
        )
        .asSuccess()
}

fun RecordEuropeanUnionFunding.updateEuropeanUnionFunding(received: RequestEuropeanUnionFunding): UpdateRecordResult<RecordEuropeanUnionFunding> =
    this.copy(
        projectIdentifier = received.projectIdentifier ?: this.projectIdentifier,
        uri = received.uri ?: this.uri,
        projectName = received.projectName ?: this.projectName
    )
        .asSuccess()

fun RecordBudgetAllocation.updateBudgetAllocation(received: RequestBudgetAllocation): UpdateRecordResult<RecordBudgetAllocation> {
    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    return this
        .copy(
            budgetBreakdownID = received.budgetBreakdownID,
            amount = received.amount ?: this.amount,
            relatedItem = received.relatedItem ?: this.relatedItem,
            period = period
        )
        .asSuccess()
}

fun RecordPlanningBudgetSource.updatePlanningBudgetSource(received: RequestPlanningBudgetSource): UpdateRecordResult<RecordPlanningBudgetSource> =
    this.copy(
        budgetBreakdownID = received.budgetBreakdownID,
        amount = received.amount ?: this.amount,
        currency = received.currency ?: this.currency
    )
        .asSuccess()

fun RecordPurposeOfNotice.updatePurposeOfNotice(received: RequestPurposeOfNotice): UpdateRecordResult<RecordPurposeOfNotice> =
    this.copy(
        isACallForCompetition = received.isACallForCompetition ?: this.isACallForCompetition
    )
        .asSuccess()

fun RecordPreQualification.updatePreQualification(received: RequestPreQualification): UpdateRecordResult<RecordPreQualification> {
    val period = received.period
        ?.let {
            this.period
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.period

    val qualificationPeriod = received.qualificationPeriod
        ?.let {
            this.qualificationPeriod
                ?.updatePeriod(it)
                ?.doReturn { e -> return failure(e) }
                ?: createPeriod(it)
        }
        ?: this.qualificationPeriod

    return  RecordPreQualification(
        period = period,
        qualificationPeriod = qualificationPeriod
    )
        .asSuccess()
}


fun RecordBids.updateBidsObject(received: RequestBids): UpdateRecordResult<RecordBids> {
    val statistics = updateStrategy(
        receivedElements = received.statistics,
        keyExtractorForReceivedElement = requestBidsStatisticKeyExtractor,
        availableElements = this.statistics,
        keyExtractorForAvailableElement = recordBidsStatisticKeyExtractor,
        updateBlock = RecordBidsStatistic::updateBidsStatistic,
        createBlock = ::createBidsStatistic
    )
        .doReturn { e -> return failure(e) }

    val details = updateStrategy(
        receivedElements = received.details,
        keyExtractorForReceivedElement = requestBidKeyExtractor,
        availableElements = this.details,
        keyExtractorForAvailableElement = recordBidKeyExtractor,
        updateBlock = RecordBid::updateBid,
        createBlock = ::createBid
    )
        .doReturn { e -> return failure(e) }

    return this
        .copy(
            statistics = statistics,
            details = details
        )
        .asSuccess()
}

val recordBidsStatisticKeyExtractor: (RecordBidsStatistic) -> String = { it.id }
val requestBidsStatisticKeyExtractor: (RequestBidsStatistic) -> String = { it.id }

val recordBidKeyExtractor: (RecordBid) -> String = { it.id }
val requestBidKeyExtractor: (RequestBid) -> String = { it.id }

fun RecordBidsStatistic.updateBidsStatistic(received: RequestBidsStatistic): UpdateRecordResult<RecordBidsStatistic> =
    this.copy(
        id = received.id,
        date = received.date ?: this.date,
        value = received.value ?: this.value,
        notes = received.notes ?: this.notes,
        measure = received.measure ?: this.measure,
        relatedLot = received.relatedLot ?: this.relatedLot
    )
        .asSuccess()

fun <R, A, K> updateStrategy(
    receivedElements: List<R>,
    keyExtractorForReceivedElement: (R) -> K,
    availableElements: List<A>,
    keyExtractorForAvailableElement: (A) -> K,
    updateBlock: A.(R) -> UpdateRecordResult<A>,
    createBlock: (R) -> A
): UpdateRecordResult<List<A>> {
    val receivedElementsById = receivedElements.associateBy { keyExtractorForReceivedElement(it) }
    val availableElementsIds = availableElements.toSetBy { keyExtractorForAvailableElement(it) }

    val updatedElements: MutableList<A> = mutableListOf()

    availableElements.forEach { availableElement ->
        val id = keyExtractorForAvailableElement(availableElement)
        val element = receivedElementsById[id]
            ?.let { receivedElement ->
                availableElement.updateBlock(receivedElement)
                    .doReturn { e -> return failure(e) }
            }
            ?: availableElement
        updatedElements.add(element)
    }

    val newIds = receivedElementsById.keys - availableElementsIds
    newIds.forEach { id ->
        val receivedElement = receivedElementsById.getValue(id)
        val element = createBlock(receivedElement)
        updatedElements.add(element)
    }

    return updatedElements.asSuccess()
}
