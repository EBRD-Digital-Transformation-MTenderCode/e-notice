package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "uri"
})
public class LegalProceedings {
    @JsonProperty("id")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated. Required by the EU")
    private final String id;

    @JsonProperty("title")
    @JsonPropertyDescription("Title(s) of any legal proceedings(s) initiated.")
    private final String title;

    @JsonProperty("uri")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated.")
    private final URI uri;

    @JsonCreator
    public LegalProceedings(@JsonProperty("id") final String id,
                            @JsonProperty("title") final String title,
                            @JsonProperty("uri") final URI uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }
}
