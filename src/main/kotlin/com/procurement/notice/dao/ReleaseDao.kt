package com.procurement.notice.dao

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.BuiltStatement
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder.eq
import com.datastax.driver.core.querybuilder.QueryBuilder.insertInto
import com.datastax.driver.core.querybuilder.QueryBuilder.select
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.Result.Companion.failure
import com.procurement.notice.domain.utils.Result.Companion.success
import com.procurement.notice.domain.utils.asSuccess
import com.procurement.notice.model.entity.ReleaseEntity
import org.springframework.stereotype.Service

@Service
class ReleaseDao(private val session: Session) {

    fun saveMs(releaseEntity: ReleaseEntity) {
        val insert = insertInto(TENDER_TABLE)
        insert
                .value(CP_ID, releaseEntity.cpId)
                .value(OC_ID, releaseEntity.ocId)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(RELEASE_ID, releaseEntity.releaseId)
                .value(STAGE, releaseEntity.stage)
                .value(JSON_DATA, releaseEntity.jsonData)

        val insertCompiled = insertInto(TENDER_COMPILED_TABLE)
        insertCompiled
                .value(CP_ID, releaseEntity.cpId)
                .value(OC_ID, releaseEntity.ocId)
                .value(PUBLISH_DATE, releaseEntity.publishDate)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(RELEASE_ID, releaseEntity.releaseId)
                .value(STAGE, releaseEntity.stage)
                .value(STATUS, releaseEntity.status)
                .value(JSON_DATA, releaseEntity.jsonData)

        val batch = QueryBuilder.batch(insert, insertCompiled)
        session.execute(batch)
    }

    fun saveRecord(releaseEntity: ReleaseEntity) {
        val insert = insertInto(TENDER_TABLE)
        insert
                .value(CP_ID, releaseEntity.cpId)
                .value(OC_ID, releaseEntity.ocId)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(RELEASE_ID, releaseEntity.releaseId)
                .value(STAGE, releaseEntity.stage)
                .value(JSON_DATA, releaseEntity.jsonData)

        val insertCompiled = insertInto(TENDER_COMPILED_TABLE)
        insertCompiled
                .value(CP_ID, releaseEntity.cpId)
                .value(OC_ID, releaseEntity.ocId)
                .value(PUBLISH_DATE, releaseEntity.publishDate)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(RELEASE_ID, releaseEntity.releaseId)
                .value(STAGE, releaseEntity.stage)
                .value(STATUS, releaseEntity.status)
                .value(JSON_DATA, releaseEntity.jsonData)

        val insertOffset = insertInto(TENDER_OFFSET_TABLE)
        insertOffset
                .value(CP_ID, releaseEntity.cpId)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(STATUS, releaseEntity.status)
                .value(STAGE, releaseEntity.stage)

        val batch = QueryBuilder.batch(insert, insertCompiled, insertOffset)
        session.execute(batch)
    }

    fun getByCpId(cpId: String): ReleaseEntity? {
        val query = select()
                .all()
                .from(TENDER_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) ReleaseEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    fun getByCpIdAndOcId(cpId: String, ocId: String): ReleaseEntity? {
        val query = select()
                .all()
                .from(TENDER_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(OC_ID, ocId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) ReleaseEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    fun tryGetByCpIdAndOcId(cpId: String, ocId: String): Result<ReleaseEntity?, Fail.Incident.Database> {
        val query = select()
                .all()
                .from(TENDER_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(OC_ID, ocId))
                .limit(1)
        return load(query)
            .doOnError { error -> return failure(error) }
            .get
            .one()
            ?.let { row -> ReleaseEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) }
            .asSuccess()
    }

    protected fun load(statement: BuiltStatement): Result<ResultSet, Fail.Incident.Database> = try {
        success(session.execute(statement))
    } catch (expected: Exception) {
        failure(Fail.Incident.Database.Access("Error accessing to database.", expected))
    }

    fun getByCpIdAndStage(cpId: String, stage: String): ReleaseEntity? {
        val query = select()
                .all()
                .from(TENDER_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(STAGE, stage))
                .allowFiltering()
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) ReleaseEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    companion object {
        private const val TENDER_TABLE = "notice_release"
        private const val TENDER_COMPILED_TABLE = "notice_compiled_release"
        private const val TENDER_OFFSET_TABLE = "notice_offset"
        private const val CP_ID = "cp_id"
        private const val OC_ID = "oc_id"
        private const val PUBLISH_DATE = "publish_date"
        private const val RELEASE_DATE = "release_date"
        private const val RELEASE_ID = "release_id"
        private const val STAGE = "stage"
        private const val STATUS = "status"
        private const val JSON_DATA = "json_data"
    }
}
