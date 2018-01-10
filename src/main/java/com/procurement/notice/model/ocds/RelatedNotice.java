package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "scheme",
        "id",
        "relationship",
        "objectOfProcurementInPIN",
        "uri"
})
public class RelatedNotice {
    @JsonProperty("scheme")
    @JsonPropertyDescription("The source of the related notice identifier. Currently only 'TED' and 'national' are " +
            "permitted values.")
    private final RelatedNotice.Scheme scheme;

    @JsonProperty("id")
    @JsonPropertyDescription("The identifier of the related national notice.")
    private final String id;

    @JsonProperty("relationship")
    @JsonPropertyDescription("Type of relationship")
    private final RelatedNotice.Relationship relationship;

    @JsonProperty("objectOfProcurementInPIN")
    @JsonPropertyDescription("If the related notice linked to is a planning or 'Prior Information Notice' (PIN) that " +
            "describes a number of potential tenders, the identifier of the specific Object to which this current " +
            "contracting process relates should be given.")
    private final String objectOfProcurementInPIN;

    @JsonProperty("uri")
    @JsonPropertyDescription("Uri of a national notice")
    private final URI uri;

    @JsonCreator
    public RelatedNotice(@JsonProperty("scheme") final Scheme scheme,
                         @JsonProperty("id") final String id,
                         @JsonProperty("relationship") final Relationship relationship,
                         @JsonProperty("objectOfProcurementInPIN") final String objectOfProcurementInPIN,
                         @JsonProperty("uri") final URI uri) {
        this.scheme = scheme;
        this.id = id;
        this.relationship = relationship;
        this.objectOfProcurementInPIN = objectOfProcurementInPIN;
        this.uri = uri;
    }

    public enum Relationship {
        PREVIOUS_NOTICE("previousNotice");
        private final static Map<String, Relationship> CONSTANTS = new HashMap<>();

        static {
            for (final Relationship c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private Relationship(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Relationship fromValue(final String value) {
            final Relationship constant = CONSTANTS.get(value);
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

    public enum Scheme {
        TED("TED"),
        NATIONAL("National");
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
            final RelatedNotice.Scheme constant = CONSTANTS.get(value);
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
