package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

interface SaveNavigationButtonsUseCase {
    suspend fun save(buttons: List<BottomBarNavigationButton>)
}