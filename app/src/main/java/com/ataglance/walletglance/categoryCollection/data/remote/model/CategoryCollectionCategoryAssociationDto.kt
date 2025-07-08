package com.ataglance.walletglance.categoryCollection.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCollectionCategoryAssociationDto(
    val categoryCollectionId: Int,
    val categoryId: Int
)