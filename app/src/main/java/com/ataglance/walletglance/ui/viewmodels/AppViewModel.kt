package com.ataglance.walletglance.ui.viewmodels

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
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.CategoryWithSubcategory
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionsWithIds
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.data.date.DateRangeMenuUiState
import com.ataglance.walletglance.data.date.DateRangeState
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.makingRecord.MadeTransferState
import com.ataglance.walletglance.data.makingRecord.MakeRecordStatus
import com.ataglance.walletglance.data.makingRecord.MakeRecordUiState
import com.ataglance.walletglance.data.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.data.settings.ThemeUiState
import com.ataglance.walletglance.data.utils.addAmountWhereAccountIdAndCategoryIdAre
import com.ataglance.walletglance.data.utils.addAmountsOfRecordUnitListWithAccountId
import com.ataglance.walletglance.data.utils.breakOnBudgetsAndAssociations
import com.ataglance.walletglance.data.utils.breakOnCollectionsAndAssociations
import com.ataglance.walletglance.data.utils.checkOrderNumbers
import com.ataglance.walletglance.data.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.data.utils.filterByDateAndAccount
import com.ataglance.walletglance.data.utils.findById
import com.ataglance.walletglance.data.utils.findByOrderNum
import com.ataglance.walletglance.data.utils.fixOrderNumbers
import com.ataglance.walletglance.data.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.data.utils.getCalendarEndLong
import com.ataglance.walletglance.data.utils.getCalendarStartLong
import com.ataglance.walletglance.data.utils.getDateRangeState
import com.ataglance.walletglance.data.utils.getExpensesIncomeWidgetUiState
import com.ataglance.walletglance.data.utils.getGreetingsWidgetTitleRes
import com.ataglance.walletglance.data.utils.getIdsThatAreNotInList
import com.ataglance.walletglance.data.utils.getOutAndInTransfersByRecordNum
import com.ataglance.walletglance.data.utils.getTodayDateLong
import com.ataglance.walletglance.data.utils.getTotalAmount
import com.ataglance.walletglance.data.utils.getTotalAmountByType
import com.ataglance.walletglance.data.utils.inverse
import com.ataglance.walletglance.data.utils.mergeWith
import com.ataglance.walletglance.data.utils.resetIfNeeded
import com.ataglance.walletglance.data.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.data.utils.subtractAmountWhereAccountIdAndCategoryIdAre
import com.ataglance.walletglance.data.utils.subtractAmountsOfRecordStack
import com.ataglance.walletglance.data.utils.toAccountList
import com.ataglance.walletglance.data.utils.toBudgetList
import com.ataglance.walletglance.data.utils.toCategoriesWithSubcategories
import com.ataglance.walletglance.data.utils.toCategoryEntityList
import com.ataglance.walletglance.data.utils.toEntityList
import com.ataglance.walletglance.data.utils.toRecordStackList
import com.ataglance.walletglance.data.utils.transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds
import com.ataglance.walletglance.data.widgets.GreetingsWidgetUiState
import com.ataglance.walletglance.data.widgets.WidgetsUiState
import com.ataglance.walletglance.domain.entities.AccountEntity
import com.ataglance.walletglance.domain.entities.BudgetEntity
import com.ataglance.walletglance.domain.entities.CategoryEntity
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.domain.repositories.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.domain.repositories.BudgetRepository
import com.ataglance.walletglance.domain.repositories.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountAndBudgetRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.domain.repositories.SettingsRepository
import com.ataglance.walletglance.ui.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.ui.theme.navigation.screens.SettingsScreens
import com.ataglance.walletglance.ui.viewmodels.records.MakeTransferUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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
    val recordAndAccountAndBudgetRepository: RecordAndAccountAndBudgetRepository,
    val budgetRepository: BudgetRepository,
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
            .breakOnCollectionsAndAssociations()
        val (originalCollections, originalAssociations) = categoryCollectionsUiState.value
            .concatenateLists().breakOnCollectionsAndAssociations()

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


    private val _budgetList: MutableStateFlow<List<Budget>> = MutableStateFlow(emptyList())
    val budgetList: StateFlow<List<Budget>> = _budgetList.asStateFlow()

    private fun fetchBudgetsFromDb() {
        viewModelScope.launch {
            val (budgetEntityList, associationList) = budgetAndBudgetAccountAssociationRepository
                .getBudgetsAndBudgetAccountAssociations()
            val categoryWithSubcategoriesList = categoriesWithSubcategories.value.expense

            val resetBudgetEntityList = budgetEntityList.resetIfNeeded()

            if (resetBudgetEntityList.isEmpty()) {
                val budgetList = budgetEntityList.toBudgetList(
                    categoryWithSubcategoriesList = categoryWithSubcategoriesList,
                    associationList = associationList
                )
                _budgetList.update { budgetList }
            } else {
                saveBudgetsEntitiesToDb(resetBudgetEntityList)
                fetchBudgetsFromDb()
            }

        }
    }

    suspend fun saveBudgetsToDb(budgets: List<Budget>) {
        val (newBudgets, newAssociations) = budgets.breakOnBudgetsAndAssociations()
        val (originalCollections, originalAssociations) = budgetList.value
            .breakOnBudgetsAndAssociations()

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

    private suspend fun saveBudgetsEntitiesToDb(budgetEntityList: List<BudgetEntity>) {
        viewModelScope.launch {
            budgetRepository.upsertBudgets(budgetList = budgetEntityList)
            fetchBudgetsFromDb()
        }
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
        _categoriesWithSubcategories
    ) { recordList, accountsUiState, categoriesWithSubcategories ->
        recordList.toRecordStackList(
            accountList = accountsUiState.accountList,
            categoriesWithSubcategories = categoriesWithSubcategories
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
        val updatedAccounts = listOf(
            uiState.account.cloneAndAddToOrSubtractFromBalance(
                amount = unitList.getTotalAmount(), recordType = uiState.type
            )
        )
            .mergeWith(accountsUiState.value.accountList)
            .toEntityList()
        val updatedBudgets = budgetList.value.addAmountsOfRecordUnitListWithAccountId(
            accountId = uiState.account.id,
            recordUnitList = unitList,
            recordDate = uiState.dateTimeState.dateLong
        ).toEntityList()

        viewModelScope.launch {
            recordAndAccountAndBudgetRepository.upsertRecordsAndUpsertAccountsAnsUpsertBudgets(
                recordList, updatedAccounts, updatedBudgets
            )
        }
    }

    private suspend fun saveEditedRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        uiState.account ?: return

        val currentRecordStack = recordStackList.value.findByOrderNum(uiState.recordNum) ?: return
        val updatedAccounts = getUpdatedAccountsAfterRecordEditing(
            uiState = uiState,
            recordStack = currentRecordStack,
            newTotalAmount = unitList.getTotalAmount()
        )
            ?.mergeWith(accountsUiState.value.accountList)
            ?.toEntityList()
            ?: return
        val updatedBudgets = getUpdatedBudgetsAfterRecordEditing(
            prevRecordStack = currentRecordStack,
            newRecordAccountId = uiState.account.id,
            newRecordUnitList = unitList,
            recordDate = uiState.dateTimeState.dateLong
        ).toEntityList()

        if (unitList.size == currentRecordStack.stack.size) {
            viewModelScope.launch {
                recordAndAccountAndBudgetRepository.upsertRecordsAndUpsertAccountsAnsUpsertBudgets(
                    recordList = uiState.toRecordListWithOldIds(unitList, currentRecordStack),
                    accountList = updatedAccounts,
                    budgetList = updatedBudgets
                )
            }
        } else {
            viewModelScope.launch {
                recordAndAccountAndBudgetRepository
                    .deleteAndUpsertRecordsAndUpsertAccountsAndUpsertBudgets(
                        recordListToDelete = currentRecordStack.toRecordList(),
                        recordListToUpsert = uiState.toRecordList(unitList),
                        accountList = updatedAccounts,
                        budgetList = updatedBudgets
                    )
            }
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

    private fun getUpdatedBudgetsAfterRecordEditing(
        prevRecordStack: RecordStack,
        newRecordAccountId: Int,
        newRecordUnitList: List<MakeRecordUnitUiState>,
        recordDate: Long
    ): List<Budget> {
        val prevUsedBudgets = budgetList.value.subtractAmountsOfRecordStack(
            recordStack = prevRecordStack,
            recordDate = recordDate
        )
        val newUsedBudgets = prevUsedBudgets
            .mergeWith(budgetList.value)
            .addAmountsOfRecordUnitListWithAccountId(
                accountId = newRecordAccountId,
                recordUnitList = newRecordUnitList,
                recordDate = recordDate
            )
        return newUsedBudgets.mergeWith(prevUsedBudgets)
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
        val recordStack = recordStackList.value.findByOrderNum(recordNum) ?: return

        val account = accountsUiState.value.accountList
            .findById(recordStack.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = recordStack.totalAmount,
                recordType = recordStack.type.inverse()
            )
            ?: return
        val updatedAccounts = listOf(account)
            .mergeWith(accountsUiState.value.accountList)
            .toEntityList()
        val updatedBudgets = budgetList.value
            .subtractAmountsOfRecordStack(recordStack = recordStack, recordDate = recordStack.date)
            .toEntityList()

        viewModelScope.launch {
            recordAndAccountAndBudgetRepository.deleteRecordsAndUpsertAccountsAndUpsertBudgets(
                recordList = recordStack.toRecordList(),
                accountList = updatedAccounts,
                budgetList = updatedBudgets
            )
        }
    }

    suspend fun saveTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return
        val madeTransferState = uiState.toMadeTransferState(appUiSettings.value.nextRecordNum())
            ?: return

        val (recordList, updatedAccounts, updatedBudgets) = if (
            madeTransferState.recordStatus == MakeRecordStatus.Create
        ) {
            getRecordsAndUpdatedAccountsAndBudgetsAfterNewTransfer(madeTransferState)
        } else {
            getRecordsAndUpdatedAccountsAndBudgetsAfterEditedTransfer(madeTransferState) ?: return
        }

        viewModelScope.launch {
            recordAndAccountAndBudgetRepository.upsertRecordsAndUpsertAccountsAnsUpsertBudgets(
                recordList = recordList,
                accountList = updatedAccounts,
                budgetList = updatedBudgets
            )
        }
    }

    private fun getRecordsAndUpdatedAccountsAndBudgetsAfterNewTransfer(
        state: MadeTransferState
    ): Triple<List<Record>, List<AccountEntity>, List<BudgetEntity>> {
        val recordList = state.toRecordsPair().toList()
        val updatedAccountList = listOf(
            state.fromAccount.cloneAndSubtractFromBalance(state.startAmount),
            state.toAccount.cloneAndAddToBalance(state.finalAmount)
        )
            .mergeWith(accountsUiState.value.accountList)
            .toEntityList()
        val updatedBudgets = budgetList.value
            .addAmountWhereAccountIdAndCategoryIdAre(
                amount = state.startAmount,
                accountId = state.fromAccount.id,
                categoryIds = listOf(12, 77),
                recordDate = state.dateTimeState.dateLong
            )
            .toEntityList()

        return Triple(recordList, updatedAccountList, updatedBudgets)
    }

    private fun getRecordsAndUpdatedAccountsAndBudgetsAfterEditedTransfer(
        state: MadeTransferState
    ): Triple<List<Record>, List<AccountEntity>, List<BudgetEntity>>? {
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
            ?.toEntityList()
            ?: return null
        val updatedBudgets = getUpdatedBudgetsAfterEditedTransfer(
            prevRecordStackFrom = currRecordStackFrom,
            state = state
        ).toEntityList()

        return Triple(recordList, updatedAccounts, updatedBudgets)
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

    private fun getUpdatedBudgetsAfterEditedTransfer(
        prevRecordStackFrom: RecordStack,
        state: MadeTransferState
    ): List<Budget> {
        val prevUsedBudgets = budgetList.value.addAmountWhereAccountIdAndCategoryIdAre(
            amount = prevRecordStackFrom.totalAmount,
            accountId = prevRecordStackFrom.account.id,
            categoryIds = listOf(12, 77),
            recordDate = state.dateTimeState.dateLong
        )
        val newUsedBudgets = prevUsedBudgets
            .mergeWith(budgetList.value)
            .subtractAmountWhereAccountIdAndCategoryIdAre(
                amount = state.startAmount,
                accountId = state.fromAccount.id,
                categoryIds = listOf(12, 77),
                recordDate = state.dateTimeState.dateLong
            )
        return newUsedBudgets.mergeWith(prevUsedBudgets)
    }

    suspend fun repeatTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return

        val madeTransferState = uiState
            .copy(
                recordIdFrom = 0,
                recordIdTo = 0,
                recordNum = null,
                dateTimeState = DateTimeState()
            )
            .toMadeTransferState(appUiSettings.value.nextRecordNum())
            ?: return

        val (recordList, updatedAccounts, updatedBudgets) =
            getRecordsAndUpdatedAccountsAndBudgetsAfterNewTransfer(madeTransferState)

        viewModelScope.launch {
            recordAndAccountAndBudgetRepository.upsertRecordsAndUpsertAccountsAnsUpsertBudgets(
                recordList = recordList,
                accountList = updatedAccounts,
                budgetList = updatedBudgets
            )
        }
    }

    suspend fun deleteTransfer(recordNum: Int) {
        val (outTransfer, inTransfer) = recordStackList.value
            .getOutAndInTransfersByRecordNum(recordNum) ?: return

        val recordList = outTransfer.toRecordList() + inTransfer.toRecordList()

        val prevAccounts = Pair(
            accountsUiState.value.accountList.findById(outTransfer.account.id) ?: return,
            accountsUiState.value.accountList.findById(inTransfer.account.id) ?: return
        )

        val updatedAccountList = listOf(
            prevAccounts.first.cloneAndAddToBalance(outTransfer.totalAmount),
            prevAccounts.second.cloneAndSubtractFromBalance(inTransfer.totalAmount)
        )
            .mergeWith(accountsUiState.value.accountList)
            .toEntityList()
        val updatedBudgets = budgetList.value
            .subtractAmountWhereAccountIdAndCategoryIdAre(
                amount = outTransfer.totalAmount,
                accountId = outTransfer.account.id,
                categoryIds = listOf(12, 77),
                recordDate = outTransfer.date
            )
            .toEntityList()

        viewModelScope.launch {
            recordAndAccountAndBudgetRepository.deleteRecordsAndUpsertAccountsAndUpsertBudgets(
                recordList = recordList,
                accountList = updatedAccountList,
                budgetList = updatedBudgets
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


    private val _greetingsWidgetTitleRes: MutableStateFlow<Int> = MutableStateFlow(
        getGreetingsWidgetTitleRes()
    )

    val widgetsUiState = combine(
        _dateRangeMenuUiState,
        _accountsUiState,
        recordStackList,
        _categoriesWithSubcategories,
        _greetingsWidgetTitleRes
    ) { dateRangeMenuUiState, accountsUiState, recordStackList,
        categoriesWithSubcategories, greetingsWidgetTitleRes ->

        val recordsFilteredByDateAndAccount = recordStackList.filterByDateAndAccount(
            dateRangeFromAndTo = dateRangeMenuUiState.dateRangeState.getRangePair(),
            activeAccount = accountsUiState.activeAccount
        )

        WidgetsUiState(
            recordsFilteredByDateAndAccount = recordsFilteredByDateAndAccount,
            greetings = GreetingsWidgetUiState(
                titleRes = greetingsWidgetTitleRes,
                expensesTotal = recordStackList
                    .filterByDateAndAccount(
                        dateRangeFromAndTo = getTodayDateLong().let { it to it + 2359 },
                        activeAccount = accountsUiState.activeAccount
                    )
                    .getTotalAmountByType(RecordType.Expense)
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

    fun updateGreetingsWidgetTitle() {
        _greetingsWidgetTitleRes.update { getGreetingsWidgetTitleRes() }
    }

    private fun getGreetingsWidgetTitleRes(): Int {
        return LocalDateTime.now().hour.getGreetingsWidgetTitleRes()
    }


    fun fetchDataOnStart() {
        fetchAccountsFromDb()
        fetchCategoriesFromDb()
        fetchLastRecordNumFromDb()
        fetchCategoryCollectionsFromDb()
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.dateRangeState)
        fetchBudgetsFromDb()
    }

}