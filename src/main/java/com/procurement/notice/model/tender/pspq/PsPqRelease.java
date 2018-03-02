package com.procurement.notice.model.tender.pspq;

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
        "hasPreviousNotice",
        "purposeOfNotice",
        "tender",
        "parties",
        "bids",
        "awards",
        "relatedProcesses"
})
public class PsPqRelease {
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
    @JsonProperty("language")
    private String language;
    @JsonProperty("hasPreviousNotice")
    private final Boolean hasPreviousNotice;
    @JsonProperty("purposeOfNotice")
    private final PurposeOfNotice purposeOfNotice;
    @JsonProperty("tender")
    private final PsPqTender tender;
    @JsonProperty("parties")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<Organization> parties;
    @JsonProperty("bids")
    private Bids bids;
    @JsonProperty("awards")
    @JsonDeserialize(as = LinkedHashSet.class)
    private Set<Award> awards;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<RelatedProcess> relatedProcesses;


    @JsonCreator
    public PsPqRelease(@JsonProperty("ocid") final String ocid,
                       @JsonProperty("id") final String id,
                       @JsonProperty("date") final LocalDateTime date,
                       @JsonProperty("tag") final List<Tag> tag,
                       @JsonProperty("initiationType") final InitiationType initiationType,
                       @JsonProperty("language") final String language,
                       @JsonProperty("parties") final LinkedHashSet<Organization> parties,
                       @JsonProperty("tender") final PsPqTender tender,
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
        this.parties = parties;
        this.tender = tender;
        this.awards = awards;
        this.language = language == null ? "en" : language;
        this.bids = bids;
        this.hasPreviousNotice = hasPreviousNotice;
        this.purposeOfNotice = purposeOfNotice;
        this.relatedProcesses = relatedProcesses == null ? new LinkedHashSet<>() : relatedProcesses;
    }
}
