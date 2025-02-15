package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.usecase.DeleteAllDataLocallyUseCase
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.core.utils.getCalendarEndLong
import com.ataglance.walletglance.core.utils.getCalendarStartLong
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.withLongDateRange
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.GetWidgetsUseCase
import com.ataglance.walletglance.settings.data.repository.SettingsRepository
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStatusUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    val settingsRepository: SettingsRepository,
    private val getAppThemeConfigurationUseCase: GetAppThemeConfigurationUseCase,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val changeAppSetupStatusUseCase: ChangeAppSetupStatusUseCase,

    private val getAccountsUseCase: GetAccountsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase,
    private val deleteAllDataLocallyUseCase: DeleteAllDataLocallyUseCase,
) : ViewModel() {

    init {
        applyAppLanguage()
        updateSetupStageIfNeeded()
        fetchAppThemeConfiguration()
        fetchAccounts()
        fetchWidgets()
    }


    private val _appTheme: MutableStateFlow<AppTheme?> = MutableStateFlow(null)
    val appConfiguration = combine(
        settingsRepository.setupStage,
        settingsRepository.userId,
        getLanguagePreferenceUseCase.getAsFlow(),
        _appTheme
    ) { setupStage, userId, language, appTheme ->
        AppConfiguration(
            isSetUp = setupStage == 1,
            isSignedIn = userId != null,
            mainStartDestination = when(setupStage) {
                1 -> MainScreens.Home
                0 -> MainScreens.Settings
                else -> MainScreens.FinishSetup
            },
            settingsStartDestination = when (setupStage) {
                1 -> SettingsScreens.SettingsHome
                else -> SettingsScreens.Start
            },
            langCode = language,
            appTheme = appTheme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppConfiguration()
    )

    private val _appThemeConfiguration = MutableStateFlow<AppThemeConfiguration?>(null)
    val appThemeConfiguration = _appThemeConfiguration.asStateFlow()

    private fun fetchAppThemeConfiguration() {
        viewModelScope.launch {
            getAppThemeConfigurationUseCase.getAsFlow().collect {
                _appThemeConfiguration.update { it }
            }
        }
    }


    fun updateConfigurationAfterSignIn(userData: UserData) {
        viewModelScope.launch {
            setUserId(userData.userId)
            saveLanguagePreferenceUseCase.save(userData.language)
            applyLanguageToSystemUseCase.execute(userData.language)
        }
    }

    suspend fun getUserId(): String? {
        return settingsRepository.userId.firstOrNull()
    }

    suspend fun setUserId(userId: String) {
        settingsRepository.saveUserIdPreference(userId)
    }

    suspend fun resetUserId() {
        settingsRepository.saveUserIdPreference("")
    }

    private fun applyAppLanguage() {
        viewModelScope.launch {
            val langCode = getLanguagePreferenceUseCase.get()
            saveLanguagePreferenceUseCase.save(langCode)
            applyLanguageToSystemUseCase.execute(langCode)
        }
    }

    fun updateAppThemeState(appTheme: AppTheme) {
        _appTheme.update { appTheme }
    }

    suspend fun finishSetup() {
        changeAppSetupStatusUseCase.finishSetup()
    }

    /**
     * Check for right screen setting after the first app setup.
     * If the app has been setup but finish screen was not closed (so 2 is still saved as a start
     * destination in the datastore preferences, which is the finish screen), reassign this
     * preference to 1 (home screen).
     */
    private fun updateSetupStageIfNeeded() {
        viewModelScope.launch {
            changeAppSetupStatusUseCase.updateSetupStageInNeeded()
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            deleteAllDataLocallyUseCase.execute()
        }
    }


    private val _accountsAndActiveOne = MutableStateFlow(AccountsAndActiveOne())
    val accountsAndActiveOne = _accountsAndActiveOne.asStateFlow()

    private fun fetchAccounts() {
        viewModelScope.launch {
            getAccountsUseCase.getAllAsFlow().collect(::applyAccounts)
        }
    }

    fun applyAccounts(accounts: List<Account>) {
        _accountsAndActiveOne.update { state ->
            state.copy(
                accounts = accounts,
                activeAccount = accounts.firstOrNull { it.isActive }
            )
        }
    }

    fun onChangeHideActiveAccountBalance() {
        _accountsAndActiveOne.update {
            it.copy(
                accounts = it.accounts.map { account ->
                    if (account.isActive) {
                        account.copy(hideBalance = !account.hideBalance)
                    } else {
                        account
                    }
                },
                activeAccount = it.activeAccount?.copy(hideBalance = !it.activeAccount.hideBalance)
            )
        }
    }

    fun applyActiveAccount(accountId: Int) {
        _accountsAndActiveOne.update { state ->
            state.copy(
                accounts = state.accounts.map { it.copy(isActive = it.id == accountId) },
                activeAccount = state.accounts.findById(accountId)?.copy(isActive = true)
            )
        }
    }


    private val _dateRangeMenuUiState: MutableStateFlow<DateRangeMenuUiState> = MutableStateFlow(
        DateRangeEnum.ThisMonth.getDateRangeMenuUiState()
    )
    val dateRangeMenuUiState = _dateRangeMenuUiState.asStateFlow()

    fun selectDateRange(dateRangeEnum: DateRangeEnum) {
        val currDateRangeWithEnum = dateRangeMenuUiState.value.dateRangeWithEnum

        if (currDateRangeWithEnum.enum == dateRangeEnum) return

        _dateRangeMenuUiState.update {
            it.copy(
                startCalendarDateMillis = dateRangeEnum.getCalendarStartLong(currDateRangeWithEnum),
                endCalendarDateMillis = dateRangeEnum.getCalendarEndLong(currDateRangeWithEnum),
                dateRangeWithEnum = dateRangeEnum.withLongDateRange(currDateRangeWithEnum)
            )
        }
    }

    fun selectCustomDateRange(pastDateMillis: Long?, futureDateMillis: Long?) {
        if (pastDateMillis == null || futureDateMillis == null) {
            return
        }

        _dateRangeMenuUiState.update {
            DateRangeMenuUiState(
                startCalendarDateMillis = pastDateMillis,
                endCalendarDateMillis = futureDateMillis,
                dateRangeWithEnum = DateRangeWithEnum(
                    enum = DateRangeEnum.Custom,
                    dateRange = LongDateRange(
                        from = convertCalendarMillisToLongWithoutSpecificTime(pastDateMillis),
                        to = convertCalendarMillisToLongWithoutSpecificTime(futureDateMillis) + 2359
                    )
                )
            )
        }
    }


    private val _widgetNames: MutableStateFlow<List<WidgetName>> = MutableStateFlow(emptyList())
    val widgetNames: StateFlow<List<WidgetName>> = _widgetNames.asStateFlow()

    private fun fetchWidgets() {
        viewModelScope.launch {
            getWidgetsUseCase.getAsFlow().collect {
                _widgetNames.update { it }
            }
        }
    }

}
