package com.ataglance.walletglance.core.data.repository

import androidx.room.Transaction
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionRepository
import com.ataglance.walletglance.core.data.preferences.SettingsRepository

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