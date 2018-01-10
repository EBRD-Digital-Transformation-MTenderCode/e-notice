package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "isAFramework",
        "typeOfFramework",
        "maxSuppliers",
        "exceptionalDurationRationale",
        "additionalBuyerCategories"
})
public class Framework {
    @JsonProperty("isAFramework")
    @JsonPropertyDescription("A True/False field to indicate whether a framework agreement has been established as " +
            "part of this procurement")
    private final Boolean isAFramework;

    @JsonProperty("typeOfFramework")
    @JsonPropertyDescription("A value from the [typeOfFramework codelist](http://standard.open-contracting" +
            ".org/1.1-dev/en/schema/codelists/#type-of-framework). The type of buyer taken from the EU's specified " +
            "list " +
            "in its TED forms.")
    private final TypeOfFramework typeOfFramework;

    @JsonProperty("maxSuppliers")
    @JsonPropertyDescription("Maximum number of economic operators to be awarded a contract")
    private final Integer maxSuppliers;

    @JsonProperty("exceptionalDurationRationale")
    @JsonPropertyDescription("Justification when the framework agreement has an exceptional duration.")
    private final String exceptionalDurationRationale;

    @JsonProperty("additionalBuyerCategories")
    @JsonPropertyDescription("Any additonal categories of buyers participating in the framework agreement and not " +
            "mentioned in the buyer section of this notice")
    private final List<String> additionalBuyerCategories;

    @JsonCreator
    public Framework(@JsonProperty("isAFramework") final Boolean isAFramework,
                     @JsonProperty("typeOfFramework") final TypeOfFramework typeOfFramework,
                     @JsonProperty("maxSuppliers") final Integer maxSuppliers,
                     @JsonProperty("exceptionalDurationRationale") final String exceptionalDurationRationale,
                     @JsonProperty("additionalBuyerCategories") final List<String> additionalBuyerCategories) {
        this.isAFramework = isAFramework;
        this.typeOfFramework = typeOfFramework;
        this.maxSuppliers = maxSuppliers;
        this.exceptionalDurationRationale = exceptionalDurationRationale;
        this.additionalBuyerCategories = additionalBuyerCategories;
    }

    public enum TypeOfFramework {
        WITH_REOPENING_OF_COMPETITION("WITH_REOPENING_OF_COMPETITION"),
        WITHOUT_REOPENING_OF_COMPETITION("WITHOUT_REOPENING_OF_COMPETITION"),
        PARTLY_WITH_PARTLY_WITHOUT_REOPENING_OF_COMPETITION("PARTLY_WITH_PARTLY_WITHOUT_REOPENING_OF_COMPETITION");
        private final static Map<String, TypeOfFramework> CONSTANTS = new HashMap<>();

        static {
            for (final Framework.TypeOfFramework c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        private TypeOfFramework(final String value) {
            this.value = value;
        }

        @JsonCreator
        public static TypeOfFramework fromValue(final String value) {
            final TypeOfFramework constant = CONSTANTS.get(value);
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
