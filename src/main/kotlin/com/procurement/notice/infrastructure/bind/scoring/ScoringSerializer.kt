package com.procurement.notice.infrastructure.bind.scoring

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.math.BigDecimal

class ScoringSerializer : JsonSerializer<BigDecimal>() {
    companion object {
        fun serialize(scoring: BigDecimal): String = "%.2f".format(scoring)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(scoring: BigDecimal, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeNumber(serialize(scoring))
}