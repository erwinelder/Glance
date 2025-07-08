package com.ataglance.walletglance.transaction.domain.utils

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.core.utils.timestampToYear
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transaction
import com.ataglance.walletglance.transaction.domain.model.Transfer


fun List<Transaction>.filterByAccount(accountId: Int?): List<Transaction> {
    if (accountId == null) return this

    return filter { transaction ->
        when (transaction) {
            is RecordWithItems -> transaction.accountId == accountId
            is Transfer -> transaction.containsAccount(accountId = accountId)
        }
    }
}


fun List<Transaction>.filterByCategoryIds(categoryIds: List<Int>): List<Transaction> {
    return mapNotNull { transaction ->
        when (transaction) {
            is RecordWithItems -> {
                transaction.filterItemsByCategoriesOrNull(categoryIds = categoryIds)
            }
            is Transfer -> {
                transaction.getIfMatchAnyCategoryId(categoryIds = categoryIds)
            }
        }
    }
}

fun List<Transaction>.filterByCategoryType(type: CategoryType): List<Transaction> {
    return when (type) {
        CategoryType.Expense -> filter { it.isOfType(type = CategoryType.Expense) }
        CategoryType.Income -> filter { it.isOfType(type = CategoryType.Income) }
    }
}

fun List<Transaction>.filterNotEmptyByCategoryTypes(): Pair<CategoryType, List<Transaction>> {
    this
        .filterByCategoryType(type = CategoryType.Expense)
        .takeIf { it.isNotEmpty() }
        ?.let { transactions ->
            return CategoryType.Expense to transactions
        }

    this
        .filterByCategoryType(type = CategoryType.Income)
        .takeIf { it.isNotEmpty() }
        ?.let { transactions ->
            return CategoryType.Income to transactions
        }

    return CategoryType.Expense to emptyList()
}

fun List<Transaction>.filterByCollectionType(type: CategoryCollectionType): List<Transaction> {
    return when (type) {
        CategoryCollectionType.Mixed -> this
        CategoryCollectionType.Expense -> this.filter { it.isOfType(type = CategoryType.Expense) }
        CategoryCollectionType.Income -> this.filter { it.isOfType(type = CategoryType.Income) }
    }
}

fun List<Transaction>.filterByCollection(collection: CategoryCollectionWithIds): List<Transaction> {
    return filterByCollectionType(type = collection.type).let { transactions ->
        if (collection.categoryIds.isNullOrEmpty()) {
            transactions
        } else {
            transactions.filterByCategoryIds(categoryIds = collection.categoryIds)
        }
    }
}


fun List<Transaction>.groupTotalAmountsByCategories(
    type: CategoryType,
    accountId: Int
): Map<Int, Map<Int?, Double>> {
    return this
        .flatMap { transaction ->
            when (transaction) {
                is RecordWithItems -> transaction.groupTotalAmountsByCategories()
                is Transfer -> transaction.groupTotalAmountsByCategories(
                    type = type, accountId = accountId
                )
            }
        }
        .groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
        .mapValues { (_, listOfLists) ->
            listOfLists
                .flatten()
                .groupBy(
                    keySelector = { it.first },
                    valueTransform = { it.second }
                )
                .mapValues { (_, amounts) -> amounts.sum() }
        }
}


fun List<Transaction>.getTotalAmountByAccountAndType(accountId: Int, type: CategoryType): Double {
    return sumOf { transaction ->
        transaction.getTotalAmountByAccountAndType(accountId = accountId, type = type)
    }
}


fun List<Transaction>.containTransactionsFromDifferentYears(): Boolean {
    return isNotEmpty() && first().date.timestampToYear() != last().date.timestampToYear()
}


fun List<RecordItem>.distinctByCategories(maxCount: Int): List<RecordItem> {
    val distinctItems = mutableListOf<RecordItem>()

    for (i in indices) {
        if (distinctItems.size >= maxCount) { break }

        this[i]
            .takeIf { item ->
                distinctItems.none { it.matchesCategory(recordItem = item) }
            }
            ?.let { distinctItems.add(it) }
    }

    return distinctItems
}

fun List<RecordItem>.foldNotesByCategory(categoryId: Int, subcategoryId: Int?): String? {
    return this
        .filter {
            it.categoryId == categoryId && it.subcategoryId == subcategoryId && !it.note.isNullOrBlank()
        }
        .map { it.note }
        .distinct()
        .joinToString(separator = ", ")
        .takeIf { it.isNotBlank() }
}
