package com.procurement.notice.infrastructure.exception

class QuantityValueException(quantity: String, description: String = "") :
    RuntimeException("Incorrect value of the quantity: '$quantity'. $description")