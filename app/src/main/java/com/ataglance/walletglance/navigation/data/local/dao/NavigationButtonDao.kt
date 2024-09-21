package com.ataglance.walletglance.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationButtonDao {

    @Upsert
    suspend fun upsertNavigationButtons(navigationButtonList: List<NavigationButtonEntity>)

    @Query("SELECT * FROM NavigationButton ORDER BY orderNum ASC")
    fun getAllNavigationButtonsSorted(): Flow<List<NavigationButtonEntity>>

}