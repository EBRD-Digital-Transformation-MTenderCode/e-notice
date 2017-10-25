package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class Framework {
    public Boolean isAFramework;
    public String typeOfFramework;
    public Integer maxSuppliers;
    public String exceptionalDurationRationale;
    public List<String> additionalBuyerCategories;
    public String description;
}
