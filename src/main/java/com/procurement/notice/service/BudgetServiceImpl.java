package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.BudgetDao;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.BudgetEntity;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.model.ocds.ein.ReleaseEIN;
import com.procurement.notice.model.ocds.ein.ReleaseFS;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

    private static final String SEPARATOR = "-";
    private final BudgetDao budgetDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public BudgetServiceImpl(final BudgetDao budgetDao,
                             final JsonUtil jsonUtil,
                             final DateUtil dateUtil) {
        this.budgetDao = budgetDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createEin(final String cpid,
                                 final String stage,
                                 final String operation,
                                 final JsonNode data) {
        final ReleaseEIN ein = jsonUtil.toObject(ReleaseEIN.class, data.toString());
        final LocalDateTime addedDate = dateUtil.getNowUTC();
        ein.setOcid(cpid);
        ein.setId(getReleaseId(cpid));
        ein.setDate(addedDate);
        ein.setTag(Arrays.asList(Tag.COMPILED));
        ein.setInitiationType(InitiationType.TENDER);
        budgetDao.saveBudget(getEntity(ein.getOcid(), ein.getOcid(), ein.getId(), stage, 0D, addedDate, ein));
        return getResponseDto(ein.getOcid(), ein.getOcid());
    }

    @Override
    public ResponseDto createFs(final String cpid,
                                final String stage,
                                final String operation,
                                final JsonNode data) {
        final ReleaseFS fs = jsonUtil.toObject(ReleaseFS.class, data.toString());
        final LocalDateTime addedDate = dateUtil.getNowUTC();
        fs.setOcid(getOcId(cpid, stage));
        fs.setId(getReleaseId(fs.getOcid()));
        fs.setDate(addedDate);
        fs.setTag(Arrays.asList(Tag.COMPILED));
        fs.setInitiationType(InitiationType.TENDER);
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        budgetDao.saveBudget(getEntity(fs.getOcid(), fs.getOcid(), fs.getId(), stage, amount, addedDate, fs));
        updateEinByFs(cpid, fs.getOcid());
        return getResponseDto(fs.getOcid(), fs.getOcid());
    }


    public void updateEinByFs(final String einCpId, final String fsOcId) {
        final Optional<BudgetEntity> entityOptional = budgetDao.getLastByCpId(einCpId);
        if (entityOptional.isPresent()) {
            final BudgetEntity entity = entityOptional.get();
            final ReleaseEIN ein = jsonUtil.toObject(ReleaseEIN.class, entity.getJsonData());
            final Double totalAmount = budgetDao.getTotalAmountByCpId(einCpId);
            final LocalDateTime addedDate = dateUtil.getNowUTC();
            ein.setId(getReleaseId(einCpId));
            ein.setDate(addedDate);
            ein.getPlanning().getBudget().getAmount().setAmount(totalAmount);
            addFsRelatedProcessToEin(ein, fsOcId);

            budgetDao.saveBudget(getEntity(
                    einCpId,
                    einCpId,
                    ein.getId(),
                    entity.getStage(),
                    totalAmount,
                    addedDate,
                    ein));
        }

    }

    private void addFsRelatedProcessToEin(final ReleaseEIN ein, final String fsOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcess.RelatedProcessType.X_FINANCE_SOURCE),
                "",
                RelatedProcess.RelatedProcessScheme.OCID,
                fsOcId,
                ""
        );
        ein.getRelatedProcesses().add(relatedProcess);
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getId() {
        return UUIDs.timeBased().toString();
    }

    private BudgetEntity getEntity(final String cpId,
                                   final String ocId,
                                   final String releaseId,
                                   final String stage,
                                   final Double amount,
                                   final LocalDateTime releaseDate,
                                   final Object jsonData) {
        final BudgetEntity entity = new BudgetEntity();
        entity.setCpId(cpId);
        entity.setOcId(ocId);
        entity.setReleaseDate(dateUtil.localToDate(releaseDate));
        entity.setReleaseId(releaseId);
        entity.setStage(stage);
        entity.setAmount(amount);
        entity.setJsonData(jsonUtil.toJson(jsonData));
        return entity;
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto(true, null, jsonForResponse);
    }
}
