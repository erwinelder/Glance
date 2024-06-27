package com.ataglance.walletglance.ui.viewmodels

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppLanguage
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryStatisticsLists
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.data.date.DateRangeState
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.records.MakeRecordStatus
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.CategoryEntity
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.domain.repositories.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.domain.repositories.SettingsRepository
import com.ataglance.walletglance.ui.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.SettingsScreens
import com.ataglance.walletglance.ui.utils.breakOnCollectionsAndAssociations
import com.ataglance.walletglance.ui.utils.breakOnDifferentLists
import com.ataglance.walletglance.ui.utils.checkOrderNumbers
import com.ataglance.walletglance.ui.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.findByOrderNum
import com.ataglance.walletglance.ui.utils.findByRecordNum
import com.ataglance.walletglance.ui.utils.fixOrderNumbers
import com.ataglance.walletglance.ui.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.ui.utils.getCalendarEndLong
import com.ataglance.walletglance.ui.utils.getCalendarStartLong
import com.ataglance.walletglance.ui.utils.getDateRangeState
import com.ataglance.walletglance.ui.utils.getIdsThatAreNotInList
import com.ataglance.walletglance.ui.utils.getOutAndInTransfersByOneRecordNum
import com.ataglance.walletglance.ui.utils.getTodayDateLong
import com.ataglance.walletglance.ui.utils.getTotalAmount
import com.ataglance.walletglance.ui.utils.getTransferSecondUnitsRecordNumbers
import com.ataglance.walletglance.ui.utils.inverse
import com.ataglance.walletglance.ui.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.ui.utils.toAccountEntityList
import com.ataglance.walletglance.ui.utils.toAccountList
import com.ataglance.walletglance.ui.utils.toCategoryEntityList
import com.ataglance.walletglance.ui.utils.toRecordStackList
import com.ataglance.walletglance.ui.utils.transfersToRecordsWithCategoryOfTransfer
import com.ataglance.walletglance.ui.utils.transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferUiState
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
import java.time.LocalDateTime
import java.util.Locale

class AppViewModel(
    val settingsRepository: SettingsRepository,
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository,
    private val categoryCollectionAndCollectionCategoryAssociationRepository:
        CategoryCollectionAndCollectionCategoryAssociationRepository,
    val recordRepository: RecordRepository,
    val recordAndAccountRepository: RecordAndAccountRepository,
    val generalRepository: GeneralRepository
) : ViewModel() {

    private val _appTheme: MutableStateFlow<AppTheme?> = MutableStateFlow(null)
    private val _lastRecordNum: MutableStateFlow<Int> = MutableStateFlow(0)
    val appUiSettings: StateFlow<AppUiSettings> =
        combine(
            settingsRepository.setupStage,
            settingsRepository.language,
            _appTheme,
            _lastRecordNum
        ) { setupStage, language, appTheme, lastRecordNum ->
            AppUiSettings(
                isSetUp = setupStage == 1,
                startMainDestination = when(setupStage) {
                    1 -> MainScreens.Home
                    0 -> MainScreens.Settings
                    else -> MainScreens.FinishSetup
                },
                startSettingsDestination = when (setupStage) {
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
            initialValue = AppUiSettings()
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

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            settingsRepository.saveLanguagePreference(langCode)
        }

        /*val request = SplitInstallRequest.newBuilder()
            .addLanguage(Locale.forLanguageTag(langCode))
            .build()
        val splitInstallManager = SplitInstallManagerFactory.create(context)
        splitInstallManager.startInstall(request)*/

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun translateDefaultCategories(context: Context) {
        val translatedCategories = DefaultCategoriesPackage(context).translateDefaultCategories(
            categoriesLists = categoriesUiState.value
        )

        viewModelScope.launch {
            categoryRepository.upsertCategories(translatedCategories.toCategoryEntityList())
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

    fun updateAppThemeState(theme: String) {
        _appTheme.update {
            when (theme) {
                AppTheme.LightDefault.name -> AppTheme.LightDefault
                AppTheme.DarkDefault.name -> AppTheme.DarkDefault
                else -> AppTheme.LightDefault
            }
        }
    }

    suspend fun preFinishSetup() {
        viewModelScope.launch {
            settingsRepository.saveIsSetUpPreference(2)
        }
    }
    suspend fun finishSetup() {
        viewModelScope.launch {
            settingsRepository.saveIsSetUpPreference(1)
        }
    }
    suspend fun resetAppData() {
        viewModelScope.launch {
            generalRepository.resetAllData()
        }
    }

    /*suspend fun exportAppData(context: Context) {
        val records: List<Record> = recordRepository.getAllRecords().first()
        val accounts: List<Account> = accountRepository.getAllAccounts().first()
        val categories: List<Category> = categoryRepository.getCategories().first()
        val settings: Settings = settingsRepository.getSettings()

        val appData = AppData(
            records = records,
            accounts = accounts,
            categories = categories,
            settings = settings
        )

        val gson = Gson()
        val appDataJson = gson.toJson(appData)

        val file = File(context.getExternalFilesDir(null), "walletglance_data.json")
        file.writeText(appDataJson)

        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(Intent.EXTRA_TITLE, "walletglance_data.json")
        }
        startActivityForResult(intent, CREATE_FILE_REQUEST)
    }*/

    private fun fetchLastRecordNumFromDb() {
        viewModelScope.launch {
            recordRepository.getLastRecordNum().collect { recordNum ->
                _lastRecordNum.update { recordNum ?: 0 }
            }
        }
    }


    private val _accountsUiState: MutableStateFlow<AccountsUiState> =
        MutableStateFlow(AccountsUiState())
    val accountsUiState: StateFlow<AccountsUiState> = _accountsUiState.asStateFlow()

    private fun fetchAccountsFromDb() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { accountList ->
                if (accountList.checkOrderNumbers()) {
                    applyAccountListToUiState(accountList.toAccountList())
                } else {
                    saveAccountsToDb(accountList.fixOrderNumbers())
                }
            }
        }
    }

    fun applyAccountListToUiState(accountList: List<Account>) {
        _accountsUiState.update { uiState ->
            uiState.copy(
                accountList = accountList,
                activeAccount = accountList.takeIf { it.isNotEmpty() }?.firstOrNull { it.isActive }
            )
        }
    }

    fun onChangeHideActiveAccountBalance() {
        _accountsUiState.update {
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

    fun applyActiveAccountByOrderNum(accountOrderNum: Int) {
        _accountsUiState.update {
            it.copy(
                accountList = accountsUiState.value.accountList.map { account ->
                    account.copy(isActive = account.orderNum == accountOrderNum)
                },
                activeAccount = accountsUiState.value.accountList
                    .findByOrderNum(accountOrderNum)?.copy(isActive = true)
            )
        }
    }

    suspend fun saveAccountsToDb(accountsList: List<AccountEntity>) {
        val listOfIdsToDelete =
            accountsUiState.value.accountList.getIdsThatAreNotInList(accountsList)

        if (listOfIdsToDelete.isNotEmpty()) {
            viewModelScope.launch {
                accountRepository.deleteAndUpsertAccounts(listOfIdsToDelete, accountsList)
            }
        } else {
            viewModelScope.launch {
                accountRepository.upsertAccounts(accountsList)
            }
        }
    }

    suspend fun deleteAccountWithItsRecords(
        accountId: Int,
        updatedAccountList: List<AccountEntity>
    ) {
        viewModelScope.launch {

            val accountTransferList = recordRepository.getTransfersByAccountId(accountId).first()
            val transferSecondUnitsNumbers =
                accountTransferList.getTransferSecondUnitsRecordNumbers().takeIf { it.isNotEmpty() }

            val convertedTransfers = transferSecondUnitsNumbers?.let {
                recordRepository.getRecordsByRecordNumbers(it).firstOrNull()
                    ?.transfersToRecordsWithCategoryOfTransfer()
            } ?: emptyList()

            recordAndAccountRepository
                .deleteAccountAndUpdateAccountsAndDeleteRecordsByAccountIdAndUpdateRecords(
                    accountIdToDelete = accountId,
                    accountListToUpsert = updatedAccountList,
                    recordListToUpsert = convertedTransfers
                )

        }
    }


    private val _categoriesUiState: MutableStateFlow<CategoriesLists> =
        MutableStateFlow(CategoriesLists())
    val categoriesUiState: StateFlow<CategoriesLists> = _categoriesUiState.asStateFlow()

    private fun fetchCategoriesFromDb() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { categoryEntityList ->

                try {
                    _categoriesUiState.update { categoryEntityList.breakOnDifferentLists() }
                } catch (e: Exception) {
                    saveCategoriesToDb(categoryEntityList.fixOrderNumbers())
                }

            }
        }
    }

    suspend fun saveCategoriesToDb(categoryList: List<CategoryEntity>) {
        val listOfIdsToDelete = categoriesUiState.value
            .concatenateLists()
            .toCategoryEntityList()
            .getIdsThatAreNotInList(categoryList)

        if (listOfIdsToDelete.isNotEmpty()) {
            viewModelScope.launch {
                categoryRepository.deleteAndUpsertCategories(listOfIdsToDelete, categoryList)
            }
        } else {
            viewModelScope.launch {
                categoryRepository.upsertCategories(categoryList)
            }
        }
    }

    fun getLastRecordCategory(accountId: Int, type: CategoryType): Pair<Category?, Category?> {

        recordStackList.value.let { list ->
            list.find { it.account.id == accountId } ?: list.firstOrNull()
        }
        ?.stack?.firstOrNull()
        ?.let { return it.category to it.subcategory }

        return categoriesUiState.value.parentCategories.getByType(type).lastOrNull()
            .let { parCategory ->
                parCategory to parCategory?.parentCategoryId?.let {
                    categoriesUiState.value.subcategories.getByType(type).lastOrNull()?.lastOrNull()
                }
            }
    }


    private val _categoryCollectionsUiState: MutableStateFlow<CategoryCollectionsWithIds> =
        MutableStateFlow(CategoryCollectionsWithIds())
    val categoryCollectionsUiState: StateFlow<CategoryCollectionsWithIds> =
        _categoryCollectionsUiState.asStateFlow()

    private fun fetchCategoryCollectionsFromDb() {
        viewModelScope.launch {
            val collectionsAndCollectionCategoryAssociations =
                categoryCollectionAndCollectionCategoryAssociationRepository
                    .getCategoryCollectionsAndCollectionCategoryAssociations()
            _categoryCollectionsUiState.update {
                transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds(
                    collectionsAndCollectionCategoryAssociations.first,
                    collectionsAndCollectionCategoryAssociations.second
                )
            }
        }
    }

    suspend fun saveCategoryCollectionsToDb(
        collectionUiStateList: List<CategoryCollectionWithIds>
    ) {

        val newCollectionsAndAssociations =
            collectionUiStateList.breakOnCollectionsAndAssociations()
        val originalCollectionsAndAssociations = categoryCollectionsUiState.value
            .concatenateLists().breakOnCollectionsAndAssociations()

        val categoryCollectionsIdsToDelete =
            newCollectionsAndAssociations.first.getIdsThatAreNotInList(
                originalCollectionsAndAssociations.first
            )
        val collectionCategoryAssociationsToDelete =
            newCollectionsAndAssociations.second.getAssociationsThatAreNotInList(
                originalCollectionsAndAssociations.second
            )

        viewModelScope.launch {
            categoryCollectionAndCollectionCategoryAssociationRepository
                .deleteAndUpsertCollectionsAndDeleteAndUpsertAssociations(
                    collectionsIdsToDelete = categoryCollectionsIdsToDelete,
                    collectionListToUpsert = newCollectionsAndAssociations.first,
                    associationsToDelete = collectionCategoryAssociationsToDelete,
                    associationsToUpsert = newCollectionsAndAssociations.second
                )
        }

    }


    private val _dateRangeMenuUiState: MutableStateFlow<DateRangeMenuUiState> =
        MutableStateFlow(
            DateRangeMenuUiState(
                startCalendarDateMillis = DateRangeEnum.ThisMonth.getCalendarStartLong(),
                endCalendarDateMillis = DateRangeEnum.ThisMonth.getCalendarEndLong(),
                dateRangeState = DateRangeEnum.ThisMonth.getDateRangeState()
            )
        )
    val dateRangeMenuUiState: StateFlow<DateRangeMenuUiState> = _dateRangeMenuUiState.asStateFlow()

    fun changeDateRange(dateRangeEnum: DateRangeEnum) {
        _dateRangeMenuUiState.update {
            DateRangeMenuUiState(
                startCalendarDateMillis = dateRangeEnum
                    .getCalendarStartLong(dateRangeMenuUiState.value.dateRangeState),
                endCalendarDateMillis = dateRangeEnum
                    .getCalendarEndLong(dateRangeMenuUiState.value.dateRangeState),
                dateRangeState = dateRangeEnum
                    .getDateRangeState(dateRangeMenuUiState.value.dateRangeState)
            )
        }
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.dateRangeState)
    }

    fun changeDateRangeToCustom(pastDateMillis: Long?, futureDateMillis: Long?) {
        if (pastDateMillis == null || futureDateMillis == null) {
            return
        }

        _dateRangeMenuUiState.update {
            DateRangeMenuUiState(
                startCalendarDateMillis = pastDateMillis,
                endCalendarDateMillis = futureDateMillis,
                dateRangeState = DateRangeState(
                    enum = DateRangeEnum.Custom,
                    fromPast = convertCalendarMillisToLongWithoutSpecificTime(pastDateMillis),
                    toFuture =
                        convertCalendarMillisToLongWithoutSpecificTime(futureDateMillis) + 2359
                )
            )
        }
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.dateRangeState)
    }


    private val _recordList: MutableStateFlow<List<Record>> = MutableStateFlow(emptyList())

    fun fetchRecordsFromDbInDateRange(dateRangeState: DateRangeState) {
        viewModelScope.launch {
            recordRepository.getRecordsInDateRange(dateRangeState).collect { recordList ->
                _recordList.update { recordList }
            }
        }
    }

    val recordStackList: StateFlow<List<RecordStack>> = combine(
        _recordList,
        _accountsUiState,
        _categoriesUiState
    ) { recordList, accountsUiState, categoriesUiState ->
        recordList.toRecordStackList(
            accountList = accountsUiState.accountList,
            categoriesLists = categoriesUiState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    suspend fun saveRecord(uiState: MakeRecordUiState, unitList: List<MakeRecordUnitUiState>) {
        if (uiState.recordStatus == MakeRecordStatus.Create) {
            saveNewRecord(uiState, unitList)
        } else {
            saveEditedRecord(uiState, unitList)
        }
    }

    private suspend fun saveNewRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        uiState.account ?: return

        val recordList = uiState.toRecordList(unitList)
        val updatedAccountList = mergeAccountLists(
            listOf(
                uiState.account.cloneAndAddToOrSubtractFromBalance(
                    amount = unitList.getTotalAmount(),
                    recordType = uiState.type
                )
            )
        )

        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList, updatedAccountList.toAccountEntityList()
            )
        }
    }

    private suspend fun saveEditedRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        val currentRecordStack = recordStackList.value.findByRecordNum(uiState.recordNum) ?: return

        if (unitList.size == currentRecordStack.stack.size) {
            updateEditedRecord(
                uiState = uiState,
                newRecordList = uiState.toRecordListWithOldIds(unitList, currentRecordStack),
                recordStack = currentRecordStack,
                thisRecordTotalAmount = unitList.getTotalAmount()
            )
        } else {
            deleteRecordAndSaveNewOne(
                uiState = uiState,
                currentRecordStack = currentRecordStack,
                currentRecordList = currentRecordStack.toRecordList(),
                newRecordList = uiState.toRecordList(unitList),
                thisRecordTotalAmount = unitList.getTotalAmount()
            )
        }
    }

    private suspend fun updateEditedRecord(
        uiState: MakeRecordUiState,
        newRecordList: List<Record>,
        recordStack: RecordStack,
        thisRecordTotalAmount: Double
    ) {
        uiState.account ?: return

        if (uiState.account.id == recordStack.account.id) {
            val updatedAccountList = mergeAccountLists(
                listOf(
                    uiState.account.cloneAndReapplyAmountToBalance(
                        prevAmount = recordStack.totalAmount,
                        newAmount = thisRecordTotalAmount,
                        recordType = uiState.type
                    )
                )
            )
            viewModelScope.launch {
                recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                    newRecordList, updatedAccountList.toAccountEntityList()
                )
            }
        } else {
            accountsUiState.value.accountList.findById(recordStack.account.id)?.let {
                updateEditedRecordDifferentAccounts(
                    recordList = newRecordList,
                    prevAccount = it,
                    newAccount = uiState.account,
                    prevAmount = recordStack.totalAmount,
                    newAmount = thisRecordTotalAmount,
                    recordType = uiState.type
                )
            }
        }
    }

    private suspend fun updateEditedRecordDifferentAccounts(
        recordList: List<Record>,
        prevAccount: Account,
        newAccount: Account,
        prevAmount: Double,
        newAmount: Double,
        recordType: RecordType
    ) {
        val updatedAccountList = mergeAccountLists(
            (prevAccount to newAccount).returnAmountToFirstBalanceAndUpdateSecondBalance(
                prevAmount = prevAmount,
                newAmount = newAmount,
                recordType = recordType
            ).toList()
        )
        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList, updatedAccountList.toAccountEntityList()
            )
        }
    }

    private suspend fun deleteRecordAndSaveNewOne(
        uiState: MakeRecordUiState,
        currentRecordStack: RecordStack,
        currentRecordList: List<Record>,
        newRecordList: List<Record>,
        thisRecordTotalAmount: Double
    ) {
        uiState.account ?: return

        if (uiState.account.id == currentRecordStack.account.id) {
            val updatedAccountList = mergeAccountLists(
                listOf(
                    uiState.account.cloneAndReapplyAmountToBalance(
                        prevAmount = currentRecordStack.totalAmount,
                        newAmount = thisRecordTotalAmount,
                        recordType = uiState.type
                    )
                )
            )
            viewModelScope.launch {
                recordAndAccountRepository.deleteAndUpsertRecordsAndUpdateAccounts(
                    currentRecordList, newRecordList, updatedAccountList.toAccountEntityList()
                )
            }
        } else {
            accountsUiState.value.accountList
                .findById(currentRecordStack.account.id)
                ?.let { prevAccount ->
                    deleteRecordAndSaveNewOneDifferentAccounts(
                        currentRecordList = currentRecordList,
                        newRecordList = newRecordList,
                        prevAccount = prevAccount,
                        newAccount = uiState.account,
                        prevAmount = currentRecordStack.totalAmount,
                        newAmount = thisRecordTotalAmount,
                        recordType = uiState.type,
                    )
                }
        }
    }

    private suspend fun deleteRecordAndSaveNewOneDifferentAccounts(
        currentRecordList: List<Record>,
        newRecordList: List<Record>,
        prevAccount: Account,
        newAccount: Account,
        prevAmount: Double,
        newAmount: Double,
        recordType: RecordType
    ) {
        val updatedAccountList = mergeAccountLists(
            Pair(prevAccount, newAccount).returnAmountToFirstBalanceAndUpdateSecondBalance(
                prevAmount = prevAmount,
                newAmount = newAmount,
                recordType = recordType
            ).toList()
        )
        viewModelScope.launch {
            viewModelScope.launch {
                recordAndAccountRepository.deleteAndUpsertRecordsAndUpdateAccounts(
                    recordListToDelete = currentRecordList,
                    recordListToUpsert = newRecordList,
                    accountList = updatedAccountList.toAccountEntityList()
                )
            }
        }
    }

    suspend fun repeatRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        saveNewRecord(
            uiState = uiState.copy(
                recordNum = appUiSettings.value.nextRecordNum(),
                dateTimeState = DateTimeState()
            ),
            unitList = unitList
        )
    }

    suspend fun deleteRecord(recordNum: Int) {

        val recordStack = recordStackList.value.findByRecordNum(recordNum) ?: return
        val recordList = recordStack.toRecordList()

        val account = accountsUiState.value.accountList
            .findById(recordStack.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = recordStack.totalAmount,
                recordType = recordStack.type.inverse()
            )
            ?: return
        val updatedAccountList = mergeAccountLists(listOf(account))

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList.toAccountEntityList()
            )
        }
    }

    suspend fun saveTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return
        val madeTransferState =
            uiState.toMadeTransferState(appUiSettings.value.lastRecordNum) ?: return

        val recordListAndUpdatedAccountList = if (
            madeTransferState.recordStatus == MakeRecordStatus.Create
        ) {
            getRecordListAndUpdatedAccountListAfterNewTransfer(madeTransferState)
        } else {
            getRecordListAndUpdatedAccountListAfterEditedTransfer(madeTransferState)
        }

        recordListAndUpdatedAccountList ?: return

        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList = recordListAndUpdatedAccountList.first,
                accountList = recordListAndUpdatedAccountList.second
            )
        }
    }

    private fun getRecordListAndUpdatedAccountListAfterNewTransfer(
        state: MadeTransferState
    ): Pair<List<Record>, List<AccountEntity>> {
        val recordList = state.toRecordsPair().toList()
        val updatedAccountList = mergeAccountLists(
            listOf(
                state.fromAccount.cloneAndSubtractFromBalance(state.startAmount),
                state.toAccount.cloneAndAddToBalance(state.finalAmount)
            )
        )

        return recordList to updatedAccountList.toAccountEntityList()
    }

    private fun getRecordListAndUpdatedAccountListAfterEditedTransfer(
        state: MadeTransferState
    ): Pair<List<Record>, List<AccountEntity>>? {
        val firstRecordStack = recordStackList.value.findByRecordNum(state.recordNum) ?: return null
        val recordNumDifference = if (firstRecordStack.isOutTransfer()) 1 else -1
        val secondRecordStack = recordStackList.value
            .findByRecordNum(state.recordNum + recordNumDifference) ?: return null

        val recordList = state.copy(
            recordNum = state.recordNum - (if (firstRecordStack.isOutTransfer()) 0 else 1)
        ).toRecordsPair().toList()
        val updatedAccountList = getUpdatedAccountsAfterEditedTransfer(
            uiState = state,
            currRecordStackFrom = if (firstRecordStack.isOutTransfer()) firstRecordStack
                else secondRecordStack,
            currRecordStackTo = if (firstRecordStack.isOutTransfer()) secondRecordStack
                else firstRecordStack
        )?.let {
            mergeAccountLists(it)
        }

        return updatedAccountList?.let { recordList to it.toAccountEntityList() }
    }

    fun getUpdatedAccountsAfterEditedTransfer(
        uiState: MadeTransferState,
        currRecordStackFrom: RecordStack,
        currRecordStackTo: RecordStack
    ): List<Account>? {

        val prevFromAccount = accountsUiState.value.accountList
            .findById(currRecordStackFrom.account.id) ?: return null
        val prevToAccount = accountsUiState.value.accountList
            .findById(currRecordStackTo.account.id) ?: return null

        val updatedPreviousAccounts = listOf(
            prevFromAccount.cloneAndAddToBalance(currRecordStackFrom.totalAmount),
            prevToAccount.cloneAndSubtractFromBalance(currRecordStackTo.totalAmount)
        )
        val updatedAccounts = applyAmountsToAccountAfterTransfer(
            state = uiState,
            prevAccounts = updatedPreviousAccounts
        )

        return updatedAccounts?.let { mergeAccountLists(it, updatedPreviousAccounts) }
    }

    private fun applyAmountsToAccountAfterTransfer(
        state: MadeTransferState,
        prevAccounts: List<Account>
    ): List<Account>? {
        val updatedAccounts = mutableListOf<Account>()

        prevAccounts.findById(state.fromAccount.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.startAmount))
        } ?: accountsUiState.value.accountList.findById(state.fromAccount.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.startAmount))
        } ?: return null

        prevAccounts.findById(state.toAccount.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(state.finalAmount))
        } ?: accountsUiState.value.accountList.findById(state.toAccount.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(state.finalAmount))
        } ?: return null

        return updatedAccounts
    }

    private fun mergeAccountLists(
        primaryList: List<Account>,
        secondaryList: List<Account> = accountsUiState.value.accountList
    ): List<Account> {
        val mergedList = mutableListOf<Account>()

        primaryList.forEach { accountFromPrimaryList ->
            mergedList.add(accountFromPrimaryList)
        }
        secondaryList.forEach { accountFromSecondaryList ->
            if (primaryList.findById(accountFromSecondaryList.id) == null) {
                mergedList.add(accountFromSecondaryList)
            }
        }

        return mergedList
    }

    suspend fun repeatTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return

        val madeTransferState = uiState.toMadeTransferState(appUiSettings.value.lastRecordNum)
            ?.copy(
                recordNum = appUiSettings.value.nextRecordNum(),
                dateTimeState = DateTimeState(),
                idFrom = 0,
                idTo = 0
            ) ?: return

        val recordListAndUpdatedAccountList =
            getRecordListAndUpdatedAccountListAfterNewTransfer(madeTransferState)

        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList = recordListAndUpdatedAccountList.first,
                accountList = recordListAndUpdatedAccountList.second
            )
        }
    }

    suspend fun deleteTransfer(recordNum: Int) {

        val outAndInTransfers = recordStackList.value
            .getOutAndInTransfersByOneRecordNum(recordNum) ?: return

        val recordList = outAndInTransfers.first.toRecordList() +
                outAndInTransfers.second.toRecordList()

        val prevAccounts = Pair(
            accountsUiState.value.accountList.findById(outAndInTransfers.first.account.id) ?: return,
            accountsUiState.value.accountList.findById(outAndInTransfers.second.account.id) ?: return
        )

        val updatedAccountList = listOf(
            prevAccounts.first.cloneAndAddToBalance(outAndInTransfers.first.totalAmount),
            prevAccounts.second.cloneAndSubtractFromBalance(outAndInTransfers.second.totalAmount)
        ).let {
            mergeAccountLists(it)
        }

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList.toAccountEntityList()
            )
        }

    }


    private val _greetingsWidgetTitleRes: MutableStateFlow<Int> = MutableStateFlow(
        getGreetingsWidgetTitleRes()
    )

    val widgetsUiState = combine(
        _dateRangeMenuUiState,
        _accountsUiState,
        recordStackList,
        categoriesUiState,
        _appTheme,
        _greetingsWidgetTitleRes
    ) { combinedArray ->
        val dateRangeMenuUiState = combinedArray[0] as DateRangeMenuUiState
        val accountsUiState = combinedArray[1] as AccountsUiState
        val recordStackList = combinedArray[2] as List<RecordStack>
        val categoriesUiState = combinedArray[3] as CategoriesLists
        val appTheme = combinedArray[4] as AppTheme
        val greetingsWidgetTitleRes = combinedArray[5] as Int

        val expensesTotalForPeriod = getRecordsTotalAmount(
            recordStackList = recordStackList,
            startDate = dateRangeMenuUiState.dateRangeState.fromPast,
            endDate = dateRangeMenuUiState.dateRangeState.toFuture,
            type = RecordType.Expense
        )
        val incomeTotalForPeriod = getRecordsTotalAmount(
            recordStackList = recordStackList,
            startDate = dateRangeMenuUiState.dateRangeState.fromPast,
            endDate = dateRangeMenuUiState.dateRangeState.toFuture,
            type = RecordType.Income
        )
        val expensesIncomePercentage = (expensesTotalForPeriod + incomeTotalForPeriod)
            .let { if (it == 0.0) null else it }
            ?.let {
                (100 / it) * expensesTotalForPeriod to (100 / it) * incomeTotalForPeriod
            } ?: (0.0 to 0.0)
        val filteredRecordStackList = filterRecordStackForWidgetsUiState(
            recordStackList = recordStackList,
            dateRangeState = dateRangeMenuUiState.dateRangeState,
            activeAccount = accountsUiState.activeAccount
        )
        WidgetsUiState(
            filteredRecordStackList = filteredRecordStackList,
            greetings = GreetingsWidgetUiState(
                titleRes = greetingsWidgetTitleRes,
                expensesTotal = getRecordsTotalAmount(
                    recordStackList = recordStackList,
                    startDate = getTodayDateLong(),
                    endDate = getTodayDateLong() + 2359,
                    type = RecordType.Expense
                )
            ),
            expensesIncome = ExpensesIncomeWidgetUiState(
                expensesTotal = expensesTotalForPeriod,
                incomeTotal = incomeTotalForPeriod,
                expensesPercentage = expensesIncomePercentage.first,
                incomePercentage = expensesIncomePercentage.second,
                expensesPercentageFloat = (expensesIncomePercentage.first / 100).toFloat(),
                incomePercentageFloat = (expensesIncomePercentage.second / 100).toFloat()
            ),
            categoryStatisticsLists = categoriesUiState.getStatistics(
                recordStackList = filteredRecordStackList,
                appTheme = appTheme,
                accountCurrency = accountsUiState.activeAccount?.currency ?: ""
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WidgetsUiState()
    )

    fun filterRecordStackForWidgetsUiState(
        recordStackList: List<RecordStack>,
        dateRangeState: DateRangeState,
        activeAccount: Account?
    ): List<RecordStack> {
        return recordStackList.filter {
            it.date in dateRangeState.fromPast..dateRangeState.toFuture &&
                    it.account.id == activeAccount?.id
        }
    }

    fun updateGreetingsWidgetTitle() {
        _greetingsWidgetTitleRes.update { getGreetingsWidgetTitleRes() }
    }

    private fun getGreetingsWidgetTitleRes(): Int {
        return when (LocalDateTime.now().hour) {
            in 6..11 -> R.string.greetings_title_morning
            in 12..17 -> R.string.greetings_title_afternoon
            in 18..22 -> R.string.greetings_title_evening
            else -> R.string.greetings_title_night
        }
    }

    private fun getRecordsTotalAmount(
        recordStackList: List<RecordStack>,
        startDate: Long,
        endDate: Long,
        type: RecordType
    ): Double {
        return accountsUiState.value.activeAccount?.let { activeAccount ->
            recordStackList
                .filter {
                    it.account.id == activeAccount.id &&
                            it.date in startDate..endDate && (
                            (it.isOfType(type)) ||
                                    (it.isOutTransfer() && type == RecordType.Expense) ||
                                    (it.isInTransfer() && type == RecordType.Income)
                            )
                }
                .fold(0.0) { total, recordStack ->
                    total + recordStack.totalAmount
                }
        } ?: 0.0
    }


    fun fetchDataOnStart() {
        fetchAccountsFromDb()
        fetchCategoriesFromDb()
        fetchLastRecordNumFromDb()
        fetchCategoryCollectionsFromDb()
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.dateRangeState)
    }

}

data class AppUiSettings(
    val isSetUp: Boolean = false,
    val startMainDestination: MainScreens = MainScreens.Home,
    val startSettingsDestination: SettingsScreens = SettingsScreens.Start,
    val langCode: String = AppLanguage.English.languageCode,
    val appTheme: AppTheme? = null,
    val lastRecordNum: Int = 0,
) {

    fun nextRecordNum(): Int {
        return lastRecordNum + 1
    }

}

data class ThemeUiState(
    val useDeviceTheme: Boolean,
    val chosenLightTheme: String,
    val chosenDarkTheme: String,
    val lastChosenTheme: String
)

data class AccountsUiState(
    val accountList: List<Account> = emptyList(),
    val activeAccount: Account? = null
)

data class DateRangeMenuUiState(
    val startCalendarDateMillis: Long,
    val endCalendarDateMillis: Long,
    val dateRangeState: DateRangeState
)

data class MadeTransferState(
    val idFrom: Int,
    val idTo: Int,
    val recordStatus: MakeRecordStatus,
    val fromAccount: Account,
    val toAccount: Account,
    val startAmount: Double,
    val finalAmount: Double,
    val dateTimeState: DateTimeState = DateTimeState(),
    val recordNum: Int
) {
    fun toRecordsPair(): Pair<Record, Record> {
        return Pair(
            Record(
                id = idFrom,
                recordNum = recordNum,
                date = dateTimeState.dateLong,
                type = '>',
                amount = startAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = fromAccount.id,
                note = toAccount.id.toString()
            ),
            Record(
                id = idTo,
                recordNum = recordNum + 1,
                date = dateTimeState.dateLong,
                type = '<',
                amount = finalAmount,
                quantity = null,
                categoryId = 0,
                subcategoryId = null,
                accountId = toAccount.id,
                note = fromAccount.id.toString()
            )
        )
    }
}

data class WidgetsUiState(
    val filteredRecordStackList: List<RecordStack> = emptyList(),
    val greetings: GreetingsWidgetUiState = GreetingsWidgetUiState(),
    val expensesIncome: ExpensesIncomeWidgetUiState = ExpensesIncomeWidgetUiState(),
    val categoryStatisticsLists: CategoryStatisticsLists = CategoryStatisticsLists()
)

data class GreetingsWidgetUiState(
    @StringRes val titleRes: Int = R.string.greetings_empty_message,
    val expensesTotal: Double = 0.0
)

data class ExpensesIncomeWidgetUiState(
    val expensesTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val expensesPercentage: Double = 0.0,
    val incomePercentage: Double = 0.0,
    val expensesPercentageFloat: Float = 0.0f,
    val incomePercentageFloat: Float = 0.0f,
) {
    private fun getFormattedNumberWithSpaces(number: Double): String {
        var numberString = "%.2f".format(Locale.US, number)
        var formattedNumber = numberString.let {
            it.substring(startIndex = it.length - 3)
        }
        numberString = numberString.let {
            it.substring(0, it.length - 3)
        }
        var digitCount = 0

        for (i in numberString.length - 1 downTo 0) {
            formattedNumber = numberString[i] + formattedNumber
            digitCount++
            if (digitCount % 3 == 0 && i != 0) {
                formattedNumber = " $formattedNumber"
            }
        }

        return formattedNumber
    }

    fun getTotalFormatted(): String {
        return getFormattedNumberWithSpaces(incomeTotal - expensesTotal)
    }
    fun getExpensesTotalFormatted(): String {
        return getFormattedNumberWithSpaces(expensesTotal)
    }
    fun getIncomeTotalFormatted(): String {
        return getFormattedNumberWithSpaces(incomeTotal)
    }
}
