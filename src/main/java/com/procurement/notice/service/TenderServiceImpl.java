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
import com.procurement.notice.model.tender.TenderPeriodEndDto;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
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
    public ResponseDto createCn(final String cpid,
                                final String stage,
                                final JsonNode data) {
        final ReleaseMS ms = jsonUtil.toObject(ReleaseMS.class, data.toString());
        ms.setOcid(cpid);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        final ReleaseTender ps = jsonUtil.toObject(ReleaseTender.class, data.toString());
        ps.setOcid(getOcId(cpid, stage));
        ps.setId(getReleaseId(ps.getOcid()));
        ps.setDate(ms.getDate());
        ps.setTag(Arrays.asList(Tag.COMPILED));
        ps.setInitiationType(InitiationType.TENDER);
        ps.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        ps.getPlanning().getBudget().setId(getId());

        ms.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.X_PRESELECTION),
                        "",
                        RelatedProcess.RelatedProcessScheme.OCID,
                        ps.getOcid(),
                        "")
        );

        ps.getRelatedProcesses().add(
                new RelatedProcess(
                        UUIDs.timeBased().toString(),
                        Arrays.asList(RelatedProcess.RelatedProcessType.PARENT),
                        "",
                        RelatedProcess.RelatedProcessScheme.OCID,
                        ms.getOcid(),
                        "")
        );
        tenderDao.saveTender(getEntity(ms.getOcid(), ms.getOcid(), "ms", ms.getDate(), ms));
        tenderDao.saveTender(getEntity(ms.getOcid(), ps.getOcid(), stage, ps.getDate(), ps));
        return getResponseDto(ms.getOcid(), ps.getOcid());
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

    private TenderEntity getEntity(final String cpId,
                                   final String ocId,
                                   final String stage,
                                   final LocalDateTime releaseDate,
                                   final Object jsonData) {
        final TenderEntity releaseEntity = new TenderEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocId);
        releaseEntity.setReleaseDate(dateUtil.localToDate(releaseDate));
        releaseEntity.setReleaseId(getReleaseId(ocId));
        releaseEntity.setStage(stage);
        releaseEntity.setJsonData(jsonUtil.toJson(jsonData));
        return releaseEntity;
    }

    private ResponseDto getResponseDto(final String cpid, final String ocid) {
        final ObjectNode jsonForResponse = jsonUtil.createObjectNode();
        jsonForResponse.put("cpid", cpid);
        jsonForResponse.put("ocid", ocid);
        return new ResponseDto<>(true, null, jsonForResponse);
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
        tenderDao.saveTender(getEntity(cpid, tender.getOcid(), stage, tender.getDate(), tender));
        return null;
    }
}
