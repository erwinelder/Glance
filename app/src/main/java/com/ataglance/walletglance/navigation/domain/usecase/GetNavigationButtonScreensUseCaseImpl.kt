package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.mapper.toDataModels
import com.ataglance.walletglance.navigation.mapper.toDomainModelsSorted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetNavigationButtonScreensUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : GetNavigationButtonScreensUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFlow(): Flow<List<AppScreenEnum>> {
        return navigationButtonRepository.getAllNavigationButtonsAsFlow().mapLatest { buttons ->
            if (buttons.isEmpty()) {
                val defaultButtons = getDefaultNavigationButtons()
                navigationButtonRepository.upsertNavigationButtons(
                    buttons = defaultButtons.toDataModels()
                )
                defaultButtons
            } else {
                buttons.toDomainModelsSorted()
            }
        }
    }

    private fun getDefaultNavigationButtons(): List<AppScreenEnum> {
        return listOf(
            AppScreenEnum.Home,
            AppScreenEnum.Records,
            AppScreenEnum.CategoryStatistics,
            AppScreenEnum.Budgets,
            AppScreenEnum.Settings
        )
    }

}