package com.procurement.notice.dao;

import com.procurement.notice.model.entity.BudgetEntity;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface BudgetDao {

    void saveBudget(BudgetEntity entity);

    Double getTotalAmountByCpId(String cpId);

    Optional<BudgetEntity> getLastByCpId(String cpId);

}
