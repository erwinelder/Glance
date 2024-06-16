package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "CategoryCollectionCategoryAssociation",
    primaryKeys = ["categoryCollectionId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryCollection::class,
            parentColumns = ["id"],
            childColumns = ["categoryAssetId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryCollectionCategoryAssociation(
    val categoryCollectionId: Int,
    val categoryId: Int
)