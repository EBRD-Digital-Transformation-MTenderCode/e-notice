package com.procurement.notice.dao;

import com.procurement.notice.model.entity.ReleaseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReleaseDao {

    void save(ReleaseEntity entity);

}
