package com.procurement.notice.dto;

import lombok.Data;

@Data
public class Details {
    public String typeOfBuyer;
    public String mainGeneralActivity;
    public String mainSectoralActivity;
    public Boolean isACentralPurchasingBody;
    public String NUTSCode;
}
