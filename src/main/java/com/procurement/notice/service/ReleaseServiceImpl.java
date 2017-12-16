package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.repository.PackageByDateRepository;
import com.procurement.notice.repository.ReleaseRepository;
import com.procurement.notice.utils.JsonUtil;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

//    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
//        .parseCaseInsensitive()
//        .append(ISO_LOCAL_DATE)
//        .appendLiteral('T')
//        .append(ISO_LOCAL_TIME)
//        .appendLiteral('Z')
//        .toFormatter();

    private final ReleaseRepository releaseRepository;
    private final PackageByDateRepository packageByDateRepository;

    private final JsonUtil jsonUtil;

    public ReleaseServiceImpl(final ReleaseRepository releaseRepository,
                              final PackageByDateRepository packageByDateRepository,
                              final JsonUtil jsonUtil) {
        this.releaseRepository = releaseRepository;
        this.packageByDateRepository = packageByDateRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public ResponseDto saveRecordRelease(final RequestDto requestDto) {
        final ReleaseEntity releaseEntity = getReleaseEntity(requestDto);
        releaseRepository.save(releaseEntity);
        return getResponseDto(releaseEntity);
    }

    private ReleaseEntity getReleaseEntity(final RequestDto requestDto) {
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setCpId(requestDto.getCpId());
        releaseEntity.setOcId(requestDto.getOcId());
        releaseEntity.setReleaseDate(requestDto.getReleaseDate());
        releaseEntity.setReleaseId(requestDto.getReleaseId());
        releaseEntity.setJsonData(jsonUtil.toJson(requestDto.getJsonData()));
        return releaseEntity;
    }

//    private PackageByDateEntity getPackageByDateEntity(final ReleaseEntity releaseEntity) {
//        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        final String dayDate = releaseEntity.getReleaseDate()
//                                            .format(formatter);
//        final PackageByDateEntity packageByDateEntity = new PackageByDateEntity();
//        packageByDateEntity.setDay_date(dayDate);
//        packageByDateEntity.setReleaseDate(releaseEntity.getReleaseDate());
//        packageByDateEntity.setCpId(releaseEntity.getCpId());
//        packageByDateEntity.setOcId(releaseEntity.getOcId());
//        packageByDateEntity.setReleaseId(releaseEntity.getReleaseId());
//        return packageByDateEntity;
//    }

    private ResponseDto getResponseDto(final ReleaseEntity releaseEntity) {
        final Map<String, Object> data = new LinkedHashMap<>();
        data.put("cpid", releaseEntity.getCpId());
        data.put("ocid", releaseEntity.getOcId());
        data.put("releaseId", releaseEntity.getReleaseId());
        data.put("releaseDate", releaseEntity.getReleaseDate());
        final ResponseDetailsDto details = new ResponseDetailsDto(HttpStatus.CREATED.toString(), "created");
        return new ResponseDto(true, Collections.singletonList(details), data);
    }
}
