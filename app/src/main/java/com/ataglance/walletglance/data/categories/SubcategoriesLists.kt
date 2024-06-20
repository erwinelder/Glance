package com.ataglance.walletglance.data.categories

import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.data.records.RecordType

data class SubcategoriesLists(
    val expense: List<List<Category>> = emptyList(),
    val income: List<List<Category>> = emptyList()
) {

    fun getByType(type: CategoryType): List<List<Category>> {
        return if (type == CategoryType.Expense) expense else income
    }

    fun getByType(type: RecordType): List<List<Category>> {
        return when (type) {
            RecordType.Expense -> expense
            RecordType.Income -> income
        }
    }

}