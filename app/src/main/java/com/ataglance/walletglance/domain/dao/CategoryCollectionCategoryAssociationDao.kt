package com.ataglance.walletglance.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ataglance.walletglance.domain.entities.CategoryCollectionCategoryAssociation

@Dao
interface CategoryCollectionCategoryAssociationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCategoryCollectionCategoryAssociations(
        categoryCollectionCategoryAssociations: List<CategoryCollectionCategoryAssociation>
    )

    @Delete
    suspend fun deleteAssociations(associations: List<CategoryCollectionCategoryAssociation>)

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    suspend fun getAllCollectionAndCategoryAssociations():
            List<CategoryCollectionCategoryAssociation>

}