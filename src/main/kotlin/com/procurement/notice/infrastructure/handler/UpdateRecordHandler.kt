package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.procurement.notice.application.service.Logger
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.extention.tryGetAttribute
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

    override fun execute(node: JsonNode): UpdateResult<Fail> {
        val dataNode = node.tryGetParams()
            .doOnError { error -> return UpdateResult.error(error) }
            .get
            .tryGetAttribute("data", JsonNodeType.OBJECT)
            .doOnError { error -> return UpdateResult.error(error) }
            .get

        val requestRelease = transform.tryMapping(dataNode, RequestRelease::class.java)
            .doOnError { error ->
                return UpdateResult.error(
                    Fail.Error.BadRequest(
                        description = "Can not parse 'data'.",
                        exception = error.exception
                    )
                )
            }
            .get

        return updateRecordService.updateRecord(data = requestRelease)
    }
}
