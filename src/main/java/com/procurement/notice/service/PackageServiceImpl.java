package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDetailsDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.model.entity.PackageEntity;
import com.procurement.notice.repository.PackageRepository;
import com.procurement.notice.utils.JsonUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    private final JsonUtil jsonUtil;

    public PackageServiceImpl(final PackageRepository packageRepository,
                              final JsonUtil jsonUtil) {
        this.packageRepository = packageRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public ResponseDto savePackage(final String cpId, final RequestDto requestDto) {
        Objects.requireNonNull(requestDto.getData());
        packageRepository.save(getPackageEntity(cpId, requestDto));
        return getResponseDto();
    }

    private PackageEntity getPackageEntity(final String cpId, final RequestDto requestDto) {
        final PackageEntity packageEntity = new PackageEntity();
        packageEntity.setCpId(cpId);
        packageEntity.setPackageDate(LocalDateTime.now());
        packageEntity.setJsonData(jsonUtil.toJson(requestDto.getData()));
        return packageEntity;
    }

    private ResponseDto getResponseDto() {
        final ResponseDetailsDto details = new ResponseDetailsDto(HttpStatus.CREATED.toString(), "created");
        return new ResponseDto(true, Collections.singletonList(details), null);
    }
}
