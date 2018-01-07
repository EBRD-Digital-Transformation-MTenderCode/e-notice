package com.procurement.notice.dao;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.procurement.notice.model.entity.ReleaseEntity;
import org.springframework.stereotype.Service;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

@Service
public class ReleaseDaoImpl implements ReleaseDao {

    private final Session session;

    public ReleaseDaoImpl(final Session session) {
        this.session = session;
    }

    @Override
    public void save(final ReleaseEntity entity) {

        final Insert insertRelease = insertInto("notice_release");
        insertRelease
                .value("cp_id", entity.getCpId())
                .value("oc_id", entity.getOcId())
                .value("release_date", entity.getReleaseDate())
                .value("release_id", entity.getReleaseId())
                .value("stage", entity.getStage())
                .value("json_data", entity.getJsonData());

        final Insert insertCompiledRelease = insertInto("notice_compiled_release");
        insertCompiledRelease
                .value("cp_id", entity.getCpId())
                .value("oc_id", entity.getOcId())
                .value("release_date", entity.getReleaseDate())
                .value("release_id", entity.getReleaseId())
                .value("stage", entity.getStage())
                .value("json_data", entity.getJsonData());

        final Insert insertOffset = insertInto("notice_offset");
        insertOffset
                .value("cp_id", entity.getCpId())
                .value("release_date", entity.getReleaseDate());

        Batch batch = QueryBuilder.batch(insertRelease, insertCompiledRelease, insertOffset);
        session.execute(batch);

    }
}
