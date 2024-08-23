package com.ataglance.walletglance.data.categoryCollections

import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.domain.utils.toCollectionsWithCategories

data class CategoryCollectionsWithIds(
    val expense: List<CategoryCollectionWithIds> = listOf(),
    val income: List<CategoryCollectionWithIds> = listOf(),
    val mixed: List<CategoryCollectionWithIds> = listOf()
) {

    fun concatenateLists(): List<CategoryCollectionWithIds> {
        return expense + income + mixed
    }

    fun getByType(type: CategoryCollectionType): List<CategoryCollectionWithIds> {
        return when (type) {
            CategoryCollectionType.Expense -> expense
            CategoryCollectionType.Income -> income
            CategoryCollectionType.Mixed -> mixed
        }
    }

    fun getByCategoryType(type: CategoryType): List<CategoryCollectionWithIds> {
        return when (type) {
            CategoryType.Expense -> expense
            CategoryType.Income -> income
        }
    }

    fun appendDefaultCollection(name: String): CategoryCollectionsWithIds {
        return CategoryCollectionsWithIds(
            expense = listOf(
                CategoryCollectionWithIds(type = CategoryCollectionType.Expense, name = name)
            ) + expense,
            income = listOf(
                CategoryCollectionWithIds(type = CategoryCollectionType.Income, name = name)
            ) + income,
            mixed = listOf(
                CategoryCollectionWithIds(type = CategoryCollectionType.Mixed, name = name)
            ) + mixed,
        )
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
