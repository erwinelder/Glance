package com.ataglance.walletglance

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.data.AppDatabase
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.data.SettingsRepository
import com.ataglance.walletglance.ui.viewmodels.AppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class WalletGlanceApplication: Application() {
    private lateinit var settingsRepository: SettingsRepository
    private val db by lazy { AppDatabase.getDatabase(this) }

    private val accountRepository by lazy { AccountRepository(db.accountDao) }
    private val categoryRepository by lazy { CategoryRepository(db.categoryDao) }
    private val recordRepository by lazy { RecordRepository(db.recordDao) }
    private val recordAndAccountRepository by lazy {
        RecordAndAccountRepository(db.recordDao, db.accountDao)
    }
    private val generalRepository by lazy {
        GeneralRepository(
            settingsRepository, accountRepository, categoryRepository, recordRepository
        )
    }

    val appViewModel by lazy { AppViewModel(
        settingsRepository = settingsRepository,
        accountRepository = accountRepository,
        categoryRepository = categoryRepository,
        recordRepository = recordRepository,
        recordAndAccountRepository = recordAndAccountRepository,
        generalRepository = generalRepository
    ) }

    override fun onCreate() {
        super.onCreate()
        Log.d("Custom message onCreate", "----------------------------")

        settingsRepository = SettingsRepository(dataStore)

        CoroutineScope(Dispatchers.IO).launch {

            /* apply saved language preference in datastore preferences */
            val langCode = settingsRepository.language.first()
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setApplicationLocales(appLocale)
            }

             /* if app has been set up but finish screen was not closed (so 2 still saved as a
             start destination in the datastore preferences, which is finish screen), reassign this
             preference to 1 (home screen) */
            val isSetUp = settingsRepository.setupStage.first()
            if (isSetUp == 2) {
                settingsRepository.saveIsSetUpPreference(1)
            }
        }
    }
}