package com.procurement.notice.model.tender;

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
        "title",
        "description",
        "language",
        "hasPreviousNotice",
        "purposeOfNotice",
        "planning",
        "tender",
        "parties",
        "buyer",
        "bids",
        "awards",
        "relatedProcesses"
})
public class ReleaseTender {
    @JsonProperty("ocid")
    private String ocid;
    @JsonProperty("id")
    private String id;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonPropertyDescription("The date this information was first released, or published.")
    private LocalDateTime date;
    @JsonProperty("tag")
    private List<Tag> tag;
    @JsonProperty("initiationType")
    private InitiationType initiationType;
    @JsonProperty("title")
    @JsonPropertyDescription("A overall title for this contracting process or release.")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("language")
    private String language;
    @JsonProperty("hasPreviousNotice")
    private final Boolean hasPreviousNotice;
    @JsonProperty("purposeOfNotice")
    private final PurposeOfNotice purposeOfNotice;
    @JsonProperty("planning")
    private final Planning planning;
    @JsonProperty("tender")
    private final Tender tender;
    @JsonProperty("parties")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<Organization> parties;
    @JsonProperty("buyer")
    private final OrganizationReference buyer;
    @JsonProperty("bids")
    private final Bids bids;
    @JsonProperty("awards")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<Award> awards;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<RelatedProcess> relatedProcesses;


    @JsonCreator
    public ReleaseTender(@JsonProperty("ocid") final String ocid,
                         @JsonProperty("id") final String id,
                         @JsonProperty("date") final LocalDateTime date,
                         @JsonProperty("tag") final List<Tag> tag,
                         @JsonProperty("initiationType") final InitiationType initiationType,
                         @JsonProperty("title") final String title,
                         @JsonProperty("description") final String description,
                         @JsonProperty("language") final String language,
                         @JsonProperty("parties") final LinkedHashSet<Organization> parties,
                         @JsonProperty("planning") final Planning planning,
                         @JsonProperty("tender") final Tender tender,
                         @JsonProperty("buyer") final OrganizationReference buyer,
                         @JsonProperty("awards") final LinkedHashSet<Award> awards,
                         @JsonProperty("bids") final Bids bids,
                         @JsonProperty("hasPreviousNotice") final Boolean hasPreviousNotice,
                         @JsonProperty("purposeOfNotice") final PurposeOfNotice purposeOfNotice,
                         @JsonProperty("relatedProcesses") final LinkedHashSet<RelatedProcess> relatedProcesses) {
        this.ocid = ocid;
        this.id = id;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.title = title;
        this.description = description;
        this.parties = parties;
        this.planning = planning;
        this.tender = tender;
        this.buyer = buyer;
        this.awards = awards;
        this.language = language == null ? "en" : language;
        this.bids = bids;
        this.hasPreviousNotice = hasPreviousNotice;
        this.purposeOfNotice = purposeOfNotice;
        this.relatedProcesses = relatedProcesses == null ? new LinkedHashSet<>() : relatedProcesses;
    }
}
