package com.procurement.notice.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.RelatedProcessScheme
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class RelatedProcessService {

    @Value("\${uri.budget}")
    private val budgetUri: String? = null
    @Value("\${uri.tender}")
    private val tenderUri: String? = null

    fun addFsRelatedProcessToEi(ei: EI, fsOcId: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        if (ei.relatedProcesses!!.asSequence().none { it.identifier == fsOcId })
            ei.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_FUNDING_SOURCE),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = fsOcId,
                    uri = getBudgetUri(cpId = ei.ocid, ocId = fsOcId)
            ))
    }

    fun addEiRelatedProcessToFs(fs: FS, eiOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        if (fs.relatedProcesses!!.asSequence().none { it.identifier == eiOcId })
            fs.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.PARENT),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = eiOcId,
                    uri = getBudgetUri(cpId = eiOcId, ocId = eiOcId)
            ))
    }

    fun addMsRelatedProcessToEi(ei: EI, msOcId: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        if (ei.relatedProcesses!!.asSequence().none { it.identifier == msOcId })
            ei.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_EXECUTION),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = msOcId,
                    uri = getTenderUri(cpId = msOcId, ocId = msOcId)
            ))
    }

    fun addMsRelatedProcessToFs(fs: FS, msOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        if (fs.relatedProcesses!!.asSequence().none { it.identifier == msOcId })
            fs.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_EXECUTION),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = msOcId,
                    uri = getTenderUri(cpId = msOcId, ocId = msOcId)
            ))
    }

    fun addEiFsRecordRelatedProcessToMs(ms: Ms, checkFs: CheckFsDto, ocId: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        val msOcId = ms.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        /*record*/
        if (ms.relatedProcesses!!.asSequence().none { it.identifier == ocId })
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(processType),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocId,
                    uri = getTenderUri(cpId = msOcId, ocId = ocId)))
        /*expenditure items*/

        checkFs.ei.asSequence().forEach { eiCpId ->
            if (ms.relatedProcesses!!.asSequence().none { it.identifier == eiCpId })
                ms.relatedProcesses?.add(RelatedProcess(
                        id = UUIDs.timeBased().toString(),
                        relationship = listOf(RelatedProcessType.X_EXPENDITURE_ITEM),
                        scheme = RelatedProcessScheme.OCID,
                        identifier = eiCpId,
                        uri = getBudgetUri(cpId = eiCpId, ocId = eiCpId))
                )
        }
        /*financial sources*/
        ms.planning?.budget?.budgetBreakdown?.asSequence()?.forEach { bb ->
            if (ms.relatedProcesses!!.asSequence().none { it.identifier == bb.id })
                ms.relatedProcesses?.add(RelatedProcess(
                        id = UUIDs.timeBased().toString(),
                        relationship = listOf(RelatedProcessType.X_FUNDING_SOURCE),
                        scheme = RelatedProcessScheme.OCID,
                        identifier = bb.id,
                        uri = getBudgetUri(cpId = getEiCpIdFromOcId(bb.id), ocId = bb.id)))
        }
    }

    fun addMsRelatedProcessToRecord(record: Record, cpId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == cpId })
            record.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.PARENT),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = cpId,
                    uri = getTenderUri(cpId = cpId, ocId = cpId)))
    }

    fun addRecordRelatedProcessToMs(ms: Ms, ocid: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        val msOcId = ms.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        if (ms.relatedProcesses!!.asSequence().none { it.identifier == ocid })
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(processType),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocid,
                    uri = getTenderUri(cpId = msOcId, ocId = ocid)))
    }

    fun addRecordRelatedProcessToRecord(record: Record, ocId: String, cpId: String, processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == ocId })
            record.relatedProcesses?.add(
                RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(processType),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocId,
                    uri = getTenderUri(cpId = cpId, ocId = ocId)
                )
            )
    }

    fun addMsRelatedProcessToContract(record: ContractRecord, cpId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == cpId })
            record.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.PARENT),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = cpId,
                    uri = getTenderUri(cpId = cpId, ocId = cpId)))

    }

    fun addRecordRelatedProcessToContractRecord(record: ContractRecord, ocId: String, cpId: String, processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == ocId })
            record.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(processType),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocId,
                    uri = getTenderUri(cpId = cpId, ocId = ocId)))
    }

    fun addContractRelatedProcessToCAN(record: Record, ocId: String, cpId: String, contract: Contract, cans: HashSet<Can>) {
        cans.asSequence().forEach { can ->
            record.contracts?.asSequence()
                    ?.firstOrNull { it.id == can.id }
                    ?.let { contract ->
                        if (contract.relatedProcesses == null) contract.relatedProcesses = hashSetOf()
                        contract.relatedProcesses!!.add(RelatedProcess(
                                id = UUIDs.timeBased().toString(),
                                relationship = listOf(RelatedProcessType.X_CONTRACTING),
                                scheme = RelatedProcessScheme.OCID,
                                identifier = ocId,
                                uri = getTenderUri(cpId = cpId, ocId = ocId)))
                    }
        }
    }

    fun addEiRelatedProcessToContract(record: ContractRecord, eiOcId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == eiOcId })
            record.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_EXPENDITURE_ITEM),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = eiOcId,
                    uri = getBudgetUri(cpId = eiOcId, ocId = eiOcId)
            ))
    }

    fun addFsRelatedProcessToContract(record: ContractRecord, fsOcId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        if (record.relatedProcesses!!.asSequence().none { it.identifier == fsOcId })
            record.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_FUNDING_SOURCE),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = fsOcId,
                    uri = getBudgetUri(cpId = getEiCpIdFromOcId(fsOcId), ocId = fsOcId)
            ))
    }


    fun addContractRelatedProcessToFs(fs: FS, cpid: String, ocid: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        if (fs.relatedProcesses!!.asSequence().none { it.identifier == ocid })
            fs.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.PARENT),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocid,
                    uri = getTenderUri(cpId = cpid, ocId = cpid)
            ))
    }

    fun addContractRelatedProcessToEi(ei: EI, cpid: String, ocid: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        if (ei.relatedProcesses!!.asSequence().none { it.identifier == ocid })
            ei.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.PARENT),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = ocid,
                    uri = getTenderUri(cpId = cpid, ocId = cpid)
            ))
    }

    fun removeContractRelatedProcessFromFs(fs: FS, ocid: String) {
        if (fs.relatedProcesses != null) {
            val rp = fs.relatedProcesses!!.asSequence().first { it.identifier == ocid }
            fs.relatedProcesses!!.remove(rp)
        }
    }

    fun removeFsRelatedProcessFromContract(recordContract: ContractRecord, fsOcid: String) {
        if (recordContract.relatedProcesses != null) {
            val rp = recordContract.relatedProcesses!!.asSequence().first { it.identifier == fsOcid }
            recordContract.relatedProcesses!!.remove(rp)
        }
    }

    fun removeEiRelatedProcessFromContract(recordContract: ContractRecord, eiOcid: String) {
        if (recordContract.relatedProcesses != null) {
            val rp = recordContract.relatedProcesses!!.asSequence().first { it.identifier == eiOcid }
            recordContract.relatedProcesses!!.remove(rp)
        }
    }

    fun removeContractRelatedProcessFromEi(ei: EI, ocid: String) {
        if (ei.relatedProcesses != null) {
            val rp = ei.relatedProcesses!!.asSequence().first { it.identifier == ocid }
            ei.relatedProcesses!!.remove(rp)
        }
    }

    fun getEiCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    fun getBudgetUri(cpId: String?, ocId: String?): String {
        var uri = budgetUri + cpId
        if (ocId != null) {
            uri = uri + URI_SEPARATOR + ocId
        }
        return uri
    }

    fun getTenderUri(cpId: String, ocId: String?): String {
        var uri = tenderUri + cpId
        if (ocId != null) {
            uri = uri + URI_SEPARATOR + ocId
        }
        return uri
    }

    companion object {
        private const val FS_SEPARATOR = "-FS-"
        private const val URI_SEPARATOR = "/"
    }
}
