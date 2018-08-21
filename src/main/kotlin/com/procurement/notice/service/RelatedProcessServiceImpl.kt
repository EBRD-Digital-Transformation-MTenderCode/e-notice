package com.procurement.notice.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.RelatedProcessScheme
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.ContractRecord
import com.procurement.notice.model.tender.record.Record
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface RelatedProcessService {

    fun addFsRelatedProcessToEi(ei: EI, fsOcId: String)

    fun addEiRelatedProcessToFs(fs: FS, eiOcId: String)

    fun addMsRelatedProcessToEi(ei: EI, msOcId: String)

    fun addMsRelatedProcessToFs(fs: FS, msOcId: String)

    fun addEiFsRecordRelatedProcessToMs(ms: Ms, checkFs: CheckFsDto, ocId: String, processType: RelatedProcessType)

    fun addMsRelatedProcessToRecord(record: Record, cpId: String)

    fun addRecordRelatedProcessToMs(ms: Ms, ocid: String, processType: RelatedProcessType)

    fun addRecordRelatedProcessToRecord(record: Record, ocId: String, cpId: String, processType: RelatedProcessType)

    fun addMsRelatedProcessToContract(record: ContractRecord, cpId: String)

    fun addRecordRelatedProcessToContractRecord(record: ContractRecord, ocId: String, cpId: String, processType: RelatedProcessType)

    fun addContractRelatedProcessToCAN(record: Record, ocId: String, cpId: String, contract: Contract)

    fun getBudgetUri(cpId: String?, ocId: String? = null): String

    fun getTenderUri(cpId: String, ocId: String? = null): String
}

@Service
class RelatedProcessServiceImpl : RelatedProcessService {

    @Value("\${uri.budget}")
    private val budgetUri: String? = null
    @Value("\${uri.tender}")
    private val tenderUri: String? = null

    override fun addFsRelatedProcessToEi(ei: EI, fsOcId: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        ei.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.X_FINANCE_SOURCE),
                scheme = RelatedProcessScheme.OCID,
                identifier = fsOcId,
                uri = getBudgetUri(cpId = ei.ocid, ocId = fsOcId)
        ))
    }

    override fun addEiRelatedProcessToFs(fs: FS, eiOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        fs.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = eiOcId,
                uri = getBudgetUri(cpId = eiOcId, ocId = eiOcId)
        ))
    }

    override fun addMsRelatedProcessToEi(ei: EI, msOcId: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        ei.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.X_EXECUTION),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(cpId = msOcId, ocId = msOcId)
        ))
    }

    override fun addMsRelatedProcessToFs(fs: FS, msOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        fs.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.X_EXECUTION),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(cpId = msOcId, ocId = msOcId)
        ))
    }

    override fun addEiFsRecordRelatedProcessToMs(ms: Ms, checkFs: CheckFsDto, ocId: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        val msOcId = ms.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        /*record*/
        ms.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = ocId,
                uri = getTenderUri(cpId = msOcId, ocId = ocId)))
        /*expenditure items*/
        checkFs.ei.asSequence().forEach { eiCpId ->
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_EXPENDITURE_ITEM),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = eiCpId,
                    uri = getBudgetUri(cpId = eiCpId, ocId = eiCpId))
            )
        }
        /*financial sources*/
        ms.planning?.budget?.budgetBreakdown?.asSequence()?.forEach {
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_BUDGET),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = it.id,
                    uri = getBudgetUri(cpId = getEiCpIdFromOcId(it.id), ocId = it.id)))
        }
    }

    override fun addMsRelatedProcessToRecord(record: Record, cpId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = cpId,
                uri = getTenderUri(cpId = cpId, ocId = cpId)))
    }

    override fun addRecordRelatedProcessToMs(ms: Ms, ocid: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        val msOcId = ms.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        ms.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = ocid,
                uri = getTenderUri(cpId = msOcId, ocId = ocid)))
    }

    override fun addRecordRelatedProcessToRecord(record: Record, ocId: String, cpId: String, processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = ocId,
                uri = getTenderUri(cpId = cpId, ocId = ocId)))
    }

    override fun addMsRelatedProcessToContract(record: ContractRecord, cpId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = cpId,
                uri = getTenderUri(cpId = cpId, ocId = cpId)))
    }

    override fun addRecordRelatedProcessToContractRecord(record: ContractRecord, ocId: String, cpId: String, processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = ocId,
                uri = getTenderUri(cpId = cpId, ocId = ocId)))
    }

    override fun addContractRelatedProcessToCAN(record: Record, ocId: String, cpId: String, contract: Contract) {
        record.contracts?.let { cans ->
            cans.asSequence()
                    .firstOrNull { it.awardId == contract.awardId }
                    ?.let { can ->
                        if (can.relatedProcesses == null) can.relatedProcesses = hashSetOf()
                        can.relatedProcesses?.add(RelatedProcess(
                                id = UUIDs.timeBased().toString(),
                                relationship = listOf(RelatedProcessType.X_CONTRACT),
                                scheme = RelatedProcessScheme.OCID,
                                identifier = ocId,
                                uri = getTenderUri(cpId = cpId, ocId = ocId)))
                    }
        }
    }

    private fun getEiCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    override fun getBudgetUri(cpId: String?, ocId: String?): String {
        var uri = budgetUri + cpId
        if (ocId != null) {
            uri = uri + URI_SEPARATOR + ocId
        }
        return uri
    }

    override fun getTenderUri(cpId: String, ocId: String?): String {
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
