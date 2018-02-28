package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.exception.ErrorException;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.*;
import com.procurement.notice.model.tender.ReleaseMS;
import com.procurement.notice.model.tender.ReleaseTender;
import com.procurement.notice.model.tender.SuspendTenderDto;
import com.procurement.notice.model.tender.TenderPeriodEndDto;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TenderServiceImpl implements TenderService {

    private static final String SEPARATOR = "-";
    private static final String RELEASE_NOT_FOUND_ERROR = "Release not found by stage: ";
    private final TenderDao tenderDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public TenderServiceImpl(final TenderDao tenderDao,
                             final JsonUtil jsonUtil,
                             final DateUtil dateUtil) {
        this.tenderDao = tenderDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public TenderEntity getTenderEntity(final String cpId,
                                        final String stage,
                                        final ReleaseTender tender) {
        final TenderEntity tenderEntity = new TenderEntity();
        tenderEntity.setCpId(cpId);
        tenderEntity.setStage(stage);
        tenderEntity.setOcId(tender.getOcid());
        tenderEntity.setJsonData(jsonUtil.toJson(tender));
        tenderEntity.setReleaseId(tender.getId());
        tenderEntity.setReleaseDate(dateUtil.localToDate(tender.getDate()));
        return tenderEntity;
    }

    @Override
    public ResponseDto createCn(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseMS ms = jsonUtil.toObject(ReleaseMS.class, data.toString());
        ms.setOcid(cpid);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        final ReleaseTender tender = jsonUtil.toObject(ReleaseTender.class, data.toString());
        tender.setOcid(getOcId(cpid, stage));
        tender.setId(getReleaseId(tender.getOcid()));
        tender.setDate(ms.getDate());
        tender.setTag(Arrays.asList(Tag.COMPILED));
        tender.setInitiationType(InitiationType.TENDER);
        tender.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        tender.getPlanning().getBudget().setId(getId());

        ms.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.X_PRESELECTION),
                        "",
                        RelatedProcess.RelatedProcessScheme.OCID,
                        tender.getOcid(),
                        "")
        );

        tender.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                        "",
                        RelatedProcess.RelatedProcessScheme.OCID,
                        ms.getOcid(),
                        "")
        );
        tenderDao.saveTender(getMSEntity(ms.getOcid(), ms));
        tenderDao.saveTender(getTenderEntity(ms.getOcid(), stage, tender));
        return getResponseDto(ms.getOcid(), tender.getOcid());
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

    private TenderEntity getMSEntity(final String cpId,
                                     final ReleaseMS ms) {
        final TenderEntity msEntity = new TenderEntity();
        msEntity.setCpId(cpId);
        msEntity.setStage("ms");
        msEntity.setOcId(ms.getOcid());
        msEntity.setJsonData(jsonUtil.toJson(ms));
        msEntity.setReleaseId(ms.getId());
        msEntity.setReleaseDate(dateUtil.localToDate(ms.getDate()));
        return msEntity;
    }

    @Override
    public ResponseDto tenderPeriodEnd(final String cpid, final String stage, final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR + stage));
        final ReleaseTender tender = jsonUtil.toObject(ReleaseTender.class, entity.getJsonData());
        final TenderPeriodEndDto updateDto = jsonUtil.toObject(TenderPeriodEndDto.class, data.toString());
        tender.setId(getReleaseId(tender.getOcid()));
        tender.setDate(updateDto.getAwardPeriod().getStartDate());
        tender.setTag(Arrays.asList(Tag.AWARD));
        tender.setAwards(new LinkedHashSet<>(updateDto.getAwards()));
        tender.setBids(new Bids(null, updateDto.getBids()));
        tenderDao.saveTender(getTenderEntity(cpid, stage, tender));
        return null;
    }

    @Override
    public ResponseDto suspendTender(final String cpid,
                                     final String stage,
                                     final JsonNode data) {
        final TenderEntity entity = Optional.ofNullable(tenderDao.getByCpIdAndStage(cpid, stage))
                .orElseThrow(() -> new ErrorException(RELEASE_NOT_FOUND_ERROR));
        final ReleaseTender release = jsonUtil.toObject(ReleaseTender.class, entity.getJsonData());
        final SuspendTenderDto dto = jsonUtil.toObject(SuspendTenderDto.class, jsonUtil.toJson(data));
        release.setDate(dateUtil.getNowUTC());
        release.setId(getReleaseId(release.getOcid()));
        release.getTender().setStatusDetails(dto.getTender().getStatusDetails());
        tenderDao.saveTender(getTenderEntity(cpid, stage, release));
        return getResponseDto(cpid, release.getOcid());
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
    }

}
