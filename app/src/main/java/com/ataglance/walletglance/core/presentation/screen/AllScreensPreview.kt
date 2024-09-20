package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.color.AccountPossibleColors
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreenPreview
import com.ataglance.walletglance.account.utils.toAccountColorWithName
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.mapper.toBudgetList
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.budget.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.category.domain.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditCategoryScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditSubcategoriesScreenPreview
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreenPreview
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionsScreenPreview
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.YearMonthDay
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.screen.AppearanceScreenPreview
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.record.presentation.screen.RecordsScreenPreview
import com.ataglance.walletglance.record.utils.asChar
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraft
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.domain.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.domain.transfer.TransferDraftSenderReceiver
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreenPreview
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreenPreview
import com.ataglance.walletglance.settings.domain.ThemeUiState
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.SettingsDataScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreenPreview

private val appTheme: AppTheme = AppTheme.LightDefault
private const val langCode: String = "en"
private const val isAppSetUp: Boolean = true
private const val isSetupProgressTopBarVisible: Boolean = false
private const val isBottomBarVisible: Boolean = true

private val accountList = listOf(
    Account(
        id = 1,
        orderNum = 1,
        name = "Czech Local Card",
        currency = "CZK",
        balance = 43551.63,
        color = AccountPossibleColors().pink.toAccountColorWithName(),
        isActive = true
    ),
    Account(
        id = 2,
        orderNum = 2,
        name = "Main USD Card",
        currency = "USD",
        balance = 1516.41,
        color = AccountPossibleColors().blue.toAccountColorWithName(),
        isActive = false
    ),
    Account(
        id = 3,
        orderNum = 3,
        name = "Work Card",
        currency = "USD",
        balance = 412.0,
        color = AccountPossibleColors().camel.toAccountColorWithName(),
        isActive = false
    ),
    Account(
        id = 4,
        orderNum = 4,
        name = "Secondary Card CZK",
        currency = "CZK",
        balance = 5000.0,
        color = AccountPossibleColors().default.toAccountColorWithName(),
        isActive = false
    ),
)
private val accountsAndActiveOne: AccountsAndActiveOne = AccountsAndActiveOne(
    accountList = accountList,
    activeAccount = accountList.find { it.isActive } ?: accountList.first()
)
private val dateRangeMenuUiState = DateRangeEnum.ThisMonth.getDateRangeMenuUiState()
private const val isCustomDateRangeWindowOpened = false
private val recordEntityList = listOf(
    RecordEntity(
        id = 1,
        recordNum = 1,
        date = YearMonthDay(2024, 9, 24).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount!!.id,
        amount = 68.43,
        quantity = null,
        categoryId = 1,
        subcategoryId = 13,
        note = "bread, milk",
        includeInBudgets = true
    ),
    RecordEntity(
        id = 2,
        recordNum = 1,
        date = YearMonthDay(2024, 9, 24).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 178.9,
        quantity = null,
        categoryId = 3,
        subcategoryId = 24,
        note = "shampoo",
        includeInBudgets = true
    ),
    RecordEntity(
        id = 3,
        recordNum = 2,
        date = YearMonthDay(2024, 9, 23).concatenate(),
        type = RecordType.OutTransfer.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 3000.0,
        quantity = null,
        categoryId = 3,
        subcategoryId = 24,
        note = accountsAndActiveOne.accountList[1].id.toString(),
        includeInBudgets = true
    ),
    RecordEntity(
        id = 4,
        recordNum = 3,
        date = YearMonthDay(2024, 9, 18).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 120.9,
        quantity = null,
        categoryId = 6,
        subcategoryId = 40,
        note = "Music platform",
        includeInBudgets = true
    ),
    RecordEntity(
        id = 5,
        recordNum = 4,
        date = YearMonthDay(2024, 9, 15).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 799.9,
        quantity = null,
        categoryId = 3,
        subcategoryId = 21,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 6,
        recordNum = 5,
        date = YearMonthDay(2024, 9, 12).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 3599.9,
        quantity = null,
        categoryId = 1,
        subcategoryId = 13,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 7,
        recordNum = 6,
        date = YearMonthDay(2024, 9, 4).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 8500.0,
        quantity = null,
        categoryId = 2,
        subcategoryId = 15,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 8,
        recordNum = 7,
        date = YearMonthDay(2024, 9, 4).concatenate(),
        type = RecordType.Income.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 42600.0,
        quantity = null,
        categoryId = 72,
        subcategoryId = null,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 9,
        recordNum = 8,
        date = YearMonthDay(2024, 9, 4).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 799.9,
        quantity = null,
        categoryId = 6,
        subcategoryId = 38,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 10,
        recordNum = 9,
        date = YearMonthDay(2024, 6, 4).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.accountList[1].id,
        amount = 450.41,
        quantity = null,
        categoryId = 9,
        subcategoryId = 50,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 10,
        recordNum = 9,
        date = YearMonthDay(2024, 9, 4).concatenate(),
        type = RecordType.Expense.asChar(),
        accountId = accountsAndActiveOne.activeAccount.id,
        amount = 690.56,
        quantity = null,
        categoryId = 10,
        subcategoryId = 58,
        note = null,
        includeInBudgets = true
    ),
)
private val categoryCollectionsWithIdsByType = CategoryCollectionsWithIdsByType(
    mixed = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Mixed,
            name = "Essentials",
            categoriesIds = listOf(13, 14, 25, 30)
        ),
        CategoryCollectionWithIds(
            id = 2,
            orderNum = 2,
            type = CategoryCollectionType.Mixed,
            name = "Other",
            categoriesIds = listOf(15, 16, 21, 30)
        ),
        CategoryCollectionWithIds(
            id = 3,
            orderNum = 3,
            type = CategoryCollectionType.Mixed,
            name = "All stuff",
            categoriesIds = (13..34).toList()
        )
    ),
)
private val budgetEntityList = listOf(
    BudgetEntity(
        id = 1,
        amountLimit = 5000.0,
        categoryId = 1,
        name = "Food & drinks",
        repeatingPeriod = RepeatingPeriod.Monthly.name,
    ),
    BudgetEntity(
        id = 2,
        amountLimit = 1000.0,
        categoryId = 9,
        name = "Travels",
        repeatingPeriod = RepeatingPeriod.Yearly.name,
    ),
    BudgetEntity(
        id = 3,
        amountLimit = 1000.0,
        categoryId = 6,
        name = "Digital life",
        repeatingPeriod = RepeatingPeriod.Monthly.name,
    ),
    BudgetEntity(
        id = 4,
        amountLimit = 1500.0,
        categoryId = 10,
        name = "Entertainment",
        repeatingPeriod = RepeatingPeriod.Monthly.name,
    ),
)
private val budgetAccountAssociationList = listOf(
    BudgetAccountAssociation(budgetId = 1, accountId = 1),
    BudgetAccountAssociation(budgetId = 1, accountId = 4),
    BudgetAccountAssociation(budgetId = 2, accountId = 2),
    BudgetAccountAssociation(budgetId = 3, accountId = 1),
    BudgetAccountAssociation(budgetId = 4, accountId = 1),
)



@Preview(
    name = "HomeScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentHomeScreenPreview() {
    val defaultCategoriesPackage = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    HomeScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountsAndActiveOne = accountsAndActiveOne,
        dateRangeMenuUiState = dateRangeMenuUiState,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        recordList = recordEntityList,
        budgetsOnWidget = budgetEntityList.toBudgetList(
            categoryWithSubcategoriesList = defaultCategoriesPackage.expense,
            associationList = budgetAccountAssociationList,
            accountList = accountsAndActiveOne.accountList
        ).fillUsedAmountsByRecords(recordEntityList).take(1),
        widgetNamesList = listOf(
            WidgetName.ChosenBudgets,
            WidgetName.TotalForPeriod,
        )
    )
}

@Preview(
    name = "RecordsScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentRecordsScreenPreview() {
    val context = LocalContext.current
    val collectionType = CategoryCollectionType.Mixed
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByType(collectionType)

    RecordsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsAndActiveOne.accountList,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        collectionType = collectionType,
        collectionList = categoryCollectionWithIdsList,
        recordList = recordEntityList
    )
}

@Preview(
    name = "CategoryStatisticsScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentCategoryStatisticsScreenPreview() {
    val context = LocalContext.current
    val currentCategoryType = CategoryType.Expense
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByCategoryType(currentCategoryType)

    CategoryStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsAndActiveOne.accountList,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        currentCategoryType = currentCategoryType,
        parentCategory = null,
        collectionList = categoryCollectionWithIdsList,
        selectedCollection = categoryCollectionWithIdsList[0],
        recordEntityList = recordEntityList
    )
}

@Preview(
    name = "BudgetsScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentBudgetsScreenPreview() {
    BudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        budgetEntityList = budgetEntityList,
        budgetAccountAssociationList = budgetAccountAssociationList,
        accountList = accountsAndActiveOne.accountList,
        recordList = recordEntityList
    )
}

@Preview(
    name = "BudgetStatisticsScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentBudgetStatisticsScreenPreview() {
    val defaultCategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
    val budget = budgetEntityList
        .toBudgetList(
            categoryWithSubcategoriesList = defaultCategories.expense,
            associationList = budgetAccountAssociationList,
            accountList = accountList
        )
        .fillUsedAmountsByRecords(recordEntityList)[0]

    BudgetStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        categoriesWithSubcategories = defaultCategories,
        accountList = accountsAndActiveOne.accountList.let { listOf(it[0], it[3]) },
        budget = budget,
        totalAmounts = listOf(4800.0, 5000.0, 4500.0, 5200.0, 4600.0),
    )
}

@Preview(
    name = "FinishSetupScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentFinishSetupScreenPreview() {
    SetupFinishScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    )
}

@Preview(
    name = "RecordCreationScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentRecordCreationScreenPreview() {
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    val recordDraft = RecordDraft(
        general = RecordDraftGeneral(
            isNew = true,
            recordNum = 1,
            account = accountsAndActiveOne.activeAccount,
            type = CategoryType.Expense,
            dateTimeState = DateTimeState()
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
                index = 0,
                categoryWithSubcategory = categoriesWithSubcategories.expense[0]
                    .getWithFirstSubcategory(),
                note = "bread",
                amount = "42.43",
                quantity = "2",
                collapsed = false
            ),
        )
    )

    RecordCreationScreenPreview(
        appTheme = appTheme,
        accountsAndActiveOne = accountsAndActiveOne,
        categoriesWithSubcategories = categoriesWithSubcategories,
        recordDraft = recordDraft
    )
}

@Preview(
    name = "TransferCreationScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentTransferCreationScreenPreview() {
    val accountList = accountsAndActiveOne.accountList
    val transferDraft = TransferDraft(
        isNew = true,
        sender = TransferDraftSenderReceiver(
            account = accountList[0],
            recordNum = 0,
            amount = "3000.0",
            rate = "22.44"
        ),
        receiver = TransferDraftSenderReceiver(
            account = accountList[1],
            recordNum = 0,
            amount = "133.69",
            rate = "1.00"
        ),
        savingIsAllowed = true
    )

    TransferCreationScreenPreview(
        appTheme = appTheme,
        accountList = accountList,
        transferDraft = transferDraft
    )
}



@Preview(
    name = "StartSetupScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentStartSetupScreenPreview() {
    StartSetupScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    )
}

@Preview(
    name = "SettingsHomeScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentSettingHomeScreenPreview() {
    SettingsHomeScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
    )
}

@Preview(
    name = "EditAccountsScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditAccountsScreenPreview() {
    EditAccountsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        accountList = accountsAndActiveOne.accountList
    )
}

@Preview(
    name = "EditAccountScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditAccountScreenPreview() {
    EditAccountScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        account = accountsAndActiveOne.accountList.first()
    )
}

@Preview(
    name = "CurrencyPickerScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentCurrencyPickerScreenPreview() {
    CurrencyPickerScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible
    )
}

@Preview(
    name = "EditBudgetsScreen",
    group = "BudgetsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditBudgetsScreenPreview() {
    EditBudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        budgetEntityList = budgetEntityList,
        budgetAccountAssociationList = budgetAccountAssociationList,
        accountList = accountsAndActiveOne.accountList
    )
}

@Preview(
    name = "EditBudgetScreen",
    group = "BudgetsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditBudgetScreenPreview() {
    EditBudgetScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        accountList = accountsAndActiveOne.accountList,
        budgetEntity = budgetEntityList.first(),
        budgetAccountAssociationList = budgetAccountAssociationList
    )
}

@Preview(
    name = "EditCategoriesScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditCategoriesScreenPreview() {
    EditCategoriesScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        categoryType = CategoryType.Expense
    )
}

@Preview(
    name = "EditSubcategoriesScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditSubcategoriesScreenPreview() {
    EditSubcategoriesScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    )
}

@Preview(
    name = "EditCategoryScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditCategoryScreenPreview() {
    EditCategoryScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    )
}

@Preview(
    name = "EditCategoryCollectionsScreen",
    group = "CollectionsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditCategoryCollectionsScreenPreview() {
    EditCategoryCollectionsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        collectionType = CategoryCollectionType.Mixed,
        categoryCollectionsWithIdsByType = categoryCollectionsWithIdsByType,
    )
}

@Preview(
    name = "EditCategoryCollectionScreen",
    group = "CollectionsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentEditCategoryCollectionScreenPreview() {
    EditCategoryCollectionScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        collectionWithIds = categoryCollectionsWithIdsByType.mixed.first()
    )
}

@Preview(
    name = "AppearanceScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentAppearanceScreenPreview() {
    AppearanceScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        themeUiState = ThemeUiState(
            useDeviceTheme = true,
            chosenLightTheme = AppTheme.LightDefault.name,
            chosenDarkTheme = AppTheme.DarkDefault.name,
            lastChosenTheme = appTheme.name
        )
    )
}

@Preview(
    name = "LanguageScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentLanguageScreenPreview() {
    LanguageScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        appLanguage = AppLanguage.English.languageCode,
        selectedLanguage = AppLanguage.German.languageCode
    )
}

@Preview(
    name = "SettingsDataScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = Devices.PIXEL_7_PRO
)
@Composable
fun MainAppContentSettingsDataScreenPreview() {
    SettingsDataScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
    )
}
