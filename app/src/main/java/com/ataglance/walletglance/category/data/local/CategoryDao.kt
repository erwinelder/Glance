package com.ataglance.walletglance.category.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseLocalDao<CategoryEntity> {

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM Category")
    override fun getAllEntities(): Flow<List<CategoryEntity>>

}