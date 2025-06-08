package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.domain.model.AppScreenEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNavigationButtonScreensUseCaseMock : GetNavigationButtonScreensUseCase {
    override fun getFlow(): Flow<List<AppScreenEnum>> {
        return flow {
            val buttons = listOf(
                AppScreenEnum.Home,
                AppScreenEnum.Records,
                AppScreenEnum.CategoryStatistics,
                AppScreenEnum.Budgets,
                AppScreenEnum.Settings
            )
            emit(buttons)
        }
    }
}