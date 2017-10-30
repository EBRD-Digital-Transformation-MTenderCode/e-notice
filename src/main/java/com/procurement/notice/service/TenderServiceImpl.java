package com.procurement.notice.service;

import com.procurement.notice.model.dto.Release;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.repository.TenderRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class TenderServiceImpl implements TenderService {

    private TenderRepository tenderRepository;

    private ConversionService conversionService;

    public TenderServiceImpl(TenderRepository tenderRepository, ConversionService conversionService) {
        this.tenderRepository = tenderRepository;
        this.conversionService = conversionService;
    }

    @Override
    public void insertData(Release dataDto) {
        Objects.requireNonNull(dataDto);
        convertDtoToEntity(dataDto)
            .ifPresent(tender -> tenderRepository.save(tender));
    }

    public Optional<TenderEntity> convertDtoToEntity(Release dataDto) {
        TenderEntity tenderEntity = conversionService.convert(dataDto, TenderEntity.class);
        return Optional.of(tenderEntity);
    }
}
