package com.ataglance.walletglance.presentation.viewmodels

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.accounts.AccountsUiState
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.app.AppUiSettings
import com.ataglance.walletglance.data.budgets.Budget
import com.ataglance.walletglance.data.budgets.BudgetsByType
import com.ataglance.walletglance.data.budgets.TotalAmountByRange
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.data.date.DateRangeMenuUiState
import com.ataglance.walletglance.data.date.DateRangeWithEnum
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.date.LongDateRange
import com.ataglance.walletglance.data.local.entities.AccountEntity
import com.ataglance.walletglance.data.local.entities.CategoryEntity
import com.ataglance.walletglance.data.local.entities.Record
import com.ataglance.walletglance.data.makingRecord.DataAfterRecordOperation
import com.ataglance.walletglance.data.makingRecord.MadeTransferState
import com.ataglance.walletglance.data.makingRecord.MakeRecordStatus
import com.ataglance.walletglance.data.makingRecord.MakeRecordUiState
import com.ataglance.walletglance.data.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.data.mappers.toDataModels
import com.ataglance.walletglance.data.preferences.SettingsRepository
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordsInDateRange
import com.ataglance.walletglance.data.repository.AccountRepository
import com.ataglance.walletglance.data.repository.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.data.repository.CategoryRepository
import com.ataglance.walletglance.data.repository.GeneralRepository
import com.ataglance.walletglance.data.repository.RecordAndAccountRepository
import com.ataglance.walletglance.data.repository.RecordRepository
import com.ataglance.walletglance.data.settings.ThemeUiState
import com.ataglance.walletglance.data.utils.checkOrderNumbers
import com.ataglance.walletglance.data.utils.fixOrderNumbers
import com.ataglance.walletglance.data.widgets.GreetingsWidgetUiState
import com.ataglance.walletglance.data.widgets.WidgetsUiState
import com.ataglance.walletglance.domain.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.domain.utils.divideIntoBudgetsAndAssociations
import com.ataglance.walletglance.domain.utils.divideIntoCollectionsAndAssociations
import com.ataglance.walletglance.domain.utils.filterByDateAndAccount
import com.ataglance.walletglance.domain.utils.findById
import com.ataglance.walletglance.domain.utils.findByOrderNum
import com.ataglance.walletglance.domain.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.domain.utils.getCalendarEndLong
import com.ataglance.walletglance.domain.utils.getCalendarStartLong
import com.ataglance.walletglance.domain.utils.getExpensesIncomeWidgetUiState
import com.ataglance.walletglance.domain.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.domain.utils.getIdsThatAreNotInList
import com.ataglance.walletglance.domain.utils.getOutAndInTransfersByRecordNum
import com.ataglance.walletglance.domain.utils.getTodayLongDateRange
import com.ataglance.walletglance.domain.utils.getTotalAmount
import com.ataglance.walletglance.domain.utils.getTotalAmountByType
import com.ataglance.walletglance.domain.utils.groupByType
import com.ataglance.walletglance.domain.utils.inverse
import com.ataglance.walletglance.domain.utils.isInRange
import com.ataglance.walletglance.domain.utils.mergeWith
import com.ataglance.walletglance.domain.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.domain.utils.toAccountList
import com.ataglance.walletglance.domain.utils.toBudgetList
import com.ataglance.walletglance.domain.utils.toCategoriesWithSubcategories
import com.ataglance.walletglance.domain.utils.toCategoryEntityList
import com.ataglance.walletglance.domain.utils.toRecordStackList
import com.ataglance.walletglance.domain.utils.transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds
import com.ataglance.walletglance.domain.utils.withLongDateRange
import com.ataglance.walletglance.presentation.ui.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.viewmodels.records.MakeTransferUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    val budgetAndBudgetAccountAssociationRepository: BudgetAndBudgetAccountAssociationRepository,
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
                mainStartDestination = when(setupStage) {
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

    fun translateAndSaveCategoriesWithDefaultNames(
        currentLangCode: String,
        newLangCode: String,
        context: Context
    ) {
        val defaultCurrentCategoryList = getDefaultCategoriesByLanguage(currentLangCode, context)
        val defaultNewCategoryList = getDefaultCategoriesByLanguage(newLangCode, context)

        val currentCategoryList = categoriesWithSubcategories.value.concatenateAsCategoryList()

        val namesToTranslateMap = currentCategoryList.mapNotNull { currentCategory ->
            defaultCurrentCategoryList.find { it.name == currentCategory.name }
                ?.let { defaultCurrentCategory ->
                    currentCategory.id to defaultNewCategoryList
                        .find { it.id == defaultCurrentCategory.id }!!.name
                }
        }.toMap()
        val translatedCategoryList = currentCategoryList.mapNotNull { currentCategory ->
            namesToTranslateMap[currentCategory.id]?.let { currentCategory.copy(name = it) }
        }

        viewModelScope.launch {
            categoryRepository.upsertCategories(translatedCategoryList.toCategoryEntityList())
        }
    }

    private fun getDefaultCategoriesByLanguage(langCode: String, context: Context): List<Category> {
        val langContext = context.createConfigurationContext(Configuration().apply {
            setLocale(Locale(langCode))
        })
        return DefaultCategoriesPackage(langContext).getDefaultCategories()
            .concatenateAsCategoryList()
    }

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
        val listOfIdsToDelete = accountsUiState.value.accountList
            .getIdsThatAreNotInList(accountsList)

        if (listOfIdsToDelete.isNotEmpty()) {
            viewModelScope.launch {
                recordAndAccountRepository
                    .deleteAndUpdateAccountsAndDeleteRecordsByAccountIdAndConvertTransfersToRecords(
                        accountsIdsToDelete = listOfIdsToDelete,
                        accountListToUpsert = accountsList
                    )
            }
        } else {
            viewModelScope.launch {
                accountRepository.upsertAccounts(accountsList)
            }
        }
    }


    private val _categoriesWithSubcategories: MutableStateFlow<CategoriesWithSubcategories> =
        MutableStateFlow(CategoriesWithSubcategories())
    val categoriesWithSubcategories: StateFlow<CategoriesWithSubcategories> =
        _categoriesWithSubcategories.asStateFlow()

    private fun fetchCategoriesFromDb() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { categoryEntityList ->
                _categoriesWithSubcategories.update {
                    categoryEntityList.toCategoriesWithSubcategories()
                }
            }
        }
    }

    suspend fun saveCategoriesToDb(categoryList: List<CategoryEntity>) {
        val listOfIdsToDelete = categoriesWithSubcategories.value
            .concatenateAsCategoryList()
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

    fun getLastRecordCategory(
        accountId: Int,
        type: CategoryType = CategoryType.Expense
    ): CategoryWithSubcategory? {

        recordStackList.value.let { list ->
            list.find { it.account.id == accountId && it.isExpenseOrIncome() } ?: list.firstOrNull()
        }
        ?.stack?.firstOrNull()
        ?.let { return it.categoryWithSubcategory }

        return categoriesWithSubcategories.value.getByTypeOrAll(type)
            .lastOrNull()?.getWithLastSubcategory()
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

        val (newCollections, newAssociations) = collectionUiStateList
            .divideIntoCollectionsAndAssociations()
        val (originalCollections, originalAssociations) = categoryCollectionsUiState.value
            .concatenateLists().divideIntoCollectionsAndAssociations()

        val collectionsIdsToDelete = originalCollections.getIdsThatAreNotInList(newCollections)
        val associationsToDelete = originalAssociations
            .getAssociationsThatAreNotInList(newAssociations)

        viewModelScope.launch {
            categoryCollectionAndCollectionCategoryAssociationRepository
                .deleteAndUpsertCollectionsAndDeleteAndUpsertAssociations(
                    collectionsIdsToDelete = collectionsIdsToDelete,
                    collectionListToUpsert = newCollections,
                    associationsToDelete = associationsToDelete,
                    associationsToUpsert = newAssociations
                )
            fetchCategoryCollectionsFromDb()
        }

    }


    private val _todayRecordList: MutableStateFlow<List<Record>> = MutableStateFlow(emptyList())

    private fun fetchRecordsFromDbForToday() {
        viewModelScope.launch {
            recordRepository.getRecordsForToday().collect { recordList ->
                _todayRecordList.update { recordList }
            }
        }
    }


    private val _recordListInDateRange: MutableStateFlow<RecordsInDateRange> = MutableStateFlow(
        RecordsInDateRange()
    )

    fun fetchRecordsFromDbInDateRange(longDateRange: LongDateRange) {
        viewModelScope.launch {
            recordRepository.getRecordsInDateRange(longDateRange).collect { recordList ->
                _recordListInDateRange.update {
                    it.copy(
                        dateRange = longDateRange,
                        recordList = recordList
                    )
                }
            }
        }
    }

    val recordStackList: StateFlow<List<RecordStack>> = combine(
        _recordListInDateRange,
        _accountsUiState,
        _categoriesWithSubcategories
    ) { recordListInDateRange, accountsUiState, categoriesWithSubcategories ->
        recordListInDateRange.recordList.toRecordStackList(
            accountList = accountsUiState.accountList,
            categoriesWithSubcategories = categoriesWithSubcategories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )


    private val _budgetsByType: MutableStateFlow<BudgetsByType> = MutableStateFlow(BudgetsByType())
    val budgetsByType: StateFlow<BudgetsByType> = _budgetsByType.asStateFlow()

    private fun fetchBudgetsFromDb() {
        viewModelScope.launch {
            val (budgetEntityList, associationList) = budgetAndBudgetAccountAssociationRepository
                .getBudgetsAndBudgetAccountAssociations()
            val categoryWithSubcategoriesList = categoriesWithSubcategories.value.expense

            val budgetsByType = budgetEntityList
                .toBudgetList(
                    categoryWithSubcategoriesList = categoryWithSubcategoriesList,
                    associationList = associationList,
                    accountList = accountsUiState.value.accountList
                )
                .groupByType()
            val budgetsMaxDateRange = budgetsByType.getMaxDateRange() ?: return@launch

            val recordList = _recordListInDateRange.value
                .takeIf { it.dateRange.containsDateRange(budgetsMaxDateRange) }
                ?.recordList
                ?: recordRepository.getRecordsInDateRange(budgetsMaxDateRange).firstOrNull()
                ?: emptyList()

            val filledBudgets = budgetsByType.fillUsedAmountsByRecords(recordList)

            _budgetsByType.update { filledBudgets }
        }
    }

    fun fetchBudgetsTotalUsedAmountsByDateRanges(
        budget: Budget,
        dateRanges: List<LongDateRange>
    ): Flow<List<TotalAmountByRange>> {
        return recordRepository.getTotalAmountForBudgetInDateRanges(budget, dateRanges)
    }

    suspend fun saveBudgetsToDb(budgetList: List<Budget>) {
        val (newBudgets, newAssociations) = budgetList.divideIntoBudgetsAndAssociations()
        val (originalCollections, originalAssociations) = budgetsByType.value.concatenate()
            .divideIntoBudgetsAndAssociations()

        val budgetsIdsToDelete = originalCollections.getIdsThatAreNotInList(newBudgets)
        val associationsToDelete = originalAssociations
            .getAssociationsThatAreNotInList(newAssociations)

        viewModelScope.launch {
            budgetAndBudgetAccountAssociationRepository
                .deleteAndUpsertBudgetsAndDeleteAndUpsertAssociations(
                    budgetsIdsToDelete = budgetsIdsToDelete,
                    budgetListToUpsert = newBudgets,
                    associationsToDelete = associationsToDelete,
                    associationsToUpsert = newAssociations
                )
            fetchBudgetsFromDb()
        }
    }


    suspend fun saveRecord(uiState: MakeRecordUiState, unitList: List<MakeRecordUnitUiState>) {
        val dataAfterRecordOperation = if (uiState.recordStatus == MakeRecordStatus.Create) {
            getDataForDatabaseAfterNewRecord(uiState = uiState, unitList = unitList)
        } else {
            getDataForDatabaseAfterEditedRecord(uiState = uiState, unitList = unitList)
        } ?: return

        _budgetsByType.update {
            dataAfterRecordOperation.updatedBudgetsByType
        }

        viewModelScope.launch {
            recordAndAccountRepository.deleteAndUpsertRecordsAndUpsertAccounts(
                recordListToDelete = dataAfterRecordOperation.recordListToDelete,
                recordListToUpsert = dataAfterRecordOperation.recordListToUpsert,
                accountListToUpsert = dataAfterRecordOperation.accountListToUpsert
            )
            if (uiState.dateTimeState.dateLong.isInRange(getTodayLongDateRange())) {
                fetchRecordsFromDbForToday()
            }
            fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.getLongDateRange())
        }
    }

    private fun getDataForDatabaseAfterNewRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ): DataAfterRecordOperation? {
        uiState.account ?: return null

        val recordList = uiState.toRecordList(unitList)
        val updatedAccounts = listOf(
            uiState.account.cloneAndAddToOrSubtractFromBalance(
                amount = unitList.getTotalAmount(), recordType = uiState.type
            )
        )
            .mergeWith(accountsUiState.value.accountList)
            .toDataModels()
        val updatedBudgetsByType = budgetsByType.value.addUsedAmountsByRecords(recordList)

        return DataAfterRecordOperation(
            recordListToUpsert = recordList,
            accountListToUpsert = updatedAccounts,
            updatedBudgetsByType = updatedBudgetsByType
        )
    }

    private fun getDataForDatabaseAfterEditedRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ): DataAfterRecordOperation? {
        uiState.account ?: return null

        val currentRecordStack = recordStackList.value.findByOrderNum(uiState.recordNum)
            ?: return null
        val currentRecordList = currentRecordStack.toRecordList()
        val updatedAccounts = getUpdatedAccountsAfterRecordEditing(
            uiState = uiState,
            recordStack = currentRecordStack,
            newTotalAmount = unitList.getTotalAmount()
        )
            ?.mergeWith(accountsUiState.value.accountList)
            ?.toDataModels()
            ?: return null
        val budgetsByType = budgetsByType.value.subtractUsedAmountsByRecords(currentRecordList)

        return if (unitList.size == currentRecordStack.stack.size) {
            val recordListToUpsert = uiState.toRecordListWithOldIds(unitList, currentRecordStack)
            DataAfterRecordOperation(
                recordListToUpsert = recordListToUpsert,
                accountListToUpsert = updatedAccounts,
                updatedBudgetsByType = budgetsByType.addUsedAmountsByRecords(recordListToUpsert)
            )
        } else {
            val recordListToUpsert = uiState.toRecordList(unitList)
            DataAfterRecordOperation(
                recordListToDelete = currentRecordList,
                recordListToUpsert = recordListToUpsert,
                accountListToUpsert = updatedAccounts,
                updatedBudgetsByType = budgetsByType.addUsedAmountsByRecords(recordListToUpsert)
            )
        }
    }

    private fun getUpdatedAccountsAfterRecordEditing(
        uiState: MakeRecordUiState,
        recordStack: RecordStack,
        newTotalAmount: Double
    ): List<Account>? {
        uiState.account ?: return null

        return if (uiState.account.id == recordStack.account.id) {
            listOf(
                uiState.account.cloneAndReapplyAmountToBalance(
                    prevAmount = recordStack.totalAmount,
                    newAmount = newTotalAmount,
                    recordType = uiState.type
                )
            )
        } else {
            accountsUiState.value.getAccountById(recordStack.account.id)?.let { prevAccount ->
                (prevAccount to uiState.account).returnAmountToFirstBalanceAndUpdateSecondBalance(
                    prevAmount = recordStack.totalAmount,
                    newAmount = newTotalAmount,
                    recordType = uiState.type
                ).toList()
            }
        }
    }

    suspend fun repeatRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        saveRecord(
            uiState = uiState.copy(
                recordStatus = MakeRecordStatus.Create,
                recordNum = appUiSettings.value.nextRecordNum(),
                dateTimeState = DateTimeState()
            ),
            unitList = unitList
        )
    }

    suspend fun deleteRecord(recordNum: Int) {
        val recordStack = recordStackList.value.findByOrderNum(recordNum) ?: return

        val updatedAccount = accountsUiState.value.accountList
            .findById(recordStack.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = recordStack.totalAmount,
                recordType = recordStack.type.inverse()
            )
            ?: return
        val recordList = recordStack.toRecordList()
        val updatedAccounts = listOf(updatedAccount)
            .mergeWith(accountsUiState.value.accountList)
            .toDataModels()
        val updatedBudgets = budgetsByType.value.subtractUsedAmountsByRecords(recordList)

        _budgetsByType.update { updatedBudgets }

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpsertAccounts(
                recordListToDelete = recordList,
                accountListToUpsert = updatedAccounts
            )
        }
    }

    suspend fun saveTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return
        val madeTransferState = uiState.toMadeTransferState(appUiSettings.value.nextRecordNum())
            ?: return

        val dataAfterRecordOperation = if (
            madeTransferState.recordStatus == MakeRecordStatus.Create
        ) {
            getDataForDatabaseAfterNewTransfer(madeTransferState)
        } else {
            getDataForDatabaseAfterEditedTransfer(madeTransferState)
        } ?: return

        _budgetsByType.update { dataAfterRecordOperation.updatedBudgetsByType }

        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpsertAccounts(
                recordListToUpsert = dataAfterRecordOperation.recordListToUpsert,
                accountListToUpsert = dataAfterRecordOperation.accountListToUpsert
            )
        }
    }

    private fun getDataForDatabaseAfterNewTransfer(
        state: MadeTransferState
    ): DataAfterRecordOperation {
        val recordList = state.toRecordsPair().toList()
        val updatedAccountList = listOf(
            state.fromAccount.cloneAndSubtractFromBalance(state.startAmount),
            state.toAccount.cloneAndAddToBalance(state.finalAmount)
        )
            .mergeWith(accountsUiState.value.accountList)
            .toDataModels()
        val updatedBudgetsByType = budgetsByType.value.addUsedAmountsByRecords(recordList)

        return DataAfterRecordOperation(
            recordListToUpsert = recordList,
            accountListToUpsert = updatedAccountList,
            updatedBudgetsByType = updatedBudgetsByType
        )
    }

    private fun getDataForDatabaseAfterEditedTransfer(
        state: MadeTransferState
    ): DataAfterRecordOperation? {
        val (currRecordStackFrom, currRecordStackTo) = recordStackList.value
            .getOutAndInTransfersByRecordNum(state.recordNum) ?: return null

        val recordList = state
            .copy(recordNum = state.recordNum -
                    (if (state.recordNum == currRecordStackFrom.recordNum) 0 else 1))
            .toRecordsPair()
            .toList()
        val updatedAccounts = getUpdatedAccountsAfterEditedTransfer(
            uiState = state,
            currRecordStackFrom = currRecordStackFrom,
            currRecordStackTo = currRecordStackTo
        )
            ?.mergeWith(accountsUiState.value.accountList)
            ?.toDataModels()
            ?: return null
        val updatedBudgetsByType = budgetsByType.value
            .subtractUsedAmountsByRecords(
                recordList = currRecordStackFrom.toRecordList() + currRecordStackTo.toRecordList()
            )
            .addUsedAmountsByRecords(recordList)

        return DataAfterRecordOperation(
            recordListToUpsert = recordList,
            accountListToUpsert = updatedAccounts,
            updatedBudgetsByType = updatedBudgetsByType
        )
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
        val updatedAccounts = applyAmountsToAccountsAfterTransfer(
            state = uiState,
            prevAccounts = updatedPreviousAccounts
        )

        return updatedAccounts?.mergeWith(updatedPreviousAccounts)
    }

    private fun applyAmountsToAccountsAfterTransfer(
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

    suspend fun repeatTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return

        val newMakeTransferState = uiState
            .copy(
                recordIdFrom = 0,
                recordIdTo = 0,
                recordNum = null,
                dateTimeState = DateTimeState()
            )

        saveTransfer(newMakeTransferState)
    }

    suspend fun deleteTransfer(recordNum: Int) {
        val (outTransfer, inTransfer) = recordStackList.value
            .getOutAndInTransfersByRecordNum(recordNum) ?: return
        val prevAccounts = Pair(
            accountsUiState.value.accountList.findById(outTransfer.account.id) ?: return,
            accountsUiState.value.accountList.findById(inTransfer.account.id) ?: return
        )

        val recordList = outTransfer.toRecordList() + inTransfer.toRecordList()
        val updatedAccountList = listOf(
            prevAccounts.first.cloneAndAddToBalance(outTransfer.totalAmount),
            prevAccounts.second.cloneAndSubtractFromBalance(inTransfer.totalAmount)
        )
            .mergeWith(accountsUiState.value.accountList)
            .toDataModels()
        val updatedBudgetsByType = budgetsByType.value.subtractUsedAmountsByRecords(recordList)

        _budgetsByType.update { updatedBudgetsByType }

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpsertAccounts(
                recordListToDelete = recordList,
                accountListToUpsert = updatedAccountList,
            )
        }
    }


    private val _dateRangeMenuUiState: MutableStateFlow<DateRangeMenuUiState> =
        MutableStateFlow(
            DateRangeMenuUiState(
                startCalendarDateMillis = DateRangeEnum.ThisMonth.getCalendarStartLong(),
                endCalendarDateMillis = DateRangeEnum.ThisMonth.getCalendarEndLong(),
                dateRangeWithEnum = DateRangeEnum.ThisMonth.withLongDateRange()
            )
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
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.getLongDateRange())
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
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.getLongDateRange())
    }


    private val _greetingsWidgetTitleRes: MutableStateFlow<Int> = MutableStateFlow(
        getGreetingsWidgetTitleRes()
    )

    fun updateGreetingsWidgetTitle() {
        _greetingsWidgetTitleRes.update { getGreetingsWidgetTitleRes() }
    }

    private fun getGreetingsWidgetTitleRes(): Int {
        return LocalDateTime.now().hour.getGreetingsWidgetTitleRes()
    }


    val widgetsUiState = combine(
        _dateRangeMenuUiState,
        _accountsUiState,
        _todayRecordList,
        recordStackList,
        _categoriesWithSubcategories,
        _greetingsWidgetTitleRes
    ) { array ->
        val dateRangeMenuUiState = array[0] as DateRangeMenuUiState
        val accountsUiState = array[1] as AccountsUiState
        val todayRecordList = array[2] as List<Record>
        val recordStackList = array[3] as List<RecordStack>
        val categoriesWithSubcategories = array[4] as CategoriesWithSubcategories
        val greetingsWidgetTitleRes = array[5] as Int

        val recordsFilteredByDateAndAccount = recordStackList.filterByDateAndAccount(
            dateRange = dateRangeMenuUiState.getLongDateRange(),
            activeAccount = accountsUiState.activeAccount
        )

        WidgetsUiState(
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount,
            greetings = GreetingsWidgetUiState(
                titleRes = greetingsWidgetTitleRes,
                expensesTotal = todayRecordList
                    .filter { it.accountId == accountsUiState.activeAccount?.id }
                    .getTotalAmountByType(CategoryType.Expense)
            ),
            expensesIncomeState = recordsFilteredByDateAndAccount.getExpensesIncomeWidgetUiState(),
            categoryStatisticsLists = categoriesWithSubcategories
                .getStatistics(recordsFilteredByDateAndAccount)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WidgetsUiState()
    )


    fun fetchDataOnStart() {
        fetchAccountsFromDb()
        fetchCategoriesFromDb()
        fetchLastRecordNumFromDb()
        fetchCategoryCollectionsFromDb()
        fetchRecordsFromDbForToday()
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.getLongDateRange())
        fetchBudgetsFromDb()
    }

}
