package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.mapper.toDataModels

class SaveNavigationButtonsUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : SaveNavigationButtonsUseCase {
    override suspend fun execute(screens: List<AppScreenEnum>) {
        val buttons = screens.toMutableList()
            .apply {
                if (!contains(AppScreenEnum.Home)) add(0, AppScreenEnum.Home)
                if (!contains(AppScreenEnum.Settings)) add(AppScreenEnum.Settings)
            }
            .toDataModels()

        navigationButtonRepository.upsertNavigationButtons(buttons = buttons)
    }
}