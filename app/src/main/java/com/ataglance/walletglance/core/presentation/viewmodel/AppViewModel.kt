package com.ataglance.walletglance.core.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.usecase.GetAccountsUseCase
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.LongDateRange
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.utils.timeInMillisToTimestampWithoutSpecificTime
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.domain.usecase.theme.GetAppThemeConfigurationUseCase
import com.ataglance.walletglance.personalization.domain.usecase.widgets.GetWidgetsUseCase
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration
import com.ataglance.walletglance.settings.domain.usecase.ChangeAppSetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.GetStartDestinationsBySetupStageUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.ApplyLanguageToSystemUseCase
import com.ataglance.walletglance.settings.domain.usecase.language.GetLanguagePreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val getAppThemeConfigurationUseCase: GetAppThemeConfigurationUseCase,
    private val applyLanguageToSystemUseCase: ApplyLanguageToSystemUseCase,
    private val getLanguagePreferenceUseCase: GetLanguagePreferenceUseCase,
    private val changeAppSetupStageUseCase: ChangeAppSetupStageUseCase,
    getStartDestinationsBySetupStageUseCase: GetStartDestinationsBySetupStageUseCase,

    private val getAccountsUseCase: GetAccountsUseCase,
    private val getWidgetsUseCase: GetWidgetsUseCase
) : ViewModel() {

    init {
        applyAppLanguage()
        viewModelScope.launch {
            changeAppSetupStageUseCase.updateSetupStageInNeeded()
        }
        fetchAppThemeConfiguration()
        fetchAccounts()
        fetchWidgets()
    }


    private val _appThemeConfiguration = MutableStateFlow<AppThemeConfiguration?>(null)
    val appThemeConfiguration = _appThemeConfiguration.asStateFlow()

    private fun fetchAppThemeConfiguration() {
        viewModelScope.launch {
            getAppThemeConfigurationUseCase.getFlow().collect { themeConfiguration ->
                _appThemeConfiguration.update { themeConfiguration }
            }
        }
    }


    private val _appTheme: MutableStateFlow<AppTheme?> = MutableStateFlow(null)

    fun setAppTheme(appTheme: AppTheme) {
        _appTheme.update { appTheme }
    }


    val appConfiguration = combine(
        getStartDestinationsBySetupStageUseCase.getFlow(),
        getLanguagePreferenceUseCase.getFlow(),
        _appTheme
    ) { startDestinations, language, appTheme ->
        AppConfiguration(
            isSetUp = startDestinations.first == MainScreens.Home,
            mainStartDestination = startDestinations.first,
            settingsStartDestination = startDestinations.second,
            langCode = language,
            appTheme = appTheme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppConfiguration()
    )

    private fun applyAppLanguage() {
        viewModelScope.launch {
            val langCode = getLanguagePreferenceUseCase.get()
            applyLanguageToSystemUseCase.execute(langCode)
        }
    }

    suspend fun finishSetup() {
        changeAppSetupStageUseCase.finishSetup()
    }


    private val _accountsAndActiveOne = MutableStateFlow(AccountsAndActiveOne())
    val accountsAndActiveOne = _accountsAndActiveOne.asStateFlow()

    private fun fetchAccounts() {
        viewModelScope.launch {
            getAccountsUseCase.getAllFlow().collect { accounts ->
                _accountsAndActiveOne.update {
                    AccountsAndActiveOne.fromAccounts(accounts = accounts)
                }
            }
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


    private val _dateRangeMenuUiState = MutableStateFlow(
        DateRangeMenuUiState.fromEnum(DateRangeEnum.ThisMonth)
    )
    val dateRangeMenuUiState = _dateRangeMenuUiState.asStateFlow()

    fun selectDateRange(dateRangeEnum: DateRangeEnum) {
        val currDateRangeWithEnum = dateRangeMenuUiState.value.dateRangeWithEnum

        if (currDateRangeWithEnum.enum == dateRangeEnum) return

        _dateRangeMenuUiState.update {
            DateRangeMenuUiState.fromEnum(
                enum = dateRangeEnum,
                currentRange = currDateRangeWithEnum.dateRange
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
                        from = timeInMillisToTimestampWithoutSpecificTime(pastDateMillis),
                        to = timeInMillisToTimestampWithoutSpecificTime(futureDateMillis) + 2359
                    )
                )
            )
        }
    }


    private val _widgetNames: MutableStateFlow<List<WidgetName>> = MutableStateFlow(emptyList())
    val widgetNames: StateFlow<List<WidgetName>> = _widgetNames.asStateFlow()

    private fun fetchWidgets() {
        viewModelScope.launch {
            getWidgetsUseCase.getFlow().collect { widgets ->
                _widgetNames.update { widgets }
            }
        }
    }

}
