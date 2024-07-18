package com.ataglance.walletglance.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "CategoryCollectionCategoryAssociation",
    primaryKeys = ["categoryCollectionId", "categoryId"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryCollection::class,
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