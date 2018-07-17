package com.procurement.notice.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.notice.model.entity.ReleaseEntity
import org.springframework.stereotype.Service

@Service
interface ReleaseDao {

    fun saveRelease(releaseEntity: ReleaseEntity, status: String)

    fun getByCpId(cpId: String): ReleaseEntity?

    fun getByCpIdAndOcId(cpId: String, ocId: String): ReleaseEntity?

    fun getByCpIdAndStage(cpId: String, stage: String): ReleaseEntity?
}

@Service
class ReleaseDaoImpl(private val session: Session) : ReleaseDao {

    override fun saveRelease(releaseEntity: ReleaseEntity, status: String) {
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
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(RELEASE_ID, releaseEntity.releaseId)
                .value(STAGE, releaseEntity.stage)
                .value(JSON_DATA, releaseEntity.jsonData)

        val insertOffset = insertInto(TENDER_OFFSET_TABLE)
        insertOffset
                .value(CP_ID, releaseEntity.cpId)
                .value(RELEASE_DATE, releaseEntity.releaseDate)
                .value(STATUS, status)

        val batch = QueryBuilder.batch(insert, insertCompiled, insertOffset)
        session.execute(batch)
    }

    override fun getByCpId(cpId: String): ReleaseEntity? {
        val query = select()
                .all()
                .from(TENDER_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) ReleaseEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    override fun getByCpIdAndOcId(cpId: String, ocId: String): ReleaseEntity? {
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
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    override fun getByCpIdAndStage(cpId: String, stage: String): ReleaseEntity? {
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
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getString(JSON_DATA)) else null
    }

    companion object {
        private val TENDER_TABLE = "notice_release"
        private val TENDER_COMPILED_TABLE = "notice_compiled_release"
        private val TENDER_OFFSET_TABLE = "notice_offset"
        private val CP_ID = "cp_id"
        private val OC_ID = "oc_id"
        private val RELEASE_DATE = "release_date"
        private val RELEASE_ID = "release_id"
        private val STAGE = "stage"
        private val STATUS = "status"
        private val JSON_DATA = "json_data"
    }
}
