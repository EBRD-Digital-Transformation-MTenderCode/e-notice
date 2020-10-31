package com.procurement.notice.infrastructure.handler.record.create

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.application.service.record.CreateRecordService
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.MaybeFail
import com.procurement.notice.infrastructure.dto.convert.convert
import com.procurement.notice.infrastructure.handler.AbstractUpdateHistoricalHandler
import com.procurement.notice.infrastructure.service.Transform
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.tryGetParams
import org.springframework.stereotype.Component

@Component
class CreateRecordHandler(
    private val createRecordService: CreateRecordService,
    private val transform: Transform,
    private val logger: Logger,
    historyDao: HistoryDao
) : AbstractUpdateHistoricalHandler<CommandType2, Fail>(historyDao = historyDao, logger = logger) {

    override fun execute(node: JsonNode): MaybeFail<Fail> {
        val paramsNode = node.tryGetParams()
            .doReturn { error -> return MaybeFail.error(error) }

        val params = transform.tryMapping(paramsNode, CreateRecordRequest::class.java)
            .doReturn { error ->
                return MaybeFail.error(
                    Fail.Error.BadRequest(
                        description = error.message,
                        json = paramsNode.toString(),
                        exception = error.exception
                    )
                )
            }
            .convert()
            .doReturn { error -> return MaybeFail.error(error) }

        return createRecordService.createRecord(params = params)
    }

    override val action: CommandType2
        get() = CommandType2.CREATE_RECORD
}
