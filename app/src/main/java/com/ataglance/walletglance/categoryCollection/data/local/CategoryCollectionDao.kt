package com.ataglance.walletglance.categoryCollection.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.categoryCollection.data.model.CategoryCollectionEntity
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryCollectionDao : BaseDao<CategoryCollectionEntity> {

    @Query("DELETE FROM CategoryCollection")
    suspend fun deleteAllCollections()

    @Query("SELECT * FROM CategoryCollection ORDER BY orderNum ASC")
    override fun getAllEntities(): Flow<List<CategoryCollectionEntity>>

}