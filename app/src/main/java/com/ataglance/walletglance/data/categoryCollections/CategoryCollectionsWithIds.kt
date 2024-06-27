package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.ui.utils.toCollectionsWithCategories

data class CategoryCollectionsWithIds(
    val expense: List<CategoryCollectionWithIds> = listOf(),
    val income: List<CategoryCollectionWithIds> = listOf(),
    val mixed: List<CategoryCollectionWithIds> = listOf()
) {

    fun concatenateLists(): List<CategoryCollectionWithIds> {
        return expense + income + mixed
    }

    fun toCollectionsWithCategories(
        allCategories: List<Category>
    ): CategoryCollectionsWithCategories {
        return CategoryCollectionsWithCategories(
            expense = expense.toCollectionsWithCategories(allCategories),
            income = income.toCollectionsWithCategories(allCategories),
            mixed = mixed.toCollectionsWithCategories(allCategories)
        )
    }

}
