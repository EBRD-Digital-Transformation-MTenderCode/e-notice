package com.procurement.notice.model.tender;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import com.procurement.notice.model.ocds.InitiationType;
import com.procurement.notice.model.ocds.RelatedProcess;
import com.procurement.notice.model.ocds.Tag;
import com.procurement.notice.model.ocds.Tender;
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
        "tender",
        "relatedProcesses"
})
public class ReleaseMS {
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
    @JsonPropertyDescription("A overall title for this contracting process or release.")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("language")
    private String language;
    @JsonProperty("tender")
    private final Tender tender;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    private final Set<RelatedProcess> relatedProcesses;

    @JsonCreator
    public ReleaseMS(@JsonProperty("ocid") final String ocid,
                     @JsonProperty("id") final String id,
                     @JsonProperty("date") final LocalDateTime date,
                     @JsonProperty("tag") final List<Tag> tag,
                     @JsonProperty("initiationType") final InitiationType initiationType,
                     @JsonProperty("title") final String title,
                     @JsonProperty("description") final String description,
                     @JsonProperty("tender") final Tender tender,
                     @JsonProperty("language") final String language,
                     @JsonProperty("relatedProcesses") final LinkedHashSet<RelatedProcess> relatedProcesses) {
        this.ocid = ocid;
        this.id = id;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.title = title;
        this.description = description;
        this.language = language == null ? "en" : language;
        this.tender = tender;
        this.relatedProcesses = relatedProcesses == null ? new LinkedHashSet<>() : relatedProcesses;
    }
}
