package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.PackageByDateEntity;
import com.procurement.notice.model.entity.ReleaseEntity;
import com.procurement.notice.repository.PackageByDateRepository;
import com.procurement.notice.repository.ReleaseRepository;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .append(ISO_LOCAL_DATE)
        .appendLiteral('T')
        .append(ISO_LOCAL_TIME)
        .appendLiteral('Z')
        .toFormatter();

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
    public ResponseDto saveRecordRelease(final String cpId, final RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
        ReleaseEntity releaseEntity = getReleaseEntity(requestDto, cpId);
        releaseRepository.save(releaseEntity);
        return getResponseDto(cpId);
    }

    @Override
    public ResponseDto saveTwineRecordRelease(final RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
//        Optional<Integer> optionalReleaseVersion = releaseRepository.getLastReleaseVersion(cpid, ocid);
//        int releaseVersion = 1;
//        if (optionalReleaseVersion.isPresent()) {
//            releaseVersion = optionalReleaseVersion.get() + 1;
//        }
//        String releaseId = ocid + "-" + releaseVersion;
        ReleaseEntity releaseEntity = getReleaseEntity(requestDto, "");
        releaseRepository.save(releaseEntity);
//        packageByDateRepository.save(getPackageByDateEntity(releaseEntity));
        return getResponseDto(releaseEntity.getCpId());
    }

    private ReleaseEntity getReleaseEntity(final RequestDto requestDto, String cpId) {
        final Map data = (LinkedHashMap<String, String>) requestDto.getData();
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        final String ocId = (String) data.get("ocid");
        if (cpId.isEmpty()){
            cpId = ocId;
        }
        final LocalDateTime addedDate = LocalDateTime.parse((String) data.get("date"), FORMATTER);
        final String releaseId = (String) data.get("id");
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocId);
        releaseEntity.setReleaseDate(addedDate);
        releaseEntity.setReleaseVersion(1);
        releaseEntity.setReleaseId(releaseId);
        releaseEntity.setJsonData(jsonUtil.toJson(data));
        return releaseEntity;
    }

//    private ReleaseEntity getReleaseEntity(String cpid,
//                                           String ocid,
//                                           String tag,
//                                           String initiationType,
//                                           String language,
//                                           final int releaseVersion,
//                                           final String releaseId,
//                                           final RequestDto requestDto) {
//        final Map data = new LinkedHashMap<String, String>();
//        final LocalDateTime addedDate = LocalDateTime.now();
//        data.put("ocid", data.get("ocid"));
//        data.put("date", addedDate.format(FORMATTER));
//        data.put("tag", Arrays.asList(tag));
//        data.put("language", language);
//        data.put("initiationType", initiationType);
//        data.putAll((LinkedHashMap<String, String>)requestDto.getData());
//        final ReleaseEntity releaseEntity = new ReleaseEntity();
//        releaseEntity.setCpId(cpid);
//        releaseEntity.setOcId(ocid);
//        releaseEntity.setReleaseDate(addedDate);
//        releaseEntity.setReleaseVersion(releaseVersion);
//        releaseEntity.setReleaseId(releaseId);
//        releaseEntity.setJsonData(jsonUtil.toJson(data));
//        return releaseEntity;
//    }

    private PackageByDateEntity getPackageByDateEntity(final ReleaseEntity releaseEntity) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String dayDate = releaseEntity.getReleaseDate()
                                            .format(formatter);
        final PackageByDateEntity packageByDateEntity = new PackageByDateEntity();
        packageByDateEntity.setDay_date(dayDate);
        packageByDateEntity.setReleaseDate(releaseEntity.getReleaseDate());
        packageByDateEntity.setCpId(releaseEntity.getCpId());
        packageByDateEntity.setOcId(releaseEntity.getOcId());
        packageByDateEntity.setReleaseId(releaseEntity.getReleaseId());
        return packageByDateEntity;
    }

    private ResponseDto getResponseDto(final String cpId) {
        final Map<String, String> data = new HashMap<>();
        data.put("cpid", cpId);
        final ResponseDetailsDto details = new ResponseDetailsDto(HttpStatus.CREATED.toString(), "created");
        return new ResponseDto(true, Collections.singletonList(details), data);
    }
}
