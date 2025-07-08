package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum

interface SaveNavigationButtonsUseCase {
    suspend fun execute(screens: List<AppScreenEnum>)
}