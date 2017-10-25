package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class Item {
    public String id;
    public String description;
    public Classification classification;
    public List<Classification> additionalClassifications;
    public Integer quantity;
    public Unit unit;
}