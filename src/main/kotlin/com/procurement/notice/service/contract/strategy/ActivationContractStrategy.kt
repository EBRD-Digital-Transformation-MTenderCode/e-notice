package com.procurement.notice.service.contract.strategy

import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.contract.activate.ActivateContractContext
import com.procurement.notice.application.service.contract.activate.ActivateContractData
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.RelatedParty
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject

class ActivationContractStrategy(
    private val releaseService: ReleaseService,
    private val releaseDao: ReleaseDao,
    private val generationService: GenerationService
) {
    fun activateContract(context: ActivateContractContext, data: ActivateContractData): ResponseDto {
        val recordContractEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val recordContract = toObject(ContractRecord::class.java, recordContractEntity.jsonData)

        //BR-2.7.3.6
        val updatedContracts = updating(
            contracts = recordContract.contracts ?: emptyList(),
            contractFromRequest = data.contract
        )
        val updatedRecordContract = recordContract.copy(
            //BR-2.7.3.4
            id = generationService.generateReleaseId(context.ocid),

            //BR-2.7.3.1
            tag = listOf(Tag.CONTRACT_UPDATE),

            //BR-2.7.3.2
            date = context.releaseDate,

            //BR-2.7.3.6
            contracts = updatedContracts
        )

        val recordStage = when (context.pmd) {
            ProcurementMethod.OT, ProcurementMethod.TEST_OT,
            ProcurementMethod.SV, ProcurementMethod.TEST_SV,
            ProcurementMethod.MV, ProcurementMethod.TEST_MV -> "EV"

            ProcurementMethod.CD, ProcurementMethod.TEST_CD,
            ProcurementMethod.DA, ProcurementMethod.TEST_DA,
            ProcurementMethod.DC, ProcurementMethod.TEST_DC,
            ProcurementMethod.IP, ProcurementMethod.TEST_IP,
            ProcurementMethod.NP, ProcurementMethod.TEST_NP,
            ProcurementMethod.OP, ProcurementMethod.TEST_OP -> "NP"

            ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ -> "RQ"

            ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
            ProcurementMethod.RT, ProcurementMethod.TEST_RT -> "TP"

            ProcurementMethod.CF, ProcurementMethod.TEST_CF,
            ProcurementMethod.FA, ProcurementMethod.TEST_FA,
            ProcurementMethod.OF, ProcurementMethod.TEST_OF -> throw ErrorException(ErrorType.INVALID_PMD)
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = context.cpid, stage = recordStage)
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val releaseEV = releaseService.getRelease(recordEvEntity.jsonData)
        val updatedReleaseEv = releaseEV.copy(
            //BR-2.4.12.4
            id = generationService.generateReleaseId(recordEvEntity.ocId),

            //BR-2.4.12.3
            date = context.releaseDate,

            //BR-2.4.12.1
            tag = listOf(Tag.TENDER_UPDATE),

            //BR-2.4.12.6
            tender = releaseEV.tender.copy(
                lots = updating(lots = releaseEV.tender.lots, lotsFromRequest = data.lots).toList()
            ),

            //BR-2.4.12.8
            contracts = updatingContractsEv(
                contracts = releaseEV.contracts,
                cans = data.cans
            ).toList(),

            //BR-2.4.12.9
            awards = updatingAwardsEv(
                awards = releaseEV.awards,
                awardsFromRequest = data.awards
            ).toList(),

            //BR-2.4.12.10
            bids = releaseEV.bids?.let { bids ->
                bids.copy(
                    details = updatingBidsEv(
                        bids = bids.details ?: emptyList(),
                        bidsFromRequest = data.bids ?: emptyList()
                    )
                )
            }
        )

        releaseService.saveContractRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecordContract,
            publishDate = recordContractEntity.publishDate
        )
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = recordStage,
            release = updatedReleaseEv,
            publishDate = recordEvEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = context.cpid, ocid = context.ocid))
    }

    /**
     * BR-2.7.3.6 Contract
     */
    private fun updating(
        contracts: Collection<Contract>,
        contractFromRequest: ActivateContractData.Contract
    ): List<Contract> {
        return contracts.map { contract ->
            if (contract.id == contractFromRequest.id)
                contract.updating(contractFromRequest)
            else
                contract
        }
    }

    private fun Contract.updating(contract: ActivateContractData.Contract): Contract {
        return this.copy(
            //BR-2.7.3.7
            status = contract.status,
            statusDetails = contract.statusDetails,

            //BR-2.7.3.8
            milestones = contract.milestones.map { milestone ->
                Milestone(
                    id = milestone.id,
                    title = milestone.title,
                    description = milestone.description,
                    type = milestone.type,
                    status = milestone.status,
                    relatedItems = milestone.relatedItems,
                    additionalInformation = milestone.additionalInformation,
                    dueDate = milestone.dueDate,
                    relatedParties = milestone.relatedParties.map { relatedParty ->
                        RelatedParty(
                            id = relatedParty.id,
                            name = relatedParty.name
                        )
                    },
                    dateModified = milestone.dateModified,
                    dateMet = milestone.dateMet
                )
            }
        )
    }

    /**
     * BR-2.4.12.7 "Status" (lot) "Status Details" (lot)
     * 1. Rewrite to new Release all tender.lots objects from previous Release;
     * 2. FOR every lot object from Request finds Lot in new formed Release with lot.ID == lot.ID from Request;
     * 3. Change value of lot.status && lot.statusDetails getting values from appropriate object with same ID from Request;
     */
    private fun updating(lots: Collection<Lot>, lotsFromRequest: List<ActivateContractData.Lot>): List<Lot> {
        val lotsFromRequestById = lotsFromRequest.associateBy { it.id }
        return lots.map { lot ->
            lotsFromRequestById[lot.id]
                ?.let {
                    lot.copy(
                        //BR-2.4.12.7
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: lot
        }
    }

    /**
     * BR-2.4.12.8 Contracts
     *
     * В секции Contracts нового релиза eNotice должен перезаписать те объекты, обновление по которым было получено
     * в запросе на создание нового релиза в секции can.
     * 1. eNotice помещает в новый релиз все объекты Contracts актуального релиза.
     * 2. eNotice производит поиск в массиве Contracts формируемого релиза по "ID" (cans.id)
     *    Contract Award Notices из запроса.
     * 3. eNotice заменяет значения атрибутов "Status" (contract.status), "Status Details" (contract.statusDetails)
     *    в найденном Contract, используя данные из полей, полученных в объектe Can с соотвествующим "ID" (can.id)
     *    из payload.
     */
    private fun updatingContractsEv(
        contracts: Collection<Contract>,
        cans: List<ActivateContractData.CAN>
    ): List<Contract> {
        val cansById = cans.associateBy { it.id }
        return contracts.map { contract ->
            cansById[contract.id]
                ?.let {
                    contract.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: contract
        }
    }

    /**
     * BR-2.4.12.9 Awards
     * В секции Awards нового релиза eNotice должен изменить значения полей "Status", "Status Details"
     * в тех объектах Award, информация по которым была получена в запросе на создание нового релиза.
     * 1. eNotice копирует из актуального релиза в секцию Awards нового релиза все сохраненные ранее объекты.
     * 2. eNotice производит поиск в массиве Awards формируемого релиза по тем ID (awards.id),
     *    которые были получены в запросе на создание релиза в секции Awards.
     * 3. eNotice заменяет значения атрибутов "Status" (awards.status), "Status Details" (awards.statusDetails)
     *    в каждом из найденных объектов Award, используя данные из полей, полученных в объектах Award
     *    с соотвествующим "ID" (awards.id) из payload.
     */
    private fun updatingAwardsEv(
        awards: Collection<Award>,
        awardsFromRequest: List<ActivateContractData.Award>
    ): List<Award> {
        val awardsFromRequestById = awardsFromRequest.associateBy { it.id }
        return awards.map { award ->
            awardsFromRequestById[award.id]
                ?.let {
                    award.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: award
        }
    }

    /**
     * BR-2.4.12.10 Bids
     * 1. eNotice analyzes availability of Bids array in Request:
     *   a. IF [there are Bids in Request] then:
     *     i.   eNotice копирует из актуального релиза EV Record в секцию Bids нового релиза
     *          все сохраненные ранее предложения Bid;
     *     ii.  eNotice производит поиск в массиве Bids формируемого релиза по тем "ID" (bids.details.ID)
     *          предложений, которые были получены в запросе в секции Bids.
     *     iii. eNotice заменяет значения атрибутов "Status" (bids.details.status),
     *          "Status Details" (bids.details.statusDetails) в каждом из найденных объектов Bid,
     *           используя данные из полей, полученных в объектах Bid с соотвествующим "ID" (bids.id) из payload.
     *   b. ELSE [no bids in Request] { system does not perform any operation;
     */
    private fun updatingBidsEv(
        bids: List<Bid>,
        bidsFromRequest: List<ActivateContractData.Bid>
    ): List<Bid> {
        if (bidsFromRequest.isEmpty()) return bids
        val bidsFromRequestById = bidsFromRequest.associateBy { it.id }
        return bids.map { bid ->
            bidsFromRequestById[bid.id]
                ?.let {
                    bid.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: bid
        }
    }
}
