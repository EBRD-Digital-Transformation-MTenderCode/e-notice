package com.procurement.notice.model.tender.ms;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import com.procurement.notice.model.ocds.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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
        "relatedProcesses"
})
public class Ms {

    @JsonProperty("ocid")
    private String ocid;

    @JsonProperty("id")
    private String id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("date")
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
    private Set<Organization> parties;

    @JsonProperty("relatedProcesses")
    private Set<RelatedProcess> relatedProcesses;

    @JsonCreator
    public Ms(@JsonProperty("ocid") final String ocid,
              @JsonProperty("id") final String id,
              @JsonProperty("date") final LocalDateTime date,
              @JsonProperty("tag") final List<Tag> tag,
              @JsonProperty("initiationType") final InitiationType initiationType,
              @JsonProperty("language") final String language,
              @JsonProperty("planning") final MsPlanning planning,
              @JsonProperty("tender") final MsTender tender,
              @JsonProperty("parties") final HashSet<Organization> parties,
              @JsonProperty("relatedProcesses") final HashSet<RelatedProcess> relatedProcesses) {
        this.ocid = ocid;
        this.id = id;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.language = language == null ? "en" : language;
        this.planning = planning;
        this.tender = tender;
        this.parties = parties == null ? new HashSet<>() : parties;
        this.relatedProcesses = relatedProcesses;
    }
}
