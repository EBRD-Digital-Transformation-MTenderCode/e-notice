package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "value",
        "scheme",
        "id",
        "uri"
})
public class Unit {
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier from the codelist referenced in the scheme property. Check the codelist " +
            "for details of how to find and use identifiers from the scheme in use.")
    private final String id;

    @JsonProperty("name")
    @JsonPropertyDescription("Name of the unit.")
    private final String name;

    @JsonProperty("scheme")
    @JsonPropertyDescription("The list from which units of measure identifiers are taken. This should be an entry " +
            "from the options available in the [unitClassificationScheme](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#unit-classification-scheme) codelist. Use of the scheme 'UNCEFACT' for " +
            "the " +
            "UN/CEFACT Recommendation 20 list of 'Codes for Units of Measure Used in International Trade' is " +
            "recommended," +
            " although other options are available.")
    private final Scheme scheme;

    @JsonProperty("value")
    private final Value value;

    @JsonProperty("uri")
    @JsonPropertyDescription("If the scheme used provide a machine-readable URI for this unit of measure, this can be" +
            " given.")
    private final URI uri;

    @JsonCreator
    public Unit(@JsonProperty("name") final String name,
                @JsonProperty("value") final Value value,
                @JsonProperty("scheme") final Scheme scheme,
                @JsonProperty("id") final String id,
                @JsonProperty("uri") final URI uri) {
        this.name = name;
        this.value = value;
        this.scheme = scheme;
        this.id = id;
        this.uri = uri;
    }

    public enum Scheme {
        UNCEFACT("UNCEFACT"),
        QUDT("QUDT");

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
