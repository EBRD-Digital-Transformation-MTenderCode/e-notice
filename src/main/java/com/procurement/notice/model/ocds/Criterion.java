package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "source",
        "relatesTo",
        "relatedItem",
        "requirementGroups"
})
public class Criterion {
    @JsonProperty("title")
    @JsonPropertyDescription("Criterion title")
    private final String title;
    @JsonProperty("description")
    @JsonPropertyDescription("Criterion description")
    private final String description;
    @JsonProperty("source")
    @JsonPropertyDescription("Source of response to the requirements specificed in the criterion, for example " +
            "responses may be submitted by tenderers or by an assessment committee at the procuringEntity.")
    private final Source source;
    @JsonProperty("relatesTo")
    @JsonPropertyDescription("The schema element that the criterion judges, evaluates or assesses. For example " +
            "criterion may be defined against items or against bidders.")
    private final RelatesTo relatesTo;
    @JsonProperty("relatedItem")
    @JsonPropertyDescription("Where relatesTo = \"item\" this field must be populated with the id of the item in this" +
            " tender section which the criterion relates to. Where relatesTo <> \"item\" this field should be omitted")
    private final String relatedItem;
    @JsonProperty("requirementGroups")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list of requirement groups for this Criterion. A criterion is satisfied by one or " +
            "more requirement groups being met. A requirement group is met when all requirements in the group are " +
            "satisfied.")
    private final Set<RequirementGroup> requirementGroups;
    @JsonProperty("id")
    @JsonPropertyDescription("The identifier for this criterion. It must be unique and cannot change within the Open " +
            "Contracting Process it is part of (defined by a single ocid). See the [identifier guidance]" +
            "(http://standard" +
            ".open-contracting.org/latest/en/schema/identifiers/) for further details.")
    private String id;

    @JsonCreator
    public Criterion(@JsonProperty("id") final String id,
                     @JsonProperty("title") final String title,
                     @JsonProperty("description") final String description,
                     @JsonProperty("source") final Source source,
                     @JsonProperty("relatesTo") final RelatesTo relatesTo,
                     @JsonProperty("relatedItem") final String relatedItem,
                     @JsonProperty("requirementGroups") final LinkedHashSet<RequirementGroup> requirementGroups) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.source = source;
        this.relatesTo = relatesTo;
        this.relatedItem = relatedItem;
        this.requirementGroups = requirementGroups;
    }

    public enum RelatesTo {
        ITEM("item"),
        TENDERER("tenderer");
        private final static Map<String, RelatesTo> CONSTANTS = new HashMap<>();

        static {
            for (final Criterion.RelatesTo c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private RelatesTo(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static RelatesTo fromValue(final String value) {
            final Criterion.RelatesTo constant = CONSTANTS.get(value);
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

    public enum Source {
        TENDERER("tenderer"),
        BUYER("buyer"),
        PROCURING_ENTITY("procuringEntity");
        private final static Map<String, Source> CONSTANTS = new HashMap<>();

        static {
            for (final Criterion.Source c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private Source(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static Source fromValue(final String value) {
            final Source constant = CONSTANTS.get(value);
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
