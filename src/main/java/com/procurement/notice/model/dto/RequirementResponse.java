package com.procurement.notice.model.dto;

import lombok.Data;

@Data
public class RequirementResponse {
    public String id;
    public String title;
    public String description;
    public Object value;
    public Period period;
    public RequirementReference requirement;
    public OrganizationReference relatedTenderer;
}
