package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class Renewals {
    public Boolean hasRenewals;
    public Integer maxNumber;
    public String renewalConditions;
}
