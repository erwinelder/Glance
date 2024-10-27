package com.ataglance.walletglance.navigation.data.local

import androidx.room.Dao
import androidx.room.Query
import com.ataglance.walletglance.core.data.local.BaseDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationButtonDao : BaseDao<NavigationButtonEntity> {

    @Query("SELECT * FROM NavigationButton ORDER BY orderNum ASC")
    fun getAllNavigationButtons(): Flow<List<NavigationButtonEntity>>

}