package com.procurement.notice.infrastructure.handler.record.update

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.ProceduralResult
import com.procurement.notice.infrastructure.dto.convert.convert
import com.procurement.notice.infrastructure.handler.AbstractUpdateHistoricalHandler
import com.procurement.notice.infrastructure.service.Transform
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.tryGetParams
import com.procurement.notice.service.UpdateRecordService
import org.springframework.stereotype.Component

@Component
class UpdateRecordHandler(
    private val updateRecordService: UpdateRecordService,
    private val transform: Transform,
    private val logger: Logger,
    historyDao: HistoryDao
) : AbstractUpdateHistoricalHandler<CommandType2, Fail>(historyDao = historyDao, logger = logger) {

    override val action: CommandType2
        get() = CommandType2.UPDATE_RECORD

    override fun execute(node: JsonNode): ProceduralResult<Fail> {
        val paramsNode = node.tryGetParams()
            .doOnError { error -> return ProceduralResult.error(error) }
            .get

        val params = transform.tryMapping(paramsNode, UpdateRecordRequest::class.java)
            .doOnError { error ->
                return ProceduralResult.error(
                    Fail.Error.BadRequest(
                        description = error.message,
                        json = paramsNode.toString(),
                        exception = error.exception
                    )
                )
            }
            .get
            .convert()
            .doOnError { error -> return ProceduralResult.error(error) }
            .get


        return updateRecordService.updateRecord(params = params)
    }
}
