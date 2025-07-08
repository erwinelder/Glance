package com.ataglance.walletglance.categoryCollection.data.local.model

data class CategoryCollectionEntityWithAssociations(
    val collection: CategoryCollectionEntity,
    val associations: List<CategoryCollectionCategoryAssociationEntity>
) {

    val collectionId: Int
        get() = collection.id

    val deleted: Boolean
        get() = collection.deleted

    fun asPair(): Pair<CategoryCollectionEntity, List<CategoryCollectionCategoryAssociationEntity>> {
        return collection to associations.takeUnless { collection.deleted }.orEmpty()
    }

}
