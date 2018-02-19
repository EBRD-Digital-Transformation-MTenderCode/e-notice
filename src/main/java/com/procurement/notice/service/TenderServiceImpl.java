package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.procurement.notice.dao.TenderDao;
import com.procurement.notice.model.bpe.ResponseDto;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.model.ocds.TenderStatusDetails;
import com.procurement.notice.model.tender.ReleaseMS;
import com.procurement.notice.model.tender.ReleasePS;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class TenderServiceImpl implements TenderService {

    private static final String SEPARATOR = "-";
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
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final ReleaseMS ms = jsonUtil.toObject(ReleaseMS.class, data.toString());
        ms.setOcid(cpid);
        ms.setId(getReleaseId(cpid));
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(InitiationType.TENDER);
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);
        final ReleasePS ps = jsonUtil.toObject(ReleasePS.class, data.toString());
        ps.setOcid(getOcId(cpid, stage));
        ps.setId(getReleaseId(ps.getOcid()));
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
        tenderDao.saveTender(getEntity(ms.getOcid(), ms.getOcid(), stage, releaseDate, ms));
        tenderDao.saveTender(getEntity(ms.getOcid(), ps.getOcid(), stage, releaseDate, ps));
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
}
