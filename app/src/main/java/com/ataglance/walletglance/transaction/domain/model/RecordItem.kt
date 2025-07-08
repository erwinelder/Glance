package com.ataglance.walletglance.transaction.domain.model

data class RecordItem(
    val id: Long,
    val recordId: Long,
    val totalAmount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?
) {

    fun containsCategory(id: Int): Boolean {
        return categoryId == id || subcategoryId == id
    }

    fun containsCategoryFrom(ids: List<Int>): Boolean {
        return ids.contains(categoryId) || (subcategoryId != null && ids.contains(subcategoryId))
    }

    fun matchesCategory(recordItem: RecordItem): Boolean {
        return categoryId == recordItem.categoryId && subcategoryId == recordItem.subcategoryId
    }

}
