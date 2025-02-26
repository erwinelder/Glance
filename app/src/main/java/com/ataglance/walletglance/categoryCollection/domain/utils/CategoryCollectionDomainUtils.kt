package com.ataglance.walletglance.categoryCollection.domain.utils

import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds


fun CategoryCollectionType.toggle(): CategoryCollectionType {
    return when (this) {
        CategoryCollectionType.Expense -> CategoryCollectionType.Income
        CategoryCollectionType.Income -> CategoryCollectionType.Mixed
        CategoryCollectionType.Mixed -> CategoryCollectionType.Expense
    }
}

fun CategoryCollectionType.toggleExpenseIncome(): CategoryCollectionType {
    return when (this) {
        CategoryCollectionType.Expense -> CategoryCollectionType.Income
        else -> CategoryCollectionType.Expense
    }
}


fun Char.asCategoryCollectionType(): CategoryCollectionType {
    return when (this) {
        '-' -> CategoryCollectionType.Expense
        '+' -> CategoryCollectionType.Income
        else -> CategoryCollectionType.Mixed
    }
}

fun CategoryCollectionType.asChar(): Char {
    return when (this) {
        CategoryCollectionType.Expense -> '-'
        CategoryCollectionType.Income -> '+'
        CategoryCollectionType.Mixed -> 'm'
    }
}


fun List<CategoryCollectionWithIds>.toCollectionsWithCategories(
    allCategories: List<Category>
): List<CategoryCollectionWithCategories> {
    return this.map { it.toCategoryCollectionWithCategories(allCategories) }
}


fun List<CategoryCollectionWithCategories>.toCollectionsWithIds(): List<CategoryCollectionWithIds> {
    return this.map { it.toCollectionWithIds() }
}
