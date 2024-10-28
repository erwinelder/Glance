package com.ataglance.walletglance.navigation.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationButtonDao : BaseDao<NavigationButtonEntity> {

    @Query("DELETE FROM NavigationButton")
    suspend fun deleteAllNavigationButtons()

    @Query("SELECT * FROM NavigationButton ORDER BY orderNum ASC")
    override fun getAllEntities(): Flow<List<NavigationButtonEntity>>

}