package com.ataglance.walletglance.categoryCollection.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.categoryCollection.data.local.model.CategoryCollectionCategoryAssociation

@Dao
interface CategoryCollectionCategoryAssociationDao {

    @Upsert
    suspend fun upsertCategoryCollectionCategoryAssociations(
        categoryCollectionCategoryAssociations: List<CategoryCollectionCategoryAssociation>
    )

    @Delete
    suspend fun deleteAssociations(associations: List<CategoryCollectionCategoryAssociation>)

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    suspend fun getAllCategoryCollectionCategoryAssociations():
            List<CategoryCollectionCategoryAssociation>

}