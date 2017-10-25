package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class Criterion {
    public String id;
    public String title;
    public String description;
    public String source;
    public String relatesTo;
    public String relatedItem;
    public List<RequirementGroup> requirementGroups;
}
