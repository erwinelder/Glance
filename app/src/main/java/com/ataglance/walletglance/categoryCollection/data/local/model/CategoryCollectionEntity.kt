package com.ataglance.walletglance.categoryCollection.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType

@Entity(tableName = "CategoryCollection")
data class CategoryCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderNum: Int,
    val type: Char,
    val name: String
) {

    fun getCategoryType(): CategoryCollectionType {
        return when (type) {
            '-' -> CategoryCollectionType.Expense
            '+' -> CategoryCollectionType.Income
            else -> CategoryCollectionType.Mixed
        }
    }

}