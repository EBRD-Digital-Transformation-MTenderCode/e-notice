package com.procurement.notice.model.dto;

import java.util.List;

public class Award {
    public String id;
    public String title;
    public String description;
    public String status;
    public String date;
    public Value value;
    public List<OrganizationReference> suppliers;
    public List<Item> items;
    public Period contractPeriod;
    public List<Document> documents;
    public List<Amendment> amendments;
    public Amendment amendment;
    public List<String> relatedLots;
    public List<RequirementResponse> requirementResponses;
    public ReviewProceedings reviewProceedings;
    public String statusDetails;
}
