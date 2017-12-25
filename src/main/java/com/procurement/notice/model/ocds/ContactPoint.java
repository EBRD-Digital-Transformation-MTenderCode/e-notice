package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.net.URI;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "email",
        "telephone",
        "faxNumber",
        "url"
})
public class ContactPoint {
    @JsonProperty("name")
    @JsonPropertyDescription("The name of the contact person, department, or contact point, for correspondence " +
            "relating to this contracting process.")
    private final String name;

    @JsonProperty("email")
    @JsonPropertyDescription("The e-mail address of the contact point/person.")
    private final String email;

    @JsonProperty("telephone")
    @JsonPropertyDescription("The telephone number of the contact point/person. This should include the international" +
            " dialing code.")
    private final String telephone;

    @JsonProperty("faxNumber")
    @JsonPropertyDescription("The fax number of the contact point/person. This should include the international " +
            "dialing code.")
    private final String faxNumber;

    @JsonProperty("url")
    @JsonPropertyDescription("A web address for the contact point/person.")
    private final URI url;

    @JsonCreator
    public ContactPoint(@JsonProperty("name") final String name,
                        @JsonProperty("email") final String email,
                        @JsonProperty("telephone") final String telephone,
                        @JsonProperty("faxNumber") final String faxNumber,
                        @JsonProperty("url") final URI url) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.faxNumber = faxNumber;
        this.url = url;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name)
                .append(email)
                .append(telephone)
                .append(faxNumber)
                .append(url)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ContactPoint)) {
            return false;
        }
        final ContactPoint rhs = (ContactPoint) other;
        return new EqualsBuilder().append(name, rhs.name)
                .append(email, rhs.email)
                .append(telephone, rhs.telephone)
                .append(faxNumber, rhs.faxNumber)
                .append(url, rhs.url)
                .isEquals();
    }
}
