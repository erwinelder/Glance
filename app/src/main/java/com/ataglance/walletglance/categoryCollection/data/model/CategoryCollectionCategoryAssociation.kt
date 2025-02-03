package com.ataglance.walletglance.categoryCollection.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.ataglance.walletglance.category.data.local.model.CategoryEntity

@Entity(
    tableName = "CategoryCollectionCategoryAssociation",
    primaryKeys = ["categoryCollectionId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryCollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryCollectionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class CategoryCollectionCategoryAssociation(
    val categoryCollectionId: Int,
    val categoryId: Int
)