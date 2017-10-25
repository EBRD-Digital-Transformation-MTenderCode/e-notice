package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class RequirementGroup {
    public String id;
    public String description;
    public List<Requirement> requirements;
}
