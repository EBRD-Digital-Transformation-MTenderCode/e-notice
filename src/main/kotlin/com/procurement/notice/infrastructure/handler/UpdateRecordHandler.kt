package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.extention.toList
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.infrastructure.dto.ApiFailResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiSuccessResponse
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.service.Transform
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.tryGetId
import com.procurement.notice.model.bpe.tryGetParams
import com.procurement.notice.model.bpe.tryGetVersion
import com.procurement.notice.service.UpdateRecordService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UpdateRecordHandler(
    private val updateRecordService: UpdateRecordService,
    private val transform: Transform,
    private val logger: Logger,
    historyDao: HistoryDao
) : AbstractUpdateHistoricalHandler<CommandType2, Fail>(historyDao = historyDao, logger = logger) {

    override val action: CommandType2
        get() = CommandType2.UPDATE_RECORD

    override fun execute(node: JsonNode): ApiResponse2 {
        val id = node.tryGetId().get
        val version = node.tryGetVersion().get

        val paramsNode = node.tryGetParams()
            .doOnError { error ->
                ApiFailResponse(
                    id = id,
                    version = version,
                    result = ApiFailResponse.Error(
                        code = error.code,
                        description = error.description
                    ).toList()
                )
            }
            .get

        val requestRelease = transform.tryMapping(paramsNode, RequestRelease::class.java)
            .doOnError { error ->
                ApiFailResponse(
                    id = id,
                    version = version,
                    result = ApiFailResponse.Error(
                        code = error.code,
                        description = error.description
                    ).toList()
                )
            }
            .get

        val updateResult = updateRecordService.updateRecord(data = requestRelease)
        return when (updateResult) {
            is UpdateResult.Ok    -> ApiSuccessResponse(
                id = id,
                version = version,
                result = updateResult
            )
            is UpdateResult.Error -> {
                updateResult.get.logging(logger)
                when(updateResult.get) {
                    is Fail.Error                              -> {
                        ApiFailResponse(
                            id = id,
                            version = version,
                            result = ApiFailResponse.Error(
                                code = updateResult.get.code,
                                description = updateResult.get.message
                            ).toList()
                        )
                    }
                    is Fail.Incident.Transform.Parsing         ,
                    is Fail.Incident.Transform.Mapping         ,
                    is Fail.Incident.Transform.Deserialization ,
                    is Fail.Incident.Transform.Serialization   ,
                    is Fail.Incident.NetworkError              ,
                    is Fail.Incident.BadResponse               ,
                    is Fail.Incident.ResponseError             ,
                    is Fail.Incident.Database.Access           ,
                    is Fail.Incident.Database.NotFound         ,
                    is Fail.Incident.Database.InvalidData      ,
                    is Fail.Incident.InternalError             -> {
                        ApiIncidentResponse(
                            id = id,
                            version = version,
                            result = ApiIncidentResponse.Incident(
                                id = id,
                                date = LocalDateTime.now(),
                                service = ApiIncidentResponse.Incident.Service(
                                    id = GlobalProperties.service.id,
                                    version = GlobalProperties.service.version,
                                    name = GlobalProperties.service.name
                                ),
                                errors = ApiIncidentResponse.Incident.Error(
                                    code = updateResult.get.code,
                                    description = updateResult.get.description,
                                    metadata = null
                                ).toList()
                            )
                        )
                    }
                }

            }
        }
    }
}
