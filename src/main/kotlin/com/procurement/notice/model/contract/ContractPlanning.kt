package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value
import java.math.BigDecimal

data class ContractPlanning @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var implementation: Implementation?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: PlanningBudget?
)

data class PlanningBudget @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetAllocation: Set<BudgetAllocation>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetSource: Set<PlanningBudgetSource>?
)

data class BudgetAllocation @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var budgetBreakdownID: String?,

    @JsonDeserialize(using = AmountDeserializer::class)
    @JsonSerialize(using = AmountSerializer::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: BigDecimal?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?
)

data class PlanningBudgetSource @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var budgetBreakdownID: String?,

    @JsonDeserialize(using = AmountDeserializer::class)
    @JsonSerialize(using = AmountSerializer::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: BigDecimal?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val currency: String?
)

data class Implementation @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val transactions: Set<Transaction>?
)

data class Transaction @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val executionPeriod: ExecutionPeriod?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedContractMilestone: String?
)

data class ExecutionPeriod @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val durationInDays: Long?
)