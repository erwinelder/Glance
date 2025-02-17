package com.ataglance.walletglance.navigation.domain.usecase

import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.navigation.mapper.toDataModelsWithDefaultOrderNums
import com.ataglance.walletglance.navigation.mapper.toDomainModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNavigationButtonsUseCaseImpl(
    private val navigationButtonRepository: NavigationButtonRepository
) : GetNavigationButtonsUseCase {
    override fun getFlow(): Flow<List<BottomBarNavigationButton>> = flow {
        navigationButtonRepository.getAllNavigationButtonsFlow().collect { entities ->
            if (entities.isNotEmpty()) {

                emit(entities.toDomainModels())

            } else {
                val defaultButtons = BottomBarNavigationButton.asDefaultList()

                navigationButtonRepository.upsertNavigationButtons(
                    buttons = defaultButtons.toDataModelsWithDefaultOrderNums()
                )

                emit(defaultButtons)
            }
        }
    }
}