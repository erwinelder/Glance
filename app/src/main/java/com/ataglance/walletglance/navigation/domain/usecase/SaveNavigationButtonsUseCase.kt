package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

interface SaveNavigationButtonsUseCase {
    suspend fun execute(buttons: List<BottomBarNavigationButton>)
}