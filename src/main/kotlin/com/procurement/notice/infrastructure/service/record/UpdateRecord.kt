package com.procurement.notice.infrastructure.service.record

import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
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
import com.procurement.notice.infrastructure.dto.entity.awards.RecordAward
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBid
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBidsStatistic
import com.procurement.notice.infrastructure.dto.entity.awards.RecordRequirementReference
import com.procurement.notice.infrastructure.dto.entity.awards.RecordRequirementResponse
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
import com.procurement.notice.infrastructure.dto.request.RequestPurposeOfNotice
import com.procurement.notice.infrastructure.dto.request.RequestRecurrentProcurement
import com.procurement.notice.infrastructure.dto.request.RequestRelatedParty
import com.procurement.notice.infrastructure.dto.request.RequestRelatedPerson
import com.procurement.notice.infrastructure.dto.request.RequestRelatedProcess
import com.procurement.notice.infrastructure.dto.request.RequestRelease
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

fun updateLot(received: RequestLot, available: RecordLot?): RecordLot =
    received.let { rqLot ->
        RecordLot(
            id = rqLot.id,
            status = rqLot.status ?: available?.status,
            statusDetails = rqLot.statusDetails ?: available?.statusDetails,
            value = rqLot.value ?: available?.value,
            title = rqLot.title ?: available?.title,
            description = rqLot.description ?: available?.description,
            recurrentProcurement = updateRecurrentProcurement(
                rqLot.recurrentProcurement,
                available?.recurrentProcurement.orEmpty()
            ),
            contractPeriod = updateContractPeriod(
                rqLot.contractPeriod,
                available?.contractPeriod
            ),
            internalId = rqLot.internalId ?: available?.internalId,
            options = updateOptions(
                rqLot.options,
                available?.options.orEmpty()
            ),
            placeOfPerformance = updatePlaceOfPerformance(
                rqLot.placeOfPerformance,
                available?.placeOfPerformance
            ),
            renewals = updateRenewals(
                rqLot.renewals,
                available?.renewals.orEmpty()
            ),
            variants = updateVariants(
                rqLot.variants,
                available?.variants.orEmpty()
            )
        )
    }

fun updateVariants(received: List<RequestVariant>, available: List<RecordVariant>): List<RecordVariant> =
    received.mapIfNotEmpty { requestVariant ->
        RecordVariant(
            hasVariants = requestVariant.hasVariants,
            variantDetails = requestVariant.variantDetails
        )
    } ?: available

fun updateRenewals(received: List<RequestRenewal>, available: List<RecordRenewal>): List<RecordRenewal> =
    received.mapIfNotEmpty { requestRenewal ->
        RecordRenewal(
            hasRenewals = requestRenewal.hasRenewals,
            renewalConditions = requestRenewal.renewalConditions,
            maxNumber = requestRenewal.maxNumber
        )
    } ?: available

fun updatePlaceOfPerformance(
    received: RequestPlaceOfPerformance?,
    available: RecordPlaceOfPerformance?
): RecordPlaceOfPerformance? =
    received?.let { placeOfPerformance ->
        RecordPlaceOfPerformance(
            address = updateAddress(
                placeOfPerformance.address,
                available?.address
            ),
            description = placeOfPerformance.description ?: available?.description,
            nutScode = placeOfPerformance.nutScode ?: available?.nutScode
        )
    } ?: available

fun updateAddress(received: RequestAddress?, available: RecordAddress?): RecordAddress? =
    received?.let { address ->
        RecordAddress(
            streetAddress = address.streetAddress ?: available?.streetAddress,
            addressDetails = address.addressDetails
                ?.let { addressDetails ->
                    RecordAddressDetails(
                        country = addressDetails.country
                            .let { country ->
                                RecordCountryDetails(
                                    id = country.id,
                                    scheme = country.scheme ?: available?.addressDetails?.country?.scheme,
                                    description = country.description ?: available?.addressDetails?.country?.description,
                                    uri = country.uri ?: available?.addressDetails?.country?.uri
                                )
                            },
                        region = addressDetails.region
                            .let { region ->
                                RecordRegionDetails(
                                    id = region.id,
                                    scheme = region.scheme ?: available?.addressDetails?.region?.scheme,
                                    description = region.description ?: available?.addressDetails?.region?.description,
                                    uri = region.uri ?: available?.addressDetails?.region?.uri
                                )
                            },
                        locality = addressDetails.locality
                            .let { locality ->
                                RecordLocalityDetails(
                                    id = locality.id,
                                    scheme = locality.scheme,
                                    description = locality.description,
                                    uri = locality.uri ?: available?.addressDetails?.locality?.uri
                                )
                            }
                    )
                } ?: available?.addressDetails,
            postalCode = address.postalCode ?: available?.postalCode
        )
    } ?: available

fun updateOptions(received: List<RequestOption>, available: List<RecordOption>): List<RecordOption> =
    received.mapIfNotEmpty { requestOption ->
        RecordOption(
            hasOptions = requestOption.hasOptions,
            optionDetails = requestOption.optionDetails
        )
    } ?: available

fun updateContractPeriod(received: RequestPeriod?, available: RecordPeriod?): RecordPeriod? =
    received?.let { rqPeriod ->
        RecordPeriod(
            startDate = rqPeriod.startDate ?: available?.startDate,
            endDate = rqPeriod.endDate ?: available?.endDate,
            durationInDays = rqPeriod.durationInDays ?: available?.durationInDays,
            maxExtentDate = rqPeriod.maxExtentDate ?: available?.maxExtentDate
        )
    } ?: available

fun updateRecurrentProcurement(
    received: List<RequestRecurrentProcurement>,
    available: List<RecordRecurrentProcurement>
): List<RecordRecurrentProcurement> =
    received.mapIfNotEmpty { recurrentProcurement ->
        RecordRecurrentProcurement(
            isRecurrent = recurrentProcurement.isRecurrent,
            description = recurrentProcurement.description,
            dates = recurrentProcurement.dates
                .map { date ->
                    updatePeriod(
                        date,
                        null
                    )!!
                }
        )
    } ?: available

fun updateItem(received: RequestItem, available: RecordItem?): RecordItem =
    received.let { rqItem ->
        RecordItem(
            id = rqItem.id,
            description = rqItem.description ?: available?.description,
            internalId = rqItem.internalId ?: available?.internalId,
            unit = updateUnit(
                rqItem.unit,
                available?.unit
            ),
            quantity = rqItem.quantity ?: available?.quantity,
            classification = updateClassification(
                rqItem.classification,
                available?.classification
            ),
            additionalClassifications = updateStrategy(
                receivedElements = rqItem.additionalClassifications,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.additionalClassifications?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateClassificationElement
            ),
            deliveryAddress = updateAddress(
                rqItem.deliveryAddress,
                available?.deliveryAddress
            ),
            relatedLot = rqItem.relatedLot ?: available?.relatedLot
        )
    }

fun updateClassification(received: RequestClassification?, available: RecordClassification?): RecordClassification? =
    received?.let { classification ->
        val classificationIdent = if (available == null || (classification.id == available.id && classification.scheme == available.scheme))
            classification.id to classification.scheme
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'classification'. Ids mismatching: " +
                    "Classification from request (id = '${classification.id}', scheme = '${classification.scheme}'), " +
                    "Classification from release (id = '${available.id}', scheme = '${available.scheme}'). "
            )
        RecordClassification(
            id = classificationIdent.first,
            scheme = classificationIdent.second,
            uri = classification.uri ?: available?.uri,
            description = classification.description ?: available?.description
        )
    } ?: available

fun updateClassificationElement(
    received: RequestClassification,
    available: RecordClassification?
): RecordClassification =
    received.let { classification ->
        RecordClassification(
            id = classification.id,
            scheme = classification.scheme ?: available?.scheme,
            uri = classification.uri ?: available?.uri,
            description = classification.description ?: available?.description
        )
    }

fun updateUnit(received: RequestUnit?, available: RecordUnit?): RecordUnit? =
    received?.let { rqUnit ->
        val unitIdent = if (available == null || rqUnit.id == available.id && rqUnit.scheme == available.scheme)
            rqUnit.id to rqUnit.scheme
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'unit'. Ids mismatching: " +
                    "Unit from request (id = '${rqUnit.id}', scheme = '${rqUnit.scheme}'), " +
                    "Unit from release (id = '${available.id}, scheme = '${available.scheme}''). "
            )
        RecordUnit(
            id = unitIdent.first,
            scheme = unitIdent.second,
            name = rqUnit.name ?: available?.name,
            uri = rqUnit.uri ?: available?.uri,
            value = rqUnit.value ?: available?.value
        )
    } ?: available

fun updateCriteria(received: RequestCriteria, available: RecordCriteria?): RecordCriteria =
    received.let { rqCriteria ->
        RecordCriteria(
            id = rqCriteria.id,
            description = rqCriteria.description ?: available?.description,
            title = rqCriteria.title,
            relatesTo = rqCriteria.relatesTo ?: available?.relatesTo,
            relatedItem = rqCriteria.relatedItem ?: available?.relatedItem,
            source = rqCriteria.source ?: available?.source,
            requirementGroups = updateStrategy(
                receivedElements = rqCriteria.requirementGroups,
                keyExtractorForReceivedElement = { bs -> bs.id },
                availableElements = available?.requirementGroups.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateRequirementGroup
            )
        )
    }

fun updateRequirementGroup(
    received: RequestRequirementGroup,
    available: RecordRequirementGroup?
): RecordRequirementGroup =
    received.let { rqRequirementGroup ->
        RecordRequirementGroup(
            id = rqRequirementGroup.id,
            description = rqRequirementGroup.description,
            requirements = updateStrategy(
                receivedElements = rqRequirementGroup.requirements,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.requirements.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateRequirement
            )
        )
    }

fun updateRequirement(received: Requirement, available: Requirement?): Requirement =
    received.let { rqRequirement ->
        Requirement(
            id = rqRequirement.id,
            description = rqRequirement.description ?: available?.description,
            title = rqRequirement.title,
            value = rqRequirement.value,
            period = rqRequirement.period ?: available?.period,
            dataType = rqRequirement.dataType
        )
    }

fun updateConversion(received: RequestConversion, available: RecordConversion?): RecordConversion =
    received.let { rqConversion ->
        RecordConversion(
            id = rqConversion.id,
            description = rqConversion.description ?: available?.description,
            relatedItem = rqConversion.relatedItem,
            relatesTo = rqConversion.relatesTo,
            rationale = rqConversion.rationale,
            coefficients = updateStrategy(
                receivedElements = rqConversion.coefficients,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.coefficients.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateCoefficient
            )
        )
    }

fun updateCoefficient(received: RequestCoefficient, available: RecordCoefficient?): RecordCoefficient =
    received.let { rqCoefficient ->
        RecordCoefficient(
            id = rqCoefficient.id,
            value = rqCoefficient.value,
            coefficient = rqCoefficient.coefficient
        )
    }

fun updateLotGroup(received: RequestLotGroup, available: RecordLotGroup?): RecordLotGroup =
    received.let { rqLotGroup ->
        RecordLotGroup(
            id = rqLotGroup.id,
            relatedLots = updateNonIdentifiable(
                rqLotGroup.relatedLots,
                available?.relatedLots.orEmpty()
            ),
            maximumValue = rqLotGroup.maximumValue ?: available?.maximumValue,
            optionToCombine = rqLotGroup.optionToCombine ?: available?.optionToCombine
        )
    }

fun updatePeriod(received: RequestPeriod?, available: RecordPeriod?): RecordPeriod? =
    received?.let { period ->
        RecordPeriod(
            startDate = period.startDate ?: available?.startDate,
            endDate = period.endDate ?: available?.endDate,
            durationInDays = period.durationInDays ?: available?.durationInDays,
            maxExtentDate = period.maxExtentDate ?: available?.maxExtentDate
        )
    } ?: available

fun updateRecordEnquiry(received: RequestRecordEnquiry, available: RecordRecordEnquiry?): RecordRecordEnquiry =
    received.let { rqRecordEnquiry ->
        RecordRecordEnquiry(
            id = rqRecordEnquiry.id,
            relatedItem = rqRecordEnquiry.relatedItem ?: available?.relatedItem,
            description = rqRecordEnquiry.description ?: available?.description,
            title = rqRecordEnquiry.title ?: available?.title,
            relatedLot = rqRecordEnquiry.relatedLot ?: available?.relatedLot,
            date = rqRecordEnquiry.date ?: available?.date,
            answer = rqRecordEnquiry.answer ?: available?.answer,
            author = updateAuthor(
                rqRecordEnquiry.author,
                available?.author
            ),
            dateAnswered = rqRecordEnquiry.dateAnswered ?: available?.dateAnswered
        )
    }

fun updateAuthor(
    received: RequestOrganizationReference?,
    available: RecordOrganizationReference?
): RecordOrganizationReference? =
    received?.let { requestOrganizationReference ->
        val authorId = if (available == null || requestOrganizationReference.id == available.id)
            requestOrganizationReference.id
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'author'. Ids mismatching: " +
                    "Author from request (id = '${requestOrganizationReference.id}'), " +
                    "Author from release (id = '${available.id}'). "
            )
        RecordOrganizationReference(
            id = authorId,
            name = requestOrganizationReference.name ?: available?.name,
            address = updateAddress(
                requestOrganizationReference.address,
                available?.address
            ),
            details = updateDetails(
                requestOrganizationReference.details,
                available?.details
            ),
            identifier = updateIdentifier(
                requestOrganizationReference.identifier,
                available?.identifier
            ),
            contactPoint = updateContactPoint(
                requestOrganizationReference.contactPoint,
                available?.contactPoint
            ),
            additionalIdentifiers = updateStrategy(
                receivedElements = requestOrganizationReference.additionalIdentifiers,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.additionalIdentifiers.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateIdentifierElement
            ),
            buyerProfile = requestOrganizationReference.buyerProfile ?: available?.buyerProfile,
            persones = updateStrategy(
                receivedElements = requestOrganizationReference.persones,
                keyExtractorForReceivedElement = { it.identifier.id },
                availableElements = available?.persones.orEmpty(),
                keyExtractorForAvailableElement = { it.identifier.id },
                block = ::updatePerson
            )
        )
    }

fun updatePerson(received: RequestPerson, available: RecordPerson?): RecordPerson =
    received.let { rqPerson ->
        RecordPerson(
            title = rqPerson.title,
            name = rqPerson.name,
            identifier = updateIdentifier(
                rqPerson.identifier,
                available?.identifier
            )!!,
            businessFunctions = updateStrategy(
                receivedElements = rqPerson.businessFunctions,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.businessFunctions.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateBusinessFunction
            )
        )
    }

fun updateBusinessFunction(
    received: RequestBusinessFunction,
    available: RecordBusinessFunction?
): RecordBusinessFunction =
    received.let { rqBusinessFunction ->
        RecordBusinessFunction(
            id = rqBusinessFunction.id,
            period = updatePeriod(
                rqBusinessFunction.period,
                available?.period
            )!!,
            documents = updateStrategy(
                receivedElements = rqBusinessFunction.documents,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.documents.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateDocumentBF
            ),
            type = rqBusinessFunction.type,
            jobTitle = rqBusinessFunction.jobTitle
        )
    }

fun updateDocumentBF(received: RequestDocumentBF, available: RecordDocumentBF?): RecordDocumentBF =
    received.let { rqDocument ->
        RecordDocumentBF(
            id = rqDocument.id,
            title = rqDocument.title ?: available?.title,
            url = rqDocument.url ?: available?.url,
            description = rqDocument.description ?: available?.description,
            dateModified = rqDocument.dateModified ?: available?.dateModified,
            datePublished = rqDocument.datePublished ?: available?.datePublished,
            documentType = rqDocument.documentType
        )
    }

fun updateContactPoint(received: RequestContactPoint?, available: RecordContactPoint?): RecordContactPoint? =
    received?.let { _requestContactPoint ->
        RecordContactPoint(
            name = _requestContactPoint.name ?: available?.name,
            url = _requestContactPoint.url ?: available?.url,
            faxNumber = _requestContactPoint.faxNumber ?: available?.faxNumber,
            telephone = _requestContactPoint.telephone ?: available?.telephone,
            email = _requestContactPoint.email ?: available?.email
        )
    } ?: available

fun updateIdentifier(received: RequestIdentifier?, available: RecordIdentifier?): RecordIdentifier? =
    received?.let { _requestIdentifier ->
        val identifierIdent = if (available == null || received.id == available.id && received.scheme == available.scheme)
            received.id to received.scheme
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'identifier'. Ids mismatching: " +
                    "Identifier from request (id = '${received.id}, scheme = '${received.scheme}''), " +
                    "Identifier from release (id = '${available.id}, scheme = '${available.scheme}''). "
            )
        RecordIdentifier(
            id = identifierIdent.first,
            scheme = identifierIdent.second,
            uri = _requestIdentifier.uri ?: available?.uri,
            legalName = _requestIdentifier.legalName ?: available?.legalName
        )
    } ?: available

fun updateIdentifierElement(received: RequestIdentifier, available: RecordIdentifier?): RecordIdentifier =
    received.let { rqIdentifier ->
        RecordIdentifier(
            id = rqIdentifier.id,
            scheme = rqIdentifier.scheme ?: available?.scheme,
            uri = rqIdentifier.uri ?: available?.uri,
            legalName = rqIdentifier.legalName ?: available?.legalName
        )
    }

fun updateDetails(received: RequestDetails?, available: RecordDetails?): RecordDetails? =
    received?.let { rqDetails ->
        RecordDetails(
            typeOfBuyer = rqDetails.typeOfBuyer ?: available?.typeOfBuyer,
            bankAccounts = updateStrategy(
                receivedElements = rqDetails.bankAccounts,
                keyExtractorForReceivedElement = { it.identifier.id!! },
                availableElements = available?.bankAccounts.orEmpty(),
                keyExtractorForAvailableElement = { it.identifier.id },
                block = ::updateBankAccount
            ),
            isACentralPurchasingBody = rqDetails.isACentralPurchasingBody ?: available?.isACentralPurchasingBody,
            legalForm = updateLegalForm(
                rqDetails.legalForm,
                available?.legalForm
            ),
            mainEconomicActivities = updateNonIdentifiable(
                rqDetails.mainEconomicActivities,
                available?.mainEconomicActivities.orEmpty()
            ),
            mainGeneralActivity = rqDetails.mainGeneralActivity ?: available?.mainGeneralActivity,
            mainSectoralActivity = rqDetails.mainSectoralActivity ?: available?.mainSectoralActivity,
            nutsCode = rqDetails.nutsCode ?: available?.nutsCode,
            permits = updateStrategy(
                receivedElements = rqDetails.permits,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.permits.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updatePermits
            ),
            scale = rqDetails.scale ?: available?.scale,
            typeOfSupplier = rqDetails.typeOfSupplier ?: available?.typeOfSupplier
        )
    }

fun updatePermits(received: RequestPermits, available: RecordPermits?): RecordPermits =
    received.let { rqPermit ->
        RecordPermits(
            id = rqPermit.id,
            scheme = rqPermit.scheme ?: available?.scheme,
            url = rqPermit.url ?: available?.url,
            permitDetails = updatePermitDetails(
                rqPermit.permitDetails,
                available?.permitDetails
            )
        )
    }

fun updatePermitDetails(received: RequestPermitDetails?, available: RecordPermitDetails?): RecordPermitDetails? =
    received?.let { rqPermitDetails ->
        RecordPermitDetails(
            issuedBy = rqPermitDetails.issuedBy?.let { requestIssue ->
                val issuedById = if (available == null || requestIssue.id == available.issuedBy?.id)
                    requestIssue.id
                else
                    throw ErrorException(
                        error = ErrorType.INCORRENT_ID,
                        message = "Cannot update 'issuedBy'. Ids mismatching: " +
                            "issuedBy from request (id = '${requestIssue.id}'), " +
                            "issuedBy from release (id = '${available.issuedBy?.id}'). "
                    )
                RecordIssue(
                    id = issuedById,
                    name = requestIssue.name ?: available?.issuedBy?.name
                )
            } ?: available?.issuedBy,
            issuedThought = rqPermitDetails.issuedThought?.let { requestIssue ->
                val issuedByThought = if (available == null || requestIssue.id == available.issuedThought?.id)
                    requestIssue.id
                else
                    throw ErrorException(
                        error = ErrorType.INCORRENT_ID,
                        message = "Cannot update 'issuedThought'. Ids mismatching: " +
                            "issuedThought from request (id = '${requestIssue.id}'), " +
                            "issuedThought from release (id = '${available.issuedThought?.id}'). "
                    )
                RecordIssue(
                    id = issuedByThought,
                    name = requestIssue.name ?: available?.issuedThought?.name
                )
            } ?: available?.issuedThought,
            validityPeriod = updatePeriod(
                rqPermitDetails.validityPeriod,
                available?.validityPeriod
            )
        )
    } ?: available

fun updateLegalForm(received: RequestLegalForm?, available: RecordLegalForm?): RecordLegalForm? =
    received?.let { rqLegalForm ->
        val legalFormIdent = if (available == null || rqLegalForm.id == available.id && rqLegalForm.scheme == available.scheme)
            received.id to received.scheme
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'legalForm'. Ids mismatching: " +
                    "LegalForm from request (id = '${received.id}', scheme = '${received.scheme}'), " +
                    "LegalForm from release (id = '${available.id}', scheme = '${available.scheme}'). "
            )
        RecordLegalForm(
            id = legalFormIdent.first,
            description = rqLegalForm.description,
            uri = rqLegalForm.uri ?: available?.uri,
            scheme = legalFormIdent.second
        )
    } ?: available

fun updateBankAccount(received: RequestBankAccount, available: RecordBankAccount?): RecordBankAccount =
    received.let { rqBankAccount ->
        RecordBankAccount(
            identifier = updateAccountIdentifier(
                rqBankAccount.identifier,
                available?.identifier
            )!!,
            address = updateAddress(
                rqBankAccount.address,
                available?.address
            ),
            description = rqBankAccount.description ?: available?.description,
            accountIdentification = updateAccountIdentifier(
                rqBankAccount.accountIdentification,
                available?.accountIdentification
            ),
            additionalAccountIdentifiers = updateStrategy(
                receivedElements = rqBankAccount.additionalAccountIdentifiers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.additionalAccountIdentifiers?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateAccountIdentifierElement
            ),
            bankName = rqBankAccount.bankName ?: available?.bankName
        )
    }

fun updateAccountIdentifier(
    received: RequestAccountIdentifier?,
    available: RecordAccountIdentifier?
): RecordAccountIdentifier? =
    received?.let { requestAccountIdentifier ->
        val accountIdentifierIdent = if (available == null || requestAccountIdentifier.id == available.id && requestAccountIdentifier.scheme == available.scheme)
            requestAccountIdentifier.id to requestAccountIdentifier.scheme
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'accountIdentifier'. Ids mismatching: " +
                    "AccountIdentifier from request (id = '${requestAccountIdentifier.id}', scheme = '${received.scheme}'), " +
                    "AccountIdentifier from release (id = '${available.id}', scheme = '${available.scheme}'). "
            )
        RecordAccountIdentifier(
            id = accountIdentifierIdent.first,
            scheme = accountIdentifierIdent.second
        )
    } ?: available

fun updateAccountIdentifierElement(
    received: RequestAccountIdentifier,
    available: RecordAccountIdentifier?
): RecordAccountIdentifier =
    received.let { requestAccountIdentifier ->
        RecordAccountIdentifier(
            id = received.id,
            scheme = requestAccountIdentifier.scheme ?: available?.scheme
        )
    }

fun updateReleaseAmendment(received: RequestAmendment, available: RecordAmendment?): RecordAmendment =
    received.let { rqAmendment ->
        RecordAmendment(
            id = rqAmendment.id,
            description = rqAmendment.description ?: available?.description,
            relatedLots = rqAmendment.relatedLots
                .mapIfNotEmpty { it }
                ?: available?.relatedLots.orEmpty(),
            documents = updateStrategy(
                receivedElements = rqAmendment.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            date = rqAmendment.date ?: available?.date,
            rationale = rqAmendment.rationale ?: available?.rationale,
            amendsReleaseID = rqAmendment.amendsReleaseID ?: available?.amendsReleaseID,
            changes = updateChanges(
                rqAmendment.changes,
                available?.changes.orEmpty()
            ),
            releaseID = rqAmendment.releaseID ?: available?.releaseID,
            status = rqAmendment.status ?: available?.status,
            type = rqAmendment.type ?: available?.type,
            relatedItem = rqAmendment.relatedItem ?: available?.relatedItem,
            relatesTo = rqAmendment.relatesTo ?: available?.relatesTo
        )
    }

fun updateChanges(received: List<RequestChange>, available: List<RecordChange>): List<RecordChange> =
    received.mapIfNotEmpty { rqChange ->
        RecordChange(
            property = rqChange.property,
            formerValue = rqChange.formerValue
        )
    } ?: available

fun updateDocument(received: RequestDocument, available: RecordDocument?): RecordDocument =
    received.let { rqDocument ->
        RecordDocument(
            id = rqDocument.id,
            relatedLots = updateNonIdentifiable(
                rqDocument.relatedLots,
                available?.relatedLots.orEmpty()
            ),
            description = rqDocument.description ?: available?.description,
            url = rqDocument.url ?: available?.url,
            documentType = rqDocument.documentType ?: available?.documentType,
            datePublished = rqDocument.datePublished ?: available?.datePublished,
            language = rqDocument.language ?: available?.language,
            dateModified = rqDocument.dateModified ?: available?.dateModified,
            format = rqDocument.format ?: available?.format,
            relatedConfirmations = updateNonIdentifiable(
                rqDocument.relatedConfirmations,
                available?.relatedConfirmations.orEmpty()
            ),
            title = rqDocument.title ?: available?.title
        )
    }

fun updateElectronicAuctions(
    received: RequestElectronicAuctions?,
    available: RecordElectronicAuctions?
): RecordElectronicAuctions? =
    received?.let { rqElectronicAuctions ->
        RecordElectronicAuctions(
            details = updateStrategy(
                receivedElements = rqElectronicAuctions.details,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.details?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateElectronicAuctionsDetails
            )
        )
    } ?: available

fun updateElectronicAuctionsDetails(
    received: RequestElectronicAuctionsDetails,
    available: RecordElectronicAuctionsDetails?
): RecordElectronicAuctionsDetails =
    received.let { rqElectronicAuctionsDetail ->
        RecordElectronicAuctionsDetails(
            id = rqElectronicAuctionsDetail.id,
            relatedLot = rqElectronicAuctionsDetail.relatedLot ?: available?.relatedLot,
            auctionPeriod = rqElectronicAuctionsDetail.auctionPeriod ?: available?.auctionPeriod,
            electronicAuctionModalities = updateElectronicAuctionModalities(
                rqElectronicAuctionsDetail.electronicAuctionModalities,
                available?.electronicAuctionModalities.orEmpty()
            ),
            electronicAuctionProgress = updateStrategy(
                receivedElements = rqElectronicAuctionsDetail.electronicAuctionProgress,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.electronicAuctionProgress?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateElectronicAuctionProgress
            ),
            electronicAuctionResult = udpdateElectronicAuctionResult(
                rqElectronicAuctionsDetail.electronicAuctionResult,
                available?.electronicAuctionResult.orEmpty()
            )
        )
    }

fun udpdateElectronicAuctionResult(
    received: List<RequestElectronicAuctionResult>,
    available: List<RecordElectronicAuctionResult>
): List<RecordElectronicAuctionResult> =
    received.mapIfNotEmpty { rqElectronicAuctionResult ->
        RecordElectronicAuctionResult(
            relatedBid = rqElectronicAuctionResult.relatedBid,
            value = rqElectronicAuctionResult.value
        )
    } ?: available

fun updateElectronicAuctionModalities(
    received: List<RequestElectronicAuctionModalities>,
    available: List<RecordElectronicAuctionModalities>
): List<RecordElectronicAuctionModalities> =
    received.mapIfNotEmpty { rqElectronicAuctionModality ->
        RecordElectronicAuctionModalities(
            url = rqElectronicAuctionModality.url,
            eligibleMinimumDifference = rqElectronicAuctionModality.eligibleMinimumDifference
        )
    } ?: available

fun updateElectronicAuctionProgress(
    received: RequestElectronicAuctionProgress,
    available: RecordElectronicAuctionProgress?
): RecordElectronicAuctionProgress =
    received.let { rqElectronicAuctionProgress ->
        RecordElectronicAuctionProgress(
            id = rqElectronicAuctionProgress.id,
            period = updatePeriod(
                rqElectronicAuctionProgress.period,
                available?.period
            ),
            breakdown = updateElectronicAuctionProgressBreakdown(
                rqElectronicAuctionProgress.breakdown,
                available?.breakdown.orEmpty()
            )
        )
    }

fun updateElectronicAuctionProgressBreakdown(
    received: List<RequestElectronicAuctionProgressBreakdown>,
    available: List<RecordElectronicAuctionProgressBreakdown>
): List<RecordElectronicAuctionProgressBreakdown> =
    received.mapIfNotEmpty { rqBreakdown ->
        RecordElectronicAuctionProgressBreakdown(
            relatedBid = rqBreakdown.relatedBid,
            value = rqBreakdown.value,
            status = rqBreakdown.status,
            dateMet = rqBreakdown.dateMet
        )
    } ?: available

fun updateNonIdentifiable(received: List<String>, available: List<String>): List<String> =
    received.mapIfNotEmpty { it } ?: available

fun updateReleaseTender(received: RequestTender, available: RecordTender): RecordTender =
    received.let { rqTender ->
        val tenderId = if (rqTender.id == available.id)
            rqTender.id
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'tender'. Ids mismatching: " +
                    "Tender from request (id = '${rqTender.id}'), " +
                    "Tender from release (id = '${available.id}'). "
            )
        RecordTender(
            id = tenderId,
            status = received.status ?: available.status,
            auctionPeriod = updatePeriod(
                received.auctionPeriod,
                available.auctionPeriod
            ),
            title = received.title ?: available.title,
            description = received.description ?: available.description,
            documents = updateStrategy(
                receivedElements = received.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available.documents,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            statusDetails = received.statusDetails ?: available.statusDetails,
            amendments = updateStrategy(
                receivedElements = received.amendments,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available.amendments,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateReleaseAmendment
            ),
            submissionMethodRationale = updateNonIdentifiable(
                received.submissionMethodRationale,
                available.submissionMethodRationale
            ),
            submissionMethodDetails = received.submissionMethodDetails ?: available.submissionMethodDetails,
            awardCriteria = received.awardCriteria ?: available.awardCriteria,
            awardCriteriaDetails = received.awardCriteriaDetails ?: available.awardCriteriaDetails,
            awardPeriod = updatePeriod(
                received.awardPeriod,
                available.awardPeriod
            ),
            conversions = updateStrategy(
                receivedElements = received.conversions,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.conversions,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateConversion
            ),
            criteria = updateStrategy(
                receivedElements = received.criteria,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.criteria,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateCriteria
            ),
            electronicAuctions = updateElectronicAuctions(
                received.electronicAuctions,
                available.electronicAuctions
            ),
            enquiries = updateStrategy(
                receivedElements = received.enquiries,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available.enquiries,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRecordEnquiry
            ).toMutableList(),
            enquiryPeriod = updatePeriod(
                received.enquiryPeriod,
                available.enquiryPeriod
            ),
            hasEnquiries = received.hasEnquiries ?: available.hasEnquiries,
            items = updateStrategy(
                receivedElements = received.items,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available.items,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateItem
            ),
            lotGroups = updateStrategy(
                receivedElements = received.lotGroups,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.lotGroups,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateLotGroup
            ),
            lots = updateStrategy(
                receivedElements = received.lots,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.lots,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateLot
            ),
            procurementMethodModalities = updateNonIdentifiable(
                received.procurementMethodModalities,
                available.procurementMethodModalities
            ),
            requiresElectronicCatalogue = received.requiresElectronicCatalogue ?: available.requiresElectronicCatalogue,
            standstillPeriod = updatePeriod(
                received.standstillPeriod,
                available.standstillPeriod
            ),
            submissionMethod = updateNonIdentifiable(
                received.submissionMethod,
                available.submissionMethod
            ),
            tenderPeriod = updatePeriod(
                received.tenderPeriod,
                available.tenderPeriod
            ),
            value = updateValue(
                received.value,
                available.value
            ),
            milestones = updateStrategy(
                receivedElements = received.milestones,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.milestones,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateMilestone
            ),
            classification = updateClassification(
                received.classification,
                available.classification
            ),
            amendment = updateAmendment(
                received.amendment,
                available.amendment
            ),
            contractPeriod = updateContractPeriod(
                received.contractPeriod,
                available.contractPeriod
            ),
            procurementMethodDetails = received.procurementMethodDetails ?: available.procurementMethodDetails,
            legalBasis = received.legalBasis ?: available.legalBasis,
            mainProcurementCategory = received.mainProcurementCategory ?: available.mainProcurementCategory,
            eligibilityCriteria = received.eligibilityCriteria ?: available.eligibilityCriteria,
            minValue = updateValue(
                received.minValue,
                available.minValue
            ),
            numberOfTenderers = received.numberOfTenderers ?: available.numberOfTenderers,
            procurementMethod = received.procurementMethod ?: available.procurementMethod,
            procurementMethodRationale = received.procurementMethodRationale ?: available.procurementMethodRationale,
            additionalProcurementCategories = updateNonIdentifiable(
                received.additionalProcurementCategories,
                available.additionalProcurementCategories
            ),
            procurementMethodAdditionalInfo = received.procurementMethodAdditionalInfo ?: available.procurementMethodAdditionalInfo,
            submissionLanguages = updateNonIdentifiable(
                received.submissionLanguages,
                available.submissionLanguages
            ),
            tenderers = updateStrategy(
                receivedElements = received.tenderers,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.tenderers,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateOrganizationReference
            ),
            acceleratedProcedure = updateAcceleratedProcedure(
                received.acceleratedProcedure,
                available.acceleratedProcedure
            ),
            designContest = updateDesignContest(
                received.designContest,
                available.designContest
            ),
            dynamicPurchasingSystem = updateDynamicPurchasingSystem(
                received.dynamicPurchasingSystem,
                available.dynamicPurchasingSystem
            ),
            electronicWorkflows = updateElectronicWorkflows(
                received.electronicWorkflows,
                available.electronicWorkflows
            ),
            framework = updateFramework(
                received.framework,
                available.framework
            ),
            jointProcurement = updateJointProcurement(
                received.jointProcurement,
                available.jointProcurement
            ),
            lotDetails = updateLotDetails(
                received.lotDetails,
                available.lotDetails
            ),
            objectives = updateObjectives(
                received.objectives,
                available.objectives
            ),
            participationFees = updateParticipationFee(
                received.participationFees,
                available.participationFees
            ),
            procedureOutsourcing = updateProcedureOutsourcing(
                received.procedureOutsourcing,
                available.procedureOutsourcing
            ),
            procuringEntity = received.procuringEntity?.let {
                updateOrganizationReference(
                    it,
                    available.procuringEntity
                )
            } ?: available.procuringEntity,
            reviewParties = updateStrategy(
                receivedElements = received.reviewParties,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available.reviewParties,
                keyExtractorForAvailableElement = { it.id },
                block = ::updateOrganizationReference
            ),
            reviewPeriod = updatePeriod(
                received.reviewPeriod,
                available.reviewPeriod
            )
        )
    }

fun updateProcedureOutsourcing(
    received: RequestProcedureOutsourcing?,
    available: RecordProcedureOutsourcing?
): RecordProcedureOutsourcing? =
    received?.let {
        RecordProcedureOutsourcing(
            procedureOutsourced = it.procedureOutsourced ?: available?.procedureOutsourced,
            outsourcedTo = it.outsourcedTo?.let {
                updateOrganization(
                    it,
                    available?.outsourcedTo
                )
            } ?: available?.outsourcedTo
        )
    } ?: available

fun updateParticipationFee(
    received: List<RequestParticipationFee>,
    available: List<RecordParticipationFee>
): List<RecordParticipationFee> =
    received.mapIfNotEmpty {
        RecordParticipationFee(
            type = it.type,
            value = it.value,
            description = it.description,
            methodOfPayment = it.methodOfPayment
        )
    } ?: available

fun updateObjectives(received: RequestObjectives?, available: RecordObjectives?): RecordObjectives? =
    received?.let {
        RecordObjectives(
            types = updateNonIdentifiable(
                it.types,
                available?.types.orEmpty()
            ),
            additionalInformation = it.additionalInformation ?: available?.additionalInformation
        )
    } ?: available

fun updateLotDetails(received: RequestLotDetails?, available: RecordLotDetails?): RecordLotDetails? =
    received?.let {
        RecordLotDetails(
            maximumLotsAwardedPerSupplier = it.maximumLotsAwardedPerSupplier ?: available?.maximumLotsAwardedPerSupplier,
            maximumLotsBidPerSupplier = it.maximumLotsBidPerSupplier ?: available?.maximumLotsBidPerSupplier
        )
    } ?: available

fun updateJointProcurement(
    received: RequestJointProcurement?,
    available: RecordJointProcurement?
): RecordJointProcurement? =
    received?.let {
        RecordJointProcurement(
            isJointProcurement = it.isJointProcurement ?: available?.isJointProcurement,
            country = it.country ?: available?.country
        )
    } ?: available

fun updateFramework(received: RequestFramework?, available: RecordFramework?): RecordFramework? =
    received?.let {
        RecordFramework(
            isAFramework = it.isAFramework ?: available?.isAFramework,
            additionalBuyerCategories = updateNonIdentifiable(
                it.additionalBuyerCategories,
                available?.additionalBuyerCategories.orEmpty()
            ),
            exceptionalDurationRationale = it.exceptionalDurationRationale ?: available?.exceptionalDurationRationale,
            maxSuppliers = it.maxSuppliers ?: available?.maxSuppliers,
            typeOfFramework = it.typeOfFramework ?: available?.typeOfFramework
        )
    } ?: available

fun updateElectronicWorkflows(
    received: RequestElectronicWorkflows?,
    available: RecordElectronicWorkflows?
): RecordElectronicWorkflows? =
    received?.let {
        RecordElectronicWorkflows(
            useOrdering = it.useOrdering ?: available?.useOrdering,
            acceptInvoicing = it.acceptInvoicing ?: available?.acceptInvoicing,
            usePayment = it.usePayment ?: available?.usePayment
        )
    } ?: available

fun updateDynamicPurchasingSystem(
    received: RequestDynamicPurchasingSystem?,
    available: RecordDynamicPurchasingSystem?
): RecordDynamicPurchasingSystem? =
    received?.let {
        RecordDynamicPurchasingSystem(
            hasDynamicPurchasingSystem = it.hasDynamicPurchasingSystem ?: available?.hasDynamicPurchasingSystem,
            hasOutsideBuyerAccess = it.hasOutsideBuyerAccess ?: available?.hasOutsideBuyerAccess,
            noFurtherContracts = it.noFurtherContracts ?: available?.noFurtherContracts
        )
    } ?: available

fun updateDesignContest(received: RequestDesignContest?, available: RecordDesignContest?): RecordDesignContest? =
    received?.let {
        RecordDesignContest(
            hasPrizes = it.hasPrizes ?: available?.hasPrizes,
            juryDecisionBinding = it.juryDecisionBinding ?: available?.juryDecisionBinding,
            juryMembers = updateStrategy(
                receivedElements = it.juryMembers,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.juryMembers.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateOrganizationReference
            ),
            participants = updateStrategy(
                receivedElements = it.participants.orEmpty(),
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.participants.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateOrganizationReference
            ),
            paymentsToParticipants = it.paymentsToParticipants ?: available?.paymentsToParticipants,
            prizes = updateStrategy(
                receivedElements = it.prizes.orEmpty(),
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.prizes.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateItem
            ),
            serviceContractAward = it.serviceContractAward ?: available?.serviceContractAward
        )
    } ?: available

fun updateAcceleratedProcedure(
    received: RequestAcceleratedProcedure?,
    available: RecordAcceleratedProcedure?
): RecordAcceleratedProcedure? =
    received?.let {
        RecordAcceleratedProcedure(
            isAcceleratedProcedure = it.isAcceleratedProcedure ?: available?.isAcceleratedProcedure,
            acceleratedProcedureJustification = it.acceleratedProcedureJustification ?: available?.acceleratedProcedureJustification
        )
    } ?: available

fun updateValue(received: Value?, available: Value?): Value? =
    received?.let { rqValue ->
        Value(
            amount = rqValue.amount ?: available?.amount,
            currency = rqValue.currency ?: available?.currency,
            amountNet = rqValue.amountNet ?: available?.amountNet,
            valueAddedTaxIncluded = rqValue.valueAddedTaxIncluded ?: available?.valueAddedTaxIncluded
        )
    } ?: available

fun updateOrganization(received: RequestOrganization, available: RecordOrganization?): RecordOrganization =
    received.let { rqOrganization ->
        RecordOrganization(
            id = rqOrganization.id,
            name = rqOrganization.name ?: available?.name,
            details = updateDetails(
                rqOrganization.details,
                available?.details
            ),
            identifier = updateIdentifier(
                rqOrganization.identifier,
                available?.identifier
            ),
            persones = updateStrategy(
                receivedElements = rqOrganization.persones,
                keyExtractorForReceivedElement = { it.identifier.id!! },
                availableElements = available?.persones?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.identifier.id!! },
                block = ::updatePerson
            ),
            buyerProfile = rqOrganization.buyerProfile ?: available?.buyerProfile,
            additionalIdentifiers = updateStrategy(
                receivedElements = rqOrganization.additionalIdentifiers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.additionalIdentifiers?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateIdentifierElement
            ),
            contactPoint = updateContactPoint(
                rqOrganization.contactPoint,
                available?.contactPoint
            ),
            address = updateAddress(
                rqOrganization.address,
                available?.address
            ),
            roles = rqOrganization.roles
        )
    }

fun updateAward(received: RequestAward, available: RecordAward?): RecordAward =
    received.let { rqAward ->
        RecordAward(
            id = rqAward.id,
            status = rqAward.status ?: available?.status,
            statusDetails = rqAward.statusDetails ?: available?.statusDetails,
            title = rqAward.title ?: available?.title,
            description = rqAward.description ?: available?.description,
            value = rqAward.value ?: available?.value,
            relatedBid = rqAward.relatedBid ?: available?.relatedBid,
            relatedLots = updateNonIdentifiable(
                rqAward.relatedLots,
                available?.relatedLots.orEmpty()
            ),
            date = rqAward.date ?: available?.date,
            contractPeriod = updateContractPeriod(
                rqAward.contractPeriod,
                available?.contractPeriod
            ),
            weightedValue = rqAward.weightedValue ?: available?.weightedValue,
            reviewProceedings = updateReviewProceedings(
                rqAward.reviewProceedings,
                available?.reviewProceedings
            ),
            items = updateStrategy(
                receivedElements = rqAward.items,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.items.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateItem
            ),
            amendments = updateStrategy(
                receivedElements = rqAward.amendments,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.amendments.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateAmendmentElement
            ),
            amendment = updateAmendment(
                rqAward.amendment,
                available?.amendment
            ),
            documents = updateStrategy(
                receivedElements = rqAward.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            requirementResponses = updateStrategy(
                receivedElements = rqAward.requirementResponses,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.requirementResponses.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRequirementResponse
            ),
            suppliers = updateStrategy(
                receivedElements = rqAward.suppliers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.suppliers.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateSupplier
            )
        )
    }

fun updateAmendmentElement(received: RequestAmendment, available: RecordAmendment?): RecordAmendment =
    received.let { rqAmendment ->
        RecordAmendment(
            id = rqAmendment.id,
            date = rqAmendment.date ?: available?.date,
            description = rqAmendment.description ?: available?.description,
            rationale = rqAmendment.rationale ?: available?.rationale,
            documents = updateStrategy(
                receivedElements = rqAmendment.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            relatedLots = updateNonIdentifiable(
                rqAmendment.relatedLots,
                available?.relatedLots.orEmpty()
            ),
            releaseID = rqAmendment.releaseID ?: available?.releaseID,
            changes = updateChanges(
                rqAmendment.changes,
                available?.changes.orEmpty()
            ),
            amendsReleaseID = rqAmendment.amendsReleaseID ?: available?.amendsReleaseID,
            type = rqAmendment.type ?: available?.type,
            status = rqAmendment.status ?: available?.status,
            relatedItem = rqAmendment.relatedItem ?: available?.relatedItem,
            relatesTo = rqAmendment.relatesTo ?: available?.relatesTo
        )
    }

fun updateAmendment(received: RequestAmendment?, available: RecordAmendment?): RecordAmendment? =
    received?.let { rqAmendment ->
        val amendmentId = if (available == null || rqAmendment.id == available.id)
            rqAmendment.id
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'amendment'. Ids mismatching: " +
                    "Amendment from request (id = '${rqAmendment.id}'), " +
                    "Amendment from release (id = '${available.id}'). "
            )
        RecordAmendment(
            id = amendmentId,
            date = rqAmendment.date ?: available?.date,
            description = rqAmendment.description ?: available?.description,
            rationale = rqAmendment.rationale ?: available?.rationale,
            documents = updateStrategy(
                receivedElements = rqAmendment.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            relatedLots = rqAmendment.relatedLots
                .mapIfNotEmpty { it }
                ?: available?.relatedLots.orEmpty(),
            releaseID = rqAmendment.releaseID ?: available?.releaseID,
            changes = updateChanges(
                rqAmendment.changes,
                available?.changes.orEmpty()
            ),
            amendsReleaseID = rqAmendment.amendsReleaseID ?: available?.amendsReleaseID,
            type = rqAmendment.type ?: available?.type,
            status = rqAmendment.status ?: available?.status,
            relatedItem = rqAmendment.relatedItem ?: available?.relatedItem,
            relatesTo = rqAmendment.relatesTo ?: available?.relatesTo
        )
    } ?: available

fun updateReviewProceedings(
    received: RequestReviewProceedings?,
    available: RecordReviewProceedings?
): RecordReviewProceedings? =
    received?.let { rqReviewProceeding ->
        RecordReviewProceedings(
            buyerProcedureReview = rqReviewProceeding.buyerProcedureReview ?: available?.buyerProcedureReview,
            legalProcedures = updateStrategy(
                receivedElements = rqReviewProceeding.legalProcedures,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.legalProcedures?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateLegalProceeding
            ),
            reviewBodyChallenge = rqReviewProceeding.reviewBodyChallenge ?: available?.reviewBodyChallenge

        )
    } ?: available

fun updateLegalProceeding(
    received: RequestLegalProceedings,
    available: RecordLegalProceedings?
): RecordLegalProceedings =
    received.let { rqLegalProceeding ->
        RecordLegalProceedings(
            id = rqLegalProceeding.id,
            title = rqLegalProceeding.title ?: available?.title,
            uri = rqLegalProceeding.uri ?: available?.uri
        )
    }

fun updateRequirementResponse(
    received: RequestRequirementResponse,
    available: RecordRequirementResponse?
): RecordRequirementResponse =
    received.let { rqRequirementResponse ->
        RecordRequirementResponse(
            id = rqRequirementResponse.id,
            title = rqRequirementResponse.title ?: available?.title,
            value = rqRequirementResponse.value,
            description = rqRequirementResponse.description ?: available?.description,
            period = updatePeriod(
                rqRequirementResponse.period,
                available?.period
            ),
            relatedTenderer = rqRequirementResponse.relatedTenderer
                ?.let {
                    updateOrganizationReference(
                        it,
                        available?.relatedTenderer
                    )
                }
                ?: available?.relatedTenderer,
            requirement = updateRequirement(
                rqRequirementResponse.requirement,
                available?.requirement
            )
        )
    }

fun updateRequirement(
    received: RequestRequirementReference?,
    available: RecordRequirementReference?
): RecordRequirementReference? =
    received?.let { rqRequirement ->
        RecordRequirementReference(
            id = rqRequirement.id,
            title = rqRequirement.title ?: available?.title
        )
    } ?: available

fun updateOrganizationReference(
    received: RequestOrganizationReference,
    available: RecordOrganizationReference?
): RecordOrganizationReference =
    received.let { requestOrganizationReference ->
        RecordOrganizationReference(
            id = available?.id,
            address = updateAddress(
                requestOrganizationReference.address,
                available?.address
            ),
            contactPoint = updateContactPoint(
                requestOrganizationReference.contactPoint,
                available?.contactPoint
            ),
            additionalIdentifiers = updateStrategy(
                receivedElements = requestOrganizationReference.additionalIdentifiers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.additionalIdentifiers?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateIdentifierElement
            ),
            persones = updateStrategy(
                receivedElements = requestOrganizationReference.persones,
                keyExtractorForReceivedElement = { it.identifier.id!! },
                availableElements = available?.persones?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.identifier.id!! },
                block = ::updatePerson
            ),
            identifier = updateIdentifier(
                requestOrganizationReference.identifier,
                available?.identifier
            ),
            name = requestOrganizationReference.name ?: available?.name,
            details = updateDetails(
                requestOrganizationReference.details,
                available?.details
            ),
            buyerProfile = requestOrganizationReference.buyerProfile ?: available?.buyerProfile
        )
    }

fun updateSupplier(
    received: RequestOrganizationReference,
    available: RecordOrganizationReference?
): RecordOrganizationReference =
    received.let { rqOrganizationRefference ->
        RecordOrganizationReference(
            id = available?.id,
            address = updateAddress(
                rqOrganizationRefference.address,
                available?.address
            ),
            contactPoint = updateContactPoint(
                rqOrganizationRefference.contactPoint,
                available?.contactPoint
            ),
            additionalIdentifiers = updateStrategy(
                receivedElements = rqOrganizationRefference.additionalIdentifiers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.additionalIdentifiers?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateIdentifierElement
            ),
            persones = updateStrategy(
                receivedElements = rqOrganizationRefference.persones,
                keyExtractorForReceivedElement = { it.identifier.id!! },
                availableElements = available?.persones?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.identifier.id!! },
                block = ::updatePerson
            ),
            identifier = updateIdentifier(
                rqOrganizationRefference.identifier,
                available?.identifier
            ),
            name = rqOrganizationRefference.name ?: available?.name,
            details = updateDetails(
                rqOrganizationRefference.details,
                available?.details
            ),
            buyerProfile = rqOrganizationRefference.buyerProfile ?: available?.buyerProfile
        )
    }

fun updateBid(received: RequestBid, available: RecordBid?): RecordBid =
    received.let { rqBid ->
        RecordBid(
            id = rqBid.id,
            value = rqBid.value ?: available?.value,
            relatedLots = updateNonIdentifiable(
                rqBid.relatedLots,
                available?.relatedLots.orEmpty()
            ),
            documents = updateStrategy(
                receivedElements = rqBid.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            date = rqBid.date ?: available?.date,
            requirementResponses = updateStrategy(
                receivedElements = rqBid.requirementResponses,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.requirementResponses?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRequirementResponse
            ),
            statusDetails = rqBid.statusDetails ?: available?.statusDetails,
            status = rqBid.status ?: available?.status,
            tenderers = updateStrategy(
                receivedElements = rqBid.tenderers,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.tenderers.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateSupplier
            )
        )
    }

fun updateContract(received: RequestContract, available: RecordContract?): RecordContract =
    received.let { rqContract ->
        RecordContract(
            id = rqContract.id,
            status = rqContract.status,
            statusDetails = rqContract.statusDetails,
            requirementResponses = updateStrategy(
                receivedElements = rqContract.requirementResponses,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.requirementResponses?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRequirementResponse
            ),
            date = rqContract.date ?: available?.date,
            documents = updateStrategy(
                receivedElements = rqContract.documents,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.documents?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateDocument
            ),
            relatedLots = rqContract.relatedLots
                .mapIfNotEmpty { it }
                ?: available?.relatedLots.orEmpty(),
            value = updateValueTax(
                rqContract.value,
                available?.value
            ),
            title = rqContract.title ?: available?.title,
            period = updatePeriod(
                rqContract.period,
                available?.period
            ),
            description = rqContract.description ?: available?.description,
            amendment = updateAmendment(
                rqContract.amendment,
                available?.amendment
            ),
            amendments = updateStrategy(
                receivedElements = rqContract.amendments,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.amendments.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateAmendmentElement
            ),
            items = updateStrategy(
                receivedElements = rqContract.items,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.items?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateItem
            ),
            classification = updateClassification(
                rqContract.classification,
                available?.classification
            ),
            agreedMetrics = updateStrategy(
                receivedElements = rqContract.agreedMetrics,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.agreedMetrics?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateAgreedMetric
            ),
            awardId = rqContract.awardId ?: available?.awardId,
            budgetSource = updateStrategy(
                receivedElements = rqContract.budgetSource,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.budgetSource.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateBudgetSource
            ),
            confirmationRequests = updateStrategy(
                receivedElements = rqContract.confirmationRequests,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.confirmationRequests.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateConfirmationRequest
            ),
            confirmationResponses = updateStrategy(
                receivedElements = rqContract.confirmationResponses,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.confirmationResponses?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateConfirmationResponse
            ),
            countryOfOrigin = rqContract.countryOfOrigin ?: available?.countryOfOrigin,
            dateSigned = rqContract.dateSigned ?: available?.dateSigned,
            extendsContractId = rqContract.extendsContractId ?: available?.extendsContractId,
            isFrameworkOrDynamic = rqContract.isFrameworkOrDynamic ?: available?.isFrameworkOrDynamic,
            lotVariant = updateNonIdentifiable(
                rqContract.lotVariant,
                available?.lotVariant.orEmpty()
            ),
            milestones = updateStrategy(
                receivedElements = rqContract.milestones,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.milestones.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateMilestone
            ),
            relatedProcesses = updateStrategy(
                receivedElements = rqContract.relatedProcesses,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.relatedProcesses?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRelatedProcess
            ),
            valueBreakdown = updateStrategy(
                receivedElements = rqContract.valueBreakdown,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.valueBreakdown?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateValueBreakdown
            )
        )
    }

fun updateValueBreakdown(
    received: RequestValueBreakdown,
    available: RecordValueBreakdown?
): RecordValueBreakdown =
    received.let { rqValueBreakdown ->
        RecordValueBreakdown(
            id = rqValueBreakdown.id,
            type = updateNonIdentifiable(
                rqValueBreakdown.type,
                available?.type.orEmpty()
            ),
            description = rqValueBreakdown.description ?: available?.description,
            amount = rqValueBreakdown.amount ?: available?.amount,
            estimationMethod = rqValueBreakdown.estimationMethod ?: available?.estimationMethod
        )
    }

fun updateRelatedProcess(
    received: RequestRelatedProcess,
    available: RecordRelatedProcess?
): RecordRelatedProcess =
    received.let { rqRelatedProcess ->
        RecordRelatedProcess(
            id = rqRelatedProcess.id,
            scheme = rqRelatedProcess.scheme ?: available?.scheme,
            identifier = rqRelatedProcess.identifier ?: available?.identifier,
            uri = rqRelatedProcess.uri ?: available?.uri,
            relationship = rqRelatedProcess.relationship
                .mapIfNotEmpty { it }
                ?: available?.relationship.orEmpty()
        )
    }

fun updateMilestone(received: RequestMilestone, available: RecordMilestone?): RecordMilestone =
    received.let { rqMilestone ->
        RecordMilestone(
            id = rqMilestone.id,
            title = rqMilestone.title ?: available?.title,
            description = rqMilestone.description ?: available?.description,
            type = rqMilestone.type ?: available?.type,
            status = rqMilestone.status ?: available?.status,
            dateMet = rqMilestone.dateMet ?: available?.dateMet,
            dateModified = rqMilestone.dateModified ?: available?.dateModified,
            additionalInformation = rqMilestone.additionalInformation ?: available?.additionalInformation,
            dueDate = rqMilestone.dueDate ?: available?.dueDate,
            relatedItems = updateNonIdentifiable(
                rqMilestone.relatedItems,
                available?.relatedItems.orEmpty()
            ),
            relatedParties = updateStrategy(
                receivedElements = rqMilestone.relatedParties,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.relatedParties.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateRelatedParty
            )
        )
    }

fun updateRelatedParty(received: RequestRelatedParty, available: RecordRelatedParty?): RecordRelatedParty =
    received.let { rqRelatedParty ->
        RecordRelatedParty(
            id = rqRelatedParty.id,
            name = rqRelatedParty.name ?: available?.name
        )
    }

fun updateConfirmationResponse(
    received: RequestConfirmationResponse,
    available: RecordConfirmationResponse?
): RecordConfirmationResponse =
    received.let { rqConfirmationResponse ->
        RecordConfirmationResponse(
            id = rqConfirmationResponse.id,
            value = updateConfirmationResponseValue(
                rqConfirmationResponse.value,
                available?.value
            ),
            request = rqConfirmationResponse.request ?: available?.request
        )
    }

fun updateConfirmationResponseValue(
    received: RequestConfirmationResponseValue?,
    available: RecordConfirmationResponseValue?
): RecordConfirmationResponseValue? =
    received?.let { rqValue ->
        RecordConfirmationResponseValue(
            id = available?.id,
            name = rqValue.name ?: available?.name,
            date = rqValue.date ?: available?.date,
            relatedPerson = updateRelatedPerson(
                rqValue.relatedPerson,
                available?.relatedPerson
            ),
            verification = updateVerification(
                rqValue.verification,
                available?.verification.orEmpty()
            )
        )
    } ?: available

fun updateVerification(
    received: List<RequestVerification>,
    available: List<RecordVerification>
): List<RecordVerification> =
    received.mapIfNotEmpty { rqVerification ->
        RecordVerification(
            type = rqVerification.type,
            value = rqVerification.value,
            rationale = rqVerification.rationale
        )
    } ?: available

fun updateRelatedPerson(received: RequestRelatedPerson?, available: RecordRelatedPerson?): RecordRelatedPerson? =
    received?.let { rqRelatedPerson ->
        val personId = if (available == null || rqRelatedPerson.id == available.id)
            rqRelatedPerson.id
        else
            throw ErrorException(
                error = ErrorType.INCORRENT_ID,
                message = "Cannot update 'relatedPerson'. Ids mismatching: " +
                    "RelatedPerson from request (id = '${rqRelatedPerson.id}'), " +
                    "RelatedPerson from release (id = '${available.id}'). "
            )
        RecordRelatedPerson(
            id = personId,
            name = rqRelatedPerson.name
        )
    } ?: available

fun updateConfirmationRequest(
    received: RequestConfirmationRequest,
    available: RecordConfirmationRequest?
): RecordConfirmationRequest =
    received.let { rqConfirmationRequest ->
        RecordConfirmationRequest(
            id = received.id,
            title = rqConfirmationRequest.title ?: available?.title,
            description = rqConfirmationRequest.description ?: available?.description,
            relatesTo = rqConfirmationRequest.relatesTo ?: available?.relatesTo,
            relatedItem = rqConfirmationRequest.relatedItem,
            type = rqConfirmationRequest.type ?: available?.type,
            source = rqConfirmationRequest.source,
            requestGroups = updateStrategy(
                receivedElements = rqConfirmationRequest.requestGroups,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.requestGroups?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateRequestGroup
            )
        )
    }

fun updateRequestGroup(received: RequestRequestGroup, available: RecordRequestGroup?): RecordRequestGroup =
    received.let { rqRequestGroup ->
        RecordRequestGroup(
            id = rqRequestGroup.id,
            requests = updateStrategy(
                receivedElements = rqRequestGroup.requests,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.requests?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateRequest
            )
        )
    }

fun updateRequest(received: RequestRequest, available: RecordRequest?): RecordRequest =
    received.let { rqRequest ->
        RecordRequest(
            id = rqRequest.id,
            relatedPerson = updateRelatedPerson(
                rqRequest.relatedPerson,
                available?.relatedPerson
            ),
            description = rqRequest.description,
            title = rqRequest.title
        )
    }

fun updateBudgetSource(received: RequestBudgetSource, available: RecordBudgetSource?): RecordBudgetSource =
    received.let { rqBudgetSource ->
        RecordBudgetSource(
            id = rqBudgetSource.id,
            currency = rqBudgetSource.currency ?: available?.currency,
            amount = rqBudgetSource.amount ?: available?.amount
        )
    }

fun updateAgreedMetric(received: RequestAgreedMetric, available: RecordAgreedMetric?): RecordAgreedMetric =
    received.let { rqAgreedMetric ->
        RecordAgreedMetric(
            id = rqAgreedMetric.id,
            description = rqAgreedMetric.description ?: available?.description,
            title = rqAgreedMetric.title ?: available?.title,
            observations = updateStrategy(
                receivedElements = rqAgreedMetric.observations,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.observations?.toList().orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateObservation
            )
        )
    }

fun updateObservationUnit(
    received: RequestObservationUnit?,
    available: RecordObservationUnit?
): RecordObservationUnit? =
    received?.let { rqUnit ->
        RecordObservationUnit(
            id = available?.id,
            name = rqUnit.name ?: available?.name,
            scheme = rqUnit.scheme ?: available?.scheme
        )
    } ?: available

fun updateObservation(received: RequestObservation, available: RecordObservation?): RecordObservation =
    received.let { rqObservation ->
        RecordObservation(
            id = rqObservation.id,
            unit = updateObservationUnit(
                rqObservation.unit,
                available?.unit
            ),
            measure = rqObservation.measure ?: available?.measure,
            notes = rqObservation.notes ?: available?.notes
        )
    }

fun updateValueTax(received: RequestValueTax?, available: RecordValueTax?): RecordValueTax? =
    received?.let { rqValue ->
        RecordValueTax(
            amount = rqValue.amount ?: available?.amount,
            valueAddedTaxIncluded = rqValue.valueAddedTaxIncluded ?: available?.valueAddedTaxIncluded,
            amountNet = rqValue.amountNet ?: available?.amountNet,
            currency = rqValue.currency ?: available?.currency
        )
    } ?: available

fun updateRelease(releaseId: String, requestRelease: RequestRelease, dbRelease: Record): Record =
    requestRelease.let { rqRelease ->
        Record(
            id = releaseId,
            ocid = rqRelease.ocid ?: dbRelease.ocid,
            date = rqRelease.date ?: dbRelease.date,
            relatedProcesses = updateStrategy(
                receivedElements = rqRelease.relatedProcesses.toList(),
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = dbRelease.relatedProcesses.toList(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateRelatedProcess
            ).toMutableList(),
            bids = updateBidsObject(
                rqRelease.bids,
                dbRelease.bids
            ),
            awards = updateStrategy(
                receivedElements = rqRelease.awards,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = dbRelease.awards,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateAward
            ),
            contracts = updateStrategy(
                receivedElements = rqRelease.contracts,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = dbRelease.contracts,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateContract
            ),
            hasPreviousNotice = rqRelease.hasPreviousNotice ?: dbRelease.hasPreviousNotice,
            initiationType = rqRelease.initiationType ?: dbRelease.initiationType,
            parties = updateStrategy(
                receivedElements = rqRelease.parties.toList(),
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = dbRelease.parties.toList(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateOrganization
            ).toMutableList(),
            purposeOfNotice = updatePurposeOfNotice(
                rqRelease.purposeOfNotice,
                dbRelease.purposeOfNotice
            ),
            tag = updateTag(
                rqRelease.tag,
                dbRelease.tag
            ),
            tender = updateReleaseTender(
                rqRelease.tender,
                dbRelease.tender
            ),
            agreedMetrics = updateStrategy(
                receivedElements = rqRelease.agreedMetrics,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = dbRelease.agreedMetrics,
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateAgreedMetric
            ),
            cpid = rqRelease.cpid ?: dbRelease.cpid,
            planning = updatePlanning(
                rqRelease.planning,
                dbRelease.planning
            )
        )
    }

fun updateTag(received: List<Tag>, available: List<Tag>): List<Tag> = received.mapIfNotEmpty { it } ?: available
fun updatePlanning(
    received: RequestPlanning?,
    available: RecordPlanning?
): RecordPlanning? =
    received?.let {
        RecordPlanning(
            implementation = updateImplementation(
                it.implementation,
                available?.implementation
            ),
            rationale = it.rationale ?: available?.rationale,
            budget = updateBudget(
                it.budget,
                available?.budget
            )
        )
    } ?: available

fun updateImplementation(
    received: RequestImplementation?,
    available: RecordImplementation?
): RecordImplementation? =
    received?.let {
        RecordImplementation(
            transactions = updateStrategy(
                receivedElements = it.transactions,
                keyExtractorForReceivedElement = { it.id!! },
                availableElements = available?.transactions.orEmpty(),
                keyExtractorForAvailableElement = { it.id!! },
                block = ::updateTransaction
            )
        )
    } ?: available

fun updateTransaction(
    received: RequestTransaction,
    available: RecordTransaction?
): RecordTransaction =
    received.let {
        RecordTransaction(
            id = it.id ?: available?.id,
            type = it.type ?: available?.type,
            value = updateValue(
                it.value,
                available?.value
            ),
            executionPeriod = updateExecutionPeriod(
                it.executionPeriod,
                available?.executionPeriod
            ),
            relatedContractMilestone = it.relatedContractMilestone ?: available?.relatedContractMilestone
        )
    }

fun updateExecutionPeriod(
    received: RequestExecutionPeriod?,
    available: RecordExecutionPeriod?
): RecordExecutionPeriod? =
    received?.let {
        RecordExecutionPeriod(
            durationInDays = it.durationInDays ?: available?.durationInDays
        )
    } ?: available

fun updateBudget(received: RequestPlanningBudget?, available: RecordPlanningBudget?): RecordPlanningBudget? =
    received?.let {
        RecordPlanningBudget(
            description = it.description ?: available?.description,
            amount = it.amount ?: available?.amount,
            isEuropeanUnionFunded = it.isEuropeanUnionFunded ?: available?.isEuropeanUnionFunded,
            budgetSource = updateStrategy(
                receivedElements = it.budgetSource,
                keyExtractorForReceivedElement = { it.budgetBreakdownID!! },
                availableElements = available?.budgetSource.orEmpty(),
                keyExtractorForAvailableElement = { it.budgetBreakdownID!! },
                block = ::updatePlanningBudgetSource
            ),
            budgetAllocation = updateStrategy(
                receivedElements = it.budgetAllocation,
                keyExtractorForReceivedElement = { it.budgetBreakdownID!! },
                availableElements = available?.budgetAllocation.orEmpty(),
                keyExtractorForAvailableElement = { it.budgetBreakdownID!! },
                block = ::updateBudgetAllocation
            ),
            budgetBreakdown = updateStrategy(
                receivedElements = it.budgetBreakdown,
                keyExtractorForReceivedElement = { it.id },
                availableElements = available?.budgetBreakdown.orEmpty(),
                keyExtractorForAvailableElement = { it.id },
                block = ::updateBudgetBreakdown
            )
        )
    } ?: available

fun updateBudgetBreakdown(received: RequestBudgetBreakdown, available: RecordBudgetBreakdown?): RecordBudgetBreakdown =
    received.let {
        RecordBudgetBreakdown(
            id = it.id,
            amount = it.amount ?: available?.amount,
            period = updatePeriod(
                it.period,
                available?.period
            ),
            description = it.description ?: available?.description,
            europeanUnionFunding = updateEuropeanUnionFunding(
                it.europeanUnionFunding,
                available?.europeanUnionFunding
            ),
            sourceParty = it.sourceParty?.let {
                updateOrganizationReference(
                    it,
                    available?.sourceParty
                )
            } ?: available?.sourceParty
        )
    }

fun updateEuropeanUnionFunding(
    received: RequestEuropeanUnionFunding?,
    available: RecordEuropeanUnionFunding?
): RecordEuropeanUnionFunding? =
    received?.let {
        RecordEuropeanUnionFunding(
            projectIdentifier = it.projectIdentifier ?: available?.projectIdentifier,
            uri = it.uri ?: available?.uri,
            projectName = it.projectName ?: available?.projectName
        )
    } ?: available

fun updateBudgetAllocation(
    received: RequestBudgetAllocation,
    available: RecordBudgetAllocation?
): RecordBudgetAllocation =
    received.let {
        RecordBudgetAllocation(
            budgetBreakdownID = it.budgetBreakdownID ?: available?.budgetBreakdownID,
            amount = it.amount ?: available?.amount,
            relatedItem = it.relatedItem ?: available?.relatedItem,
            period = updatePeriod(
                it.period,
                available?.period
            )
        )
    }

fun updatePlanningBudgetSource(
    received: RequestPlanningBudgetSource,
    available: RecordPlanningBudgetSource?
): RecordPlanningBudgetSource =
    received.let {
        RecordPlanningBudgetSource(
            budgetBreakdownID = it.budgetBreakdownID ?: available?.budgetBreakdownID,
            amount = it.amount ?: available?.amount,
            currency = it.currency ?: available?.currency
        )
    }

fun updatePurposeOfNotice(
    received: RequestPurposeOfNotice?,
    available: RecordPurposeOfNotice?
): RecordPurposeOfNotice? =
    received?.let { rqPurposeOfNotice ->
        RecordPurposeOfNotice(
            isACallForCompetition = rqPurposeOfNotice.isACallForCompetition ?: available?.isACallForCompetition
        )
    } ?: available

fun updateBidsObject(received: RequestBids?, available: RecordBids?): RecordBids? = received?.let { rqBids ->
    RecordBids(
        statistics = updateStrategy(
            receivedElements = rqBids.statistics,
            keyExtractorForReceivedElement = { it.id!! },
            availableElements = available?.statistics.orEmpty(),
            keyExtractorForAvailableElement = { it.id!! },
            block = ::updateBidsStatistic
        ),
        details = updateStrategy(
            receivedElements = rqBids.details,
            keyExtractorForReceivedElement = { it.id!! },
            availableElements = available?.details.orEmpty(),
            keyExtractorForAvailableElement = { it.id!! },
            block = ::updateBid
        )
    )
}

fun updateBidsStatistic(received: RequestBidsStatistic, available: RecordBidsStatistic?): RecordBidsStatistic =
    received.let {
        RecordBidsStatistic(
            id = it.id ?: available?.id,
            date = it.date ?: available?.date,
            value = it.value ?: available?.value,
            notes = it.notes ?: available?.notes,
            measure = it.measure ?: available?.measure,
            relatedLot = it.relatedLot ?: available?.relatedLot
        )
    }

fun <R, A, K> updateStrategy(
    receivedElements: List<R>,
    keyExtractorForReceivedElement: (R) -> K,
    availableElements: List<A>,
    keyExtractorForAvailableElement: (A) -> K,
    block: (R, A?) -> A
): List<A> {
    val receivedElementsById = receivedElements.associateBy { keyExtractorForReceivedElement(it) }
    val availableElementsIds = availableElements.toSetBy { keyExtractorForAvailableElement(it) }

    val updatedElements: MutableList<A> = mutableListOf()

    availableElements.forEach { availableElement ->
        val id = keyExtractorForAvailableElement(availableElement)
        val element = receivedElementsById[id]
            ?.let { receivedElement ->
                block(receivedElement, availableElement)
            }
            ?: availableElement
        updatedElements.add(element)
    }

    val newIds = receivedElementsById.keys - availableElementsIds
    newIds.forEach { id ->
        val receivedElement = receivedElementsById.getValue(id)
        val element = block(receivedElement, null)
        updatedElements.add(element)
    }

    return updatedElements
}
