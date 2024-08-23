package com.ataglance.walletglance.categoryCollection.utils

import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds


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
