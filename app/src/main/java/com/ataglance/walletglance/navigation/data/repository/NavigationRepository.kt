package com.ataglance.walletglance.navigation.data.repository

import com.ataglance.walletglance.navigation.data.local.NavigationButtonDao
import com.ataglance.walletglance.navigation.data.model.NavigationButtonEntity
import kotlinx.coroutines.flow.Flow

class NavigationRepository(
    private val dao: NavigationButtonDao
) {

    suspend fun upsertNavigationButtons(navigationButtonList: List<NavigationButtonEntity>) {
        dao.upsertNavigationButtons(navigationButtonList)
    }

    fun getNavigationButtonsSorted(): Flow<List<NavigationButtonEntity>> {
        return dao.getAllNavigationButtons()
    }

}