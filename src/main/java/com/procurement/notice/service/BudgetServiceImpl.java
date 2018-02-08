package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.BudgetDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.ein.ReleaseEIN;
import com.procurement.notice.model.ein.ReleaseFS;
import com.procurement.notice.model.entity.BudgetEntity;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

    private static final String SEPARATOR = "-";
    private static final String EIN_NOT_FOUND_ERROR = "EIN not found.";
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
        final ReleaseEIN ein = jsonUtil.toObject(ReleaseEIN.class, data.toString());
        ein.setTag(Arrays.asList(Tag.COMPILED));
        ein.setInitiationType(InitiationType.TENDER);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, ein));
        return getResponseDto(ein.getOcid(), ein.getOcid());
    }

    @Override
    public ResponseDto updateEi(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(cpid))
                .orElseThrow(() -> new ErrorException(EIN_NOT_FOUND_ERROR));
        final ReleaseEIN updateEinDto = jsonUtil.toObject(ReleaseEIN.class, data.toString());
        final ReleaseEIN einFromEntity = jsonUtil.toObject(ReleaseEIN.class, entity.getJsonData());
        updateEinDto(einFromEntity, updateEinDto);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, einFromEntity));
        return getResponseDto(einFromEntity.getOcid(), einFromEntity.getOcid());
    }

    private void updateEinDto(final ReleaseEIN einFromEntity, final ReleaseEIN updateEinDto) {
        einFromEntity.setTitle(updateEinDto.getTitle());
        einFromEntity.setDescription(updateEinDto.getDescription());
        einFromEntity.setPlanning(updateEinDto.getPlanning());
        einFromEntity.setTender(updateEinDto.getTender());
        einFromEntity.setParties(updateEinDto.getParties());
        einFromEntity.setBuyer(updateEinDto.getBuyer());
    }

    @Override
    public ResponseDto createFs(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseFS fs = jsonUtil.toObject(ReleaseFS.class, data.toString());
        fs.setOcid(getOcId(cpid, stage));
        fs.setTag(Arrays.asList(Tag.COMPILED));
        fs.setInitiationType(InitiationType.TENDER);
        addEinRelatedProcessToFs(fs, cpid);
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        budgetDao.saveBudget(getEntity(cpid, fs.getOcid(), stage, amount, fs));
        updateEinByFs(cpid, fs.getOcid());
        return getResponseDto(fs.getOcid(), fs.getOcid());
    }

    @Override
    public ResponseDto updateFs(final String cpid,
                                final String ocid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(FS_NOT_FOUND_ERROR));
        final ReleaseFS updateFsDto = jsonUtil.toObject(ReleaseFS.class, data.toString());
        final ReleaseFS fsFromEntity = jsonUtil.toObject(ReleaseFS.class, entity.getJsonData());
        updateFsDto(fsFromEntity, updateFsDto);
        budgetDao.saveBudget(getEntity(cpid, cpid, stage, 0D, fsFromEntity));
        return getResponseDto(fsFromEntity.getOcid(), fsFromEntity.getOcid());
    }

    private void updateFsDto(final ReleaseFS fsFromEntity, final ReleaseFS updateFsDto) {
        fsFromEntity.setTitle(updateFsDto.getTitle());
        fsFromEntity.setDescription(updateFsDto.getDescription());
        fsFromEntity.setParties(updateFsDto.getParties());
        fsFromEntity.setPlanning(updateFsDto.getPlanning());
    }

    private String getOcId(final String cpId, final String stage) {
        return cpId + SEPARATOR + stage + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    public void updateEinByFs(final String einCpId, final String fsOcId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(einCpId))
                .orElseThrow(() -> new ErrorException(EIN_NOT_FOUND_ERROR));
        final ReleaseEIN ein = jsonUtil.toObject(ReleaseEIN.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(einCpId);
        ein.getPlanning().getBudget().getAmount().setAmount(totalAmount);
        addFsRelatedProcessToEin(ein, fsOcId);
        budgetDao.saveBudget(getEntity(
                einCpId,
                einCpId,
                entity.getStage(),
                totalAmount,
                ein));
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

    private void addEinRelatedProcessToFs(final ReleaseFS fs, final String einOcId) {
        final RelatedProcess relatedProcess = new RelatedProcess(
                UUIDs.timeBased().toString(),
                Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                "",
                RelatedProcess.RelatedProcessScheme.OCID,
                einOcId,
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
