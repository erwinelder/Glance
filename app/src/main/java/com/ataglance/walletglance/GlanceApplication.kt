package com.ataglance.walletglance

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ataglance.walletglance.auth.presentation.viewmodel.AuthViewModel
import com.ataglance.walletglance.billing.domain.BillingManager
import com.ataglance.walletglance.core.data.local.AppDatabase
import com.ataglance.walletglance.core.data.preferences.SettingsRepository
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.data.repository.RepositoryManager
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.data.repository.NavigationRepository
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.personalization.data.repository.BudgetOnWidgetRepository
import com.ataglance.walletglance.personalization.data.repository.WidgetRepository
import com.ataglance.walletglance.personalization.presentation.viewmodel.PersonalizationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class GlanceApplication : Application() {

    private lateinit var repositoryManager: RepositoryManager

    private lateinit var db: AppDatabase
    private lateinit var firestore: FirebaseFirestore
    lateinit var authViewModel: AuthViewModel
    lateinit var billingManager: BillingManager
    private lateinit var settingsRepository: SettingsRepository
    lateinit var appViewModel: AppViewModel
    lateinit var navViewModel: NavigationViewModel
    lateinit var personalizationViewModel: PersonalizationViewModel

    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getDatabase(this)
        initializeFirestore()
        initializeAuthViewModel()
        initializeBillingManager()

        repositoryManager = RepositoryManager(db, authViewModel.user, firestore)

        initializeSettingsRepository()
        initializeAppViewModel()
        initializeNavViewModel()
        initializePersonalizationViewModel()

        applyAppLanguage()
        updateSetupStageIfNeeded()
    }

    private fun initializeFirestore() {
        firestore = FirebaseFirestore.getInstance()
    }

    private fun initializeAuthViewModel() {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        authViewModel = AuthViewModel(auth = auth)
    }

    private fun initializeBillingManager() {
        billingManager = BillingManager(context = this)
    }

    private fun initializeSettingsRepository() {
        settingsRepository = SettingsRepository(dataStore)
    }

    /**
     * Initialize repositories and then initialize AppViewModel with passing them into it.
     */
    private fun initializeAppViewModel() {
        val accountRepository = repositoryManager.getAccountRepository()
        val categoryRepository = repositoryManager.getCategoryRepository()
        val categoryCollectionRepository = repositoryManager.getCategoryCollectionRepository()
        val categoryCollectionAndCollectionCategoryAssociationRepository = repositoryManager
            .getCategoryCollectionAndCollectionCategoryAssociationRepository()
        val recordRepository = repositoryManager.getRecordRepository()
        val budgetAndBudgetAccountAssociationRepository = repositoryManager
            .getBudgetAndBudgetAccountAssociationRepository()
        val recordAndAccountRepository by lazy {
            repositoryManager.getRecordAndAccountRepository()
        }

        val generalRepository by lazy {
            GeneralRepository(
                settingsRepository = settingsRepository,
                accountRepository = accountRepository,
                categoryRepository = categoryRepository,
                categoryCollectionRepository = categoryCollectionRepository
            )
        }

        appViewModel = AppViewModel(
            settingsRepository = settingsRepository,
            accountRepository = accountRepository,
            categoryRepository = categoryRepository,
            categoryCollectionAndCollectionCategoryAssociationRepository =
            categoryCollectionAndCollectionCategoryAssociationRepository,
            recordRepository = recordRepository,
            recordAndAccountRepository = recordAndAccountRepository,
            budgetAndBudgetAccountAssociationRepository =
            budgetAndBudgetAccountAssociationRepository,
            generalRepository = generalRepository
        )
    }

    private fun initializeNavViewModel() {
        val navigationRepository = NavigationRepository(db.navigationButtonDao)

        navViewModel = NavigationViewModel(navigationRepository)
    }

    private fun initializePersonalizationViewModel() {
        val widgetRepository = WidgetRepository(db.widgetDao)
        val budgetOnWidgetRepository = BudgetOnWidgetRepository(db.budgetOnWidgetDao)

        personalizationViewModel = PersonalizationViewModel(
            widgetRepository = widgetRepository,
            budgetOnWidgetRepository = budgetOnWidgetRepository
        )
    }

    /**
     * Apply app language saved in data preferences to the app using AppCompatDelegate's
     * setApplicationLocales method.
     */
    private fun applyAppLanguage() {
        CoroutineScope(Dispatchers.IO).launch {
            val langCode = settingsRepository.language.first()
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }
    }

    /**
     * Check for right screen setting after the first app setup.
     * If the app has been setup but finish screen was not closed (so 2 is still saved as a start
     * destination in the datastore preferences, which is the finish screen), reassign this
     * preference to 1 (home screen).
     */
    private fun updateSetupStageIfNeeded() {
        CoroutineScope(Dispatchers.IO).launch {
            val isSetUp = settingsRepository.setupStage.first()
            if (isSetUp == 2) {
                settingsRepository.saveIsSetUpPreference(1)
            }
        }
    }

}
