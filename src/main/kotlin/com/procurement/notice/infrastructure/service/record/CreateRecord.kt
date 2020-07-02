package com.procurement.notice.infrastructure.service.record

import com.procurement.notice.infrastructure.dto.enObservationtity.awards.RecordAward
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
import com.procurement.notice.infrastructure.dto.entity.parties.RecordMainEconomicActivity
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
import com.procurement.notice.infrastructure.dto.request.parties.RequestMainEconomicActivity
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
import com.procurement.notice.model.ocds.RecordParticipationFee
import com.procurement.notice.model.ocds.RequestParticipationFee
import com.procurement.notice.model.ocds.Requirement
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value

fun createLot(received: RequestLot): RecordLot =
    RecordLot(
        id = received.id,
        status = received.status,
        statusDetails = received.statusDetails,
        value = received.value,
        title = received.title,
        description = received.description,
        recurrentProcurement = createRecurrentProcurement(received.recurrentProcurement),
        contractPeriod = received.contractPeriod?.let { createContractPeriod(it) },
        internalId = received.internalId,
        options = createOptions(received.options),
        placeOfPerformance = received.placeOfPerformance?.let { createPlaceOfPerformance(it) },
        renewals = createRenewals(received.renewals),
        variants = createVariants(received.variants)
    )

fun createVariants(received: List<RequestVariant>): List<RecordVariant> =
    received.mapIfNotEmpty { requestVariant ->
        RecordVariant(
            hasVariants = requestVariant.hasVariants,
            variantDetails = requestVariant.variantDetails
        )
    }.orEmpty()

fun createRenewals(received: List<RequestRenewal>): List<RecordRenewal> =
    received.mapIfNotEmpty { requestRenewal ->
        RecordRenewal(
            hasRenewals = requestRenewal.hasRenewals,
            renewalConditions = requestRenewal.renewalConditions,
            maxNumber = requestRenewal.maxNumber
        )
    }.orEmpty()

fun createPlaceOfPerformance(received: RequestPlaceOfPerformance): RecordPlaceOfPerformance =
    RecordPlaceOfPerformance(
        address = received.address?.let { createAddress(it) },
        description = received.description,
        nutScode = received.nutScode
    )

fun createAddress(received: RequestAddress): RecordAddress =
    RecordAddress(
        streetAddress = received.streetAddress,
        addressDetails = received.addressDetails
            ?.let { addressDetails ->
                RecordAddressDetails(
                    country = addressDetails.country
                        .let { country ->
                            RecordCountryDetails(
                                id = country.id,
                                scheme = country.scheme,
                                description = country.description,
                                uri = country.uri
                            )
                        },
                    region = addressDetails.region
                        .let { region ->
                            RecordRegionDetails(
                                id = region.id,
                                scheme = region.scheme,
                                description = region.description,
                                uri = region.uri
                            )
                        },
                    locality = addressDetails.locality
                        .let { locality ->
                            RecordLocalityDetails(
                                id = locality.id,
                                scheme = locality.scheme,
                                description = locality.description,
                                uri = locality.uri
                            )
                        }
                )
            },
        postalCode = received.postalCode
    )

fun createOptions(received: List<RequestOption>): List<RecordOption> =
    received.mapIfNotEmpty { requestOption ->
        RecordOption(
            hasOptions = requestOption.hasOptions,
            optionDetails = requestOption.optionDetails
        )
    }.orEmpty()

fun createContractPeriod(received: RequestPeriod): RecordPeriod =
    RecordPeriod(
        startDate = received.startDate,
        endDate = received.endDate,
        durationInDays = received.durationInDays,
        maxExtentDate = received.maxExtentDate
    )

fun createRecurrentProcurement(received: List<RequestRecurrentProcurement>): List<RecordRecurrentProcurement> =
    received.mapIfNotEmpty { recurrentProcurement ->
        RecordRecurrentProcurement(
            isRecurrent = recurrentProcurement.isRecurrent,
            description = recurrentProcurement.description,
            dates = recurrentProcurement.dates
                .map { date -> createPeriod(date) }
        )
    }.orEmpty()

fun createItem(received: RequestItem): RecordItem =
    RecordItem(
        id = received.id,
        description = received.description,
        internalId = received.internalId,
        unit = received.unit?.let { createUnit(it) },
        quantity = received.quantity,
        classification = received.classification?.let { createClassification(it) },
        additionalClassifications = received.additionalClassifications
            .map { createClassification(it) },
        deliveryAddress = received.deliveryAddress?.let { createAddress(it) },
        relatedLot = received.relatedLot
    )

fun createClassification(received: RequestClassification): RecordClassification =
    RecordClassification(
        id = received.id,
        scheme = received.scheme,
        uri = received.uri,
        description = received.description
    )

fun createUnit(received: RequestUnit): RecordUnit =
    RecordUnit(
        id = received.id,
        scheme = received.scheme,
        name = received.name,
        uri = received.uri,
        value = received.value
    )

fun createCriteria(received: RequestCriteria): RecordCriteria =
    RecordCriteria(
        id = received.id,
        description = received.description,
        title = received.title,
        relatesTo = received.relatesTo,
        relatedItem = received.relatedItem,
        source = received.source,
        requirementGroups = received.requirementGroups
            .map { createRequirementGroup(it) }
    )

fun createRequirementGroup(received: RequestRequirementGroup): RecordRequirementGroup =
    RecordRequirementGroup(
        id = received.id,
        description = received.description,
        requirements = received.requirements
            .map { createRequirement(it) }
    )

fun createRequirement(received: Requirement): Requirement =
    Requirement(
        id = received.id,
        description = received.description,
        title = received.title,
        value = received.value,
        period = received.period,
        dataType = received.dataType
    )

fun createConversion(received: RequestConversion): RecordConversion =
    RecordConversion(
        id = received.id,
        description = received.description,
        relatedItem = received.relatedItem,
        relatesTo = received.relatesTo,
        rationale = received.rationale,
        coefficients = received.coefficients
            .map { createCoefficient(it) }
    )

fun createCoefficient(received: RequestCoefficient): RecordCoefficient =
    RecordCoefficient(
        id = received.id,
        value = received.value,
        coefficient = received.coefficient
    )

fun createLotGroup(received: RequestLotGroup): RecordLotGroup =
    RecordLotGroup(
        id = received.id,
        relatedLots = received.relatedLots,
        maximumValue = received.maximumValue,
        optionToCombine = received.optionToCombine
    )

fun createPeriod(received: RequestPeriod): RecordPeriod =
    RecordPeriod(
        startDate = received.startDate,
        endDate = received.endDate,
        durationInDays = received.durationInDays,
        maxExtentDate = received.maxExtentDate
    )

fun createRecordEnquiry(received: RequestRecordEnquiry): RecordRecordEnquiry =
    RecordRecordEnquiry(
        id = received.id,
        relatedItem = received.relatedItem,
        description = received.description,
        title = received.title,
        relatedLot = received.relatedLot,
        date = received.date,
        answer = received.answer,
        author = received.author?.let { createAuthor(it) },
        dateAnswered = received.dateAnswered
    )

fun createAuthor(received: RequestOrganizationReference): RecordOrganizationReference =
    RecordOrganizationReference(
        id = received.id,
        name = received.name,
        buyerProfile = received.buyerProfile,
        address = received.address?.let { createAddress(it) },
        details = received.details?.let { createDetails(it) },
        identifier = received.identifier?.let { createIdentifier(it) },
        contactPoint = received.contactPoint?.let { createContactPoint(it) },
        additionalIdentifiers = received.additionalIdentifiers
            .map { createIdentifier(it) },
        persones = received.persones
            .map { createPerson(it) }
    )

fun createPerson(received: RequestPerson): RecordPerson =
    RecordPerson(
        id = received.id,
        title = received.title,
        name = received.name,
        identifier = createIdentifier(received.identifier),
        businessFunctions = received.businessFunctions
            .map { createBusinessFunction(it) }
    )

fun createBusinessFunction(received: RequestBusinessFunction): RecordBusinessFunction =
    RecordBusinessFunction(
        id = received.id,
        period = createPeriod(received.period),
        documents = received.documents
            .map { createDocumentBF(it) },
        type = received.type,
        jobTitle = received.jobTitle
    )

fun createDocumentBF(received: RequestDocumentBF): RecordDocumentBF =
    RecordDocumentBF(
        id = received.id,
        title = received.title,
        url = received.url,
        description = received.description,
        dateModified = received.dateModified,
        datePublished = received.datePublished,
        documentType = received.documentType
    )

fun createContactPoint(received: RequestContactPoint): RecordContactPoint =
    RecordContactPoint(
        name = received.name,
        url = received.url,
        faxNumber = received.faxNumber,
        telephone = received.telephone,
        email = received.email
    )

fun createIdentifier(received: RequestIdentifier): RecordIdentifier =
    RecordIdentifier(
        id = received.id,
        scheme = received.scheme,
        uri = received.uri,
        legalName = received.legalName
    )

fun createDetails(received: RequestDetails): RecordDetails =
    RecordDetails(
        typeOfBuyer = received.typeOfBuyer,
        bankAccounts = received.bankAccounts
            .map { createBankAccount(it) },
        isACentralPurchasingBody = received.isACentralPurchasingBody,
        legalForm = received.legalForm?.let { createLegalForm(it) },
        mainEconomicActivities = received.mainEconomicActivities.map { createMainEconomicActivity(it) },
        mainGeneralActivity = received.mainGeneralActivity,
        mainSectoralActivity = received.mainSectoralActivity,
        nutsCode = received.nutsCode,
        permits = received.permits
            .map { createPermits(it) },
        scale = received.scale,
        typeOfSupplier = received.typeOfSupplier
    )

fun createMainEconomicActivity(received: RequestMainEconomicActivity) =
    RecordMainEconomicActivity(
        id = received.id,
        uri = received.uri,
        scheme = received.scheme,
        description = received.description
    )

fun createPermits(received: RequestPermits): RecordPermits =
    RecordPermits(
        id = received.id,
        scheme = received.scheme,
        url = received.url,
        permitDetails = received.permitDetails?.let { createPermitDetails(it) }
    )

fun createPermitDetails(received: RequestPermitDetails): RecordPermitDetails =
    RecordPermitDetails(
        issuedBy = received.issuedBy?.let { requestIssue ->
            RecordIssue(
                id = requestIssue.id,
                name = requestIssue.name
            )
        },
        issuedThought = received.issuedThought?.let { requestIssue ->
            RecordIssue(
                id = requestIssue.id,
                name = requestIssue.name
            )
        },
        validityPeriod = received.validityPeriod?.let { createPeriod(it) }
    )

fun createLegalForm(received: RequestLegalForm): RecordLegalForm =
    RecordLegalForm(
        id = received.id,
        description = received.description,
        uri = received.uri,
        scheme = received.scheme
    )

fun createBankAccount(received: RequestBankAccount): RecordBankAccount =
    RecordBankAccount(
        identifier = createAccountIdentifier(received.identifier),
        address = received.address?.let { createAddress(it) },
        description = received.description,
        accountIdentification = received.accountIdentification?.let { createAccountIdentifier(it) },
        additionalAccountIdentifiers = received.additionalAccountIdentifiers
            .map { createAccountIdentifier(it) },
        bankName = received.bankName
    )

fun createAccountIdentifier(received: RequestAccountIdentifier): RecordAccountIdentifier =
    RecordAccountIdentifier(
        id = received.id,
        scheme = received.scheme
    )

fun createChanges(received: List<RequestChange>): List<RecordChange> =
    received.mapIfNotEmpty { rqChange ->
        RecordChange(
            property = rqChange.property,
            formerValue = rqChange.formerValue
        )
    }.orEmpty()

fun createDocument(received: RequestDocument): RecordDocument =
    RecordDocument(
        id = received.id,
        relatedLots = received.relatedLots,
        description = received.description,
        url = received.url,
        documentType = received.documentType,
        datePublished = received.datePublished,
        language = received.language,
        dateModified = received.dateModified,
        format = received.format,
        relatedConfirmations = received.relatedConfirmations,
        title = received.title
    )

fun createElectronicAuctions(received: RequestElectronicAuctions): RecordElectronicAuctions =
    RecordElectronicAuctions(
        details = received.details
            .map { createElectronicAuctionsDetails(it) }
    )

fun createElectronicAuctionsDetails(received: RequestElectronicAuctionsDetails): RecordElectronicAuctionsDetails =
    RecordElectronicAuctionsDetails(
        id = received.id,
        relatedLot = received.relatedLot,
        auctionPeriod = received.auctionPeriod,
        electronicAuctionModalities = createElectronicAuctionModalities(received.electronicAuctionModalities),
        electronicAuctionProgress = received.electronicAuctionProgress
            .map { createElectronicAuctionProgress(it) },
        electronicAuctionResult = createElectronicAuctionResult(received.electronicAuctionResult)
    )

fun createElectronicAuctionResult(received: List<RequestElectronicAuctionResult>): List<RecordElectronicAuctionResult> =
    received.mapIfNotEmpty { rqElectronicAuctionResult ->
        RecordElectronicAuctionResult(
            relatedBid = rqElectronicAuctionResult.relatedBid,
            value = rqElectronicAuctionResult.value
        )
    }.orEmpty()

fun createElectronicAuctionModalities(received: List<RequestElectronicAuctionModalities>): List<RecordElectronicAuctionModalities> =
    received.mapIfNotEmpty { rqElectronicAuctionModality ->
        RecordElectronicAuctionModalities(
            url = rqElectronicAuctionModality.url,
            eligibleMinimumDifference = rqElectronicAuctionModality.eligibleMinimumDifference
        )
    }.orEmpty()

fun createElectronicAuctionProgress(received: RequestElectronicAuctionProgress): RecordElectronicAuctionProgress =
    RecordElectronicAuctionProgress(
        id = received.id,
        period = received.period?.let { createPeriod(it) },
        breakdown = createElectronicAuctionProgressBreakdown(received.breakdown)
    )

fun createElectronicAuctionProgressBreakdown(received: List<RequestElectronicAuctionProgressBreakdown>): List<RecordElectronicAuctionProgressBreakdown> =
    received.mapIfNotEmpty { rqBreakdown ->
        RecordElectronicAuctionProgressBreakdown(
            relatedBid = rqBreakdown.relatedBid,
            value = rqBreakdown.value,
            status = rqBreakdown.status,
            dateMet = rqBreakdown.dateMet
        )
    }.orEmpty()

fun createReleaseTender(received: RequestTender): RecordTender =
    RecordTender(
        id = received.id!!,
        status = received.status,
        auctionPeriod = received.auctionPeriod?.let { createPeriod(it) },
        title = received.title,
        description = received.description,
        documents = received.documents
            .map { createDocument(it) },
        statusDetails = received.statusDetails,
        amendments = received.amendments
            .map { createAmendment(it) },
        submissionMethodRationale = received.submissionMethodRationale,
        submissionMethodDetails = received.submissionMethodDetails,
        awardCriteria = received.awardCriteria,
        awardCriteriaDetails = received.awardCriteriaDetails,
        awardPeriod = received.awardPeriod?.let { createPeriod(it) },
        conversions = received.conversions
            .map { createConversion(it) },
        criteria = received.criteria
            .map { createCriteria(it) },
        electronicAuctions = received.electronicAuctions?.let { createElectronicAuctions(it) },
        enquiries = received.enquiries
            .map { createRecordEnquiry(it) }
            .toMutableList(),
        enquiryPeriod = received.enquiryPeriod?.let { createPeriod(it) },
        hasEnquiries = received.hasEnquiries,
        items = received.items
            .map { createItem(it) },
        lotGroups = received.lotGroups
            .map { createLotGroup(it) },
        lots = received.lots
            .map { createLot(it) },
        procurementMethodModalities = received.procurementMethodModalities,
        requiresElectronicCatalogue = received.requiresElectronicCatalogue,
        standstillPeriod = received.standstillPeriod?.let { createPeriod(it) },
        submissionMethod = received.submissionMethod,
        tenderPeriod = received.tenderPeriod?.let { createPeriod(it) },
        value = received.value?.let { createValue(it) },
        milestones = received.milestones
            .map { createMilestone(it) },
        classification = received.classification?.let { createClassification(it) },
        amendment = received.amendment?.let { createAmendment(it) },
        contractPeriod = received.contractPeriod?.let { createContractPeriod(it) },
        procurementMethodDetails = received.procurementMethodDetails,
        legalBasis = received.legalBasis,
        mainProcurementCategory = received.mainProcurementCategory,
        eligibilityCriteria = received.eligibilityCriteria,
        minValue = received.minValue?.let { createValue(it) },
        numberOfTenderers = received.numberOfTenderers,
        procurementMethod = received.procurementMethod,
        procurementMethodRationale = received.procurementMethodRationale,
        additionalProcurementCategories = received.additionalProcurementCategories,
        procurementMethodAdditionalInfo = received.procurementMethodAdditionalInfo,
        submissionLanguages = received.submissionLanguages,
        tenderers = received.tenderers
            .map { createOrganizationReference(it) },
        acceleratedProcedure = received.acceleratedProcedure?.let { createAcceleratedProcedure(it) },
        designContest = received.designContest?.let { createDesignContest(it) },
        dynamicPurchasingSystem = received.dynamicPurchasingSystem?.let { createDynamicPurchasingSystem(it) },
        electronicWorkflows = received.electronicWorkflows?.let { createElectronicWorkflows(it) },
        framework = received.framework?.let { createFramework(it) },
        jointProcurement = received.jointProcurement?.let { createJointProcurement(it) },
        lotDetails = received.lotDetails?.let { createLotDetails(it) },
        objectives = received.objectives?.let { createObjectives(it) },
        participationFees = createParticipationFee(received.participationFees),
        procedureOutsourcing = received.procedureOutsourcing?.let { createProcedureOutsourcing(it) },
        procuringEntity = received.procuringEntity?.let { createOrganizationReference(it) },
        reviewParties = received.reviewParties
            .map { createOrganizationReference(it) },
        reviewPeriod = received.reviewPeriod?.let { createPeriod(it) },
        secondStage = received.secondStage
    )

fun createProcedureOutsourcing(received: RequestProcedureOutsourcing): RecordProcedureOutsourcing =
    RecordProcedureOutsourcing(
        procedureOutsourced = received.procedureOutsourced,
        outsourcedTo = received.outsourcedTo?.let { createOrganization(it) }
    )

fun createParticipationFee(received: List<RequestParticipationFee>): List<RecordParticipationFee> =
    received.mapIfNotEmpty {
        RecordParticipationFee(
            type = it.type,
            value = it.value,
            description = it.description,
            methodOfPayment = it.methodOfPayment
        )
    }.orEmpty()

fun createObjectives(received: RequestObjectives): RecordObjectives =
    RecordObjectives(
        types = received.types,
        additionalInformation = received.additionalInformation
    )

fun createLotDetails(received: RequestLotDetails): RecordLotDetails =
    RecordLotDetails(
        maximumLotsAwardedPerSupplier = received.maximumLotsAwardedPerSupplier,
        maximumLotsBidPerSupplier = received.maximumLotsBidPerSupplier
    )

fun createJointProcurement(received: RequestJointProcurement): RecordJointProcurement =
    RecordJointProcurement(
        isJointProcurement = received.isJointProcurement,
        country = received.country
    )

fun createFramework(received: RequestFramework): RecordFramework =
    RecordFramework(
        isAFramework = received.isAFramework,
        additionalBuyerCategories = received.additionalBuyerCategories,
        exceptionalDurationRationale = received.exceptionalDurationRationale,
        maxSuppliers = received.maxSuppliers,
        typeOfFramework = received.typeOfFramework
    )

fun createElectronicWorkflows(received: RequestElectronicWorkflows): RecordElectronicWorkflows =
    RecordElectronicWorkflows(
        useOrdering = received.useOrdering,
        acceptInvoicing = received.acceptInvoicing,
        usePayment = received.usePayment
    )

fun createDynamicPurchasingSystem(received: RequestDynamicPurchasingSystem): RecordDynamicPurchasingSystem =
    RecordDynamicPurchasingSystem(
        hasDynamicPurchasingSystem = received.hasDynamicPurchasingSystem,
        hasOutsideBuyerAccess = received.hasOutsideBuyerAccess,
        noFurtherContracts = received.noFurtherContracts
    )

fun createDesignContest(received: RequestDesignContest): RecordDesignContest =
    RecordDesignContest(
        hasPrizes = received.hasPrizes,
        juryDecisionBinding = received.juryDecisionBinding,
        juryMembers = received.juryMembers
            .map { createOrganizationReference(it) },
        participants = received.participants
            .map { createOrganizationReference(it) },
        paymentsToParticipants = received.paymentsToParticipants,
        prizes = received.prizes
            .map { createItem(it) },
        serviceContractAward = received.serviceContractAward
    )

fun createAcceleratedProcedure(received: RequestAcceleratedProcedure): RecordAcceleratedProcedure =
    RecordAcceleratedProcedure(
        isAcceleratedProcedure = received.isAcceleratedProcedure,
        acceleratedProcedureJustification = received.acceleratedProcedureJustification
    )

fun createValue(received: Value): Value =
    Value(
        amount = received.amount,
        currency = received.currency,
        amountNet = received.amountNet,
        valueAddedTaxIncluded = received.valueAddedTaxIncluded
    )

fun createOrganization(received: RequestOrganization): RecordOrganization =
    RecordOrganization(
        id = received.id,
        name = received.name,
        details = received.details?.let { createDetails(it) },
        identifier = received.identifier?.let { createIdentifier(it) },
        persones = received.persones
            .map { createPerson(it) },
        buyerProfile = received.buyerProfile,
        additionalIdentifiers = received.additionalIdentifiers
            .map { createIdentifier(it) },
        contactPoint = received.contactPoint?.let { createContactPoint(it) },
        address = received.address?.let { createAddress(it) },
        roles = received.roles
    )

fun createSubmissionDetail(received: RequestSubmissionDetail): RecordSubmissionDetail =
    RecordSubmissionDetail(
        id = received.id,
        status = received.status,
        date = received.date,
        documents = received.documents
            .map { createDocument(it) },
        candidates = received.candidates
            .map { createCandidate(it) },
        requirementResponses = received.requirementResponses
            .map { createRequirementResponse(it) }
        )

fun createQualification(received: RequestQualification): RecordQualification =
    RecordQualification(
        id = received.id,
        status = received.status,
        statusDetails = received.statusDetails,
        date = received.date,
        relatedSubmission = received.relatedSubmission,
        scoring = received.scoring,
        requirementResponses = received.requirementResponses
            .map { createRequirementResponse(it) }
    )

fun createCandidate(received: RequestCandidate): RecordCandidate =
    RecordCandidate(
        id = received.id,
        name = received.name
    )

fun createAward(received: RequestAward): RecordAward =
    RecordAward(
        id = received.id,
        status = received.status,
        statusDetails = received.statusDetails,
        title = received.title,
        description = received.description,
        value = received.value,
        relatedBid = received.relatedBid,
        relatedLots = received.relatedLots,
        date = received.date,
        contractPeriod = received.contractPeriod?.let { createContractPeriod(it) },
        weightedValue = received.weightedValue,
        reviewProceedings = received.reviewProceedings?.let { createReviewProceedings(it) },
        items = received.items
            .map { createItem(it) },
        amendments = received.amendments
            .map { createAmendment(it) },
        amendment = received.amendment?.let { createAmendment(it) },
        documents = received.documents
            .map { createDocument(it) },
        requirementResponses = received.requirementResponses
            .map { createRequirementResponse(it) },
        suppliers = received.suppliers
            .map { createOrganizationReference(it) }
    )

fun createAmendment(received: RequestAmendment): RecordAmendment =
    RecordAmendment(
        id = received.id,
        date = received.date,
        description = received.description,
        rationale = received.rationale,
        documents = received.documents
            .map { createDocument(it) },
        relatedLots = received.relatedLots,
        releaseID = received.releaseID,
        changes = createChanges(received.changes),
        amendsReleaseID = received.amendsReleaseID,
        type = received.type,
        status = received.status,
        relatedItem = received.relatedItem,
        relatesTo = received.relatesTo
    )

fun createReviewProceedings(received: RequestReviewProceedings): RecordReviewProceedings? =
    RecordReviewProceedings(
        buyerProcedureReview = received.buyerProcedureReview,
        legalProcedures = received.legalProcedures
            .map { createLegalProceeding(it) },
        reviewBodyChallenge = received.reviewBodyChallenge
    )

fun createLegalProceeding(received: RequestLegalProceedings): RecordLegalProceedings =
    RecordLegalProceedings(
        id = received.id,
        title = received.title,
        uri = received.uri
    )

fun createRequirementResponse(received: RequestRequirementResponse): RecordRequirementResponse =
    RecordRequirementResponse(
        id = received.id,
        title = received.title,
        value = received.value,
        description = received.description,
        period = received.period?.let { createPeriod(it) },
        relatedTenderer = received.relatedTenderer
            ?.let { createOrganizationReference(it) },
        requirement = received.requirement
            ?.let { createRequirement(it) },
        responder = received.responder
            ?.let { createResponder(it) }
    )

fun createRequirement(received: RequestRequirementReference): RecordRequirementReference? =
    RecordRequirementReference(
        id = received.id,
        title = received.title
    )

fun createResponder(received: RequestResponder) = RecordResponder(
    id = received.id,
    name = received.name
)

fun createOrganizationReference(received: RequestOrganizationReference): RecordOrganizationReference =
    RecordOrganizationReference(
        id = received.id,
        address = received.address
            ?.let { createAddress(it) },
        contactPoint = received.contactPoint
            ?.let { createContactPoint(it) },
        additionalIdentifiers = received.additionalIdentifiers
            .map { createIdentifier(it) },
        persones = received.persones
            .map { createPerson(it) },
        identifier = received.identifier
            ?.let { createIdentifier(it) },
        name = received.name,
        details = received.details
            ?.let { createDetails(it) },
        buyerProfile = received.buyerProfile
    )

fun createBid(received: RequestBid): RecordBid =
    RecordBid(
        id = received.id,
        value = received.value,
        relatedLots = received.relatedLots,
        documents = received.documents
            .map { createDocument(it) },
        date = received.date,
        requirementResponses = received.requirementResponses
            .map { createRequirementResponse(it) },
        statusDetails = received.statusDetails,
        status = received.status,
        tenderers = received.tenderers
            .map { createOrganizationReference(it) }
    )

fun createContract(received: RequestContract): RecordContract =
    RecordContract(
        id = received.id,
        status = received.status,
        statusDetails = received.statusDetails,
        requirementResponses = received.requirementResponses
            .map { createRequirementResponse(it) },
        date = received.date,
        documents = received.documents
            .map { createDocument(it) },
        relatedLots = received.relatedLots,
        value = received.value
            ?.let { createValueTax(it) },
        title = received.title,
        period = received.period
            ?.let { createPeriod(it) },
        description = received.description,
        amendment = received.amendment?.let { createAmendment(it) },
        amendments = received.amendments
            .map { createAmendment(it) },
        items = received.items
            .map { createItem(it) },
        classification = received.classification
            ?.let { createClassification(it) },
        agreedMetrics = received.agreedMetrics
            .map { createAgreedMetric(it) },
        awardId = received.awardId,
        budgetSource = received.budgetSource
            .map { createBudgetSource(it) },
        confirmationRequests = received.confirmationRequests
            .map { createConfirmationRequest(it) },
        confirmationResponses = received.confirmationResponses
            .map { createConfirmationResponse(it) },
        countryOfOrigin = received.countryOfOrigin,
        dateSigned = received.dateSigned,
        extendsContractId = received.extendsContractId,
        isFrameworkOrDynamic = received.isFrameworkOrDynamic,
        lotVariant = received.lotVariant,
        milestones = received.milestones
            .map { createMilestone(it) },
        relatedProcesses = received.relatedProcesses
            .map { createRelatedProcess(it) },
        valueBreakdown = received.valueBreakdown
            .map { createValueBreakdown(it) }
    )

fun createValueBreakdown(received: RequestValueBreakdown): RecordValueBreakdown =
    RecordValueBreakdown(
        id = received.id,
        type = received.type,
        description = received.description,
        amount = received.amount,
        estimationMethod = received.estimationMethod
    )

fun createRelatedProcess(received: RequestRelatedProcess): RecordRelatedProcess =
    RecordRelatedProcess(
        id = received.id,
        scheme = received.scheme,
        identifier = received.identifier,
        uri = received.uri,
        relationship = received.relationship
    )

fun createMilestone(received: RequestMilestone): RecordMilestone =
    RecordMilestone(
        id = received.id,
        title = received.title,
        description = received.description,
        type = received.type,
        status = received.status,
        dateMet = received.dateMet,
        dateModified = received.dateModified,
        additionalInformation = received.additionalInformation,
        dueDate = received.dueDate,
        relatedItems = received.relatedItems,
        relatedParties = received.relatedParties
            .map { createRelatedParty(it) }
    )

fun createRelatedParty(received: RequestRelatedParty): RecordRelatedParty =
    RecordRelatedParty(
        id = received.id,
        name = received.name
    )

fun createConfirmationResponse(received: RequestConfirmationResponse): RecordConfirmationResponse =
    RecordConfirmationResponse(
        id = received.id,
        value = received.value?.let { createConfirmationResponseValue(it) },
        request = received.request
    )

fun createConfirmationResponseValue(received: RequestConfirmationResponseValue): RecordConfirmationResponseValue =
    RecordConfirmationResponseValue(
        id = received.id,
        name = received.name,
        date = received.date,
        relatedPerson = received.relatedPerson?.let { createRelatedPerson(it) },
        verification = createVerification(received.verification)
    )

fun createVerification(received: List<RequestVerification>): List<RecordVerification> =
    received.mapIfNotEmpty { rqVerification ->
        RecordVerification(
            type = rqVerification.type,
            value = rqVerification.value,
            rationale = rqVerification.rationale
        )
    }.orEmpty()

fun createRelatedPerson(received: RequestRelatedPerson): RecordRelatedPerson =
    RecordRelatedPerson(
        id = received.id,
        name = received.name
    )

fun createConfirmationRequest(received: RequestConfirmationRequest): RecordConfirmationRequest =
    RecordConfirmationRequest(
        id = received.id,
        title = received.title,
        description = received.description,
        relatesTo = received.relatesTo,
        relatedItem = received.relatedItem,
        type = received.type,
        source = received.source,
        requestGroups = received.requestGroups
            .map { createRequestGroup(it) }
    )

fun createRequestGroup(received: RequestRequestGroup): RecordRequestGroup =
    RecordRequestGroup(
        id = received.id,
        requests = received.requests
            .map { createRequest(it) }
    )

fun createRequest(received: RequestRequest): RecordRequest =
    RecordRequest(
        id = received.id,
        relatedPerson = received.relatedPerson?.let { createRelatedPerson(it) },
        description = received.description,
        title = received.title
    )

fun createBudgetSource(received: RequestBudgetSource): RecordBudgetSource =
    RecordBudgetSource(
        id = received.id,
        currency = received.currency,
        amount = received.amount
    )

fun createAgreedMetric(received: RequestAgreedMetric): RecordAgreedMetric =
    RecordAgreedMetric(
        id = received.id,
        description = received.description,
        title = received.title,
        observations = received.observations
            .map { createObservation(it) }
    )

fun createObservationUnit(received: RequestObservationUnit): RecordObservationUnit =
    RecordObservationUnit(
        id = received.id,
        name = received.name,
        scheme = received.scheme
    )

fun createObservation(received: RequestObservation): RecordObservation =
    RecordObservation(
        id = received.id,
        unit = received.unit?.let { createObservationUnit(it) },
        measure = received.measure,
        notes = received.notes
    )

fun createValueTax(received: RequestValueTax): RecordValueTax =
    RecordValueTax(
        amount = received.amount,
        valueAddedTaxIncluded = received.valueAddedTaxIncluded,
        amountNet = received.amountNet,
        currency = received.currency
    )

fun createTags(received: List<Tag>): List<Tag> = received.mapIfNotEmpty { it }.orEmpty()

fun createPlanning(received: RequestPlanning): RecordPlanning =
    RecordPlanning(
        implementation = received.implementation
            ?.let { createImplementation(it) },
        rationale = received.rationale,
        budget = received.budget
            ?.let { createBudget(it) }
    )

fun createImplementation(received: RequestImplementation): RecordImplementation? =
    RecordImplementation(
        transactions = received.transactions
            .map { createTransaction(it) }
    )

fun createTransaction(received: RequestTransaction): RecordTransaction =
    RecordTransaction(
        id = received.id,
        type = received.type,
        value = received.value?.let { createValue(it) },
        executionPeriod = received.executionPeriod?.let { createExecutionPeriod(it) },
        relatedContractMilestone = received.relatedContractMilestone
    )

fun createExecutionPeriod(received: RequestExecutionPeriod): RecordExecutionPeriod? =
    RecordExecutionPeriod(
        durationInDays = received.durationInDays
    )

fun createBudget(received: RequestPlanningBudget): RecordPlanningBudget =
    RecordPlanningBudget(
        description = received.description,
        amount = received.amount,
        isEuropeanUnionFunded = received.isEuropeanUnionFunded,
        budgetSource = received.budgetSource
            .map { createPlanningBudgetSource(it) },
        budgetAllocation = received.budgetAllocation
            .map { createBudgetAllocation(it) },
        budgetBreakdown = received.budgetBreakdown
            .map { createBudgetBreakdown(it) }
    )

fun createBudgetBreakdown(received: RequestBudgetBreakdown): RecordBudgetBreakdown =
    RecordBudgetBreakdown(
        id = received.id,
        amount = received.amount,
        period = received.period?.let { createPeriod(it) },
        description = received.description,
        europeanUnionFunding = received.europeanUnionFunding?.let { createEuropeanUnionFunding(it) },
        sourceParty = received.sourceParty
            ?.let { createOrganizationReference(it) }
    )

fun createEuropeanUnionFunding(received: RequestEuropeanUnionFunding): RecordEuropeanUnionFunding =
    RecordEuropeanUnionFunding(
        projectIdentifier = received.projectIdentifier,
        uri = received.uri,
        projectName = received.projectName
    )

fun createBudgetAllocation(received: RequestBudgetAllocation): RecordBudgetAllocation =
    RecordBudgetAllocation(
        budgetBreakdownID = received.budgetBreakdownID,
        amount = received.amount,
        relatedItem = received.relatedItem,
        period = received.period?.let { createPeriod(it) }
    )

fun createPlanningBudgetSource(received: RequestPlanningBudgetSource): RecordPlanningBudgetSource =
    RecordPlanningBudgetSource(
        budgetBreakdownID = received.budgetBreakdownID,
        amount = received.amount,
        currency = received.currency
    )

fun createPurposeOfNotice(received: RequestPurposeOfNotice): RecordPurposeOfNotice =
    RecordPurposeOfNotice(
        isACallForCompetition = received.isACallForCompetition
    )

fun createPreQualification(received: RequestPreQualification): RecordPreQualification =
    RecordPreQualification(
        period = received.period?.let { createPeriod(it) },
        qualificationPeriod = received.qualificationPeriod?.let { createPeriod(it) }
    )

fun createBidsObject(received: RequestBids): RecordBids =
    RecordBids(
        statistics = received.statistics
            .map { createBidsStatistic(it) },
        details = received.details
            .map { createBid(it) }
    )

fun createBidsStatistic(received: RequestBidsStatistic): RecordBidsStatistic =
    RecordBidsStatistic(
        id = received.id,
        date = received.date,
        value = received.value,
        notes = received.notes,
        measure = received.measure,
        relatedLot = received.relatedLot
    )