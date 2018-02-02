package com.procurement.notice.dao;

import com.procurement.notice.model.entity.BudgetEntity;
import org.springframework.stereotype.Service;

@Service
public interface BudgetDao {

    void saveBudget(BudgetEntity entity);

    Double getTotalAmountByCpId(String cpId);

    BudgetEntity getLastByCpId(String cpId);

    BudgetEntity getLastByCpIdAndOcId(String cpId, String ocId);

}
