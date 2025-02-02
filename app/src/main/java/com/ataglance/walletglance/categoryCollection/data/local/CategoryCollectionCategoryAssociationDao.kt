package com.ataglance.walletglance.categoryCollection.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionCategoryAssociation
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryCollectionCategoryAssociationDao :
    BaseLocalDao<CategoryCollectionCategoryAssociation> {

    @Query("SELECT * FROM CategoryCollectionCategoryAssociation")
    override fun getAllEntities(): Flow<List<CategoryCollectionCategoryAssociation>>

}