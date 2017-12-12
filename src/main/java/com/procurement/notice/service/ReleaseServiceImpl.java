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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ReleaseServiceImpl implements ReleaseService {

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
    public ResponseDto savePackage(final String cpId, final RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
        releaseRepository.save(getReleaseEntity(cpId, cpId, 1, cpId, requestDto));
        return getResponseDto(cpId);
    }

    @Override
    public ResponseDto saveRelease(String cpid,
                                   String ocid,
                                   String tag,
                                   String language,
                                   String initiationType,
                                   RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
        Optional<Integer> optionalReleaseVersion = releaseRepository.getLastReleaseVersion(cpid, ocid);
        int releaseVersion = 1;
        if (optionalReleaseVersion.isPresent()) {
            releaseVersion = optionalReleaseVersion.get() + 1;
        }
        String releaseId = ocid + "-" + releaseVersion;
        ReleaseEntity releaseEntity = getReleaseEntity(cpid, ocid, releaseVersion, releaseId, requestDto);
        releaseRepository.save(releaseEntity);
        packageByDateRepository.save(getPackageByDateEntity(releaseEntity));
        return getResponseDto(cpid);
    }

    private ReleaseEntity getReleaseEntity(final String cpId,
                                           final String ocid,
                                           final int releaseVersion,
                                           final String releaseId,
                                           final RequestDto requestDto) {
        final ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setCpId(cpId);
        releaseEntity.setOcId(ocid);
        releaseEntity.setReleaseDate(LocalDateTime.now());
        releaseEntity.setReleaseVersion(releaseVersion);
        releaseEntity.setReleaseId(releaseId);
        releaseEntity.setJsonData(jsonUtil.toJson(requestDto.getData()));
        return releaseEntity;
    }

    private PackageByDateEntity getPackageByDateEntity(final ReleaseEntity releaseEntity) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String dayDate = releaseEntity.getReleaseDate().format(formatter);
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
