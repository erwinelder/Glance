package com.ataglance.walletglance.core.domain.usecase

import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository

class DeleteAllDataLocallyUseCaseImpl(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryCollectionRepository: CategoryCollectionRepository,
    private val widgetRepository: WidgetRepository,
    private val navigationButtonRepository: NavigationButtonRepository
) : DeleteAllDataLocallyUseCase {
    override suspend fun execute() {
        settingsRepository.clearAllPreferences()
        accountRepository.deleteAllAccountsLocally()
        categoryRepository.deleteAllCategoriesLocally()
        categoryCollectionRepository.deleteAllCollectionsLocally()
        widgetRepository.deleteAllWidgetsLocally()
        navigationButtonRepository.deleteAllNavigationButtonsLocally()
    }
}