package com.ataglance.walletglance.transaction.domain.model

import com.ataglance.walletglance.category.domain.model.CategoryType

sealed class Transaction {

    abstract val date: Long
    abstract val includeInBudgets: Boolean


    abstract fun isOfType(type: CategoryType): Boolean

    abstract fun getTotalAmountByAccountAndType(accountId: Int, type: CategoryType): Double

    abstract fun getTotalExpensesByAccount(accountId: Int): Double

    abstract fun getTotalExpensesByAccountsAndCategory(
        accountIds: List<Int>,
        categoryId: Int
    ): Double

}