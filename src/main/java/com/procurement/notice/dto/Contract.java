package com.procurement.notice.dto;

import java.util.List;

public class Contract {
    public String id;
    public String awardID;
    public String extendsContractID;
    public String title;
    public String description;
    public String status;
    public Period period;
    public Value value;
    public List<Item> items;
    public String dateSigned;
    public List<Document> documents;
    public Implementation implementation;
    public List<RelatedProcess> relatedProcesses;
    public List<Milestone> milestones;
    public List<Amendment> amendments;
    public Amendment amendment;
    public List<RequirementResponse> requirementResponses;
    public String countryOfOrigin;
    public List<String> lotVariant;
    public List<ValueBreakdown> valueBreakdown;
    public Boolean isFrameworkOrDynamic;
}
