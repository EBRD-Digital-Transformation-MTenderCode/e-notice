package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class RecurrentProcurement {
    public Boolean isRecurrent;
    public List<Period> dates;
    public String description;
}
