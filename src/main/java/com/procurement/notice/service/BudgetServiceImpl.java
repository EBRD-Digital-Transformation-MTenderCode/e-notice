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
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.util.Arrays;
import java.util.LinkedHashSet;
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
        ei.setId(getReleaseId(cpid));
        ei.setTag(Arrays.asList(Tag.COMPILED));
        ei.setInitiationType(InitiationType.TENDER);
        processEiParties(ei);
        budgetDao.saveBudget(getEiEntity(ei, stage));
        return getResponseDto(ei.getOcid(), ei.getOcid());
    }

    @Override
    public ResponseDto updateEi(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(cpid))
                .orElseThrow(() -> new ErrorException(EI_NOT_FOUND_ERROR));
        final ReleaseEI updateEi = jsonUtil.toObject(ReleaseEI.class, data.toString());
        final ReleaseEI ei = jsonUtil.toObject(ReleaseEI.class, entity.getJsonData());
        ei.setId(getReleaseId(cpid));
        updateEiDto(ei, updateEi);
        budgetDao.saveBudget(getEiEntity(ei, stage));
        return getResponseDto(ei.getOcid(), ei.getOcid());
    }

    @Override
    public ResponseDto createFs(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseFS fs = jsonUtil.toObject(ReleaseFS.class, data.toString());
        fs.setId(getReleaseId(fs.getOcid()));
        fs.setTag(Arrays.asList(Tag.COMPILED));
        fs.setInitiationType(InitiationType.TENDER);
        addEiRelatedProcessToFs(fs, cpid);
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, amount));
        createEiByFs(cpid, fs.getOcid());
        return getResponseDto(cpid, fs.getOcid());
    }

    @Override
    public ResponseDto updateFs(final String cpid,
                                final String ocid,
                                final String stage,
                                final JsonNode data) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpIdAndOcId(cpid, ocid))
                .orElseThrow(() -> new ErrorException(FS_NOT_FOUND_ERROR));
        final ReleaseFS updateFs = jsonUtil.toObject(ReleaseFS.class, data.toString());
        final ReleaseFS fs = jsonUtil.toObject(ReleaseFS.class, entity.getJsonData());
        final Double updateAmount = updateFs.getPlanning().getBudget().getAmount().getAmount();
        final Double amount = fs.getPlanning().getBudget().getAmount().getAmount();
        fs.setId(getReleaseId(ocid));
        updateFsDto(fs, updateFs);
        budgetDao.saveBudget(getFsEntity(cpid, fs, stage, updateAmount));
        if (updateAmount!=amount){
            updateEiAmountByFs(cpid);
        }
        return getResponseDto(cpid, fs.getOcid());
    }

    private void processEiParties(final ReleaseEI ei) {
        ei.getParties().forEach(p -> p.setId(p.getIdentifier().getScheme() + "-" + p.getIdentifier().getId()));
        final Optional<Organization> partyOptional = ei.getParties().stream()
                .filter(p -> p.getRoles().contains(Organization.PartyRole.BUYER))
                .findFirst();
        if (partyOptional.isPresent()) {
            Organization party = partyOptional.get();
            final OrganizationReference buyer = new OrganizationReference(
                    party.getId(),
                    party.getName(),
                    party.getIdentifier(),
                    party.getAddress(),
                    new LinkedHashSet(party.getAdditionalIdentifiers()),
                    party.getContactPoint()
            );
            ei.setBuyer(buyer);
        }
    }

    private void updateEiDto(final ReleaseEI ei, final ReleaseEI updateEi) {
        ei.setTitle(updateEi.getTitle());
        ei.setDescription(updateEi.getDescription());
        ei.setPlanning(updateEi.getPlanning());
        ei.setTender(updateEi.getTender());
        ei.setParties(updateEi.getParties());
    }

    private void updateFsDto(final ReleaseFS fs, final ReleaseFS updateFs) {
        fs.setTitle(updateFs.getTitle());
        fs.setDescription(updateFs.getDescription());
        fs.setTender(updateFs.getTender());
        fs.setParties(updateFs.getParties());
        fs.setPlanning(updateFs.getPlanning());
    }

    private String getReleaseId(final String ocId) {
        return ocId + SEPARATOR + dateUtil.getMilliNowUTC();
    }

    public void createEiByFs(final String eiCpId, final String fsOcId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                .orElseThrow(() -> new ErrorException(EI_NOT_FOUND_ERROR));
        final ReleaseEI ei = jsonUtil.toObject(ReleaseEI.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(eiCpId);
        ei.getPlanning().getBudget().getAmount().setAmount(totalAmount);
        ei.setId(getReleaseId(eiCpId));
        ei.setDate(dateUtil.getNowUTC());
        addFsRelatedProcessToEi(ei, fsOcId);
        budgetDao.saveBudget(getEiEntity(ei, entity.getStage()));
    }

    public void updateEiAmountByFs(final String eiCpId) {
        final BudgetEntity entity = Optional.ofNullable(budgetDao.getByCpId(eiCpId))
                .orElseThrow(() -> new ErrorException(EI_NOT_FOUND_ERROR));
        final ReleaseEI ei = jsonUtil.toObject(ReleaseEI.class, entity.getJsonData());
        final Double totalAmount = budgetDao.getTotalAmountByCpId(eiCpId);
        ei.getPlanning().getBudget().getAmount().setAmount(totalAmount);
        ei.setId(getReleaseId(eiCpId));
        ei.setDate(dateUtil.getNowUTC());
        budgetDao.saveBudget(getEiEntity(ei, entity.getStage()));
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

    private BudgetEntity getEiEntity(final ReleaseEI ei,
                                     final String stage) {
        final BudgetEntity entity = new BudgetEntity();
        entity.setCpId(ei.getOcid());
        entity.setOcId(ei.getOcid());
        entity.setReleaseDate(dateUtil.localToDate(ei.getDate()));
        entity.setReleaseId(ei.getId());
        entity.setStage(stage);
        entity.setJsonData(jsonUtil.toJson(ei));
        return entity;
    }

    private BudgetEntity getFsEntity(final String cpId,
                                     final ReleaseFS fs,
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
