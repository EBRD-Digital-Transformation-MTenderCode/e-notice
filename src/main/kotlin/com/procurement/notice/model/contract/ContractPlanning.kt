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

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var implementation: Implementation?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: PlanningBudget?
)

data class PlanningBudget @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetAllocation: Set<BudgetAllocation>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetSource: Set<PlanningBudgetSource>?
)

data class BudgetAllocation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var budgetBreakdownID: String?,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?
)

data class PlanningBudgetSource @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var budgetBreakdownID: String?,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val currency: String?
)

data class Implementation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val transactions: Set<Transaction>?
)

data class Transaction @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val type: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val executionPeriod: ExecutionPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedContractMilestone: String?
)

data class ExecutionPeriod @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val durationInDays: Long?
)
