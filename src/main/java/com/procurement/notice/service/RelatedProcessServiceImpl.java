package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.notice.model.budget.EI;
import com.procurement.notice.model.budget.FS;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.RelatedProcessScheme;
import com.procurement.notice.model.ocds.RelatedProcessType;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.record.Record;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RelatedProcessServiceImpl implements RelatedProcessService {

    @Value("${uri.budget}")
    private String budgetUri;
    @Value("${uri.tender}")
    private String tenderUri;

    @Override
    public void addFsRelatedProcessToEi(final EI ei, final String fsOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(RelatedProcessType.X_FINANCE_SOURCE),
                RelatedProcessScheme.OCID,
                fsOcId,
                getBudgetUri(fsOcId)
        );
        if (Objects.isNull(ei.getRelatedProcesses())) {
            ei.setRelatedProcesses(new LinkedHashSet<>());
        }
        ei.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addEiRelatedProcessToFs(final FS fs, final String eiOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(RelatedProcessType.PARENT),
                RelatedProcessScheme.OCID,
                eiOcId,
                getBudgetUri(eiOcId)
        );
        if (Objects.isNull(fs.getRelatedProcesses())) {
            fs.setRelatedProcesses(new LinkedHashSet<>());
        }
        fs.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addMsRelatedProcessToEi(final EI ei, final String msOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(RelatedProcessType.X_EXECUTION),
                RelatedProcessScheme.OCID,
                msOcId,
                getTenderUri(msOcId)
        );
        if (Objects.isNull(ei.getRelatedProcesses())) {
            ei.setRelatedProcesses(new LinkedHashSet<>());
        }
        ei.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addMsRelatedProcessToFs(final FS fs, final String msOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(RelatedProcessType.X_EXECUTION),
                RelatedProcessScheme.OCID,
                msOcId,
                getTenderUri(msOcId)
        );
        if (Objects.isNull(fs.getRelatedProcesses())) {
            fs.setRelatedProcesses(new LinkedHashSet<>());
        }
        fs.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addEiFsRecordRelatedProcessToMs(final Ms ms,
                                                final CheckFsDto checkFs,
                                                final String ocId,
                                                final RelatedProcessType recordProcessType) {
        if (Objects.isNull(ms.getRelatedProcesses())) {
            ms.setRelatedProcesses(new LinkedHashSet<>());
        }
        /*record*/
        ms.getRelatedProcesses().add(new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(recordProcessType),
                RelatedProcessScheme.OCID,
                ocId,
                getTenderUri(ocId)));
        /*expenditure items*/
        checkFs.getEi().forEach(eiCpId ->
                ms.getRelatedProcesses().add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Collections.singletonList(RelatedProcessType.X_EXPENDITURE_ITEM),
                        RelatedProcessScheme.OCID,
                        eiCpId,
                        getBudgetUri(eiCpId))
                )
        );
        /*financial sources*/
        ms.getPlanning().getBudget().getBudgetBreakdown().forEach(fs ->
                ms.getRelatedProcesses().add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Collections.singletonList(RelatedProcessType.X_BUDGET),
                        RelatedProcessScheme.OCID,
                        fs.getId(),
                        getBudgetUri(fs.getId()))
                )
        );
    }

    @Override
    public void addMsRelatedProcessToRecord(final Record record, final String msOcId) {
        /*ms*/
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Collections.singletonList(RelatedProcessType.PARENT),
                RelatedProcessScheme.OCID,
                msOcId,
                getTenderUri(msOcId));
        if (Objects.isNull(record.getRelatedProcesses())) {
            record.setRelatedProcesses(new LinkedHashSet<>());
        }
        record.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addRecordRelatedProcessToMs(final Record record, final String msOcId, final RelatedProcessType recordProcessType) {
        record.getRelatedProcesses()
                .add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Collections.singletonList(recordProcessType),
                        RelatedProcessScheme.OCID,
                        msOcId,
                        getTenderUri(msOcId)));
    }

    @Override
    public void addPervRecordRelatedProcessToRecord(final Record record, final String prevRecordOcId) {
        record.getRelatedProcesses()
                .add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Collections.singletonList(RelatedProcessType.X_PRESELECTION),
                        RelatedProcessScheme.OCID,
                        prevRecordOcId,
                        getTenderUri(prevRecordOcId)));
    }

    private String getBudgetUri(final String id) {
        return budgetUri + id;
    }

    private String getTenderUri(final String id) {
        return tenderUri + id;
    }
}
