package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.notice.model.budget.ReleaseEI;
import com.procurement.notice.model.budget.ReleaseFS;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.RelatedProcessScheme;
import com.procurement.notice.model.ocds.RelatedProcessType;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import com.procurement.notice.model.tender.pspq.PsPq;
import java.util.Arrays;
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
    public void addFsRelatedProcessToEi(final ReleaseEI ei, final String fsOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcessType.X_FINANCE_SOURCE),
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
    public void addEiRelatedProcessToFs(final ReleaseFS fs, final String eiOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcessType.PARENT),
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
    public void addMsRelatedProcessToEi(final ReleaseEI ei, final String msOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcessType.X_EXECUTION),
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
    public void addMsRelatedProcessToFs(final ReleaseFS fs, final String msOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcessType.X_EXECUTION),
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
    public void addRelatedProcessToMs(final Ms ms,
                                      final CheckFsDto checkFs,
                                      final String ocId,
                                      final RelatedProcessType recordProcessType) {
        if (Objects.isNull(ms.getRelatedProcesses())) {
            ms.setRelatedProcesses(new LinkedHashSet<>());
        }
        /*record*/
        ms.getRelatedProcesses().add(new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(recordProcessType),
                RelatedProcessScheme.OCID,
                ocId,
                getTenderUri(ocId)));
        /*expenditure items*/
        checkFs.getEi().forEach(eiCpId ->
                ms.getRelatedProcesses().add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcessType.X_EXPENDITURE_ITEM),
                        RelatedProcessScheme.OCID,
                        eiCpId,
                        getBudgetUri(eiCpId))
                )
        );
        /*financial sources*/
        ms.getPlanning().getBudget().getBudgetBreakdown().forEach(fs ->
                ms.getRelatedProcesses().add(new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcessType.X_BUDGET),
                        RelatedProcessScheme.OCID,
                        fs.getId(),
                        getBudgetUri(fs.getId()))
                )
        );
    }

    @Override
    public void addMsRelatedProcess(final PsPq release, final String msOcId) {
        /*ms*/
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcessType.PARENT),
                RelatedProcessScheme.OCID,
                msOcId,
                getTenderUri(msOcId));
        if (Objects.isNull(release.getRelatedProcesses())) {
            release.setRelatedProcesses(new LinkedHashSet<>());
        }
        release.getRelatedProcesses().add(relatedProcess);
    }

    @Override
    public void addRelatedProcessToPq(PsPq release,Ms ms) {
        release.getRelatedProcesses()
               .add(new RelatedProcess(UUIDs.timeBased()
                                            .toString(), Arrays.asList(RelatedProcessType.X_PREQUALIFICATION),
                                       RelatedProcessScheme.OCID,ms.getOcid(),getTenderUri(ms.getOcid())));
    }

    private String getBudgetUri(final String id) {
        return budgetUri + id;
    }

    private String getTenderUri(final String id) {
        return tenderUri + id;
    }
}
