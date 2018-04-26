package com.procurement.notice.model.tender.record

enum class TenderDescription(val text: String) {

    PS("Preselection stage of contracting process"),
    PQ("Prequalification stage of contracting process"),
    EV("Evaluation stage of contracting process"),
    PN("Contracting process is planned"),
    PIN("Date of tender launch is determined")
}
