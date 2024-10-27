package com.ataglance.walletglance.categoryCollection.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryCollectionCategoryAssociationDao : BaseDao<CategoryCollectionCategoryAssociation> {

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    fun getAllAssociations(): Flow<List<CategoryCollectionCategoryAssociation>>

}