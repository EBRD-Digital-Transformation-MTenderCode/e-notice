package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.net.URI;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "id",
        "identifier",
        "additionalIdentifiers",
        "address",
        "contactPoint",
        "roles",
        "details",
        "buyerProfile"
})
public class Organization {
    @JsonProperty("id")
    @JsonPropertyDescription("The ID used for cross-referencing to this party from other sections of the release. " +
            "This field may be built with the following structure {identifier.scheme}-{identifier.id}" +
            "(-{department-identifier}).")
    private String id;

    @JsonProperty("name")
    @JsonPropertyDescription("A common name for this organization or other participant in the contracting process. " +
            "The identifier object provides an space for the formal legal name, and so this may either repeat that " +
            "value," +
            " or could provide the common name by which this organization or entity is known. This field may also " +
            "include" +
            " details of the department or sub-unit involved in this contracting process.")
    private final String name;

    @JsonProperty("identifier")
    @JsonPropertyDescription("The primary identifier for this organization or participant. Identifiers that uniquely " +
            "pick out a legal entity should be preferred. Consult the [organization identifier guidance]" +
            "(http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/) for the preferred scheme and identifier to use.")
    private final Identifier identifier;

    @JsonProperty("additionalIdentifiers")
    @JsonPropertyDescription("A list of additional / supplemental identifiers for the organization or participant, " +
            "using the [organization identifier guidance](http://standard.open-contracting" +
            ".org/latest/en/schema/identifiers/). This could be used to provide an internally used identifier for " +
            "this " +
            "organization in addition to the primary legal entity identifier.")
    private final Set<Identifier> additionalIdentifiers;

    @JsonProperty("address")
    @JsonPropertyDescription("A list of additional / supplemental identifiers for the organization or participant, " +
            "using the [organization identifier guidance](http://standard.open-contracting" +
            ".org/latest/en/schema/identifiers/). This could be used to provide an internally used identifier for " +
            "this " +
            "organization in addition to the primary legal entity identifier.")
    private final Address address;

    @JsonProperty("contactPoint")
    @JsonPropertyDescription("The party's role(s) in the contracting process. Role(s) should be taken from the " +
            "[partyRole codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). " +
            "Values " +
            "from the provided codelist should be used wherever possible, though extended values can be provided if " +
            "the " +
            "codelist does not have a relevant code.")
    private final ContactPoint contactPoint;

    @JsonProperty("roles")
    @JsonPropertyDescription("The party's role(s) in the contracting process. Role(s) should be taken from the " +
            "[partyRole codelist](http://standard.open-contracting.org/latest/en/schema/codelists/#party-role). " +
            "Values " +
            "from the provided codelist should be used wherever possible, though extended values can be provided if " +
            "the " +
            "codelist does not have a relevant code.")
    private final List<PartyRole> roles;

    @JsonProperty("details")
    @JsonPropertyDescription("Additional classification information about parties can be provided using partyDetail " +
            "extensions that define particular properties and classification schemes. ")
    private final Details details;

    @JsonProperty("buyerProfile")
    @JsonPropertyDescription("For buyer organisations only: the url of the organization's buyer profile. Specified by" +
            " the EU")
    private final URI buyerProfile;

    @JsonCreator
    public Organization(@JsonProperty("name") final String name,
                        @JsonProperty("id") final String id,
                        @JsonProperty("identifier") final Identifier identifier,
                        @JsonProperty("additionalIdentifiers") final LinkedHashSet<Identifier> additionalIdentifiers,
                        @JsonProperty("address") final Address address,
                        @JsonProperty("contactPoint") final ContactPoint contactPoint,
                        @JsonProperty("roles") final List<PartyRole> roles,
                        @JsonProperty("details") final Details details,
                        @JsonProperty("buyerProfile") final URI buyerProfile) {
        this.name = name;
        this.id = id;
        this.identifier = identifier;
        this.additionalIdentifiers = additionalIdentifiers;
        this.address = address;
        this.contactPoint = contactPoint;
        this.roles = roles;
        this.details = details;
        this.buyerProfile = buyerProfile;
    }

    public enum PartyRole {
        BUYER("buyer"),
        PROCURING_ENTITY("procuringEntity"),
        SUPPLIER("supplier"),
        TENDERER("tenderer"),
        FUNDER("funder"),
        ENQUIRER("enquirer"),
        PAYER("payer"),
        PAYEE("payee"),
        REVIEW_BODY("reviewBody");

        private final static Map<String, PartyRole> CONSTANTS = new HashMap<>();

        static {
            for (final PartyRole c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private PartyRole(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static PartyRole fromValue(final String value) {
            final PartyRole constant = CONSTANTS.get(value);
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
