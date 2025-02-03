package com.ataglance.walletglance.category.presentation.model

import com.ataglance.walletglance.category.domain.model.CategoriesStatsMapItem
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.record.domain.model.RecordStack
import com.ataglance.walletglance.record.domain.model.RecordStackItem
import com.ataglance.walletglance.record.domain.model.RecordType

data class CategoryStatisticsLists(
    val expense: List<CategoryStatisticsElementUiState> = emptyList(),
    val income: List<CategoryStatisticsElementUiState> = emptyList()
) {

    companion object {

        fun fromRecordStacks(recordStacks: List<RecordStack>): CategoryStatisticsLists {
            return CategoryStatisticsLists(
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
        ): List<CategoryStatisticsElementUiState> {
            val categoryStatsMap = mutableMapOf<Int, CategoriesStatsMapItem>()
            val subcategoriesStatsMap = mutableMapOf<Int, MutableMap<Int, CategoriesStatsMapItem>>()

            recordStackList.forEach { recordStack ->
                if (recordStack.isOfType(type)) {
                    recordStack.stack.forEach { stackUnit ->
                        categoryStatsMap.increaseTotalAmountOrAddNewOne(stackUnit)
                        subcategoriesStatsMap.increaseTotalAmountOrAddNewOne(stackUnit)
                    }
                }
            }

            val accountCurrency = recordStackList.firstOrNull()?.account?.currency ?: ""
            val totalAmount = categoryStatsMap.values.sumOf { it.totalAmount }
            return categoryStatsMap.values.sortedByDescending { it.totalAmount }
                .map { statsMapItem ->
                    statsMapItem.toCategoryStatisticsElementUiState(
                        accountCurrency = accountCurrency,
                        allCategoriesTotalAmount = totalAmount,
                        subcategoriesStatistics = subcategoriesStatsMap[statsMapItem.category.id]
                    )
                }
        }

        private fun MutableMap<Int, CategoriesStatsMapItem>.increaseTotalAmountOrAddNewOne(
            stackUnit: RecordStackItem
        ) {
            stackUnit.categoryWithSubcategory ?: return
            val category = stackUnit.categoryWithSubcategory.category

            if (this.containsKey(category.id)) {
                this[category.id]!!.totalAmount += stackUnit.amount
            } else {
                this[category.id] = CategoriesStatsMapItem(
                    category = category,
                    totalAmount = stackUnit.amount
                )
            }
        }

        private fun MutableMap<Int, MutableMap<Int, CategoriesStatsMapItem>>.increaseTotalAmountOrAddNewOne(
            stackUnit: RecordStackItem
        ) {
            stackUnit.categoryWithSubcategory?.subcategory ?: return
            val category = stackUnit.categoryWithSubcategory.category
            val subcategory = stackUnit.categoryWithSubcategory.subcategory

            if (!this.containsKey(category.id)) {
                this[category.id] = mutableMapOf(
                    subcategory.id to CategoriesStatsMapItem(subcategory, stackUnit.amount)
                )
                return
            }

            if (this[category.id]!!.containsKey(subcategory.id)) {
                this[category.id]!![subcategory.id]!!.totalAmount += stackUnit.amount
            } else {
                this[category.id]!![subcategory.id] = CategoriesStatsMapItem(
                    category = subcategory,
                    totalAmount = stackUnit.amount
                )
            }
        }

    }


    fun getByType(type: CategoryType): List<CategoryStatisticsElementUiState> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun getItemByParentCategoryId(id: Int): CategoryStatisticsElementUiState? {
        return (expense + income).find { it.category.id == id }
    }

}