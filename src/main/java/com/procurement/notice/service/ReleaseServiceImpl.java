package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.databind.JsonNode;
import com.procurement.notice.dao.ReleaseDao;
import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.model.ocds.TenderStatusDetails;
import com.procurement.notice.model.ocds.cn.ReleaseMS;
import com.procurement.notice.model.ocds.cn.ReleasePS;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private static final String SEPARATOR = "-";
    private final ReleaseDao releaseDao;
    private final JsonUtil jsonUtil;
    private final DateUtil dateUtil;

    public ReleaseServiceImpl(final ReleaseDao releaseDao,
                              final JsonUtil jsonUtil,
                              final DateUtil dateUtil) {
        this.releaseDao = releaseDao;
        this.jsonUtil = jsonUtil;
        this.dateUtil = dateUtil;
    }

    @Override
    public ResponseDto createCn(final String cpid,
                                final String stage,
                                final String operation,
                                final LocalDateTime releaseDate,
                                final JsonNode data) {
        final ReleaseMS ms = jsonUtil.toObject(ReleaseMS.class, data.toString());
        final LocalDateTime addedDate = dateUtil.getNowUTC();
        final long timeStamp = dateUtil.getMilliUTC(addedDate);
        ms.setOcid(cpid);
        ms.setDate(releaseDate);
        ms.setTag(Arrays.asList(Tag.COMPILED));
        ms.setInitiationType(ReleaseMS.InitiationType.TENDER);
        ms.setId(getId(cpid, timeStamp));
        ms.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);

        final ReleasePS ps = jsonUtil.toObject(ReleasePS.class, data.toString());
        ps.setOcid(getOcId(cpid, stage, timeStamp));
        ps.setDate(releaseDate);
        ps.setTag(Arrays.asList(Tag.COMPILED));
        ps.setInitiationType(ReleasePS.InitiationType.TENDER);
        ps.setId(getId(ps.getOcid(), timeStamp));
        ps.getTender().setStatusDetails(TenderStatusDetails.PRESELECTION);

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
        releaseDao.save(getEntity(ms.getOcid(), ms.getOcid(), ms.getId(), stage, releaseDate, ms));

        releaseDao.save(getEntity(ms.getOcid(), ps.getOcid(), ps.getId(), stage, releaseDate, ps));

        return new ResponseDto(true, null, Arrays.asList(ms.getOcid(), ps.getOcid()));
    }

    @Override
    public ResponseDto saveRecordRelease(final RequestDto requestDto) {
//        final LocalDateTime addedDate = dateUtil.getNowUTC();
//        final long timeStamp = dateUtil.getMilliUTC(addedDate);
//        final ReleaseExt releaseExt = jsonUtil.toObject(ReleaseExt.class, requestDto.getJsonData().toString());
//        releaseExt.setDate(addedDate);
//        releaseExt.setOcid(requestDto.getOcId());
//        releaseExt.setTag(getTags(requestDto.getTag()));
//        releaseExt.setInitiationType(ReleaseExt.InitiationType.fromValue(requestDto.getInitiationType()));
//        releaseExt.setLanguage(requestDto.getLanguage());
//        releaseExt.setId(getOcId(requestDto.getOcId(), requestDto.getStage(), timeStamp));
//        final ReleaseEntity releaseEntity = getReleaseEntity(requestDto.getCpId(), requestDto.getStage(), releaseExt);
//        releaseDao.save(releaseEntity);
//        return getResponseDto(releaseEntity);
        return null;
    }


    private String getOcId(final String ocId, final String stage, final long timeStamp) {
        return ocId + SEPARATOR + stage + SEPARATOR + timeStamp;
    }

    private String getId(final String ocId, final long timeStamp) {
        return ocId + SEPARATOR + timeStamp;
    }

    private ReleaseEntity getEntity(final String cpId,
                                    final String ocId,
                                    final String releaseId,
                                    final String stage,
                                    final LocalDateTime releaseDate,
                                    final Object jsonData) {
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocId);
        releaseEntity.setReleaseDate(dateUtil.localToDate(releaseDate));
        releaseEntity.setReleaseId(releaseId);
        releaseEntity.setStage(stage);
        releaseEntity.setJsonData(jsonUtil.toJson(jsonData));
        return releaseEntity;
    }
}
