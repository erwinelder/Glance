package com.ataglance.walletglance.domain.utils

import com.ataglance.walletglance.domain.categories.Category
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionWithIds


fun CategoryCollectionType.toggle(): CategoryCollectionType {
    return when (this) {
        CategoryCollectionType.Expense -> CategoryCollectionType.Income
        CategoryCollectionType.Income -> CategoryCollectionType.Mixed
        CategoryCollectionType.Mixed -> CategoryCollectionType.Expense
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
