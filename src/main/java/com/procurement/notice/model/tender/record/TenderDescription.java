package com.procurement.notice.model.tender.record;

public enum TenderDescription {

    PS("Preselection stage of contracting process"),
    PQ("Preselection stage of contracting process"),
    PN("Contracting process is planned"),
    PIN("Date of tender launch is determined");

    private final String text;

    TenderDescription(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
