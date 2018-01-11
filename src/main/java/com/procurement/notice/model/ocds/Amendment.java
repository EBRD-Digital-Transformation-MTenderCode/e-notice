package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "rationale",
        "id",
        "description",
        "amendsReleaseID",
        "releaseID",
        "changes"
})
public class Amendment {
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this amendment: often the amendment number")
    private final String id;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date of this amendment.")
    private final LocalDateTime date;

    @JsonProperty("description")
    @JsonPropertyDescription("A free text, or semi-structured, description of the changes made in this amendment.")
    private final String description;

    @JsonProperty("rationale")
    @JsonPropertyDescription("An explanation for the amendment.")
    private final String rationale;

    @JsonProperty("amendsReleaseID")
    @JsonPropertyDescription("Provide the identifier (release.id) of the OCDS release (from this contracting process)" +
            " that provides the values for this contracting process **before** the amendment was made.")
    private final String amendsReleaseID;

    @JsonProperty("releaseID")
    @JsonPropertyDescription("Provide the identifier (release.id) of the OCDS release (from this contracting process)" +
            " that provides the values for this contracting process **after** the amendment was made.")
    private final String releaseID;

    @JsonProperty("changes")
    @JsonPropertyDescription("An array change objects describing the fields changed, and their former values. " +
            "(Deprecated in 1.1)")
    private final List<Change> changes;

    @JsonCreator
    public Amendment(@JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                         LocalDateTime date,
                     @JsonProperty("releaseID") final String releaseID,
                     @JsonProperty("id") final String id,
                     @JsonProperty("description") final String description,
                     @JsonProperty("amendsReleaseID") final String amendsReleaseID,
                     @JsonProperty("rationale") final String rationale,
                     @JsonProperty("changes") final List<Change> changes) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.rationale = rationale;
        this.amendsReleaseID = amendsReleaseID;
        this.releaseID = releaseID;
        this.changes = changes;
    }
}