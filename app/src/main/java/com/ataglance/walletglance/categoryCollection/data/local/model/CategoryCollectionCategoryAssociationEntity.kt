package com.ataglance.walletglance.categoryCollection.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.ataglance.walletglance.category.data.local.model.CategoryEntity

@Entity(
    tableName = "category_collection_category_association",
    primaryKeys = ["collectionId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryCollectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["collectionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["collectionId"]),
        Index(value = ["categoryId"])
    ]
)
data class CategoryCollectionCategoryAssociationEntity(
    val collectionId: Int,
    val categoryId: Int
)