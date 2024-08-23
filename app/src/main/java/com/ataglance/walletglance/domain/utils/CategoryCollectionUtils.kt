package com.ataglance.walletglance.domain.utils

import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds


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
