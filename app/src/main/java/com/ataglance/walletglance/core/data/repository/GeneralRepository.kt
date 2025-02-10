package com.ataglance.walletglance.core.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.navigation.data.repository.NavigationButtonRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository

class GeneralRepository(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryCollectionRepository: CategoryCollectionRepository,
    private val widgetRepository: WidgetRepository,
    private val navigationButtonRepository: NavigationButtonRepository
) {

    @Transaction
    suspend fun resetAllData() {
        settingsRepository.clearAllPreferences()
        accountRepository.deleteAllAccountsLocally()
        categoryRepository.deleteAllCategoriesLocally()
        categoryCollectionRepository.deleteAllCategoryCollectionsLocally()
        widgetRepository.deleteAllEntities()
        navigationButtonRepository.deleteAllEntities()
    }

    @Transaction
    suspend fun deleteAllDataLocally() {
        settingsRepository.clearAllPreferences()
        accountRepository.deleteAllAccountsLocally()
        categoryRepository.deleteAllCategoriesLocally()
        categoryCollectionRepository.deleteAllCategoryCollectionsLocally()
        widgetRepository.deleteAllEntitiesLocally()
        navigationButtonRepository.deleteAllEntitiesLocally()
    }

}