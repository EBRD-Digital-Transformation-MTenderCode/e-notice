package com.procurement.notice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class Milestone {
    public String id;
    public String title;
    public String type;
    public String description;
    public String code;
    public String dueDate;
    public String dateMet;
    public String dateModified;
    public String status;
    public List<Document> documents;
    public List<String> relatedLots;
    public List<OrganizationReference> relatedParties;
    public String additionalInformation;
}