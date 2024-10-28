package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.model.BudgetEntity
import com.ataglance.walletglance.core.data.repository.BaseEntityAndAssociationRepository

interface BudgetAndBudgetAccountAssociationRepository :
    BaseEntityAndAssociationRepository<BudgetEntity, BudgetAccountAssociation>