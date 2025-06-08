package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.data.local.model.NavigationButtonEntity
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import com.ataglance.walletglance.navigation.mapper.toDomainModelsSorted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetNavigationButtonScreensUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : GetNavigationButtonScreensUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFlow(): Flow<List<AppScreenEnum>> {
        return navigationButtonRepository.getAllNavigationButtonsFlow().mapLatest { entities ->
            if (entities.isNotEmpty()) {
                entities.toDomainModelsSorted()
            } else {
                val defaultButtons = listOf(
                    AppScreenEnum.Home,
                    AppScreenEnum.Records,
                    AppScreenEnum.CategoryStatistics,
                    AppScreenEnum.Budgets,
                    AppScreenEnum.Settings
                )

                defaultButtons
                    .mapIndexed { index, screen ->
                        NavigationButtonEntity(screenName = screen.name, orderNum = index)
                    }
                    .also { entities ->
                        navigationButtonRepository.upsertNavigationButtons(
                            buttons = entities
                        )
                    }

                defaultButtons
            }
        }
    }
}