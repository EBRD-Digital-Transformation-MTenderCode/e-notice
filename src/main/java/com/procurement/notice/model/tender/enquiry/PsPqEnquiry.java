package com.procurement.notice.model.tender.enquiry;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "date",
        "author",
        "title",
        "description",
        "answer",
        "dateAnswered",
        "relatedItem",
        "relatedLot"
})
public class PsPqEnquiry {
    @JsonProperty("id")
    @JsonPropertyDescription("A unique identifier for the enquiry.")
    private final String id;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date the enquiry was received or processed.")
    private final LocalDateTime date;

    @JsonProperty("author")
    @JsonPropertyDescription("The identifier and name of the party asking this question. ")
    @Valid
    private final EnquiryAuthor author;

    @JsonProperty("title")
    @JsonPropertyDescription("The subject line of the question.")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("The body of the question.")
    private final String description;

    @JsonProperty("answer")
    @JsonPropertyDescription("The answer to this question, when available.")
    private String answer;

    @JsonProperty("dateAnswered")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date the answer to the question was provided.")
    private final LocalDateTime dateAnswered;

    @JsonProperty("relatedItem")
    @JsonPropertyDescription("If this question relates to a specific line-item, this field contains the line-item " +
            "identifier.")
    private final String relatedItem;

    @JsonProperty("relatedLot")
    @JsonPropertyDescription("Where lots are used, if this question relates to a specific lot, this field contains " +
            "the lot identifier.")
    private final String relatedLot;

    @JsonCreator
    public PsPqEnquiry(@JsonProperty("id") final String id,
                       @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime date,
                       @JsonProperty("author") final EnquiryAuthor author,
                       @JsonProperty("title") final String title,
                       @JsonProperty("description") final String description,
                       @JsonProperty("answer") final String answer,
                       @JsonProperty("dateAnswered") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final LocalDateTime dateAnswered,
                       @JsonProperty("relatedItem") final String relatedItem,
                       @JsonProperty("relatedLot") final String relatedLot) {
        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id)
                .append(date)
                .append(author)
                .append(title)
                .append(description)
                .append(answer)
                .append(dateAnswered)
                .append(relatedItem)
                .append(relatedLot)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PsPqEnquiry)) {
            return false;
        }
        final PsPqEnquiry rhs = (PsPqEnquiry) other;
        return new EqualsBuilder().append(id, rhs.id)
                .append(date, rhs.date)
                .append(author, rhs.author)
                .append(title, rhs.title)
                .append(description, rhs.description)
                .append(answer, rhs.answer)
                .append(dateAnswered, rhs.dateAnswered)
                .append(relatedItem, rhs.relatedItem)
                .append(relatedLot, rhs.relatedLot)
                .isEquals();
    }
}
