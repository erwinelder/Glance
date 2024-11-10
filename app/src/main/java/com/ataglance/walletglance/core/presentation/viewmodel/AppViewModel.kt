package com.ataglance.walletglance.core.presentation.viewmodel

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.account.data.model.AccountEntity
import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.data.utils.checkOrderNumbers
import com.ataglance.walletglance.account.data.utils.fixOrderNumbers
import com.ataglance.walletglance.account.data.utils.getThatAreNotInList
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.utils.findById
import com.ataglance.walletglance.account.domain.utils.findByOrderNum
import com.ataglance.walletglance.account.domain.utils.mergeWith
import com.ataglance.walletglance.account.domain.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.account.domain.utils.toAccountList
import com.ataglance.walletglance.account.mapper.toAccountEntityList
import com.ataglance.walletglance.budget.data.repository.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.budget.data.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.budget.data.utils.getBudgetsThatAreNotInList
import com.ataglance.walletglance.budget.domain.model.Budget
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.budget.domain.model.TotalAmountByRange
import com.ataglance.walletglance.budget.domain.utils.groupByType
import com.ataglance.walletglance.budget.mapper.divideIntoBudgetsAndAssociations
import com.ataglance.walletglance.budget.mapper.toBudgetList
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.data.utils.getThatAreNotInList
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategory
import com.ataglance.walletglance.category.domain.model.CategoryWithSubcategoryByType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.utils.toRecordType
import com.ataglance.walletglance.category.domain.utils.translateCategories
import com.ataglance.walletglance.category.mapper.toCategoriesWithSubcategories
import com.ataglance.walletglance.category.mapper.toCategoryEntityList
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.categoryCollection.data.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.categoryCollection.data.utils.getThatAreNotInList
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.mapper.divideIntoCollectionsAndAssociations
import com.ataglance.walletglance.categoryCollection.mapper.transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds
import com.ataglance.walletglance.core.data.model.LongDateRange
import com.ataglance.walletglance.core.data.model.UserRemotePreferences
import com.ataglance.walletglance.core.data.preferences.SettingsRepository
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.AppUiSettings
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.presentation.navigation.MainScreens
import com.ataglance.walletglance.core.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.core.utils.getCalendarEndLong
import com.ataglance.walletglance.core.utils.getCalendarStartLong
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayLongDateRange
import com.ataglance.walletglance.core.utils.isInRange
import com.ataglance.walletglance.core.utils.withLongDateRange
import com.ataglance.walletglance.record.data.model.RecordEntity
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.data.utils.filterByAccountId
import com.ataglance.walletglance.record.data.utils.getTotalAmountByType
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordsInDateRange
import com.ataglance.walletglance.record.domain.utils.findByRecordNum
import com.ataglance.walletglance.record.domain.utils.getFirstByTypeAndAccountIdOrJustType
import com.ataglance.walletglance.record.domain.utils.getOutAndInTransfersByRecordNums
import com.ataglance.walletglance.record.domain.utils.inverse
import com.ataglance.walletglance.record.mapper.toRecordList
import com.ataglance.walletglance.record.mapper.toRecordStackList
import com.ataglance.walletglance.recordAndAccount.data.repository.RecordAndAccountRepository
import com.ataglance.walletglance.recordCreation.domain.DataAfterRecordOperation
import com.ataglance.walletglance.recordCreation.domain.mapper.toCreatedRecord
import com.ataglance.walletglance.recordCreation.domain.mapper.toCreatedTransfer
import com.ataglance.walletglance.recordCreation.domain.mapper.toRecordEntityList
import com.ataglance.walletglance.recordCreation.domain.mapper.toRecordEntityListWithOldIds
import com.ataglance.walletglance.recordCreation.domain.mapper.toRecordsPair
import com.ataglance.walletglance.recordCreation.domain.record.CreatedRecord
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.CreatedTransfer
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferSenderReceiverRecordNums
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.navigation.SettingsScreens
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
import java.util.Locale

class AppViewModel(
    val settingsRepository: SettingsRepository,
    val accountRepository: AccountRepository,
    val categoryRepository: CategoryRepository,
    private val categoryCollectionAndAssociationRepository:
    CategoryCollectionAndCollectionCategoryAssociationRepository,
    val recordRepository: RecordRepository,
    val recordAndAccountRepository: RecordAndAccountRepository,
    val budgetAndAssociationRepository: BudgetAndBudgetAccountAssociationRepository,
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
        val translatedCategories = translateCategories(
            defaultCategoriesInCurrLocale = getDefaultCategoriesByLanguage(currentLangCode, context),
            defaultCategoriesInNewLocale = getDefaultCategoriesByLanguage(newLangCode, context),
            currCategoryList = categoriesWithSubcategories.value.concatenateAsCategoryList()
        )
        viewModelScope.launch {
            categoryRepository.upsertEntities(translatedCategories.toCategoryEntityList())
        }
    }

    private fun getDefaultCategoriesByLanguage(langCode: String, context: Context): List<Category> {
        val langContext = context.createConfigurationContext(Configuration().apply {
            setLocale(Locale(langCode))
        })
        return DefaultCategoriesPackage(langContext).getDefaultCategories()
            .concatenateAsCategoryList()
    }

    fun updatePreferencesAfterSignIn(preferences: UserRemotePreferences) {
        setLanguage(preferences.language)
    }

    suspend fun setUserId(userId: String) {
        settingsRepository.saveUserIdPreference(userId)
    }

    suspend fun resetUserId() {
        settingsRepository.saveUserIdPreference("")
    }

    fun getUserId(): Flow<String?> {
        return settingsRepository.userId
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
    suspend fun resetAppData() {
        generalRepository.resetAllData()
    }

    private fun fetchLastRecordNum() {
        viewModelScope.launch {
            recordRepository.getLastRecordNum().collect { recordNum ->
                _lastRecordNum.update { recordNum ?: 0 }
            }
        }
    }


    private val _accountsAndActiveOne: MutableStateFlow<AccountsAndActiveOne> =
        MutableStateFlow(AccountsAndActiveOne())
    val accountsAndActiveOne: StateFlow<AccountsAndActiveOne> = _accountsAndActiveOne.asStateFlow()

    private fun fetchAccounts() {
        viewModelScope.launch {
            accountRepository.getAllEntities().collect { accountList ->
                if (accountList.checkOrderNumbers()) {
                    applyAccountListToUiState(accountList.toAccountList())
                } else {
                    saveAccounts(accountList.fixOrderNumbers())
                }
            }
        }
    }

    fun applyAccountListToUiState(accountList: List<Account>) {
        _accountsAndActiveOne.update { uiState ->
            uiState.copy(
                accountList = accountList,
                activeAccount = accountList.takeIf { it.isNotEmpty() }?.firstOrNull { it.isActive }
            )
        }
    }

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

    suspend fun saveAccounts(accountsList: List<AccountEntity>) {
        val listToDelete = accountsAndActiveOne.value.accountList
            .toAccountEntityList()
            .getThatAreNotInList(accountsList)

        if (listToDelete.isNotEmpty()) {
            recordAndAccountRepository.deleteAndUpdateAccountsAndConvertTransfersToRecords(
                accountListToDelete = listToDelete,
                accountListToUpsert = accountsList
            )
        } else {
            accountRepository.upsertEntities(accountsList)
        }
    }


    private val _categoriesWithSubcategories: MutableStateFlow<CategoriesWithSubcategories> =
        MutableStateFlow(CategoriesWithSubcategories())
    val categoriesWithSubcategories: StateFlow<CategoriesWithSubcategories> =
        _categoriesWithSubcategories.asStateFlow()

    private fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getAllEntities().collect { categoryEntityList ->
                _categoriesWithSubcategories.update {
                    categoryEntityList.toCategoriesWithSubcategories()
                }
            }
        }
    }

    suspend fun saveCategories(categoryList: List<CategoryEntity>) {
        val listToDelete = categoriesWithSubcategories.value
            .concatenateAsCategoryList()
            .toCategoryEntityList()
            .getThatAreNotInList(categoryList)

        if (listToDelete.isNotEmpty()) {
            categoryRepository.deleteAndUpsertEntities(listToDelete, categoryList)
        } else {
            categoryRepository.upsertEntities(categoryList)
        }
    }

    fun getLastUsedRecordCategories(): CategoryWithSubcategoryByType {
        return CategoryWithSubcategoryByType(
            expense = getLastUsedRecordCategoryByType(CategoryType.Expense),
            income = getLastUsedRecordCategoryByType(CategoryType.Income),
        )
    }

    private fun getLastUsedRecordCategoryByType(type: CategoryType): CategoryWithSubcategory? {
        return accountsAndActiveOne.value.activeAccount?.id
            ?.let { recordStackListFilteredByDate.value.getFirstByTypeAndAccountIdOrJustType(type, it) }
            ?.stack?.firstOrNull()?.categoryWithSubcategory
            ?: categoriesWithSubcategories.value.getLastCategoryWithSubcategoryByType(type)
    }


    private val _categoryCollectionsUiState: MutableStateFlow<CategoryCollectionsWithIdsByType> =
        MutableStateFlow(CategoryCollectionsWithIdsByType())
    val categoryCollectionsUiState: StateFlow<CategoryCollectionsWithIdsByType> =
        _categoryCollectionsUiState.asStateFlow()

    private fun fetchCategoryCollections() {
        viewModelScope.launch {
            val (collections, associations) = categoryCollectionAndAssociationRepository
                .getEntitiesAndAssociations()
            _categoryCollectionsUiState.update {
                transformCategCollectionsAndCollectionCategAssociationsToCollectionsWithIds(
                    collectionList = collections,
                    collectionCategoryAssociationList = associations
                )
            }
        }
    }

    suspend fun saveCategoryCollections(
        collectionUiStateList: List<CategoryCollectionWithIds>
    ) {

        val (newCollections, newAssociations) = collectionUiStateList
            .divideIntoCollectionsAndAssociations()
        val (originalCollections, originalAssociations) = categoryCollectionsUiState.value
            .concatenateLists().divideIntoCollectionsAndAssociations()

        val collectionsToDelete = originalCollections.getThatAreNotInList(newCollections)
        val associationsToDelete = originalAssociations
            .getAssociationsThatAreNotInList(newAssociations)

        categoryCollectionAndAssociationRepository
            .deleteAndUpsertEntitiesAndDeleteAndUpsertAssociations(
                entitiesToDelete = collectionsToDelete,
                entitiesToUpsert = newCollections,
                associationsToDelete = associationsToDelete,
                associationsToUpsert = newAssociations
            )
        fetchCategoryCollections()

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

    fun getActiveAccountExpensesForToday(): Double {
        return accountsAndActiveOne.value.activeAccount?.id
            ?.let {
                _todayRecordList.value
                    .filterByAccountId(it)
                    .getTotalAmountByType(CategoryType.Expense)
            }
            ?: 0.0
    }


    private val _recordListInDateRange: MutableStateFlow<RecordsInDateRange> = MutableStateFlow(
        RecordsInDateRange()
    )

    fun fetchRecordsInDateRange(longDateRange: LongDateRange) {
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

    val recordStackListFilteredByDate: StateFlow<List<RecordStack>> = combine(
        _recordListInDateRange,
        _accountsAndActiveOne,
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

    private fun fetchBudgets() {
        viewModelScope.launch {
            val (budgetEntityList, associationList) = budgetAndAssociationRepository
                .getEntitiesAndAssociations()
            val categoryWithSubcategoriesList = categoriesWithSubcategories.value.expense

            val budgetsByType = budgetEntityList
                .toBudgetList(
                    categoryWithSubcategoriesList = categoryWithSubcategoriesList,
                    associationList = associationList,
                    accountList = accountsAndActiveOne.value.accountList
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

        val budgetsToDelete = originalCollections.getBudgetsThatAreNotInList(newBudgets)
        val associationsToDelete = originalAssociations.getAssociationsThatAreNotInList(newAssociations)

        budgetAndAssociationRepository
            .deleteAndUpsertEntitiesAndDeleteAndUpsertAssociations(
                entitiesToDelete = budgetsToDelete,
                entitiesToUpsert = newBudgets,
                associationsToDelete = associationsToDelete,
                associationsToUpsert = newAssociations
            )
        fetchBudgets()
    }


    suspend fun saveRecord(recordDraft: RecordDraft) {
        val createdRecord = recordDraft.toCreatedRecord() ?: return

        val dataAfterRecordOperation = if (recordDraft.general.isNew) {
            getDataForDatabaseAfterNewRecord(createdRecord)
        } else {
            getDataForDatabaseAfterEditedRecord(createdRecord)
        } ?: return

        _budgetsByType.update {
            dataAfterRecordOperation.updatedBudgetsByType
        }

        recordAndAccountRepository.deleteAndUpsertRecordsAndUpsertAccounts(
            recordListToDelete = dataAfterRecordOperation.recordListToDelete,
            recordListToUpsert = dataAfterRecordOperation.recordListToUpsert,
            accountListToUpsert = dataAfterRecordOperation.accountListToUpsert
        )
        if (recordDraft.general.dateTimeState.dateLong.isInRange(getTodayLongDateRange())) {
            fetchRecordsForToday()
        }
        fetchRecordsInDateRange(dateRangeMenuUiState.value.getLongDateRange())
    }

    private fun getDataForDatabaseAfterNewRecord(
        createdRecord: CreatedRecord
    ): DataAfterRecordOperation {
        val recordList = createdRecord.toRecordEntityList()
        val updatedAccounts = listOf(
            createdRecord.account.cloneAndAddToOrSubtractFromBalance(
                amount = createdRecord.totalAmount,
                recordType = createdRecord.type.toRecordType()
            )
        )
            .mergeWith(accountsAndActiveOne.value.accountList)
            .toAccountEntityList()
        val updatedBudgetsByType = budgetsByType.value.addUsedAmountsByRecords(recordList)

        return DataAfterRecordOperation(
            recordListToUpsert = recordList,
            accountListToUpsert = updatedAccounts,
            updatedBudgetsByType = updatedBudgetsByType
        )
    }

    private fun getDataForDatabaseAfterEditedRecord(
        createdRecord: CreatedRecord
    ): DataAfterRecordOperation? {
        val currentRecordStack = recordStackListFilteredByDate.value.findByRecordNum(createdRecord.recordNum)
            ?: return null
        val currentRecordList = currentRecordStack.toRecordList()
        val updatedAccounts = getUpdatedAccountsAfterRecordEditing(
            createdRecord = createdRecord,
            recordStack = currentRecordStack,
            newTotalAmount = createdRecord.totalAmount
        )
            ?.mergeWith(accountsAndActiveOne.value.accountList)
            ?.toAccountEntityList()
            ?: return null
        val budgetsByType = budgetsByType.value.subtractUsedAmountsByRecords(currentRecordList)

        return if (createdRecord.items.size == currentRecordStack.stack.size) {
            val recordListToUpsert = createdRecord.toRecordEntityListWithOldIds(currentRecordStack)
            DataAfterRecordOperation(
                recordListToUpsert = recordListToUpsert,
                accountListToUpsert = updatedAccounts,
                updatedBudgetsByType = budgetsByType.addUsedAmountsByRecords(recordListToUpsert)
            )
        } else {
            val recordListToUpsert = createdRecord.toRecordEntityList()
            DataAfterRecordOperation(
                recordListToDelete = currentRecordList,
                recordListToUpsert = recordListToUpsert,
                accountListToUpsert = updatedAccounts,
                updatedBudgetsByType = budgetsByType.addUsedAmountsByRecords(recordListToUpsert)
            )
        }
    }

    private fun getUpdatedAccountsAfterRecordEditing(
        createdRecord: CreatedRecord,
        recordStack: RecordStack,
        newTotalAmount: Double
    ): List<Account>? {
        return if (createdRecord.account.id == recordStack.account.id) {
            listOf(
                createdRecord.account.cloneAndReapplyAmountToBalance(
                    prevAmount = recordStack.totalAmount,
                    newAmount = newTotalAmount,
                    recordType = createdRecord.type.toRecordType()
                )
            )
        } else {
            val prevAccount = accountsAndActiveOne.value.getAccountById(recordStack.account.id)
                ?: return null
            (prevAccount to createdRecord.account).returnAmountToFirstBalanceAndUpdateSecondBalance(
                prevAmount = recordStack.totalAmount,
                newAmount = newTotalAmount,
                recordType = createdRecord.type.toRecordType()
            ).toList()
        }
    }

    suspend fun repeatRecord(recordDraft: RecordDraft) {
        saveRecord(
            recordDraft = recordDraft.copy(
                general = recordDraft.general.copy(
                    isNew = true,
                    recordNum = appUiSettings.value.nextRecordNum(),
                    dateTimeState = DateTimeState()
                )
            )
        )
    }

    suspend fun deleteRecord(recordNum: Int) {
        val recordStack = recordStackListFilteredByDate.value.findByRecordNum(recordNum) ?: return

        val updatedAccount = accountsAndActiveOne.value.accountList
            .findById(recordStack.account.id)
            ?.cloneAndAddToOrSubtractFromBalance(
                amount = recordStack.totalAmount,
                recordType = recordStack.type.inverse()
            )
            ?: return
        val recordList = recordStack.toRecordList()
        val updatedAccounts = listOf(updatedAccount)
            .mergeWith(accountsAndActiveOne.value.accountList)
            .toAccountEntityList()
        val updatedBudgets = budgetsByType.value.subtractUsedAmountsByRecords(recordList)

        _budgetsByType.update { updatedBudgets }

        recordAndAccountRepository.deleteRecordsAndUpsertAccounts(
            recordListToDelete = recordList,
            accountListToUpsert = updatedAccounts
        )
    }

    suspend fun saveTransfer(transferDraft: TransferDraft) {
        val createdTransfer = transferDraft.toCreatedTransfer() ?: return

        val dataAfterRecordOperation = if (createdTransfer.isNew) {
            getDataAfterNewTransfer(createdTransfer)
        } else {
            getDataAfterEditedTransfer(createdTransfer)
        } ?: return

        _budgetsByType.update { dataAfterRecordOperation.updatedBudgetsByType }

        recordAndAccountRepository.upsertRecordsAndUpsertAccounts(
            recordListToUpsert = dataAfterRecordOperation.recordListToUpsert,
            accountListToUpsert = dataAfterRecordOperation.accountListToUpsert
        )
    }

    private fun getDataAfterNewTransfer(
        state: CreatedTransfer
    ): DataAfterRecordOperation {
        val recordList = state.toRecordsPair().toList()
        val updatedAccountList = listOf(
            state.sender.account.cloneAndSubtractFromBalance(state.sender.amount),
            state.receiver.account.cloneAndAddToBalance(state.receiver.amount)
        )
            .mergeWith(accountsAndActiveOne.value.accountList)
            .toAccountEntityList()
        val updatedBudgetsByType = budgetsByType.value.addUsedAmountsByRecords(recordList)

        return DataAfterRecordOperation(
            recordListToUpsert = recordList,
            accountListToUpsert = updatedAccountList,
            updatedBudgetsByType = updatedBudgetsByType
        )
    }

    private fun getDataAfterEditedTransfer(
        state: CreatedTransfer
    ): DataAfterRecordOperation? {
        val (currRecordStackFrom, currRecordStackTo) = recordStackListFilteredByDate.value
            .getOutAndInTransfersByRecordNums(state.getSenderReceiverRecordNums()) ?: return null

        val recordList = state.toRecordsPair().toList()
        val updatedAccounts = getUpdatedAccountsAfterEditedTransfer(
            uiState = state,
            currRecordStackFrom = currRecordStackFrom,
            currRecordStackTo = currRecordStackTo
        )
            ?.mergeWith(accountsAndActiveOne.value.accountList)
            ?.toAccountEntityList()
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
        uiState: CreatedTransfer,
        currRecordStackFrom: RecordStack,
        currRecordStackTo: RecordStack
    ): List<Account>? {
        val prevFromAccount = accountsAndActiveOne.value.accountList
            .findById(currRecordStackFrom.account.id) ?: return null
        val prevToAccount = accountsAndActiveOne.value.accountList
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
        state: CreatedTransfer,
        prevAccounts: List<Account>
    ): List<Account>? {
        val updatedAccounts = mutableListOf<Account>()

        prevAccounts.findById(state.sender.account.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.sender.amount))
        } ?: accountsAndActiveOne.value.accountList.findById(state.sender.account.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.sender.amount))
        } ?: return null

        prevAccounts.findById(state.receiver.account.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(state.receiver.amount))
        } ?: accountsAndActiveOne.value.accountList.findById(state.receiver.account.id)?.let {
            updatedAccounts.add(it.cloneAndAddToBalance(state.receiver.amount))
        } ?: return null

        return updatedAccounts
    }

    suspend fun repeatTransfer(state: TransferDraft) {
        if (!state.savingIsAllowed()) return

        val nextRecordNum = appUiSettings.value.nextRecordNum()

        val newMakeTransferState = state.copy(
            sender = state.sender.copy(
                recordNum = nextRecordNum,
                recordId = 0
            ),
            receiver = state.receiver.copy(
                recordNum = nextRecordNum + 1,
                recordId = 0
            ),
            dateTimeState = DateTimeState()
        )

        saveTransfer(newMakeTransferState)
    }

    suspend fun deleteTransfer(senderReceiverRecordNums: TransferSenderReceiverRecordNums) {

        val (outTransfer, inTransfer) = recordStackListFilteredByDate.value
            .getOutAndInTransfersByRecordNums(senderReceiverRecordNums) ?: return
        val prevAccounts = Pair(
            accountsAndActiveOne.value.accountList.findById(outTransfer.account.id) ?: return,
            accountsAndActiveOne.value.accountList.findById(inTransfer.account.id) ?: return
        )

        val recordList = outTransfer.toRecordList() + inTransfer.toRecordList()
        val updatedAccountList = listOf(
            prevAccounts.first.cloneAndAddToBalance(outTransfer.totalAmount),
            prevAccounts.second.cloneAndSubtractFromBalance(inTransfer.totalAmount)
        )
            .mergeWith(accountsAndActiveOne.value.accountList)
            .toAccountEntityList()
        val updatedBudgetsByType = budgetsByType.value.subtractUsedAmountsByRecords(recordList)

        _budgetsByType.update { updatedBudgetsByType }

        recordAndAccountRepository.deleteRecordsAndUpsertAccounts(
            recordListToDelete = recordList,
            accountListToUpsert = updatedAccountList,
        )
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


    fun fetchDataOnStart() {
        fetchAccounts()
        fetchCategories()
        fetchLastRecordNum()
        fetchCategoryCollections()
        fetchRecordsForToday()
        fetchRecordsInDateRange(dateRangeMenuUiState.value.getLongDateRange())
        fetchBudgets()
    }

}
