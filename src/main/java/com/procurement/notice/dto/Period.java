package com.procurement.notice.dto;

import lombok.Data;

@Data
public class Period {
    public String startDate;
    public String endDate;
    public String maxExtentDate;
    public Integer durationInDays;
}