package com.ataglance.walletglance.budget.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class BudgetDtoWithAssociations(
    val budget: BudgetDto,
    val associations: List<BudgetAccountAssociationDto>
)
