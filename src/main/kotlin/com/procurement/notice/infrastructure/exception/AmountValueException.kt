package com.procurement.notice.infrastructure.exception

class AmountValueException(amount: String, description: String = "") :
    RuntimeException("Incorrect value of the amount: '$amount'. $description")