package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.mapper.toDataModels

class SaveNavigationButtonsUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : SaveNavigationButtonsUseCase {
    override suspend fun save(buttons: List<BottomBarNavigationButton>) {
        navigationButtonRepository.upsertNavigationButtons(
            buttons = buttons.toDataModels()
        )
    }
}