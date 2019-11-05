package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.lib.errorIfEmpty
import com.procurement.notice.lib.orThrow
import com.procurement.notice.application.service.cn.UpdateCNData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.cn.update.UpdateCNRequest

fun UpdateCNRequest.convert(): UpdateCNData =
    UpdateCNData(
        planning = this.planning.let { planning ->
            UpdateCNData.Planning(
                rationale = planning.rationale,
                budget = planning.budget.let { budget ->
                    UpdateCNData.Planning.Budget(
                        description = budget.description,
                        amount = budget.amount,
                        isEuropeanUnionFunded = budget.isEuropeanUnionFunded,
                        budgetBreakdowns = budget.budgetBreakdowns.map { budgetBreakdown ->
                            UpdateCNData.Planning.Budget.BudgetBreakdown(
                                id = budgetBreakdown.id,
                                description = budgetBreakdown.description,
                                amount = budgetBreakdown.amount,
                                period = budgetBreakdown.period.let { period ->
                                    UpdateCNData.Planning.Budget.BudgetBreakdown.Period(
                                        startDate = period.startDate,
                                        endDate = period.endDate
                                    )
                                },
                                sourceParty = budgetBreakdown.sourceParty.let { sourceParty ->
                                    UpdateCNData.Planning.Budget.BudgetBreakdown.SourceParty(
                                        id = sourceParty.id,
                                        name = sourceParty.name
                                    )
                                },
                                europeanUnionFunding = budgetBreakdown.europeanUnionFunding?.let { europeanUnionFunding ->
                                    UpdateCNData.Planning.Budget.BudgetBreakdown.EuropeanUnionFunding(
                                        projectIdentifier = europeanUnionFunding.projectIdentifier,
                                        projectName = europeanUnionFunding.projectName,
                                        uri = europeanUnionFunding.uri
                                    )
                                }
                            )
                        }
                    )
                }
            )
        },
        tender = this.tender.let { tender ->
            UpdateCNData.Tender(
                id = tender.id,
                status = tender.status,
                statusDetails = tender.statusDetails,
                title = tender.title,
                description = tender.description,
                classification = tender.classification.let { classification ->
                    UpdateCNData.Tender.Classification(
                        scheme = classification.scheme,
                        id = classification.id,
                        description = classification.description
                    )
                },
                tenderPeriod = tender.tenderPeriod.let { tenderPeriod ->
                    UpdateCNData.Tender.TenderPeriod(
                        startDate = tenderPeriod.startDate,
                        endDate = tenderPeriod.endDate
                    )
                },
                enquiryPeriod = tender.enquiryPeriod.let { enquiryPeriod ->
                    UpdateCNData.Tender.EnquiryPeriod(
                        startDate = enquiryPeriod.startDate,
                        endDate = enquiryPeriod.endDate
                    )
                },
                acceleratedProcedure = tender.acceleratedProcedure.let { acceleratedProcedure ->
                    UpdateCNData.Tender.AcceleratedProcedure(
                        isAcceleratedProcedure = acceleratedProcedure.isAcceleratedProcedure
                    )
                },
                designContest = tender.designContest.let { designContest ->
                    UpdateCNData.Tender.DesignContest(
                        serviceContractAward = designContest.serviceContractAward
                    )
                },
                electronicWorkflows = tender.electronicWorkflows.let { electronicWorkflows ->
                    UpdateCNData.Tender.ElectronicWorkflows(
                        useOrdering = electronicWorkflows.useOrdering,
                        usePayment = electronicWorkflows.usePayment,
                        acceptInvoicing = electronicWorkflows.acceptInvoicing
                    )
                },
                jointProcurement = tender.jointProcurement.let { jointProcurement ->
                    UpdateCNData.Tender.JointProcurement(
                        isJointProcurement = jointProcurement.isJointProcurement
                    )
                },
                procedureOutsourcing = tender.procedureOutsourcing.let { procedureOutsourcing ->
                    UpdateCNData.Tender.ProcedureOutsourcing(
                        procedureOutsourced = procedureOutsourcing.procedureOutsourced
                    )
                },
                framework = tender.framework.let { framework ->
                    UpdateCNData.Tender.Framework(
                        isAFramework = framework.isAFramework
                    )
                },
                dynamicPurchasingSystem = tender.dynamicPurchasingSystem.let { dynamicPurchasingSystem ->
                    UpdateCNData.Tender.DynamicPurchasingSystem(
                        hasDynamicPurchasingSystem = dynamicPurchasingSystem.hasDynamicPurchasingSystem
                    )
                },
                legalBasis = tender.legalBasis,
                procurementMethod = tender.procurementMethod,
                procurementMethodDetails = tender.procurementMethodDetails,
                procurementMethodRationale = tender.procurementMethodRationale,
                procurementMethodAdditionalInfo = tender.procurementMethodAdditionalInfo,
                mainProcurementCategory = tender.mainProcurementCategory,
                eligibilityCriteria = tender.eligibilityCriteria,
                contractPeriod = tender.contractPeriod.let { tenderPeriod ->
                    UpdateCNData.Tender.ContractPeriod(
                        startDate = tenderPeriod.startDate,
                        endDate = tenderPeriod.endDate
                    )
                },
                procurementMethodModalities = tender.procurementMethodModalities
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The tender contain empty list of the procurement method modalities."
                        )
                    }
                    ?.toList()
                    .orEmpty(),
                auctionPeriod = tender.auctionPeriod?.let { auctionPeriod ->
                    UpdateCNData.Tender.AuctionPeriod(
                        startDate = auctionPeriod.startDate
                    )
                },
                electronicAuctions = tender.electronicAuctions?.let { electronicAuctions ->
                    UpdateCNData.Tender.ElectronicAuctions(
                        details = electronicAuctions.details.map { detail ->
                            UpdateCNData.Tender.ElectronicAuctions.Detail(
                                id = detail.id,
                                auctionPeriod = detail.auctionPeriod?.let { auctionPeriod ->
                                    UpdateCNData.Tender.ElectronicAuctions.Detail.AuctionPeriod(
                                        startDate = auctionPeriod.startDate
                                    )
                                },
                                relatedLot = detail.relatedLot,
                                electronicAuctionModalities = detail.electronicAuctionModalities.map { modality ->
                                    UpdateCNData.Tender.ElectronicAuctions.Detail.ElectronicAuctionModality(
                                        url = modality.url,
                                        eligibleMinimumDifference = modality.eligibleMinimumDifference
                                    )
                                }
                            )
                        }
                    )
                },
                procuringEntity = tender.procuringEntity.let { procuringEntity ->
                    UpdateCNData.Tender.ProcuringEntity(
                        id = procuringEntity.id,
                        name = procuringEntity.name,
                        identifier = procuringEntity.identifier.let { identifier ->
                            UpdateCNData.Tender.ProcuringEntity.Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                legalName = identifier.legalName,
                                uri = identifier.uri
                            )
                        },
                        additionalIdentifiers = procuringEntity.additionalIdentifiers
                            .errorIfEmpty {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The procuring entity contain empty list of the additional identifiers."
                                )
                            }
                            ?.map { additionalIdentifier ->
                                UpdateCNData.Tender.ProcuringEntity.AdditionalIdentifier(
                                    scheme = additionalIdentifier.scheme,
                                    id = additionalIdentifier.id,
                                    legalName = additionalIdentifier.legalName,
                                    uri = additionalIdentifier.uri
                                )
                            }
                            .orEmpty(),
                        address = procuringEntity.address.let { address ->
                            UpdateCNData.Tender.ProcuringEntity.Address(
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode,
                                addressDetails = address.addressDetails.let { addressDetails ->
                                    UpdateCNData.Tender.ProcuringEntity.Address.AddressDetails(
                                        country = addressDetails.country.let { country ->
                                            UpdateCNData.Tender.ProcuringEntity.Address.AddressDetails.Country(
                                                scheme = country.scheme,
                                                id = country.id,
                                                description = country.description,
                                                uri = country.uri
                                            )
                                        },
                                        region = addressDetails.region.let { region ->
                                            UpdateCNData.Tender.ProcuringEntity.Address.AddressDetails.Region(
                                                scheme = region.scheme,
                                                id = region.id,
                                                description = region.description,
                                                uri = region.uri
                                            )
                                        },
                                        locality = addressDetails.locality.let { locality ->
                                            UpdateCNData.Tender.ProcuringEntity.Address.AddressDetails.Locality(
                                                scheme = locality.scheme,
                                                id = locality.id,
                                                description = locality.description,
                                                uri = locality.uri
                                            )
                                        }
                                    )
                                }
                            )
                        },
                        persons = procuringEntity.persons
                            .errorIfEmpty {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The procuring entity contain empty list of the persons."
                                )
                            }
                            ?.map { person ->
                                UpdateCNData.Tender.ProcuringEntity.Person(
                                    title = person.title,
                                    name = person.name,
                                    identifier = person.identifier.let { identifier ->
                                        UpdateCNData.Tender.ProcuringEntity.Person.Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            uri = identifier.uri
                                        )
                                    },
                                    businessFunctions = person.businessFunctions.map { businessFunction ->
                                        UpdateCNData.Tender.ProcuringEntity.Person.BusinessFunction(
                                            id = businessFunction.id,
                                            type = businessFunction.type,
                                            jobTitle = businessFunction.jobTitle,
                                            period = businessFunction.period.let { period ->
                                                UpdateCNData.Tender.ProcuringEntity.Person.BusinessFunction.Period(
                                                    startDate = period.startDate
                                                )
                                            },
                                            documents = businessFunction.documents
                                                .errorIfEmpty {
                                                    ErrorException(
                                                        error = ErrorType.IS_EMPTY,
                                                        message = "The business functions contain empty list of the documents."
                                                    )
                                                }
                                                ?.map { document ->
                                                    UpdateCNData.Tender.ProcuringEntity.Person.BusinessFunction.Document(
                                                        id = document.id,
                                                        documentType = document.documentType,
                                                        title = document.title,
                                                        description = document.description,
                                                        datePublished = document.datePublished,
                                                        url = document.url
                                                    )
                                                }
                                                .orEmpty()
                                        )
                                    }
                                )
                            }
                            .orEmpty(),
                        contactPoint = procuringEntity.contactPoint.let { contactPoint ->
                            UpdateCNData.Tender.ProcuringEntity.ContactPoint(
                                name = contactPoint.name,
                                email = contactPoint.email,
                                telephone = contactPoint.telephone,
                                faxNumber = contactPoint.faxNumber,
                                url = contactPoint.url
                            )
                        }
                    )
                },
                value = tender.value,
                lotGroups = tender.lotGroups.map { lotGroup ->
                    UpdateCNData.Tender.LotGroup(
                        optionToCombine = lotGroup.optionToCombine
                    )
                },
                lots = tender.lots.map { lot ->
                    UpdateCNData.Tender.Lot(
                        id = lot.id,
                        internalId = lot.internalId,
                        title = lot.title,
                        description = lot.description,
                        status = lot.status,
                        statusDetails = lot.statusDetails,
                        value = lot.value,
                        options = lot.options.map { option ->
                            UpdateCNData.Tender.Lot.Option(
                                hasOptions = option.hasOptions
                            )
                        },
                        variants = lot.variants.map { variant ->
                            UpdateCNData.Tender.Lot.Variant(
                                hasVariants = variant.hasVariants
                            )
                        },
                        renewals = lot.renewals.map { renewal ->
                            UpdateCNData.Tender.Lot.Renewal(
                                hasRenewals = renewal.hasRenewals
                            )
                        },
                        recurrentProcurements = lot.recurrentProcurements.map { recurrentProcurement ->
                            UpdateCNData.Tender.Lot.RecurrentProcurement(
                                isRecurrent = recurrentProcurement.isRecurrent
                            )
                        },
                        contractPeriod = lot.contractPeriod.let { contractPeriod ->
                            UpdateCNData.Tender.Lot.ContractPeriod(
                                startDate = contractPeriod.startDate,
                                endDate = contractPeriod.endDate
                            )
                        },
                        placeOfPerformance = lot.placeOfPerformance?.let { placeOfPerformance ->
                            UpdateCNData.Tender.Lot.PlaceOfPerformance(
                                address = placeOfPerformance.address.let { address ->
                                    UpdateCNData.Tender.Lot.PlaceOfPerformance.Address(
                                        streetAddress = address.streetAddress,
                                        postalCode = address.postalCode,
                                        addressDetails = address.addressDetails.let { addressDetails ->
                                            UpdateCNData.Tender.Lot.PlaceOfPerformance.Address.AddressDetails(
                                                country = addressDetails.country.let { country ->
                                                    UpdateCNData.Tender.Lot.PlaceOfPerformance.Address.AddressDetails.Country(
                                                        scheme = country.scheme,
                                                        id = country.id,
                                                        description = country.description,
                                                        uri = country.uri
                                                    )
                                                },
                                                region = addressDetails.region.let { region ->
                                                    UpdateCNData.Tender.Lot.PlaceOfPerformance.Address.AddressDetails.Region(
                                                        scheme = region.scheme,
                                                        id = region.id,
                                                        description = region.description,
                                                        uri = region.uri
                                                    )
                                                },
                                                locality = addressDetails.locality.let { locality ->
                                                    UpdateCNData.Tender.Lot.PlaceOfPerformance.Address.AddressDetails.Locality(
                                                        scheme = locality.scheme,
                                                        id = locality.id,
                                                        description = locality.description,
                                                        uri = locality.uri
                                                    )
                                                }
                                            )
                                        }
                                    )
                                },
                                description = placeOfPerformance.description
                            )
                        }
                    )
                },
                items = tender.items.map { item ->
                    UpdateCNData.Tender.Item(
                        id = item.id,
                        internalId = item.internalId,
                        classification = item.classification.let { classification ->
                            UpdateCNData.Tender.Item.Classification(
                                scheme = classification.scheme,
                                id = classification.id,
                                description = classification.description
                            )
                        },
                        additionalClassifications = item.additionalClassifications
                            .errorIfEmpty {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The item contain empty list of the additional classifications."
                                )
                            }
                            ?.map { additionalClassification ->
                                UpdateCNData.Tender.Item.AdditionalClassification(
                                    scheme = additionalClassification.scheme,
                                    id = additionalClassification.id,
                                    description = additionalClassification.description
                                )
                            }
                            .orEmpty(),
                        quantity = item.quantity,
                        unit = item.unit.let { unit ->
                            UpdateCNData.Tender.Item.Unit(
                                id = unit.id,
                                name = unit.name
                            )
                        },
                        description = item.description,
                        relatedLot = item.relatedLot
                    )
                },
                requiresElectronicCatalogue = tender.requiresElectronicCatalogue,
                submissionMethod = tender.submissionMethod.toList(),
                submissionMethodRationale = tender.submissionMethodRationale.toList(),
                submissionMethodDetails = tender.submissionMethodDetails,
                documents = tender.documents.map { document ->
                    UpdateCNData.Tender.Document(
                        documentType = document.documentType,
                        id = document.id,
                        title = document.title,
                        description = document.description,
                        relatedLots = document.relatedLots
                            .errorIfEmpty {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The documents in tender contain empty list of the related lots."
                                )
                            }
                            ?.toList()
                            .orEmpty(),
                        datePublished = document.datePublished,
                        url = document.url
                    )
                }
            )
        },
        amendment = amendment?.let { amendment ->
            UpdateCNData.Amendment(
                relatedLots = amendment.relatedLots
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The amendment contain empty list of the related lots."
                        )
                    }
                    ?.toList()
                    .orThrow {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The amendment not contain list of the related lots."
                        )
                    }
            )
        },
        isAuctionPeriodChanged = this.isAuctionPeriodChanged ?: false
    )
