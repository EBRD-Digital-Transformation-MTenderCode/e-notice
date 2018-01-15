package com.procurement.notice.model.ein;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.procurement.notice.databinding.LocalDateTimeDeserializer;
import com.procurement.notice.databinding.LocalDateTimeSerializer;
import com.procurement.notice.model.ocds.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ocid",
        "id",
        "date",
        "tag",
        "initiationType",
        "title",
        "description",
        "language",
        "parties",
        "planning",
        "relatedProcesses"
})
public class ReleaseFS {
    @JsonProperty("ocid")
    @JsonPropertyDescription("A globally unique identifier for this Open Contracting Process. Composed of a publisher" +
            " prefix and an identifier for the contracting process. For more information see the [Open Contracting " +
            "Identifier guidance](http://standard.open-contracting.org/latest/en/schema/identifiers/)")
    private String ocid;
    @JsonProperty("id")
    @JsonPropertyDescription("An identifier for this particular release of information. A release identifier must be " +
            "unique within the scope of its related contracting process (defined by a common ocid), and unique within" +
            " any" +
            " release package it appears in. A release identifier must not contain the # character.")
    private String id;
    @JsonProperty("date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonPropertyDescription("The date this information was first released, or published.")
    private LocalDateTime date;
    @JsonProperty("tag")
    @JsonPropertyDescription("One or more values from the [releaseTag codelist](http://standard.open-contracting" +
            ".org/latest/en/schema/codelists/#release-tag). Tags may be used to filter release and to understand the " +
            "kind" +
            " of information that a release might contain.")
    private List<Tag> tag;
    @JsonProperty("initiationType")
    @JsonPropertyDescription("String specifying the type of initiation process used for this contract, taken from the" +
            " [initiationType](http://standard.open-contracting.org/latest/en/schema/codelists/#initiation-type) " +
            "codelist" +
            ". Currently only tender is supported.")
    private InitiationType initiationType;
    @JsonProperty("title")
    @JsonPropertyDescription("A overall title for this contracting process or release.")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("language")
    @JsonPropertyDescription("Specifies the default language of the data using either two-letter [ISO639-1]" +
            "(https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes), or extended [BCP47 language tags](http://www" +
            ".w3.org/International/articles/language-tags/). The use of lowercase two-letter codes from [ISO639-1]" +
            "(https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) is strongly recommended.")
    private String language;
    @JsonProperty("parties")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("Information on the parties (organizations, economic operators and other participants) " +
            "who are involved in the contracting process and their roles, e.g. buyer, procuring entity, supplier etc." +
            " " +
            "Organization references elsewhere in the schema are used to refer back to this entries in this list.")
    private final Set<Organization> parties;
    @JsonProperty("planning")
    @JsonPropertyDescription("Information from the planning phase of the contracting process. Note that many other " +
            "fields may be filled in a planning release, in the appropriate fields in other schema sections, these " +
            "would " +
            "likely be estimates at this stage e.g. totalValue in tender")
    private final Planning planning;
    @JsonProperty("relatedProcesses")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("If this process follows on from one or more prior process, represented under a separate" +
            " open contracting identifier (ocid) then details of the related process can be provided here. This is " +
            "commonly used to relate mini-competitions to their parent frameworks, full tenders to a " +
            "pre-qualification " +
            "phase, or individual tenders to a broad planning process.")
    private final Set<RelatedProcess> relatedProcesses;

    public ReleaseFS(@JsonProperty("ocid") final String ocid,
                     @JsonProperty("id") final String id,
                     @JsonProperty("date") final LocalDateTime date,
                     @JsonProperty("tag") final List<Tag> tag,
                     @JsonProperty("initiationType") final InitiationType initiationType,
                     @JsonProperty("title") final String title,
                     @JsonProperty("description") final String description,
                     @JsonProperty("language") final String language,
                     @JsonProperty("parties") final Set<Organization> parties,
                     @JsonProperty("planning") final Planning planning,
                     @JsonProperty("relatedProcesses") final LinkedHashSet<RelatedProcess> relatedProcesses) {

        this.id = id;
        this.ocid = ocid;
        this.date = date;
        this.tag = tag;
        this.initiationType = initiationType;
        this.title = title;
        this.description = description;
        this.language = language == null ? "en" : language;
        this.parties = parties;
        this.planning = planning;
        this.relatedProcesses = relatedProcesses;
    }
}
