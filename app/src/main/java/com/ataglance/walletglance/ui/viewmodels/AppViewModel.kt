package com.ataglance.walletglance.ui.viewmodels

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppLanguage
import com.ataglance.walletglance.data.app.AppScreen
import com.ataglance.walletglance.data.app.Colors
import com.ataglance.walletglance.data.app.MakeRecordStatus
import com.ataglance.walletglance.data.app.SettingsScreen
import com.ataglance.walletglance.data.categories.CategoriesLists
import com.ataglance.walletglance.data.categories.CategoryColors
import com.ataglance.walletglance.data.categories.CategoryIcon
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.date.DateRangeEnum
import com.ataglance.walletglance.data.date.DateRangeState
import com.ataglance.walletglance.data.date.DateTimeState
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.entities.Record
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.domain.repositories.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.domain.repositories.SettingsRepository
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors
import com.ataglance.walletglance.ui.utils.breakOnCollectionsAndAssociations
import com.ataglance.walletglance.ui.utils.breakOnDifferentLists
import com.ataglance.walletglance.ui.utils.checkOrderNumbers
import com.ataglance.walletglance.ui.utils.convertCalendarMillisToLongWithoutSpecificTime
import com.ataglance.walletglance.ui.utils.findById
import com.ataglance.walletglance.ui.utils.findByRecordNum
import com.ataglance.walletglance.ui.utils.fixOrderNumbers
import com.ataglance.walletglance.ui.utils.getAssociationsThatAreNotInList
import com.ataglance.walletglance.ui.utils.getCalendarEndLong
import com.ataglance.walletglance.ui.utils.getCalendarStartLong
import com.ataglance.walletglance.ui.utils.getDateRangeState
import com.ataglance.walletglance.ui.utils.getIdsThatAreNotInList
import com.ataglance.walletglance.ui.utils.getLastUsedCategoryByAccountId
import com.ataglance.walletglance.ui.utils.getTodayDateLong
import com.ataglance.walletglance.ui.utils.getTotalAmount
import com.ataglance.walletglance.ui.utils.getTransferSecondUnitsRecordNumbers
import com.ataglance.walletglance.ui.utils.inverse
import com.ataglance.walletglance.ui.utils.returnAmountToFirstBalanceAndUpdateSecondBalance
import com.ataglance.walletglance.ui.utils.toRecordStackList
import com.ataglance.walletglance.ui.utils.transfersToRecordsWithCategoryOfTransfer
import com.ataglance.walletglance.ui.utils.transformCategoryCollectionsAndCollectionCategoryAssociationsToOneList
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
                    1 -> AppScreen.Home.route
                    0 -> AppScreen.Settings.route
                    else -> AppScreen.FinishSetup.route
                },
                startSettingsDestination = when (setupStage) {
                    1 -> SettingsScreen.SettingsHome.route
                    else -> SettingsScreen.Start.route
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
            categoriesUiState = categoriesUiState.value
        )

        viewModelScope.launch {
            categoryRepository.upsertCategories(translatedCategories)
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

    fun needToMoveScreenTowardsLeft(currentRoute: String, newRoute: String): Boolean {
        listOf(
            AppScreen.Home.route,
            AppScreen.Records.route,
            AppScreen.MakeRecord.route,
            AppScreen.CategoriesStatistics.route,
            AppScreen.Settings.route
        ).forEach { screenRoute ->
            if (currentRoute.substringBefore('/') == screenRoute) {
                return true
            } else if (newRoute.substringBefore('/') == screenRoute) {
                return false
            }
        }
        return true
    }


    private val _accountsUiState: MutableStateFlow<AccountsUiState> =
        MutableStateFlow(AccountsUiState())
    val accountsUiState: StateFlow<AccountsUiState> = _accountsUiState.asStateFlow()

    private fun fetchAccountsFromDb() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { accountList ->
                if (accountList.checkOrderNumbers()) {
                    applyAccountListToUiState(accountList)
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
                activeAccount = accountsUiState.value.accountList.find {  account ->
                    account.orderNum == accountOrderNum
                }?.copy(isActive = true)
            )
        }
    }

    suspend fun saveAccountsToDb(accountsList: List<Account>) {
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

    suspend fun deleteAccountWithItsRecords(accountId: Int, updatedAccountList: List<Account>) {
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

    val categoryIconNameToIconResMap = mapOf(
        CategoryIcon.FoodAndDrinks.name to CategoryIcon.FoodAndDrinks.res,
        CategoryIcon.Groceries.name to CategoryIcon.Groceries.res,
        CategoryIcon.Restaurant.name to CategoryIcon.Restaurant.res,
        CategoryIcon.Housing.name to CategoryIcon.Housing.res,
        CategoryIcon.HousingPurchase.name to CategoryIcon.HousingPurchase.res,
        CategoryIcon.Shopping.name to CategoryIcon.Shopping.res,
        CategoryIcon.Clothes.name to CategoryIcon.Clothes.res,
        CategoryIcon.Transport.name to CategoryIcon.Transport.res,
        CategoryIcon.Vehicle.name to CategoryIcon.Vehicle.res,
        CategoryIcon.DigitalLife.name to CategoryIcon.DigitalLife.res,
        CategoryIcon.ProfessionalSubs.name to CategoryIcon.ProfessionalSubs.res,
        CategoryIcon.EntertainmentSubs.name to CategoryIcon.EntertainmentSubs.res,
        CategoryIcon.Games.name to CategoryIcon.Games.res,
        CategoryIcon.Medicine.name to CategoryIcon.Medicine.res,
        CategoryIcon.Education.name to CategoryIcon.Education.res,
        CategoryIcon.Travels.name to CategoryIcon.Travels.res,
        CategoryIcon.Entertainment.name to CategoryIcon.Entertainment.res,
        CategoryIcon.Investments.name to CategoryIcon.Investments.res,
        CategoryIcon.Other.name to CategoryIcon.Other.res,
        CategoryIcon.Transfers.name to CategoryIcon.Transfers.res,
        CategoryIcon.Missing.name to CategoryIcon.Missing.res,
        CategoryIcon.Salary.name to CategoryIcon.Salary.res,
        CategoryIcon.Scholarship.name to CategoryIcon.Scholarship.res,
        CategoryIcon.Sales.name to CategoryIcon.Sales.res,
        CategoryIcon.Refunds.name to CategoryIcon.Refunds.res,
        CategoryIcon.Gifts.name to CategoryIcon.Gifts.res,
    )

    val categoryColorNameToColorMap = combine(_appTheme) { appThemeArray ->
        val theme = appThemeArray.first()
        mapOf(
            CategoryColors.Olive(theme).let { it.color.name to it.color },
            CategoryColors.Camel(theme).let { it.color.name to it.color },
            CategoryColors.Pink(theme).let { it.color.name to it.color },
            CategoryColors.Green(theme).let { it.color.name to it.color },
            CategoryColors.Red(theme).let { it.color.name to it.color },
            CategoryColors.LightBlue(theme).let { it.color.name to it.color },
            CategoryColors.Lavender(theme).let { it.color.name to it.color },
            CategoryColors.Blue(theme).let { it.color.name to it.color },
            CategoryColors.Aquamarine(theme).let { it.color.name to it.color },
            CategoryColors.Orange(theme).let { it.color.name to it.color },
            CategoryColors.Yellow(theme).let { it.color.name to it.color },
            CategoryColors.GrayDefault(theme).let { it.color.name to it.color }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyMap()
    )

    private fun fetchCategoriesFromDb() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect { categoryList ->

                try {
                    _categoriesUiState.update { categoryList.breakOnDifferentLists() }
                } catch (e: Exception) {
                    saveCategoriesToDb(categoryList.fixOrderNumbers())
                }

            }
        }
    }

    suspend fun saveCategoriesToDb(categoryList: List<Category>) {
        val listOfIdsToDelete =
            categoriesUiState.value.concatenateLists().getIdsThatAreNotInList(categoryList)

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
        recordStackList.value
            .getLastUsedCategoryByAccountId(accountId)
            ?.let { categoriesUiState.value.getCategoryPairByIds(it.first, it.second, type) }
            ?.let { return it }

        recordStackList.value
            .firstOrNull()?.stack
            ?.firstOrNull()
            ?.let {
                categoriesUiState.value.getCategoryPairByIds(it.categoryId, it.subcategoryId, type)
            }
            ?.let { return it }

        return categoriesUiState.value.parentCategories.getByType(type).lastOrNull()
            .let { parCategory ->
                parCategory to parCategory?.parentCategoryId?.let {
                    categoriesUiState.value.subcategories.getByType(type).lastOrNull()?.lastOrNull()
                }
            }
    }


    private val _categoryCollectionsUiState: MutableStateFlow<List<CategoryCollectionUiState>> =
        MutableStateFlow(listOf())
    val categoryCollectionsUiState: StateFlow<List<CategoryCollectionUiState>> =
        _categoryCollectionsUiState.asStateFlow()

    private fun fetchCategoryCollectionsFromDb() {
        viewModelScope.launch {
            val collectionsAndCollectionCategoryAssociations =
                categoryCollectionAndCollectionCategoryAssociationRepository
                    .getCategoryCollectionsAndCollectionCategoryAssociations()
            _categoryCollectionsUiState.update {
                transformCategoryCollectionsAndCollectionCategoryAssociationsToOneList(
                    collectionsAndCollectionCategoryAssociations.first,
                    collectionsAndCollectionCategoryAssociations.second
                )
            }
        }
    }

    suspend fun saveCategoryCollectionsToDb(collectionsUiState: List<CategoryCollectionUiState>) {

        val newCollectionsAndAssociations = collectionsUiState.breakOnCollectionsAndAssociations()
        val originalCollectionsAndAssociations =
            categoryCollectionsUiState.value.breakOnCollectionsAndAssociations()

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


    private val _recordStackList: MutableStateFlow<List<RecordStack>> =
        MutableStateFlow(emptyList())
    val recordStackList: StateFlow<List<RecordStack>> = _recordStackList.asStateFlow()

    fun fetchRecordsFromDbInDateRange(dateRangeState: DateRangeState) {
        viewModelScope.launch {
            recordRepository.getRecordsInDateRange(dateRangeState).collect { recordList ->
                _recordStackList.update { recordList.toRecordStackList() }
            }
        }
    }

    suspend fun saveRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        if (
            uiState.recordStatus != MakeRecordStatus.Edit ||
            uiState.recordNum == null ||
            uiState.recordNum == 0
        ) {
            saveNewRecord(uiState, unitList, unitList.getTotalAmount())
        } else {
            saveEditedRecord(uiState, unitList, unitList.getTotalAmount())
        }
    }

    private suspend fun saveNewRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>,
        thisRecordTotalAmount: Double
    ) {
        uiState.account ?: return

        val recordList = uiState.toRecordList(unitList, appUiSettings.value.lastRecordNum)
        val updatedAccountList = mergeAccountLists(
            listOf(
                uiState.account
                    .cloneAndAddToOrSubtractFromBalance(thisRecordTotalAmount, uiState.type)
            )
        )

        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList, updatedAccountList
            )
        }
    }

    private suspend fun saveEditedRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>,
        thisRecordTotalAmount: Double
    ) {
        val currentRecordStack = recordStackList.value.find {
            it.recordNum == uiState.recordNum
        } ?: return

        if (unitList.size == currentRecordStack.stack.size) {
            updateEditedRecord(
                uiState = uiState,
                newRecordList = uiState.toRecordListWithOldIds(unitList, currentRecordStack),
                recordStack = currentRecordStack,
                thisRecordTotalAmount = thisRecordTotalAmount,
            )
        } else {
            deleteRecordAndSaveNewOne(
                uiState = uiState,
                currentRecordStack = currentRecordStack,
                currentRecordList = currentRecordStack.toRecordList(),
                newRecordList = uiState.toRecordList(unitList, appUiSettings.value.lastRecordNum),
                thisRecordTotalAmount = thisRecordTotalAmount
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

        if (uiState.account.id == recordStack.accountId) {
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
                    newRecordList, updatedAccountList
                )
            }
        } else {
            accountsUiState.value.accountList.findById(recordStack.accountId)?.let { prevAccount ->
                updateEditedRecordDifferentAccounts(
                    recordList = newRecordList,
                    prevAccount = prevAccount,
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
            Pair(prevAccount, newAccount).returnAmountToFirstBalanceAndUpdateSecondBalance(
                prevAmount = prevAmount,
                newAmount = newAmount,
                recordType = recordType
            ).toList()
        )
        viewModelScope.launch {
            recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                recordList, updatedAccountList
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

        if (uiState.account.id == currentRecordStack.accountId) {
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
                    currentRecordList, newRecordList, updatedAccountList
                )
            }
        } else {
            accountsUiState.value.accountList
                .findById(currentRecordStack.accountId)
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
                    accountList = updatedAccountList
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
                recordNum = appUiSettings.value.lastRecordNum + 1,
                dateTimeState = DateTimeState()
            ),
            unitList = unitList,
            thisRecordTotalAmount = unitList.getTotalAmount()
        )
    }

    suspend fun deleteRecord(recordNum: Int) {

        val recordStack = recordStackList.value.findByRecordNum(recordNum) ?: return
        val recordList = recordStack.toRecordList()
        val recordType = recordStack.getRecordType() ?: return

        val account = accountsUiState.value.accountList
            .findById(recordStack.accountId)
            ?.cloneAndAddToOrSubtractFromBalance(recordStack.totalAmount, recordType.inverse())
            ?: return
        val updatedAccountList = mergeAccountLists(listOf(account))

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList
            )
        }
    }

    suspend fun saveTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return
        val madeTransferState =
            uiState.toMadeTransferState(appUiSettings.value.lastRecordNum) ?: return

        val recordListAndUpdatedAccountList = if (
            madeTransferState.recordStatus != MakeRecordStatus.Edit ||
            madeTransferState.recordNum == 0
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
    ): Pair<List<Record>, List<Account>> {
        val recordList = state.toRecordsPair().toList()
        val updatedAccountList = mergeAccountLists(
            listOf(
                state.fromAccount.cloneAndSubtractFromBalance(state.startAmount),
                state.toAccount.cloneAndAddToBalance(state.finalAmount)
            )
        )

        return recordList to updatedAccountList
    }

    private fun getRecordListAndUpdatedAccountListAfterEditedTransfer(
        state: MadeTransferState
    ): Pair<List<Record>, List<Account>>? {
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

        return updatedAccountList?.let { recordList to it }
    }

    fun getUpdatedAccountsAfterEditedTransfer(
        uiState: MadeTransferState,
        currRecordStackFrom: RecordStack,
        currRecordStackTo: RecordStack
    ): List<Account>? {

        val prevAccounts = Pair(
            accountsUiState.value.accountList
                .findById(currRecordStackFrom.accountId) ?: return null,
            accountsUiState.value.accountList
                .findById(currRecordStackTo.accountId) ?: return null
        )

        val updatedPreviousAccounts = returnAmountsToAccountsAfterEditedTransfer(
            prevAccounts = prevAccounts,
            fromAmount = currRecordStackFrom.totalAmount,
            toAmount = currRecordStackTo.totalAmount
        )
        val updatedAccounts = applyAmountsToAccountAfterTransfer(
            state = uiState,
            prevAccounts = updatedPreviousAccounts
        )

        return updatedAccounts?.let { mergeAccountLists(it, updatedPreviousAccounts) }
    }

    private fun returnAmountsToAccountsAfterEditedTransfer(
        prevAccounts: Pair<Account, Account>,
        fromAmount: Double,
        toAmount: Double
    ): List<Account> {
        return listOf(
            prevAccounts.first.cloneAndAddToBalance(fromAmount),
            prevAccounts.second.cloneAndSubtractFromBalance(toAmount)
        )
    }

    private fun applyAmountsToAccountAfterTransfer(
        state: MadeTransferState,
        prevAccounts: List<Account>? = null
    ): List<Account>? {
        val updatedAccounts = mutableListOf<Account>()

        prevAccounts?.findById(state.fromAccount.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.startAmount))
        } ?: accountsUiState.value.accountList.findById(state.fromAccount.id)?.let {
            updatedAccounts.add(it.cloneAndSubtractFromBalance(state.startAmount))
        } ?: return null

        prevAccounts?.findById(state.toAccount.id)?.let {
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
                recordNum = appUiSettings.value.lastRecordNum + 1,
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
        val firstRecordStack = recordStackList.value.findByRecordNum(recordNum) ?: return
        val recordNumDifference = if (firstRecordStack.isOutTransfer()) 1 else -1
        val secondRecordStack = recordStackList.value
            .findByRecordNum(recordNum + recordNumDifference) ?: return

        val recordList = firstRecordStack.toRecordList() + secondRecordStack.toRecordList()

        val updatedAccountList = returnAmountsToAccountsAfterEditedTransfer(
            prevAccounts = Pair(
                accountsUiState.value.accountList.findById(
                    if (firstRecordStack.isOutTransfer()) firstRecordStack.accountId
                    else secondRecordStack.accountId
                ) ?: return,
                accountsUiState.value.accountList.findById(
                    if (firstRecordStack.isOutTransfer()) secondRecordStack.accountId
                    else firstRecordStack.accountId
                ) ?: return,
            ),
            fromAmount = if (firstRecordStack.isOutTransfer()) firstRecordStack.totalAmount
                else secondRecordStack.totalAmount,
            toAmount = if (firstRecordStack.isOutTransfer()) secondRecordStack.totalAmount
                else firstRecordStack.totalAmount
        ).let {
            mergeAccountLists(it)
        }

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList
            )
        }
    }


    private val _greetingsWidgetTitleRes: MutableStateFlow<Int> = MutableStateFlow(
        getGreetingsWidgetTitleRes()
    )

    val widgetsUiState = combine(
        _dateRangeMenuUiState,
        _accountsUiState,
        _recordStackList,
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
                categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                categoryColorNameToColorMap = categoryColorNameToColorMap.value,
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
                    it.accountId == activeAccount?.id
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
        if (accountsUiState.value.activeAccount != null) {
            return recordStackList
                .filter {
                    it.accountId == accountsUiState.value.activeAccount!!.id &&
                            it.date in startDate..endDate && (
                                ((it.type == '-') && type == RecordType.Expense) ||
                                    ((it.type == '>') && type == RecordType.Expense) ||
                                    ((it.type == '+') && type == RecordType.Income) ||
                                    ((it.type == '<') && type == RecordType.Income)
                                )
                }
                .fold(0.0) { total, recordStack ->
                    total + recordStack.totalAmount
                }
        } else {
            return 0.0
        }
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
    val startMainDestination: String = AppScreen.Home.route,
    val startSettingsDestination: String = SettingsScreen.Start.route,
    val langCode: String = AppLanguage.English.languageCode,
    val appTheme: AppTheme? = null,
    val lastRecordNum: Int = 0,
)

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

data class CategoryCollectionUiState(
    val id: Int,
    val orderNum: Int,
    val name: String,
    val categoriesIds: List<Int>?
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

data class CategoryStatisticsLists(
    val expense: List<CategoryStatisticsElementUiState> = emptyList(),
    val income: List<CategoryStatisticsElementUiState> = emptyList()
)

data class CategoriesStatsMapItem(
    val category: Category,
    var totalAmount: Double = 0.0
) {

    fun toCategoryStatisticsElementUiState(
        categoryIconNameToIconResMap: Map<String, Int>,
        categoryColorNameToColorMap: Map<String, Colors>,
        appTheme: AppTheme?,
        accountCurrency: String,
        allCategoriesTotalAmount: Double,
        subcategoriesStatistics: MutableMap<Int, CategoriesStatsMapItem>? = null
    ): CategoryStatisticsElementUiState {
        return CategoryStatisticsElementUiState(
            categoryId = category.id,
            categoryName = category.name,
            categoryIconRes = categoryIconNameToIconResMap[category.iconName],
            categoryColor = categoryColorNameToColorMap[category.colorName]?.lightAndDark ?:
                CategoryColors.GrayDefault(appTheme).color.lightAndDark,
            totalAmount = getFormattedTotalAmount(),
            currency = accountCurrency,
            percentage = getPercentage(allCategoriesTotalAmount),
            subcategoriesStatisticsUiState = subcategoriesStatistics?.values
                ?.sortedByDescending { it.totalAmount }
                ?.map {
                    it.toCategoryStatisticsElementUiState(
                        categoryIconNameToIconResMap = categoryIconNameToIconResMap,
                        categoryColorNameToColorMap = categoryColorNameToColorMap,
                        appTheme = appTheme,
                        accountCurrency = accountCurrency,
                        allCategoriesTotalAmount = totalAmount
                    )
                }
        )
    }

    private fun getFormattedTotalAmount(): String {
        return "%.2f".format(Locale.US, totalAmount)
    }

    private fun getPercentage(allCategoriesTotalAmount: Double): Float {
        return if (allCategoriesTotalAmount != 0.0) {
            ((100 / allCategoriesTotalAmount) * totalAmount).toFloat()
        } else {
            0.0f
        }
    }

}

data class CategoryStatisticsElementUiState(
    val categoryId: Int,
    val categoryName: String,
    val categoryIconRes: Int?,
    val categoryColor: LighterDarkerColors,
    val totalAmount: String,
    val currency: String,
    val percentage: Float,
    val subcategoriesStatisticsUiState: List<CategoryStatisticsElementUiState>? = null
) {
    fun getFormattedPercentage(): String {
        return "%.2f".format(Locale.US, percentage) + "%"
    }
}
