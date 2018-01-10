package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "legalName",
        "uri"
})
public class Identifier {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier of the organization in the selected scheme.")
    private final String id;

    @JsonProperty("scheme")
    @JsonPropertyDescription("Organization identifiers should be drawn from an existing organization identifier list." +
            " The scheme field is used to indicate the list or register from which the identifier is drawn. This " +
            "value " +
            "should be drawn from the [Organization Identifier Scheme](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#organization-identifier-scheme) codelist.")
    private final String scheme;

    @JsonProperty("legalName")
    @JsonPropertyDescription("The legally registered name of the organization.")
    private final String legalName;

    @JsonProperty("uri")
    @JsonPropertyDescription("A URI to identify the organization, such as those provided by [Open Corporates]" +
            "(http://www.opencorporates.com) or some other relevant URI provider. This is not for listing the website" +
            " of " +
            "the organization: that can be done through the URL field of the Organization contact point.")
    private final URI uri;

    @JsonCreator
    public Identifier(@JsonProperty("scheme") final String scheme,
                      @JsonProperty("id") final String id,
                      @JsonProperty("legalName") final String legalName,
                      @JsonProperty("uri") final URI uri) {
        this.id = id;
        this.scheme = scheme;
        this.legalName = legalName;
        this.uri = uri;
    }
}
