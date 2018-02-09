package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.BudgetDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.budget.ReleaseEI;
import com.procurement.notice.model.budget.ReleaseFS;
import com.procurement.notice.model.entity.BudgetEntity;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

    private static final String SEPARATOR = "-";
    private static final String EI_NOT_FOUND_ERROR = "EI not found.";
    private static final String FS_NOT_FOUND_ERROR = "FS not found.";
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
    public ResponseDto createEi(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseEI ei = jsonUtil.toObject(ReleaseEI.class, data.toString());
        ei.setTag(Arrays.asList(Tag.COMPILED));
        ei.setInitiationType(InitiationType.TENDER);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, ei));
        return getResponseDto(ei.getOcid(), ei.getOcid());
    }

    @Override
    public ResponseDto updateEi(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(cpid))
                .orElseThrow(() -> new ErrorException(EI_NOT_FOUND_ERROR));
        final ReleaseEI updateReleaseEI = jsonUtil.toObject(ReleaseEI.class, data.toString());
        final ReleaseEI eiFromEntity = jsonUtil.toObject(ReleaseEI.class, entity.getJsonData());
        updateEiDto(eiFromEntity, updateReleaseEI);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, eiFromEntity));
        return getResponseDto(eiFromEntity.getOcid(), eiFromEntity.getOcid());
    }

    private void updateEiDto(final ReleaseEI eiFromEntity, final ReleaseEI updateReleaseEI) {
        eiFromEntity.setTitle(updateReleaseEI.getTitle());
        eiFromEntity.setDescription(updateReleaseEI.getDescription());
        eiFromEntity.setPlanning(updateReleaseEI.getPlanning());
        eiFromEntity.setTender(updateReleaseEI.getTender());
        eiFromEntity.setParties(updateReleaseEI.getParties());
        eiFromEntity.setBuyer(updateReleaseEI.getBuyer());
    }

    @Override
    public ResponseDto createFs(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseFS fs = jsonUtil.toObject(ReleaseFS.class, data.toString());
        fs.setOcid(getOcId(cpid, stage));
        fs.setTag(Arrays.asList(Tag.COMPILED));
        fs.setInitiationType(InitiationType.TENDER);
        addEiRelatedProcessToFs(fs, cpid);
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        budgetDao.saveBudget(getEntity(cpid, fs.getOcid(), stage, amount, fs));
        updateEiByFs(cpid, fs.getOcid());
        return getResponseDto(cpid, fs.getOcid());
    }

    @Override
    public ResponseDto updateFs(final String cpid,
                                final String ocid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(FS_NOT_FOUND_ERROR));
        final ReleaseFS updateReleaseFS = jsonUtil.toObject(ReleaseFS.class, data.toString());
        final ReleaseFS fsFromEntity = jsonUtil.toObject(ReleaseFS.class, entity.getJsonData());
        updateFsDto(fsFromEntity, updateReleaseFS);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, fsFromEntity));
        return getResponseDto(cpid, fsFromEntity.getOcid());
    }

    private void updateFsDto(final ReleaseFS fsFromEntity, final ReleaseFS updateReleaseFS) {
        fsFromEntity.setTitle(updateReleaseFS.getTitle());
        fsFromEntity.setDescription(updateReleaseFS.getDescription());
        fsFromEntity.setTender(updateReleaseFS.getTender());
        fsFromEntity.setParties(updateReleaseFS.getParties());
        fsFromEntity.setPlanning(updateReleaseFS.getPlanning());
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    public void updateEiByFs(final String eiCpId, final String fsOcId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                .orElseThrow(() -> new ErrorException(EI_NOT_FOUND_ERROR));
        final ReleaseEI ei = jsonUtil.toObject(ReleaseEI.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(eiCpId);
        ei.getPlanning().getBudget().getAmount().setAmount(totalAmount);
        addFsRelatedProcessToEi(ei, fsOcId);
        budgetDao.saveBudget(getEntity(
                eiCpId,
                eiCpId,
                entity.getStage(),
                totalAmount,
                ei));
    }

    private void addFsRelatedProcessToEi(final ReleaseEI ei, final String fsOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcess.RelatedProcessType.X_FINANCE_SOURCE),
                "",
                RelatedProcess.RelatedProcessScheme.OCID,
                fsOcId,
                ""
        );
        ei.getRelatedProcesses().add(relatedProcess);
    }

    private void addEiRelatedProcessToFs(final ReleaseFS fs, final String eiOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                "",
                RelatedProcess.RelatedProcessScheme.OCID,
                eiOcId,
                ""
        );
        fs.getRelatedProcesses().add(relatedProcess);
    }

    private BudgetEntity getEntity(final String cpId,
                                   final String ocId,
                                   final String stage,
                                   final Double amount,
                                   final Object jsonData) {
        final BudgetEntity entity = new BudgetEntity();
        entity.setCpId(cpId);
        entity.setOcId(ocId);
        entity.setReleaseDate(dateUtil.localToDate(dateUtil.getNowUTC()));
        entity.setReleaseId(getReleaseId(ocId));
        entity.setStage(stage);
        entity.setAmount(amount);
        entity.setJsonData(jsonUtil.toJson(jsonData));
        return entity;
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }
}
