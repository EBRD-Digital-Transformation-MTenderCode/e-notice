package com.procurement.notice.dao;

import com.procurement.notice.model.entity.ReleaseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReleaseDao {

    void saveRelease(ReleaseEntity entity);

    ReleaseEntity getByCpIdAndOcId(String cpId, String ocId);

    ReleaseEntity getRecordByCpIdAndStage(String cpId, String stage);
}
