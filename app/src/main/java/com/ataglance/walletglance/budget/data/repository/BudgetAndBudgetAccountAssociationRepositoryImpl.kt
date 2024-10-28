package com.ataglance.walletglance.budget.data.repository

import com.ataglance.walletglance.budget.data.local.BudgetAccountAssociationLocalDataSource
import com.ataglance.walletglance.budget.data.local.BudgetLocalDataSource
import com.ataglance.walletglance.budget.data.remote.BudgetAccountAssociationRemoteDataSource
import com.ataglance.walletglance.budget.data.remote.BudgetRemoteDataSource

class BudgetAndBudgetAccountAssociationRepositoryImpl(
    override val entityLocalSource: BudgetLocalDataSource,
    override val entityRemoteSource: BudgetRemoteDataSource?,
    override val associationLocalSource: BudgetAccountAssociationLocalDataSource,
    override val associationRemoteSource: BudgetAccountAssociationRemoteDataSource?
) : BudgetAndBudgetAccountAssociationRepository