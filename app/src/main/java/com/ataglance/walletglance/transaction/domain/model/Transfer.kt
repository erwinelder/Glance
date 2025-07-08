package com.ataglance.walletglance.transaction.domain.model

import com.ataglance.walletglance.category.domain.model.CategoryType

data class Transfer(
    val id: Long,
    override val date: Long,
    val sender: TransferItem,
    val receiver: TransferItem,
    override val includeInBudgets: Boolean
) : Transaction() {

    val isNew: Boolean
        get() = id == 0L

    val senderAccountId: Int
        get() = sender.accountId

    val receiverAccountId: Int
        get() = receiver.accountId

    val senderAmount: Double
        get() = sender.amount

    val receiverAmount: Double
        get() = receiver.amount


    fun containsAccount(accountId: Int): Boolean {
        return senderAccountId == accountId || receiverAccountId == accountId
    }


    override fun isOfType(type: CategoryType): Boolean = true

    override fun getTotalAmountByAccountAndType(accountId: Int, type: CategoryType): Double {
        return when (type) {
            CategoryType.Expense -> if (senderAccountId == accountId) senderAmount else 0.0
            CategoryType.Income -> if (receiverAccountId == accountId) receiverAmount else 0.0
        }
    }

    override fun getTotalExpensesByAccount(accountId: Int): Double {
        if (senderAccountId != accountId) return 0.0

        return senderAmount
    }

    override fun getTotalExpensesByAccountsAndCategory(
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        if (senderAccountId !in accountIds) return 0.0

        return if (categoryId == 12 || categoryId == 67) senderAmount else 0.0
    }

    fun groupTotalAmountsByCategories(
        type: CategoryType,
        accountId: Int
    ): List<Pair<Int, List<Pair<Int?, Double>>>> {
        if (type == CategoryType.Expense && senderAccountId != accountId) return emptyList()
        if (type == CategoryType.Income && receiverAccountId != accountId) return emptyList()

        return when (type) {
            CategoryType.Expense -> listOf(12 to listOf(79 to senderAmount))
            CategoryType.Income -> listOf(67 to listOf(null to receiverAmount))
        }
    }

    fun getIfMatchAnyCategoryId(categoryIds: List<Int>): Transfer? {
        return takeIf {
            categoryIds.contains(12) || categoryIds.contains(79) || categoryIds.contains(67)
        }
    }

}
