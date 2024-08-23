package com.ataglance.walletglance.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.data.preferences.SettingsRepository

class GeneralRepository(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val categoryCollectionRepository: CategoryCollectionRepository
) {
    @Transaction
    suspend fun resetAllData() {
        settingsRepository.saveIsSetUpPreference(0)
        accountRepository.deleteAllAccounts()
        categoryRepository.deleteAllCategories()
        categoryCollectionRepository.deleteAllCollections()
    }
}