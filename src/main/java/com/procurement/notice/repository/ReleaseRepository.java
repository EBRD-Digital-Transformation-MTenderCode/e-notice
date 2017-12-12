package com.procurement.notice.repository;

import com.procurement.notice.model.entity.ReleaseEntity;
import java.util.Optional;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends CassandraRepository<ReleaseEntity, String> {

    @Query(value = "select * from notice_release where cp_id=?0 LIMIT 1")
    ReleaseEntity getLastByCpId(String cpId);

    @Query(value = "SELECT release_version FROM notice_release WHERE cp_id=?0 AND oc_id=?1 LIMIT 1")
    Optional<Integer> getLastReleaseVersion(String cpId, String ocId);
}
