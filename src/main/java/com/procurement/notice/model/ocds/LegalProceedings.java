package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "uri"
})
public class LegalProceedings {
    @JsonProperty("title")
    @JsonPropertyDescription("Title(s) of any legal proceedings(s) initiated.")
    private final String title;
    @JsonProperty("uri")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated.")
    private final String uri;
    @JsonProperty("id")
    @JsonPropertyDescription("Legal identifier(s) of any review procedure(s) initiated. Required by the EU")
    private String id;

    @JsonCreator
    public LegalProceedings(@JsonProperty("id") final String id,
                            @JsonProperty("title") final String title,
                            @JsonProperty("uri") final String uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }
}
