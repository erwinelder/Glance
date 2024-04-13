package com.ataglance.walletglance.model

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.AccountRepository
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.data.CategoryRepository
import com.ataglance.walletglance.data.GeneralRepository
import com.ataglance.walletglance.data.Record
import com.ataglance.walletglance.data.RecordAndAccountRepository
import com.ataglance.walletglance.data.RecordRepository
import com.ataglance.walletglance.data.SettingsRepository
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.theme.LighterDarkerColors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Locale

class AppViewModel(
    private val settingsRepository: SettingsRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val recordRepository: RecordRepository,
    private val recordAndAccountRepository: RecordAndAccountRepository,
    private val generalRepository: GeneralRepository
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

    fun setLanguage(langCode: String, context: Context) {
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


    private val _accountsUiState: MutableStateFlow<AccountsUiState> = MutableStateFlow(AccountsUiState())
    val accountsUiState: StateFlow<AccountsUiState> = _accountsUiState.asStateFlow()

    private fun fetchAccountsFromDb() {
        viewModelScope.launch {
            accountRepository.getAllAccounts().collect { accountsList ->
                applyAccountListToUiState(accountsList)
            }
        }
    }

    fun applyAccountListToUiState(accountsList: List<Account>) {
        _accountsUiState.update { uiState ->
            uiState.copy(
                accountList = accountsList,
                activeAccount = accountsList.takeIf { it.isNotEmpty() }?.firstOrNull { it.isActive }
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

    private fun isAccountIdInList(id: Int, accountList: List<Account>): Boolean {
        accountList.forEach { account ->
            if (account.id == id) {
                return true
            }
        }
        return false
    }

    private fun deleteAccountsByIds(originalList: List<Account>, newList: List<Account>): List<Int> {
        val listOfIdsToDelete = mutableListOf<Int>()

        originalList.forEach { account ->
            if (!isAccountIdInList(account.id, newList)) {
                listOfIdsToDelete.add(account.id)
            }
        }

        return listOfIdsToDelete
    }

    suspend fun saveAccountsToDb(accountsList: List<Account>) {
        val listOfIdsToDelete = deleteAccountsByIds(accountsUiState.value.accountList, accountsList)

        if (listOfIdsToDelete.isEmpty()) {
            viewModelScope.launch {
                accountRepository.upsertAccounts(accountsList)
            }
        } else {
            viewModelScope.launch {
                accountRepository.deleteAndUpsertAccounts(listOfIdsToDelete, accountsList)
            }
        }
    }

    suspend fun deleteAccountWithItsRecords(accountId: Int, updatedAccountList: List<Account>) {
        viewModelScope.launch {

            val accountTransferList = recordRepository.getTransfersByAccountId(accountId).first()
            val transferSecondUnitsNumbers = getTransferSecondUnitRecordNumbers(accountTransferList)
                .takeIf { it.isNotEmpty() }

            val convertedTransfers = transferSecondUnitsNumbers?.let { transfers ->
                val recordList = recordRepository.getRecordsByRecordNumbers(transfers).first()
                RecordController().convertTransfersToRecordsWithCategoryTransfer(recordList)
            } ?: emptyList()

            recordAndAccountRepository.deleteAccountAndUpdateAccountsAndDeleteRecordsByAccountIdAndUpdateRecords(
                accountIdToDelete = accountId,
                accountListToUpsert = updatedAccountList,
                recordListToUpsert = convertedTransfers
            )

        }
    }

    fun chooseNewActiveAccount(accountOrderNum: Int) {
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
        /*updateWidgetsStatistic(recordStackList.value)*/
    }

    private fun getTransferSecondUnitRecordNumbers(transferList: List<Record>): List<Int> {
        val recordNumbersList = mutableListOf<Int>()

        transferList.forEach { record ->
            if (record.type == '>') {
                recordNumbersList.add(record.recordNum + 1)
            } else {
                recordNumbersList.add(record.recordNum - 1)
            }
        }

        return recordNumbersList
    }

    private val _categoriesUiState: MutableStateFlow<CategoriesUiState> = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _categoriesUiState.asStateFlow()

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
        CategoryIcon.EntertainmentSubs.name to CategoryIcon.EntertainmentSubs.res,
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

                _categoriesUiState.update {
                    CategoryController().breakCategoriesOnDifferentLists(categoryList)
                }
            }
        }
    }

    private fun isCategoryIdInList(id: Int, categoryList: List<Category>): Boolean {
        categoryList.forEach { category ->
            if (category.id == id) {
                return true
            }
        }
        return false
    }

    private fun getCategoriesIdsToDelete(originalList: List<Category>, newList: List<Category>): List<Int> {
        val listOfIdsToDelete = mutableListOf<Int>()

        originalList.forEach { category ->
            if (!isCategoryIdInList(category.id, newList)) {
                listOfIdsToDelete.add(category.id)
            }
        }

        return listOfIdsToDelete
    }

    suspend fun saveCategoriesToDb(categoryList: List<Category>) {
        val listOfIdsToDelete = getCategoriesIdsToDelete(
            originalList = CategoryController().concatenateCategoryLists(
                categoriesUiState.value.parentCategories,
                categoriesUiState.value.subcategories
            ),
            newList = categoryList
        )

        if (listOfIdsToDelete.isEmpty()) {
            viewModelScope.launch {
                categoryRepository.upsertCategories(categoryList)
            }
        } else {
            viewModelScope.launch {
                categoryRepository.deleteAndUpsertCategories(listOfIdsToDelete, categoryList)
            }
        }
    }

    private fun getCategoryPairByIds(parentId: Int, subId: Int?, type: CategoryType): Pair<Category, Category?>? {

        val parentCategory = CategoryController().getParCategoryFromList(
            parentId,
            if (type == CategoryType.Expense) {
                categoriesUiState.value.parentCategories.expense
            } else {
                categoriesUiState.value.parentCategories.income
            }
        )

        if (parentCategory != null) {

            val subcategory = if (subId != null) {
                CategoryController().getSubcategByParCategOrderNumFromLists(
                    subId,
                    parentCategory.orderNum,
                    if (type == CategoryType.Expense) {
                        categoriesUiState.value.subcategories.expense
                    } else {
                        categoriesUiState.value.subcategories.income
                    }
                )
            } else {
                null
            }

            return parentCategory to subcategory
        }

        return null
    }

    fun getLastRecordCategory(accountId: Int, type: CategoryType): Pair<Category, Category?> {
        val categoryIdsPair = RecordController().getLastUsedCategoryPairByAccountId(accountId, recordStackList.value)
        if (categoryIdsPair != null) {
            val categoryPair = getCategoryPairByIds(categoryIdsPair.first, categoryIdsPair.second, type)
            if (categoryPair != null) {
                return categoryPair
            }
        }

        if (recordStackList.value.isNotEmpty()) {
            val categoryPair = recordStackList.value.firstOrNull()?.stack?.firstOrNull()?.let {
                getCategoryPairByIds(it.categoryId, it.subcategoryId, type)
            }
            if (categoryPair != null) {
                return categoryPair
            }
        }

        return if (type == CategoryType.Expense) {
            val parentCategory = categoriesUiState.value.parentCategories.expense.last()
            val subcategory = if (parentCategory.parentCategoryId != null) {
                categoriesUiState.value.subcategories.expense.last().last()
            } else null
            parentCategory to subcategory
        } else {
            val parentCategory = categoriesUiState.value.parentCategories.income.last()
            val subcategory = if (parentCategory.parentCategoryId != null) {
                categoriesUiState.value.subcategories.income.last().last()
            } else null
            parentCategory to subcategory
        }
    }

    private val _dateRangeMenuUiState: MutableStateFlow<DateRangeMenuUiState> =
        MutableStateFlow(
            DateRangeMenuUiState(
                startCalendarDateMillis = DateRangeController().getCalendarStartLongByEnum(
                    DateRangeEnum.ThisMonth
                ),
                endCalendarDateMillis = DateRangeController().getCalendarEndLongByEnum(
                    DateRangeEnum.ThisMonth
                ),
                dateRangeState = DateRangeController().getDateRangeStateByEnum(
                    DateRangeEnum.ThisMonth
                )
            )
        )
    val dateRangeMenuUiState: StateFlow<DateRangeMenuUiState> = _dateRangeMenuUiState.asStateFlow()

    fun changeDateRange(dateRangeEnum: DateRangeEnum) {
        _dateRangeMenuUiState.update {
            DateRangeMenuUiState(
                startCalendarDateMillis = DateRangeController().getCalendarStartLongByEnum(
                    dateRangeEnum, dateRangeMenuUiState.value.dateRangeState
                ),
                endCalendarDateMillis = DateRangeController().getCalendarEndLongByEnum(
                    dateRangeEnum, dateRangeMenuUiState.value.dateRangeState
                ),
                dateRangeState = DateRangeController().getDateRangeStateByEnum(
                    dateRangeEnum, dateRangeMenuUiState.value.dateRangeState
                )
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
                    fromPast = DateRangeController()
                        .convertCalendarMillisToLongWithoutSpecificTime(pastDateMillis),
                    toFuture = DateRangeController()
                        .convertCalendarMillisToLongWithoutSpecificTime(futureDateMillis) + 2359
                )
            )
        }
        fetchRecordsFromDbInDateRange(dateRangeMenuUiState.value.dateRangeState)
    }


    private val _recordStackList: MutableStateFlow<List<RecordStack>> = MutableStateFlow(emptyList())
    val recordStackList: StateFlow<List<RecordStack>> = _recordStackList.asStateFlow()

    private fun fetchRecordsFromDbInDateRange(dateRangeState: DateRangeState) {
        viewModelScope.launch {
            recordRepository.getRecordsInDateRange(dateRangeState).collect { recordList ->
                val recordStackList =
                    RecordController().convertRecordListToRecordStackList(recordList)
                _recordStackList.update { recordStackList }
            }
        }
    }

    private fun getRecordTotalAmount(unitList: List<MakeRecordUnitUiState>): Double {
        return unitList.fold(0.0) { total, recordUnit ->
            total + (recordUnit.amount.toDouble() * recordUnit.quantity.ifBlank { "1" }.toInt())
        }
    }

    suspend fun saveRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>
    ) {
        val thisRecordTotalAmount = getRecordTotalAmount(unitList)

        if (
            uiState.recordStatus != MakeRecordStatus.Edit ||
            uiState.recordNum == null ||
            uiState.recordNum == 0
        ) {
            saveNewRecord(uiState, unitList, thisRecordTotalAmount)
        } else {
            saveEditedRecord(uiState, unitList, thisRecordTotalAmount)
        }
    }

    private suspend fun saveNewRecord(
        uiState: MakeRecordUiState,
        unitList: List<MakeRecordUnitUiState>,
        thisRecordTotalAmount: Double
    ) {
        uiState.account ?: return

        val recordList = uiState.toRecordList(unitList, appUiSettings.value.lastRecordNum)
        val updatedAccountList = mergeAccountsList(
            listOf(
                uiState.account.copy(
                    balance = AccountController().addToOrSubtractFromBalance(
                        balance = uiState.account.balance,
                        amount = thisRecordTotalAmount,
                        recordType = uiState.type
                    )
                )
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
            val updatedAccountList = mergeAccountsList(
                listOf(
                    uiState.account.copy(
                        balance = AccountController().reapplyAmountToBalance(
                            balance = uiState.account.balance,
                            prevAmount = recordStack.totalAmount,
                            newAmount = thisRecordTotalAmount,
                            recordType = uiState.type
                        )
                    )
                )
            )
            viewModelScope.launch {
                recordAndAccountRepository.upsertRecordsAndUpdateAccounts(
                    newRecordList, updatedAccountList
                )
            }
        } else {
            AccountController().getAccountById(
                recordStack.accountId, accountsUiState.value.accountList
            )?.let { prevAccount ->
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
        val balances = AccountController().returnAmountToOneBalanceAndUpdateAnotherBalance(
            balancePrevAccount = prevAccount.balance,
            balanceNewAccount = newAccount.balance,
            prevAmount = prevAmount,
            newAmount = newAmount,
            recordType = recordType
        )
        val updatedAccountList = mergeAccountsList(
            listOf(
                prevAccount.copy(balance = balances.first),
                newAccount.copy(balance = balances.second)
            )
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
            val updatedAccountList = mergeAccountsList(
                listOf(
                    uiState.account.copy(
                        balance = AccountController().reapplyAmountToBalance(
                            balance = uiState.account.balance,
                            prevAmount = currentRecordStack.totalAmount,
                            newAmount = thisRecordTotalAmount,
                            recordType = uiState.type
                        )
                    )
                )
            )
            viewModelScope.launch {
                recordAndAccountRepository.deleteAndUpsertRecordsAndUpdateAccounts(
                    currentRecordList, newRecordList, updatedAccountList
                )
            }
        } else {
            AccountController().getAccountById(
                currentRecordStack.accountId, accountsUiState.value.accountList
            )?.let { prevAccount ->
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
        val balances = AccountController().returnAmountToOneBalanceAndUpdateAnotherBalance(
            balancePrevAccount = prevAccount.balance,
            balanceNewAccount = newAccount.balance,
            prevAmount = prevAmount,
            newAmount = newAmount,
            recordType = recordType
        )
        val updatedAccountList = mergeAccountsList(
            listOf(
                prevAccount.copy(balance = balances.first),
                newAccount.copy(balance = balances.second)
            )
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
        val thisRecordTotalAmount = getRecordTotalAmount(unitList)

        saveNewRecord(
            uiState = uiState.copy(
                recordNum = appUiSettings.value.lastRecordNum + 1,
                dateTimeState = DateTimeState()
            ),
            unitList = unitList,
            thisRecordTotalAmount = thisRecordTotalAmount
        )
    }

    suspend fun deleteRecord(recordNum: Int) {

        val recordStack = recordStackList.value.find { it.recordNum == recordNum } ?: return
        val recordList = recordStack.toRecordList()
        val recordType = recordStack.getRecordType() ?: return

        val account = AccountController().getAccountById(
            recordStack.accountId, accountsUiState.value.accountList
        )?.let {
            it.copy(
                balance = AccountController().addToOrSubtractFromBalance(
                    balance = it.balance,
                    amount = recordStack.totalAmount,
                    recordType = if (recordType == RecordType.Expense) RecordType.Income else RecordType.Expense
                )
            )
        } ?: return
        val updatedAccountList = mergeAccountsList(listOf(account))

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList
            )
        }
    }

    private fun MakeTransferUiState.toMadeTransferState(): MadeTransferState? {
        if (this.fromAccount == null || this.toAccount == null) return null

        return MadeTransferState(
            idFrom = this.idFrom,
            idTo = this.idTo,
            recordStatus = this.recordStatus,
            fromAccount = this.fromAccount,
            toAccount = this.toAccount,
            startAmount = this.startAmount.toDouble(),
            finalAmount = this.finalAmount.toDouble(),
            dateTimeState = this.dateTimeState,
            recordNum = this.recordNum ?: (appUiSettings.value.lastRecordNum + 1)
        )
    }

    suspend fun saveTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return
        val madeTransferState = uiState.toMadeTransferState() ?: return

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
        val updatedAccountList = mergeAccountsList(
            listOf(
                state.fromAccount.copy(
                    balance = AccountController().subtractFromBalance(
                        balance = state.fromAccount.balance,
                        amount = state.startAmount
                    )
                ),
                state.toAccount.copy(
                    balance = AccountController().addToBalance(
                        balance = state.toAccount.balance,
                        amount = state.finalAmount
                    )
                )
            )
        )

        return recordList to updatedAccountList
    }

    private fun getRecordListAndUpdatedAccountListAfterEditedTransfer(
        state: MadeTransferState
    ): Pair<List<Record>, List<Account>>? {
        val firstRecordStack = recordStackList.value.find {
            it.recordNum == state.recordNum
        } ?: return null
        val recordNumDifference = if (firstRecordStack.isOutTransfer()) 1 else -1
        val secondRecordStack = recordStackList.value.find {
            it.recordNum == state.recordNum.plus(recordNumDifference)
        } ?: return null

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
            mergeAccountsList(it)
        }

        return updatedAccountList?.let {
            recordList to updatedAccountList
        }
    }

    fun getUpdatedAccountsAfterEditedTransfer(
        uiState: MadeTransferState,
        currRecordStackFrom: RecordStack,
        currRecordStackTo: RecordStack
    ): List<Account>? {

        val prevAccounts = Pair(
            AccountController().getAccountById(
                currRecordStackFrom.accountId, accountsUiState.value.accountList
            ) ?: return null,
            AccountController().getAccountById(
                currRecordStackTo.accountId, accountsUiState.value.accountList
            ) ?: return null
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
        updatedAccounts ?: return null

        return mergeAccountsList(updatedAccounts, updatedPreviousAccounts)
    }

    private fun returnAmountsToAccountsAfterEditedTransfer(
        prevAccounts: Pair<Account, Account>,
        fromAmount: Double,
        toAmount: Double
    ): List<Account> {
        return listOf(
            prevAccounts.first.copy(
                balance = AccountController().addToBalance(
                    balance = prevAccounts.first.balance,
                    amount = fromAmount
                )
            ),
            prevAccounts.second.copy(
                balance = AccountController().subtractFromBalance(
                    balance = prevAccounts.second.balance,
                    amount = toAmount
                )
            )
        )
    }

    private fun applyAmountsToAccountAfterTransfer(
        state: MadeTransferState,
        prevAccounts: List<Account>? = null
    ): List<Account>? {
        val updatedAccounts = mutableListOf<Account>()

        prevAccounts?.find { it.id == state.fromAccount.id }?.let {
            updatedAccounts.add(
                it.copy(
                    balance = AccountController().subtractFromBalance(it.balance, state.startAmount)
                )
            )
        } ?: accountsUiState.value.accountList.find { it.id == state.fromAccount.id }?.let {
            updatedAccounts.add(
                it.copy(
                    balance = AccountController().subtractFromBalance(it.balance, state.startAmount)
                )
            )
        } ?: return null

        prevAccounts?.find { it.id == state.toAccount.id }?.let {
            updatedAccounts.add(
                it.copy(
                    balance = AccountController().addToBalance(it.balance, state.finalAmount)
                )
            )
        } ?: accountsUiState.value.accountList.find { it.id == state.toAccount.id }?.let {
            updatedAccounts.add(
                it.copy(
                    balance = AccountController().addToBalance(it.balance, state.finalAmount)
                )
            )
        } ?: return null

        return updatedAccounts
    }

    private fun mergeAccountsList(
        primaryList: List<Account>,
        secondaryList: List<Account> = accountsUiState.value.accountList
    ): List<Account> {
        val mergedList = mutableListOf<Account>()

        primaryList.forEach { accountFromPrimaryList ->
            mergedList.add(accountFromPrimaryList)
        }
        secondaryList.forEach { accountFromSecondaryList ->
            if (primaryList.find { it.id == accountFromSecondaryList.id } == null) {
                mergedList.add(accountFromSecondaryList)
            }
        }

        return mergedList
    }

    suspend fun repeatTransfer(uiState: MakeTransferUiState) {
        if (uiState.fromAccount == uiState.toAccount) return

        val madeTransferState = uiState.toMadeTransferState()?.copy(
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
        val firstRecordStack = recordStackList.value.find {
            it.recordNum == recordNum
        } ?: return
        val recordNumDifference = if (firstRecordStack.isOutTransfer()) 1 else -1
        val secondRecordStack = recordStackList.value.find {
            it.recordNum == recordNum + recordNumDifference
        } ?: return

        val recordList = firstRecordStack.toRecordList() + secondRecordStack.toRecordList()

        val updatedAccountList = returnAmountsToAccountsAfterEditedTransfer(
            prevAccounts = Pair(
                AccountController().getAccountById(
                    id = if (firstRecordStack.isOutTransfer()) firstRecordStack.accountId
                        else secondRecordStack.accountId,
                    accountList = accountsUiState.value.accountList
                ) ?: return,
                AccountController().getAccountById(
                    id = if (firstRecordStack.isOutTransfer()) secondRecordStack.accountId
                        else firstRecordStack.accountId,
                    accountList = accountsUiState.value.accountList
                ) ?: return
            ),
            fromAmount = if (firstRecordStack.isOutTransfer()) firstRecordStack.totalAmount
                else secondRecordStack.totalAmount,
            toAmount = if (firstRecordStack.isOutTransfer()) secondRecordStack.totalAmount
                else firstRecordStack.totalAmount
        ).let { accountList ->
            mergeAccountsList(accountList)
        }

        viewModelScope.launch {
            recordAndAccountRepository.deleteRecordsAndUpdateAccounts(
                recordList, updatedAccountList
            )
        }
    }


    val widgetsUiState = combine(
        _dateRangeMenuUiState,
        _accountsUiState,
        _recordStackList,
        categoriesUiState,
        _appTheme
    ) { dateRangeMenuUiState, accountsUiState, recordStackList, categoriesUiState, appTheme ->
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
        val filteredRecordStackList = recordStackList.filter {
            it.date in dateRangeMenuUiState.dateRangeState.fromPast..dateRangeMenuUiState.dateRangeState.toFuture &&
                    it.accountId == accountsUiState.activeAccount?.id
        }
        WidgetsUiState(
            filteredRecordStackList = filteredRecordStackList,
            greetings = GreetingsWidgetUiState(
                titleRes = getGreetingsWidgetTitleRes(),
                expensesTotal = getRecordsTotalAmount(
                    recordStackList = recordStackList,
                    startDate = DateRangeController().getTodayDateLong(),
                    endDate = DateRangeController().getTodayDateLong() + 2359,
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
            categoryStatisticsLists = CategoryController().getCategoriesStatistics(
                recordStackList = filteredRecordStackList,
                parentCategoriesLists = categoriesUiState.parentCategories,
                subcategoriesLists = categoriesUiState.subcategories,
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
    val activeAccount: Account? = Account()
)

data class CategoriesUiState(
    val parentCategories: ParentCategoriesLists = ParentCategoriesLists(),
    val subcategories: SubcategoriesLists = SubcategoriesLists()
) {
    fun getParCategoryListAndSubcategoryListsByType(
        type: RecordType?
    ): Pair<List<Category>, List<List<Category>>> {
        return if (type == RecordType.Expense) {
            parentCategories.expense to subcategories.expense
        } else {
            parentCategories.income to subcategories.income
        }
    }
}

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
