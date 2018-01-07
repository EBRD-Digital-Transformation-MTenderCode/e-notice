package com.procurement.notice.service;

import com.procurement.notice.dao.ReleaseDao;
import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.model.ocds.ReleaseExt;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.utils.DateUtil;
import com.procurement.notice.utils.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService {

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
    public ResponseDto saveRecordRelease(final RequestDto requestDto) {
        final LocalDateTime addedDate = dateUtil.getNowUTC();
        final long timeStamp = dateUtil.getMilliUTC(addedDate);
        final ReleaseExt releaseExt = jsonUtil.toObject(ReleaseExt.class, requestDto.getJsonData().toString());
        releaseExt.setDate(addedDate);
        releaseExt.setOcid(requestDto.getOcId());
        releaseExt.setTag(getTags(requestDto.getTag()));
        releaseExt.setInitiationType(ReleaseExt.InitiationType.fromValue(requestDto.getInitiationType()));
        releaseExt.setLanguage(requestDto.getLanguage());
        releaseExt.setId(getId(requestDto.getOcId(), requestDto.getStage(), timeStamp));
        final ReleaseEntity releaseEntity = getReleaseEntity(requestDto.getCpId(), requestDto.getStage(), releaseExt);
        releaseDao.save(releaseEntity);
        return getResponseDto(releaseEntity);
    }

    private List<Tag> getTags(final List<String> tag) {
        return tag.stream().map(s -> Tag.fromValue(s)).collect(Collectors.toList());
    }

    private String getId(final String ocId, String stage, final long timeStamp) {
        return ocId + "-" + stage + "-" + timeStamp;
    }

    private ReleaseEntity getReleaseEntity(final String cpId, final String stage, final ReleaseExt releaseExt) {
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(releaseExt.getOcid());
        releaseEntity.setReleaseDate(dateUtil.localDateTimeToDate(releaseExt.getDate()));
        releaseEntity.setReleaseId(releaseExt.getId());
        releaseEntity.setStage(stage);
        releaseEntity.setJsonData(jsonUtil.toJson(releaseExt));
        return releaseEntity;
    }

    private ResponseDto getResponseDto(final ReleaseEntity releaseEntity) {
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("cpid", releaseEntity.getCpId());
        data.put("ocid", releaseEntity.getOcId());
        data.put("releaseId", releaseEntity.getReleaseId());
        data.put("releaseDate", dateUtil.dateToLocalDateTime(releaseEntity.getReleaseDate()));
        data.put("stage", releaseEntity.getStage());
        final ResponseDetailsDto details = new ResponseDetailsDto(HttpStatus.CREATED.toString(), "created");
        return new ResponseDto(true, Collections.singletonList(details), data);
    }
}
