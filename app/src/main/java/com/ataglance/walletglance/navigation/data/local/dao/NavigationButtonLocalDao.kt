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
        deleteButtons(toDelete)
        upsertButtons(toUpsert)
    }

    @Query("DELETE FROM NavigationButton")
    suspend fun deleteAllNavigationButtons()

    @Query("SELECT * FROM NavigationButton ORDER BY orderNum ASC")
    fun getAllNavigationButtons(): Flow<List<NavigationButtonEntity>>

}