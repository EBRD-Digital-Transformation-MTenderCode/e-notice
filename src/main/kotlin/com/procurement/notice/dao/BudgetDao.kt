package com.procurement.notice.dao

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.querybuilder.QueryBuilder.*
import com.procurement.notice.model.entity.BudgetEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BudgetDao(private val session: Session) {

    fun saveBudget(entity: BudgetEntity) {
        val insertRelease = insertInto(BUDGET_TABLE)
        insertRelease
                .value(CP_ID, entity.cpId)
                .value(OC_ID, entity.ocId)
                .value(RELEASE_DATE, entity.releaseDate)
                .value(RELEASE_ID, entity.releaseId)
                .value(STAGE, entity.stage)
                .value(JSON_DATA, entity.jsonData)

        val insertCompiledRelease = insertInto(BUDGET_COMPILED_TABLE)
        insertCompiledRelease
                .value(CP_ID, entity.cpId)
                .value(OC_ID, entity.ocId)
                .value(PUBLISH_DATE, entity.publishDate)
                .value(RELEASE_DATE, entity.releaseDate)
                .value(RELEASE_ID, entity.releaseId)
                .value(STAGE, entity.stage)
                .value(AMOUNT, entity.amount)
                .value(JSON_DATA, entity.jsonData)

        val insertOffset = insertInto(BUDGET_OFFSET_TABLE)
        insertOffset
                .value(CP_ID, entity.cpId)
                .value(RELEASE_DATE, entity.releaseDate)

        val batch = QueryBuilder.batch(insertRelease, insertCompiledRelease, insertOffset)
        session.execute(batch)
    }

    fun getTotalAmountByCpId(cpId: String): BigDecimal? {
        val query = select().sum(AMOUNT).`as`(AMOUNT)
                .from(BUDGET_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
        val row = session.execute(query).one()
        return row?.getDecimal(AMOUNT)
    }

    fun getEiByCpId(cpId: String): BudgetEntity? {
        val query = select()
                .all()
                .from(BUDGET_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(OC_ID, cpId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) BudgetEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getDecimal(AMOUNT),
                row.getString(JSON_DATA)) else null
    }

    fun getFsByCpIdAndOcId(cpId: String, ocId: String): BudgetEntity? {
        val query = select()
                .all()
                .from(BUDGET_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .and(eq(OC_ID, ocId))
                .limit(1)
        val row = session.execute(query).one()
        return if (row != null) BudgetEntity(
                row.getString(CP_ID),
                row.getString(OC_ID),
                row.getTimestamp(PUBLISH_DATE),
                row.getTimestamp(RELEASE_DATE),
                row.getString(RELEASE_ID),
                row.getString(STAGE),
                row.getDecimal(AMOUNT),
                row.getString(JSON_DATA)) else null
    }

    companion object {
        private const val BUDGET_TABLE = "notice_budget_release"
        private const val BUDGET_COMPILED_TABLE = "notice_budget_compiled_release"
        private const val BUDGET_OFFSET_TABLE = "notice_budget_offset"
        private const val CP_ID = "cp_id"
        private const val OC_ID = "oc_id"
        private const val PUBLISH_DATE = "publish_date"
        private const val RELEASE_DATE = "release_date"
        private const val RELEASE_ID = "release_id"
        private const val STAGE = "stage"
        private const val JSON_DATA = "json_data"
        private const val AMOUNT = "amount"
    }
}
