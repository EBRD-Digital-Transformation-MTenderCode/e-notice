package com.procurement.notice.dao;

import com.procurement.notice.model.entity.TenderEntity;
import org.springframework.stereotype.Service;

@Service
public interface TenderDao {

    void saveTender(TenderEntity entity);

}
