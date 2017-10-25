package com.procurement.notice.dto;

import lombok.Data;

@Data
public class LotDetails {
    public Integer maximumLotsBidPerSupplier;
    public Integer maximumLotsAwardedPerSupplier;
}
