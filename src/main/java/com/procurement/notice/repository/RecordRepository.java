package com.procurement.notice.repository;

import com.procurement.notice.model.entity.RecordEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends CassandraRepository<RecordEntity, String> {

    @Query(value = "select * from notice_record where cp_id=?0 LIMIT 1")
    RecordEntity getLastByCpId(String cpId);
}
