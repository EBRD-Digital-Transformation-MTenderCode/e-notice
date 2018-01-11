package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.net.URI;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "source",
        "date",
        "value",
        "payer",
        "payee",
        "uri",
        "amount",
        "providerOrganization",
        "receiverOrganization"
})
public class Transaction {
    @JsonProperty("source")
    @JsonPropertyDescription("Used to point either to a corresponding Fiscal Data Package, IATI file, or machine or " +
            "human-readable source where users can find further information on the budget line item identifiers, or " +
            "project identifiers, provided here.")
    private final URI source;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date of the transaction")
    private final LocalDateTime date;
    @JsonProperty("value")
    private final Value value;
    @JsonProperty("payer")
    @JsonPropertyDescription("The id and name of the party being referenced. Used to cross-reference to the parties " +
            "section")
    private final OrganizationReference payer;
    @JsonProperty("payee")
    @JsonPropertyDescription("The id and name of the party being referenced. Used to cross-reference to the parties " +
            "section")
    private final OrganizationReference payee;
    @JsonProperty("uri")
    @JsonPropertyDescription("A URI pointing directly to a machine-readable record about this spending transaction.")
    private final URI uri;
    @JsonProperty("amount")
    private final Value amount;
    @JsonProperty("providerOrganization")
    private final Identifier providerOrganization;
    @JsonProperty("receiverOrganization")
    private final Identifier receiverOrganization;
    @JsonProperty("id")
    @JsonPropertyDescription("A unique identifier for this transaction. This identifier should be possible to " +
            "cross-reference against the provided data source. For IATI this is the transaction reference.")
    private String id;

    @JsonCreator
    public Transaction(@JsonProperty("id") final String id,
                       @JsonProperty("source") final URI source,
                       @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                       LocalDateTime date,
                       @JsonProperty("value") final Value value,
                       @JsonProperty("payer") final OrganizationReference payer,
                       @JsonProperty("payee") final OrganizationReference payee,
                       @JsonProperty("uri") final URI uri,
                       @JsonProperty("amount") final Value amount,
                       @JsonProperty("providerOrganization") final Identifier providerOrganization,
                       @JsonProperty("receiverOrganization") final Identifier receiverOrganization) {
        this.id = id;
        this.source = source;
        this.date = date;
        this.value = value;
        this.payer = payer;
        this.payee = payee;
        this.uri = uri;
        this.amount = amount;
        this.providerOrganization = providerOrganization;
        this.receiverOrganization = receiverOrganization;
    }
}
