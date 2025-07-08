package com.ataglance.walletglance.transaction.domain.model

import com.ataglance.walletglance.category.domain.model.CategoryType

data class RecordWithItems(
    val record: Record,
    val items: List<RecordItem>
) : Transaction() {

    val isNew: Boolean
        get() = record.isNew

    val recordId: Long
        get() = record.id

    override val date: Long
        get() = record.date

    val type: CategoryType
        get() = record.type

    val accountId: Int
        get() = record.accountId

    val totalAmount: Double
        get() = items.sumOf { it.totalAmount }

    override val includeInBudgets: Boolean
        get() = record.includeInBudgets


    override fun isOfType(type: CategoryType): Boolean = this.type == type

    override fun getTotalAmountByAccountAndType(accountId: Int, type: CategoryType): Double {
        if (this.accountId != accountId || this.type != type) return 0.0

        return items.sumOf { it.totalAmount }
    }

    override fun getTotalExpensesByAccount(accountId: Int): Double {
        if (type == CategoryType.Income || this.accountId != accountId) return 0.0

        return items.sumOf { it.totalAmount }
    }

    override fun getTotalExpensesByAccountsAndCategory(
        accountIds: List<Int>,
        categoryId: Int
    ): Double {
        if (type == CategoryType.Income || accountId !in accountIds) return 0.0

        return items.sumOf { item ->
            item.takeIf { it.containsCategory(id = categoryId) }?.totalAmount ?: 0.0
        }
    }

    fun groupTotalAmountsByCategories(): List<Pair<Int, List<Pair<Int?, Double>>>> {
        return items
            .groupBy(
                keySelector = { it.categoryId },
                valueTransform = { it.subcategoryId to it.totalAmount }
            )
            .toList()
    }

    fun filterItemsByCategoriesOrNull(categoryIds: List<Int>): Transaction? {
        return items
            .filter { it.containsCategoryFrom(ids = categoryIds) }
            .takeUnless { it.isEmpty() }
            ?.let { copy(items = it) }
    }

}
