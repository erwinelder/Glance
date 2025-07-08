package com.ataglance.walletglance.categoryCollection.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryCollectionDtoWithAssociations(
    val collection: CategoryCollectionDto,
    val associations: List<CategoryCollectionCategoryAssociationDto>
)
