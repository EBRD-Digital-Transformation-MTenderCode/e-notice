package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "description",
        "uri"
})
public class Classification {
    @JsonProperty("description")
    @JsonPropertyDescription("A textual description or title for the code.")
    private final String description;
    @JsonProperty("scheme")
    @JsonPropertyDescription("An classification should be drawn from an existing scheme or list of codes. This field " +
            "is used to indicate the scheme/codelist from which the classification is drawn. For line item " +
            "classifications, this value should represent an known [Item Classification Scheme](http://standard" +
            ".open-contracting.org/latest/en/schema/codelists/#item-classification-scheme) wherever possible.")
    private final Scheme scheme;
    @JsonProperty("uri")
    @JsonPropertyDescription("A URI to identify the code. In the event individual URIs are not available for items in" +
            " the identifier scheme this value should be left blank.")
    private final String uri;
    @JsonProperty("id")
    @JsonPropertyDescription("The classification code drawn from the selected scheme.")
    private String id;

    @JsonCreator
    public Classification(@JsonProperty("scheme") final Scheme scheme,
                          @JsonProperty("id") final String id,
                          @JsonProperty("description") final String description,
                          @JsonProperty("uri") final String uri) {
        this.id = id;
        this.description = description;
        this.scheme = scheme;
        this.uri = uri;
    }

    public enum Scheme {
        CPV("CPV"),
        CPVS("CPVS"),
        GSIN("GSIN"),
        UNSPSC("UNSPSC"),
        CPC("CPC"),
        OKDP("OKDP"),
        OKPD("OKPD");

        private final static Map<String, Scheme> CONSTANTS = new HashMap<>();

        static {
            for (final Scheme c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private Scheme(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Scheme fromValue(final String value) {
            final Scheme constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            }
            return constant;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }
    }
}
