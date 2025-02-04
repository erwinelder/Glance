package com.ataglance.walletglance.category.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.category.data.local.model.CategoryEntity
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryLocalDao : BaseLocalDao<CategoryEntity> {

    @Query("DELETE FROM Category")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

}