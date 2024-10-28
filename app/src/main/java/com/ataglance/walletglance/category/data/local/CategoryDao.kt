package com.ataglance.walletglance.category.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<CategoryEntity> {

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM Category")
    override fun getAllEntities(): Flow<List<CategoryEntity>>

}