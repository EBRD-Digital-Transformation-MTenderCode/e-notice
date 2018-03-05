package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import com.procurement.notice.model.ocds.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ocid",
        "id",
        "date",
        "tag",
        "initiationType",
        "language",
        "planning",
        "tender",
        "parties",
        "buyer",
        "relatedProcesses"
})
public class MsRelease {
    @JsonProperty("ocid")
    private String ocid;
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    @JsonProperty("tag")
    private List<Tag> tag;
    @JsonProperty("initiationType")
    private InitiationType initiationType;
    @JsonProperty("language")
    private String language;
    @JsonProperty("planning")
    private final MsPlanning planning;
    @JsonProperty("tender")
    private final MsTender tender;
    @JsonProperty("parties")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<Organization> parties;
    @JsonProperty("buyer")
    private final OrganizationReference buyer;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<RelatedProcess> relatedProcesses;

    @JsonCreator
    public MsRelease(@JsonProperty("ocid") final String ocid,
                     @JsonProperty("id") final String id,
                     @JsonProperty("date") final LocalDateTime date,
                     @JsonProperty("tag") final List<Tag> tag,
                     @JsonProperty("initiationType") final InitiationType initiationType,
                     @JsonProperty("language") final String language,
                     @JsonProperty("planning") final MsPlanning planning,
                     @JsonProperty("tender") final MsTender tender,
                     @JsonProperty("parties") final LinkedHashSet<Organization> parties,
                     @JsonProperty("buyer") final OrganizationReference buyer,
                     @JsonProperty("relatedProcesses") final LinkedHashSet<RelatedProcess> relatedProcesses) {
        this.ocid = ocid;
        this.id = id;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.language = language == null ? "en" : language;
        this.planning = planning;
        this.tender = tender;
        this.parties = parties;
        this.buyer = buyer;
        this.relatedProcesses = relatedProcesses == null ? new LinkedHashSet<>() : relatedProcesses;
    }
}
