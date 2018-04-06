package com.procurement.notice.model.tender.record;

public enum TenderTitle {

    PS("Preselection"),
    PQ("Preselection"),
    PN("Planning Notice"),
    PIN("Prior Notice");

    private final String text;

    TenderTitle(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
