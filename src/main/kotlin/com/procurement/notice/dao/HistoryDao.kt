package com.procurement.notice.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.HistoryEntity
import com.procurement.notice.utils.localNowUTC
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import org.springframework.stereotype.Service

@Service
interface HistoryDao {

    fun getHistory(operationId: String, command: String): HistoryEntity?

    fun saveHistory(operationId: String, command: String, response: ResponseDto): HistoryEntity

}

@Service
class HistoryDaoImpl(private val session: Session) : HistoryDao {

    override fun getHistory(operationId: String, command: String): HistoryEntity? {
        val query = select()
                .all()
                .from(HISTORY_TABLE)
                .where(eq(OPERATION_ID, operationId))
                .and(eq(COMMAND, command))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) HistoryEntity(
                row.getString(OPERATION_ID),
                row.getString(COMMAND),
                row.getTimestamp(OPERATION_DATE),
                row.getString(JSON_DATA)) else null
    }

    override fun saveHistory(operationId: String, command: String, response: ResponseDto): HistoryEntity {
        val entity = HistoryEntity(
                operationId = operationId,
                command = command,
                operationDate = localNowUTC().toDate(),
                jsonData = toJson(response))

        val insert = insertInto(HISTORY_TABLE).ifNotExists()
                .value(OPERATION_ID, entity.operationId)
                .value(COMMAND, entity.command)
                .value(OPERATION_DATE, entity.operationDate)
                .value(JSON_DATA, entity.jsonData)
        val resultSet = session.execute(insert)
        return if (!resultSet.wasApplied()) {
            getHistory(entity.operationId, entity.command) ?: throw ErrorException(ErrorType.HISTORY_ERROR)
        } else {
            entity
        }
    }

    companion object {
        private const val HISTORY_TABLE = "notice_history"
        private const val OPERATION_ID = "operation_id"
        private const val COMMAND = "command"
        private const val OPERATION_DATE = "OPERATION_date"
        private const val JSON_DATA = "json_data"
    }

}
