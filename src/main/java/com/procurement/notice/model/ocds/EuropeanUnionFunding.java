package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "projectIdentifier",
        "projectName",
        "uri"
})
public class EuropeanUnionFunding {
    @JsonProperty("projectIdentifier")
    @JsonPropertyDescription("National identifier of the EU project providing partial or full funding")
    private final String projectIdentifier;

    @JsonProperty("projectName")
    @JsonPropertyDescription("Name or other national identification of the project providing full or partial funding.")
    private final String projectName;

    @JsonProperty("uri")
    @JsonPropertyDescription("Uri of the project providing full or partial funding.")
    private final URI uri;

    @JsonCreator
    public EuropeanUnionFunding(@JsonProperty("projectIdentifier") final String projectIdentifier,
                                @JsonProperty("projectName") final String projectName,
                                @JsonProperty("uri") final URI uri) {
        this.projectIdentifier = projectIdentifier;
        this.projectName = projectName;
        this.uri = uri;
    }
}
