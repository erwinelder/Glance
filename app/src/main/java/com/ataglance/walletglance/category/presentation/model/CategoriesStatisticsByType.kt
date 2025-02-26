package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoriesStatsItem
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType

data class CategoriesStatisticsByType(
    val expense: List<CategoryStatistics> = emptyList(),
    val income: List<CategoryStatistics> = emptyList()
) {

    companion object {

        fun fromRecordStacks(recordStacks: List<RecordStack>): CategoriesStatisticsByType {
            return CategoriesStatisticsByType(
                expense = getCategoryStatisticsByType(
                    recordStackList = recordStacks,
                    type = RecordType.Expense,
                ),
                income = getCategoryStatisticsByType(
                    recordStackList = recordStacks,
                    type = RecordType.Income,
                )
            )
        }

        private fun getCategoryStatisticsByType(
            recordStackList: List<RecordStack>,
            type: RecordType,
        ): List<CategoryStatistics> {
            val categoryStatsMap = mutableMapOf<Int, CategoriesStatsItem>()
            val subcategoriesStatsMap = mutableMapOf<Int, MutableMap<Int, CategoriesStatsItem>>()

            recordStackList.forEach { recordStack ->
                if (recordStack.isOfType(type)) {
                    recordStack.stack.forEach { stackUnit ->
                        categoryStatsMap.increaseTotalAmountOrAddNewOneToCategory(stackUnit)
                        subcategoriesStatsMap.increaseTotalAmountOrAddNewOneToSubcategory(stackUnit)
                    }
                }
            }

            val accountCurrency = recordStackList.firstOrNull()?.account?.currency ?: ""
            val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }

            return categoryStatsMap.values.sortedByDescending { it.totalAmount }
                .map { statsMapItem ->
                    CategoryStatistics.fromStatsMap(
                        accountCurrency = accountCurrency,
                        allCategoriesTotalAmount = totalAmount,
                        categoriesStatsItem = statsMapItem,
                        subcategoriesStatistics = subcategoriesStatsMap[statsMapItem.category.id]
                    )
                }
        }

        private fun MutableMap<Int, CategoriesStatsItem>.increaseTotalAmountOrAddNewOneToCategory(
            stackUnit: RecordStackItem
        ) {
            stackUnit.categoryWithSub ?: return
            val category = stackUnit.categoryWithSub.category

            if (this.containsKey(category.id)) {
                this[category.id]!!.totalAmount += stackUnit.amount
            } else {
                this[category.id] = CategoriesStatsItem(
                    category = category,
                    totalAmount = stackUnit.amount
                )
            }
        }

        private fun MutableMap<Int, MutableMap<Int, CategoriesStatsItem>>
                .increaseTotalAmountOrAddNewOneToSubcategory(stackUnit: RecordStackItem)
        {
            stackUnit.categoryWithSub?.subcategory ?: return
            val category = stackUnit.categoryWithSub.category
            val subcategory = stackUnit.categoryWithSub.subcategory

            if (!this.containsKey(category.id)) {
                this[category.id] = mutableMapOf(
                    subcategory.id to CategoriesStatsItem(subcategory, stackUnit.amount)
                )
                return
            }

            if (this[category.id]!!.containsKey(subcategory.id)) {
                this[category.id]!![subcategory.id]!!.totalAmount += stackUnit.amount
            } else {
                this[category.id]!![subcategory.id] = CategoriesStatsItem(
                    category = subcategory,
                    totalAmount = stackUnit.amount
                )
            }
        }

    }


    fun getByType(type: CategoryType): List<CategoryStatistics> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun getByType(type: CategoryCollectionType): List<CategoryStatistics> {
        return when (type) {
            CategoryCollectionType.Expense -> expense
            CategoryCollectionType.Income -> income
            else -> emptyList()
        }
    }

    fun getExpenseIfNotEmptyOrIncome(): List<CategoryStatistics> {
        return expense.ifEmpty { income }
    }

    fun getParentStatsIfSubStatsPresent(id: Int): CategoryStatistics? {
        return (expense + income)
            .find { it.category.id == id }
            ?.takeIf { it.subcategoriesStatistics != null }
    }

}