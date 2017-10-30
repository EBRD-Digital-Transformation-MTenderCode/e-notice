package com.procurement.notice.service;


import com.procurement.notice.model.dto.Tender;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.repository.TenderRepository;
import com.procurement.notice.utils.JsonUtil;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TenderServiceImpl implements TenderService {

    private TenderRepository tenderRepository;

    private JsonUtil jsonUtil;

    public TenderServiceImpl(TenderRepository tenderRepository,
                             JsonUtil jsonUtil) {
        this.tenderRepository = tenderRepository;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void insertData(String ocId, Date addedDate, Tender tenderDto) {
        Objects.requireNonNull(ocId);
        Objects.requireNonNull(addedDate);
        Objects.requireNonNull(tenderDto);
        convertDtoToEntity(ocId, addedDate, tenderDto)
            .ifPresent(tender -> {
                tenderRepository.save(tender);
            });
    }

    @Override
    public void updateData(String ocId, Date addedDate, Tender tenderDto) {
        Objects.requireNonNull(ocId);
        Objects.requireNonNull(addedDate);
        Objects.requireNonNull(tenderDto);
        TenderEntity sourceTenderEntity = tenderRepository.getLastByOcId(ocId);
        Tender mergedTender = mergeJson(sourceTenderEntity, tenderDto);
        convertDtoToEntity(ocId, addedDate, mergedTender)
            .ifPresent(tender -> {
                tenderRepository.save(tender);
            });
    }

    public Tender mergeJson(TenderEntity tenderEntity, Tender tenderDto) {
        Objects.requireNonNull(tenderEntity);
        Objects.requireNonNull(tenderDto);
        String sourceJson = tenderEntity.getJsonData();
        String updateJson = jsonUtil.toJson(tenderDto);
        String mergedJson = jsonUtil.merge(sourceJson, updateJson);
        return jsonUtil.toObject(Tender.class, mergedJson);
    }

    public Optional<TenderEntity> convertDtoToEntity(String ocId, Date addedDate, Tender tenderDto) {
        String tenderJson = jsonUtil.toJson(tenderDto);
        if (!tenderJson.equals("{}")) {
            TenderEntity tenderEntity = new TenderEntity();
            tenderEntity.setOcId(ocId);
            tenderEntity.setAddedDate(addedDate);
            tenderEntity.setJsonData(tenderJson);
            return Optional.of(tenderEntity);
        } else {
            return Optional.empty();
        }
    }
}
