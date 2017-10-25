package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class ParticipationFee {
    public List<String> type;
    public Value value;
    public String description;
    public List<String> methodOfPayment;
}
