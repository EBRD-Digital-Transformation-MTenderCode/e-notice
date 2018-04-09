package com.procurement.notice.dao;

import com.procurement.notice.model.entity.ReleaseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReleaseDao {

    void saveRelease(ReleaseEntity entity);

    ReleaseEntity getRecordByCpIdAndStage(String cpId, String stage);
}
