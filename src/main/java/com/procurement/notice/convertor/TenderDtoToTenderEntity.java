package com.procurement.notice.convertor;

import com.procurement.notice.model.dto.Release;
import com.procurement.notice.model.entity.TenderEntity;
import com.procurement.notice.utils.JsonUtil;
import org.springframework.core.convert.converter.Converter;

public class TenderDtoToTenderEntity implements Converter<Release, TenderEntity> {

    private JsonUtil jsonUtil;

    public TenderDtoToTenderEntity(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    @Override
    public TenderEntity convert(final Release dataDto) {
        String tenderJson = jsonUtil.toJson(dataDto.getTender());
        TenderEntity tenderEntity = new TenderEntity();
        tenderEntity.setOcId(dataDto.getOcid());
        tenderEntity.setAddedDate(dataDto.getDate());
        tenderEntity.setJsonData(tenderJson);
        return tenderEntity;
    }
}
