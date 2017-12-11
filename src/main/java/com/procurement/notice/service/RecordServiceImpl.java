package com.procurement.notice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.repository.ReleaseRepository;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

    private final ReleaseRepository recordRepository;

    private final JsonUtil jsonUtil;

    public RecordServiceImpl(final ReleaseRepository recordRepository,
                             final JsonUtil jsonUtil) {
        this.recordRepository = recordRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public ResponseDto savePackage(final String cpId, final RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
        recordRepository.save(getPackageEntity(cpId, requestDto));
        return getResponseDto(cpId);
    }

    private ReleaseEntity getPackageEntity(final String cpId, final RequestDto requestDto) {
        final ReleaseEntity recordEntity = new ReleaseEntity();
        recordEntity.setCpId(cpId);
        recordEntity.setOcId(cpId);
        recordEntity.setReleaseDate(LocalDateTime.now());
        recordEntity.setReleaseId(UUIDs.timeBased());
        recordEntity.setJsonData(jsonUtil.toJson(requestDto.getData()));
        return recordEntity;
    }

    private ResponseDto getResponseDto(final String cpId) {
        final Map<String, String> data = new HashMap<>();
        data.put("cpid", cpId);
        final ResponseDetailsDto details = new ResponseDetailsDto(HttpStatus.CREATED.toString(), "created");
        return new ResponseDto(true, Collections.singletonList(details), data);
    }
}
