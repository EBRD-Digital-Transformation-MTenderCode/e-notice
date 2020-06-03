package com.procurement.notice.infrastructure.exception

class ScoringValueException(scoring: String, description: String = "") :
    RuntimeException("Incorrect value of the scoring: '$scoring'. $description")