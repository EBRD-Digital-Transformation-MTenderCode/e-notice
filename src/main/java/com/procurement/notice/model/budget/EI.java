package com.procurement.notice.model.budget;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
        "title",
        "description",
        "language",
        "planning",
        "tender",
        "parties",
        "buyer",
        "relatedProcesses"
})
public class EI {

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

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("language")
    private String language;

    @JsonProperty("planning")
    private EiPlanning planning;

    @JsonProperty("tender")
    private Tender tender;

    @JsonProperty("parties")
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<Organization> parties;

    @JsonProperty("buyer")
    private final OrganizationReference buyer;

    @JsonProperty("relatedProcesses")
    private Set<RelatedProcess> relatedProcesses;

    @JsonCreator
    public EI(@JsonProperty("ocid") final String ocid,
              @JsonProperty("id") final String id,
              @JsonProperty("date") final LocalDateTime date,
              @JsonProperty("tag") final List<Tag> tag,
              @JsonProperty("initiationType") final InitiationType initiationType,
              @JsonProperty("title") final String title,
              @JsonProperty("description") final String description,
              @JsonProperty("language") final String language,
              @JsonProperty("tender") final Tender tender,
              @JsonProperty("buyer") final OrganizationReference buyer,
              @JsonProperty("parties") final Set<Organization> parties,
              @JsonProperty("planning") final EiPlanning planning,
              @JsonProperty("relatedProcesses") final HashSet<RelatedProcess> relatedProcesses) {
        this.ocid = ocid;
        this.id = id;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.title = title;
        this.description = description;
        this.language = language == null ? "en" : language;
        this.tender = tender;
        this.parties = parties;
        this.planning = planning;
        this.buyer = buyer;
        this.relatedProcesses = relatedProcesses;
    }
}
