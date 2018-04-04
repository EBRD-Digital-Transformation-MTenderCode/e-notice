package com.procurement.notice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.BudgetDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.exception.ErrorType;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.budget.EI;
import com.procurement.notice.model.budget.FS;
import com.procurement.notice.model.entity.BudgetEntity;
import com.procurement.notice.model.ocds.BudgetBreakdown;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

    private static final String SEPARATOR = "-";
    private static final String FS_SEPARATOR = "-FS-";
    private final BudgetDao budgetDao;
    private final OrganizationService organizationService;
    private final RelatedProcessService relatedProcessService;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public BudgetServiceImpl(final BudgetDao budgetDao,
                             final OrganizationService organizationService,
                             final RelatedProcessService relatedProcessService,
                             final JsonUtil jsonUtil,
                             final DateUtil dateUtil) {
        this.budgetDao = budgetDao;
        this.organizationService = organizationService;
        this.relatedProcessService = relatedProcessService;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createEi(final String cpid,
                                final String stage,
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final EI ei = jsonUtil.toObject(EI.class, data.toString());
        ei.setId(getReleaseId(cpid));
        ei.setDate(releaseDate);
        ei.setTag(Collections.singletonList(Tag.COMPILED));
        ei.setInitiationType(InitiationType.TENDER);
        organizationService.processEiParties(ei);
        budgetDao.saveBudget(getEiEntity(ei, stage));
        return getResponseDto(ei.getOcid(), ei.getOcid());
    }

    @Override
    public ResponseDto updateEi(final String cpid,
                                final String stage,
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(cpid))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final EI updateEi = jsonUtil.toObject(EI.class, data.toString());
        final EI ei = jsonUtil.toObject(EI.class, entity.getJsonData());
        ei.setId(getReleaseId(cpid));
        ei.setDate(releaseDate);
        updateEiDto(ei, updateEi);
        budgetDao.saveBudget(getEiEntity(ei, stage));
        return getResponseDto(ei.getOcid(), ei.getOcid());
    }

    @Override
    public ResponseDto createFs(final String cpid,
                                final String stage,
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final FS fs = jsonUtil.toObject(FS.class, data.toString());
        fs.setId(getReleaseId(fs.getOcid()));
        fs.setDate(releaseDate);
        fs.setTag(Collections.singletonList(Tag.PLANNING));
        fs.setInitiationType(InitiationType.TENDER);
        organizationService.processFsParties(fs);
        relatedProcessService.addEiRelatedProcessToFs(fs, cpid);
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, amount));
        createEiByFs(cpid, fs.getOcid());
        return getResponseDto(cpid, fs.getOcid());
    }

    @Override
    public ResponseDto updateFs(final String cpid,
                                final String ocid,
                                final String stage,
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final FS updateFs = jsonUtil.toObject(FS.class, data.toString());
        final FS fs = jsonUtil.toObject(FS.class, entity.getJsonData());
        final Double updateAmount = updateFs.getPlanning().getBudget().getAmount().getAmount();
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        fs.setId(getReleaseId(ocid));
        fs.setDate(releaseDate);
        updateFsDto(fs, updateFs);
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, updateAmount));
        if (!updateAmount.equals(amount)) {
            updateEiAmountByFs(cpid);
        }
        return getResponseDto(cpid, fs.getOcid());
    }

    @Override
    public void createEiByMs(final List<String> eiIds,
                             final String msCpId,
                             final LocalDateTime dateTime) {
        eiIds.forEach(eiCpId -> {
            final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                    .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
            final EI ei = jsonUtil.toObject(EI.class, entity.getJsonData());
            ei.setId(getReleaseId(eiCpId));
            ei.setDate(dateTime);
            relatedProcessService.addMsRelatedProcessToEi(ei, msCpId);
            budgetDao.saveBudget(getEiEntity(ei, entity.getStage()));
        });
    }

    @Override
    public void createFsByMs(final List<BudgetBreakdown> budgetBreakdown,
                             final String msCpId,
                             final LocalDateTime dateTime) {
        budgetBreakdown.forEach(br -> {
            final String eiCpId = getCpIdFromOcId(br.getId());
            final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpIdAndOcId(eiCpId, br.getId()))
                    .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
            final FS fs = jsonUtil.toObject(FS.class, entity.getJsonData());
            fs.setId(getReleaseId(fs.getOcid()));
            fs.setDate(dateTime);
            fs.setTag(Collections.singletonList(Tag.PLANNING_UPDATE));
            relatedProcessService.addMsRelatedProcessToFs(fs, msCpId);
            budgetDao.saveBudget(getFsEntity(entity.getCpId(), fs, entity.getStage(), entity.getAmount()));
        });
    }

    private String getCpIdFromOcId(final String ocId) {
        final int pos = ocId.indexOf(FS_SEPARATOR);
        return ocId.substring(0, pos);
    }

    private void updateEiDto(final EI ei, final EI updateEi) {
        ei.setTitle(updateEi.getTitle());
        ei.setPlanning(updateEi.getPlanning());
        ei.setTender(updateEi.getTender());
    }

    private void updateFsDto(final FS fs, final FS updateFs) {
        fs.setTitle(updateFs.getTitle());
        fs.setTender(updateFs.getTender());
        fs.setParties(updateFs.getParties());
        fs.setPlanning(updateFs.getPlanning());
    }

    private void createEiByFs(final String eiCpId, final String fsOcId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final EI ei = jsonUtil.toObject(EI.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(eiCpId);
        ei.getPlanning().getBudget().getAmount().setAmount(round2(totalAmount));
        ei.setId(getReleaseId(eiCpId));
        ei.setDate(dateUtil.localNowUTC());
        relatedProcessService.addFsRelatedProcessToEi(ei, fsOcId);
        budgetDao.saveBudget(getEiEntity(ei, entity.getStage()));
    }

    private void updateEiAmountByFs(final String eiCpId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                .orElseThrow(() -> new ErrorException(ErrorType.DATA_NOT_FOUND));
        final EI ei = jsonUtil.toObject(EI.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(eiCpId);
        ei.getPlanning().getBudget().getAmount().setAmount(totalAmount);
        ei.setId(getReleaseId(eiCpId));
        ei.setDate(dateUtil.localNowUTC());
        budgetDao.saveBudget(getEiEntity(ei, entity.getStage()));
    }

    private BudgetEntity getEiEntity(final EI ei, final String stage) {
        final BudgetEntity entity = new BudgetEntity();
        entity.setCpId(ei.getOcid());
        entity.setOcId(ei.getOcid());
        entity.setReleaseDate(dateUtil.localToDate(ei.getDate()));
        entity.setReleaseId(ei.getId());
        entity.setStage(stage);
        entity.setJsonData(jsonUtil.toJson(ei));
        return entity;
    }

    private Double round2(final Double val) {
        if (Objects.nonNull(val))
            return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        else
            return null;
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.milliNowUTC();
    }

    private BudgetEntity getFsEntity(final String cpId,
                                     final FS fs,
                                     final String stage,
                                     final Double amount) {
        final BudgetEntity entity = new BudgetEntity();
        entity.setCpId(cpId);
        entity.setOcId(fs.getOcid());
        entity.setReleaseDate(dateUtil.localToDate(fs.getDate()));
        entity.setReleaseId(fs.getId());
        entity.setStage(stage);
        entity.setAmount(amount);
        entity.setJsonData(jsonUtil.toJson(fs));
        return entity;
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }
}
