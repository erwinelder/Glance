package com.ataglance.walletglance.navigation.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.dao.BaseLocalDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationButtonDao : BaseLocalDao<NavigationButtonEntity> {

    @Query("DELETE FROM NavigationButton")
    suspend fun deleteAllNavigationButtons()

    @Query("SELECT * FROM NavigationButton ORDER BY orderNum ASC")
    override fun getAllEntities(): Flow<List<NavigationButtonEntity>>

}