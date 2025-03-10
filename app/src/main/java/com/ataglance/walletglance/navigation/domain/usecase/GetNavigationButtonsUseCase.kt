package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import kotlinx.coroutines.flow.Flow

interface GetNavigationButtonsUseCase {
    fun getFlow(): Flow<List<BottomBarNavigationButton>>
}