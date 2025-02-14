package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.usecase.SaveAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findByOrderNum
import com.ataglance.walletglance.auth.data.model.UserData
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.usecase.GetCategoryCollectionsUseCase
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.data.repository.SettingsRepository
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.core.utils.getCalendarEndLong
import com.ataglance.walletglance.core.utils.getCalendarStartLong
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.withLongDateRange
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.data.model.RecordsInDateRange
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.utils.getTotalAmountByType
import com.ataglance.walletglance.record.domain.usecase.GetLastRecordNumUseCase
import com.ataglance.walletglance.record.domain.usecase.GetRecordStacksInDateRangeUseCase
import com.ataglance.walletglance.record.domain.usecase.GetTodayTotalExpensesForAccountUseCase
import com.ataglance.walletglance.settings.domain.usecase.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.domain.usecase.SaveLanguagePreferenceUseCase
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import com.ataglance.walletglance.settings.presentation.model.ThemeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    val settingsRepository: SettingsRepository,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase,
    private val saveLanguagePreferenceUseCase: SaveLanguagePreferenceUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,

    private val saveAccountsUseCase: SaveAccountsUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,

    private val getCategoryCollectionsUseCase: GetCategoryCollectionsUseCase,

    val recordRepository: RecordRepository,
    private val getLastRecordNumUseCase: GetLastRecordNumUseCase,
    private val getTodayTotalExpensesForAccountUseCase: GetTodayTotalExpensesForAccountUseCase,
    private val getRecordStacksInDateRangeUseCase: GetRecordStacksInDateRangeUseCase,

    val generalRepository: GeneralRepository
) : ViewModel() {

    private val _appTheme: MutableStateFlow<AppTheme?> = MutableStateFlow(null)
    private val _lastRecordNum: MutableStateFlow<Int> = MutableStateFlow(0)
    val appConfiguration: StateFlow<AppConfiguration> =
        combine(
            settingsRepository.setupStage,
            settingsRepository.userId,
            getLanguagePreferenceUseCase.getAsFlow(),
            _appTheme,
            _lastRecordNum
        ) { setupStage, userId, language, appTheme, lastRecordNum ->
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
                appTheme = appTheme,
                lastRecordNum = lastRecordNum
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppConfiguration()
        )

    val themeUiState: StateFlow<ThemeUiState?> =
        combine(
            settingsRepository.useDeviceTheme,
            settingsRepository.chosenLightTheme,
            settingsRepository.chosenDarkTheme,
            settingsRepository.lastChosenTheme
        ) { useDeviceTheme, chosenLightTheme, chosenDarkTheme, lastChosenTheme ->
            ThemeUiState(
                useDeviceTheme = useDeviceTheme,
                chosenLightTheme = chosenLightTheme,
                chosenDarkTheme = chosenDarkTheme,
                lastChosenTheme = lastChosenTheme
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


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

    fun setUseDeviceTheme(value: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveUseDeviceThemePreference(value)
        }
    }

    fun chooseLightTheme(theme: String) {
        val state = themeUiState.value

        viewModelScope.launch {

            /* !!!
                Now it setting up useDeviceTheme to false when any theme is chosen
                (when user clicks on the preview theme element on the SetupAppearanceScreen or settings AppearanceScreen)
                because currently there are only 2 themes (Light and Dark) to choose between.
                When there are more themes of light or dark versions, it is necessary to remove this block of code
                that unsets useDeviceTheme preference so user can specify which theme they want to use as light and which as a dark one.
            !!! */
            if (state?.useDeviceTheme == true) {
                settingsRepository.saveUseDeviceThemePreference(false)
            }

            settingsRepository.saveChosenLightThemePreference(theme)
            settingsRepository.saveLastChosenThemePreference(theme)
        }
    }

    fun chooseDarkTheme(theme: String) {
        val state = themeUiState.value

        viewModelScope.launch {

            /* !!!
                Now it setting up useDeviceTheme to false when any theme is chosen
                (when user clicks on the preview theme element on the SetupAppearanceScreen or settings AppearanceScreen)
                because currently there are only 2 themes (Light and Dark) to choose between.
                When there are more themes of light or dark versions, it is necessary to remove this block of code
                that unsets useDeviceTheme preference so user can specify which theme they want to use as light and which as a dark one.
            !!! */
            if (state?.useDeviceTheme == true) {
                settingsRepository.saveUseDeviceThemePreference(false)
            }

            settingsRepository.saveChosenDarkThemePreference(theme)
            settingsRepository.saveLastChosenThemePreference(theme)
        }
    }

    fun updateAppThemeState(appTheme: AppTheme) {
        _appTheme.update {
            when (appTheme) {
                AppTheme.LightDefault -> AppTheme.LightDefault
                AppTheme.DarkDefault -> AppTheme.DarkDefault
            }
        }
    }

    suspend fun preFinishSetup() {
        settingsRepository.saveIsSetUpPreference(2)
    }
    suspend fun finishSetup() {
        settingsRepository.saveIsSetUpPreference(1)
    }

    /**
     * Check for right screen setting after the first app setup.
     * If the app has been setup but finish screen was not closed (so 2 is still saved as a start
     * destination in the datastore preferences, which is the finish screen), reassign this
     * preference to 1 (home screen).
     */
    private fun updateSetupStageIfNeeded() {
        viewModelScope.launch {
            val isSetUp = settingsRepository.setupStage.first()
            if (isSetUp == 2) {
                finishSetup()
            }
        }
    }

    suspend fun resetAppData() {
        generalRepository.resetAllData()
    }

    fun deleteAllData() {
        viewModelScope.launch {
            generalRepository.deleteAllDataLocally()
        }
    }

    private fun fetchLastRecordNum() {
        viewModelScope.launch {
            getLastRecordNumUseCase.getAsFlow().collect { recordNum ->
                _lastRecordNum.update { recordNum ?: 0 }
            }
        }
    }


    // TODO-ACCOUNTS
    private val _accountsAndActiveOne: MutableStateFlow<AccountsAndActiveOne> = MutableStateFlow(
        AccountsAndActiveOne()
    )
    val accountsAndActiveOne: StateFlow<AccountsAndActiveOne> = _accountsAndActiveOne.asStateFlow()

    // TODO-ACCOUNTS
    private fun fetchAccounts() {
        viewModelScope.launch {
            getAccountsUseCase.getAllAsFlow().collect(::applyAccountsToUiState)
        }
    }

    // TODO-ACCOUNTS
    fun applyAccountsToUiState(accountList: List<Account>) {
        _accountsAndActiveOne.update { uiState ->
            uiState.copy(
                accountList = accountList,
                activeAccount = accountList.takeIf { it.isNotEmpty() }?.firstOrNull { it.isActive }
            )
        }
    }

    // TODO-ACCOUNTS
    fun onChangeHideActiveAccountBalance() {
        _accountsAndActiveOne.update {
            it.copy(
                accountList = it.accountList.map { account ->
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

    // TODO-ACCOUNTS
    fun applyActiveAccountByOrderNum(accountOrderNum: Int) {
        _accountsAndActiveOne.update {
            it.copy(
                accountList = accountsAndActiveOne.value.accountList.map { account ->
                    account.copy(isActive = account.orderNum == accountOrderNum)
                },
                activeAccount = accountsAndActiveOne.value.accountList
                    .findByOrderNum(accountOrderNum)?.copy(isActive = true)
            )
        }
    }

    // TODO-ACCOUNTS
    // TODO-RECORDS
    suspend fun saveAccounts(accountsList: List<Account>) {
        saveAccountsUseCase.execute(
            accountsToSave = accountsList,
            currentAccounts = accountsAndActiveOne.value.accountList
        )
    }


    private val _categoryCollectionsUiState = MutableStateFlow(CategoryCollectionsWithIdsByType())
    val categoryCollectionsUiState = _categoryCollectionsUiState.asStateFlow()

    private fun fetchCategoryCollections() {
        viewModelScope.launch {
            getCategoryCollectionsUseCase.getAsFlow().collect { collections ->
                _categoryCollectionsUiState.update { collections }
            }
        }
    }


    private val _todayRecordList: MutableStateFlow<List<RecordEntity>> = MutableStateFlow(
        emptyList()
    )

    private fun fetchRecordsForToday() {
        viewModelScope.launch {
            recordRepository.getRecordsForToday().collect { recordList ->
                _todayRecordList.update { recordList }
            }
        }
    }

    // TODO-ACCOUNTS-DEPENDENCY
    fun getActiveAccountExpensesForToday(): Double {
        return accountsAndActiveOne.value.activeAccount?.id
            ?.let { accountId ->
                _todayRecordList.value
                    .filter { it.accountId == accountId }
                    .getTotalAmountByType(CategoryType.Expense)
            }
            ?: 0.0
    }

    suspend fun getActiveAccountExpensesForTodayT(): Double {
        return accountsAndActiveOne.value.activeAccount?.id?.let {
            getTodayTotalExpensesForAccountUseCase.execute(accountId = it)
        } ?: 0.0
    }


    private val _recordStacksInDateRange = MutableStateFlow(RecordsInDateRange())
    val recordStacksInDateRange = _recordStacksInDateRange.asStateFlow()

    private fun fetchRecordsInDateRange(longDateRange: LongDateRange) {
        viewModelScope.launch {
            getRecordStacksInDateRangeUseCase.getAsFlow(range = longDateRange).collect { recordStacks ->
                _recordStacksInDateRange.update {
                    it.copy(dateRange = longDateRange, recordStacks = recordStacks)
                }
            }
        }
    }


    private val _dateRangeMenuUiState: MutableStateFlow<DateRangeMenuUiState> = MutableStateFlow(
        DateRangeEnum.ThisMonth.getDateRangeMenuUiState()
    )
    val dateRangeMenuUiState: StateFlow<DateRangeMenuUiState> = _dateRangeMenuUiState.asStateFlow()

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
        fetchRecordsInDateRange(dateRangeMenuUiState.value.getLongDateRange())
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
        fetchRecordsInDateRange(dateRangeMenuUiState.value.getLongDateRange())
    }


    private fun fetchDataOnStart() {
        fetchAccounts()
        fetchLastRecordNum()
        fetchCategoryCollections()
        fetchRecordsForToday()
        fetchRecordsInDateRange(dateRangeMenuUiState.value.getLongDateRange())
    }


    init {
        applyAppLanguage()
        updateSetupStageIfNeeded()
        fetchDataOnStart()
    }

}
