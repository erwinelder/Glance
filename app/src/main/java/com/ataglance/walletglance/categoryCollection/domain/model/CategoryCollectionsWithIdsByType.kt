package com.ataglance.walletglance.categoryCollection.domain.model

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.utils.toCollectionsWithCategories

data class CategoryCollectionsWithIdsByType(
    val expense: List<CategoryCollectionWithIds> = listOf(),
    val income: List<CategoryCollectionWithIds> = listOf(),
    val mixed: List<CategoryCollectionWithIds> = listOf()
) {

    companion object {

        fun fromCollections(
            collections: List<CategoryCollectionWithIds>
        ): CategoryCollectionsWithIdsByType {
            val map = collections.groupBy { it.type }

            return CategoryCollectionsWithIdsByType(
                expense = map[CategoryCollectionType.Expense].orEmpty(),
                income = map[CategoryCollectionType.Income].orEmpty(),
                mixed = map[CategoryCollectionType.Mixed].orEmpty()
            )
        }

    }


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

    fun appendDefaultCollection(name: String): CategoryCollectionsWithIdsByType {
        return CategoryCollectionsWithIdsByType(
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
