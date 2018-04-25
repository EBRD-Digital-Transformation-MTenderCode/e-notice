package com.procurement.notice.service

import com.datastax.driver.core.utils.UUIDs
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.RelatedProcessScheme
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.ContractRecord
import com.procurement.notice.model.tender.record.Record
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
interface RelatedProcessService {

    fun addFsRelatedProcessToEi(ei: EI, fsOcId: String)

    fun addEiRelatedProcessToFs(fs: FS, eiOcId: String)

    fun addMsRelatedProcessToEi(ei: EI, msOcId: String)

    fun addMsRelatedProcessToFs(fs: FS, msOcId: String)

    fun addEiFsRecordRelatedProcessToMs(ms: Ms, checkFs: CheckFsDto, ocId: String, processType: RelatedProcessType)

    fun addMsRelatedProcessToRecord(record: Record, msOcId: String)

    fun addRecordRelatedProcessToMs(ms: Ms, recordOcId: String, processType: RelatedProcessType)

    fun addRecordRelatedProcessToRecord(record: Record, prevRecordOcId: String, msOcId: String, processType: RelatedProcessType)

    fun addMsRelatedProcessToContract(record: ContractRecord, msOcId: String)

    fun addRecordRelatedProcessToContract(record: ContractRecord, recordOcId: String, msOcId: String, processType: RelatedProcessType)

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
                uri = getBudgetUri(ei.ocid, fsOcId)
        ))
    }

    override fun addEiRelatedProcessToFs(fs: FS, eiOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        fs.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = eiOcId,
                uri = getBudgetUri(eiOcId, eiOcId)
        ))
    }

    override fun addMsRelatedProcessToEi(ei: EI, msOcId: String) {
        if (ei.relatedProcesses == null) ei.relatedProcesses = hashSetOf()
        ei.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.X_EXECUTION),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(msOcId, msOcId)
        ))
    }

    override fun addMsRelatedProcessToFs(fs: FS, msOcId: String) {
        if (fs.relatedProcesses == null) fs.relatedProcesses = hashSetOf()
        fs.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.X_EXECUTION),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(msOcId, msOcId)
        ))
    }

    override fun addEiFsRecordRelatedProcessToMs(ms: Ms, checkFs: CheckFsDto, ocId: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        /*record*/
        ms.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = ocId,
                uri = getTenderUri(ms.ocid!!, ocId)))
        /*expenditure items*/
        checkFs.ei.asSequence().forEach { eiCpId ->
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_EXPENDITURE_ITEM),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = eiCpId,
                    uri = getBudgetUri(eiCpId, eiCpId))
            )
        }
        /*financial sources*/
        ms.planning?.budget?.budgetBreakdown?.asSequence()?.forEach {
            ms.relatedProcesses?.add(RelatedProcess(
                    id = UUIDs.timeBased().toString(),
                    relationship = listOf(RelatedProcessType.X_BUDGET),
                    scheme = RelatedProcessScheme.OCID,
                    identifier = it.id,
                    uri = getBudgetUri(getEiCpIdFromOcId(it.id!!), it.id)))
        }
    }

    override fun addMsRelatedProcessToRecord(record: Record, msOcId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(msOcId, msOcId)))
    }

    override fun addRecordRelatedProcessToMs(ms: Ms, recordOcId: String, processType: RelatedProcessType) {
        if (ms.relatedProcesses == null) ms.relatedProcesses = hashSetOf()
        ms.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = recordOcId,
                uri = getTenderUri(ms.ocid!!, recordOcId)))
    }

    override fun addRecordRelatedProcessToRecord(record: Record, prevRecordOcId: String, msOcId: String, processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = prevRecordOcId,
                uri = getTenderUri(msOcId, prevRecordOcId)))
    }

    override fun addMsRelatedProcessToContract(record: ContractRecord, msOcId: String) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(RelatedProcessType.PARENT),
                scheme = RelatedProcessScheme.OCID,
                identifier = msOcId,
                uri = getTenderUri(msOcId, msOcId)))
    }

    override fun addRecordRelatedProcessToContract(record: ContractRecord, recordOcId: String, msOcId: String,
                                                   processType: RelatedProcessType) {
        if (record.relatedProcesses == null) record.relatedProcesses = hashSetOf()
        record.relatedProcesses?.add(RelatedProcess(
                id = UUIDs.timeBased().toString(),
                relationship = listOf(processType),
                scheme = RelatedProcessScheme.OCID,
                identifier = recordOcId,
                uri = getTenderUri(msOcId, recordOcId)))
    }

    private fun getEiCpIdFromOcId(ocId: String): String {
        val pos = ocId.indexOf(FS_SEPARATOR)
        return ocId.substring(0, pos)
    }

    private fun getBudgetUri(cpId: String?, ocId: String?): String {
        return budgetUri + cpId + URI_SEPARATOR + ocId
    }

    private fun getTenderUri(cpId: String, ocId: String?): String {
        return tenderUri + cpId + URI_SEPARATOR + ocId
    }

    companion object {
        private val FS_SEPARATOR = "-FS-"
        private val URI_SEPARATOR = "/"
    }
}
