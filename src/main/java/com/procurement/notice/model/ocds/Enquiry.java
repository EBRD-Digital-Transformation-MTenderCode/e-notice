package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
        "relatedLot",
        "threadID"
})
public class Enquiry {
    @JsonProperty("id")
    @JsonPropertyDescription("A unique identifier for the enquiry.")
    private String id;

    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonPropertyDescription("The date the enquiry was received or processed.")
    private final LocalDateTime date;

    @JsonProperty("author")
    @JsonPropertyDescription("The identifier and name of the party asking this question. ")
    private final Author author;

    @JsonProperty("title")
    @JsonPropertyDescription("The subject line of the question.")
    private final String title;

    @JsonProperty("description")
    @JsonPropertyDescription("The body of the question.")
    private final String description;

    @JsonProperty("answer")
    @JsonPropertyDescription("The answer to this question, when available.")
    private final String answer;

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

    @JsonProperty("threadID")
    @JsonPropertyDescription("If this question and answer forms part of a discussion thread (e.g. the question is a " +
            "follow up to a previous answer) an optional thread identifier can be used to associate together multiple" +
            " " +
            "enquiries.")
    private final String threadID;

    @JsonCreator
    public Enquiry(@JsonProperty("id") final String id,
                   @JsonProperty("date") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                   LocalDateTime date,
                   @JsonProperty("author") final Author author,
                   @JsonProperty("title") final String title,
                   @JsonProperty("description") final String description,
                   @JsonProperty("answer") final String answer,
                   @JsonProperty("dateAnswered") @JsonDeserialize(using = LocalDateTimeDeserializer.class) final
                       LocalDateTime dateAnswered,
                   @JsonProperty("relatedItem") final String relatedItem,
                   @JsonProperty("relatedLot") final String relatedLot,
                   @JsonProperty("threadID") final String threadID) {
        this.id = id;
        this.date = date;
        this.author = author;
        this.title = title;
        this.description = description;
        this.answer = answer;
        this.dateAnswered = dateAnswered;
        this.relatedItem = relatedItem;
        this.relatedLot = relatedLot;
        this.threadID = threadID;
    }
}
