package com.ataglance.walletglance.navigation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationButtonLocalDao {

    @Upsert
    suspend fun upsertButtons(buttons: List<NavigationButtonEntity>)

    @Delete
    suspend fun deleteButtons(buttons: List<NavigationButtonEntity>)

    @Transaction
    suspend fun deleteAndUpsertButtons(
        toDelete: List<NavigationButtonEntity>,
        toUpsert: List<NavigationButtonEntity>
    ) {
        deleteButtons(buttons = toDelete)
        upsertButtons(buttons = toUpsert)
    }

    @Query("DELETE FROM navigation_button")
    suspend fun deleteAllNavigationButtons()

    @Query("SELECT * FROM navigation_button WHERE timestamp > :timestamp ORDER BY orderNum ASC")
    suspend fun getNavigationButtonsAfterTimestamp(timestamp: Long): List<NavigationButtonEntity>

    @Query("SELECT * FROM navigation_button WHERE deleted = 0 ORDER BY orderNum ASC")
    fun getAllNavigationButtonsAsFlow(): Flow<List<NavigationButtonEntity>>

}