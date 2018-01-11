package com.procurement.notice.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.procurement.notice.model.entity.BudgetEntity;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Service;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

@Service
public class BudgetDaoImpl implements BudgetDao {

    private static final String BUDGET_TABLE = "notice_budget_release";
    private static final String BUDGET_COMPILED_TABLE = "notice_budget_compiled_release";
    private static final String BUDGET_OFFSET_TABLE = "notice_budget_offset";
    private static final String CP_ID = "cp_id";
    private static final String OC_ID = "oc_id";
    private static final String RELEASE_DATE = "release_date";
    private static final String RELEASE_ID = "release_id";
    private static final String STAGE = "stage";
    private static final String JSON_DATA = "json_data";
    private static final String AMOUNT = "amount";

    private final Session session;

    public BudgetDaoImpl(final Session session) {
        this.session = session;
    }

    @Override
    public void saveBudget(final BudgetEntity entity) {
        final Insert insertRelease = insertInto(BUDGET_TABLE);
        insertRelease
                .value(CP_ID, entity.getCpId())
                .value(OC_ID, entity.getOcId())
                .value(RELEASE_DATE, entity.getReleaseDate())
                .value(RELEASE_ID, entity.getReleaseId())
                .value(STAGE, entity.getStage())
                .value(JSON_DATA, entity.getJsonData());

        final Insert insertCompiledRelease = insertInto(BUDGET_COMPILED_TABLE);
        insertCompiledRelease
                .value(CP_ID, entity.getCpId())
                .value(OC_ID, entity.getOcId())
                .value(RELEASE_DATE, entity.getReleaseDate())
                .value(RELEASE_ID, entity.getReleaseId())
                .value(STAGE, entity.getStage())
                .value(AMOUNT, entity.getAmount())
                .value(JSON_DATA, entity.getJsonData());

        final Insert insertOffset = insertInto(BUDGET_OFFSET_TABLE);
        insertOffset
                .value(CP_ID, entity.getCpId())
                .value(RELEASE_DATE, entity.getReleaseDate());

        final Batch batch = QueryBuilder.batch(insertRelease, insertCompiledRelease, insertOffset);
        session.execute(batch);
    }

    @Override
    public Optional<BudgetEntity> getLastByCpId(final String cpId) {
        final Statement query = select()
                .all()
                .from(BUDGET_COMPILED_TABLE)
                .where(eq(CP_ID, cpId))
                .limit(1);

        final ResultSet rows = session.execute(query);

        return Optional.ofNullable(rows.one())
                .map(row -> {
                    final String cpid = row.getString(CP_ID);
                    final String ocid = row.getString(OC_ID);
                    final Date releaseDate = row.getTimestamp(RELEASE_DATE);
                    final String releaseId = row.getString(RELEASE_ID);
                    final String stage = row.getString(STAGE);
                    final Double amount = row.getDouble(AMOUNT);
                    final String jsonData = row.getString(JSON_DATA);
                    return new BudgetEntity(cpid, ocid, releaseDate, releaseId, stage, amount, jsonData);
                });
    }

    @Override
    public Double getTotalAmountByCpId(final String cpId) {
        final Statement query = select().sum(AMOUNT).as(AMOUNT)
                .from(BUDGET_COMPILED_TABLE)
                .where(eq(CP_ID, cpId));
        final ResultSet rows = session.execute(query);
        return rows.one().getDouble(AMOUNT);
    }
}
