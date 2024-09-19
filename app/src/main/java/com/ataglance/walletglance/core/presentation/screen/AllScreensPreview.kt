package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.account.domain.AccountsUiState
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
import com.ataglance.walletglance.core.utils.getDateRangeMenuUiState
import com.ataglance.walletglance.core.utils.getTodayDateLong
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
private val accountsUiState: AccountsUiState = AccountsUiState(
    accountList = listOf(
        Account(
            id = 1,
            orderNum = 1,
            name = "Main USD Card",
            currency = "USD",
            balance = 1516.41,
            color = AccountPossibleColors().pink.toAccountColorWithName(),
            isActive = true
        ),
        Account(
            id = 2,
            orderNum = 2,
            name = "Czech Local Card",
            currency = "CZK",
            balance = 43551.63,
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
    ),
    activeAccount = Account(
        id = 1,
        orderNum = 1,
        name = "Main USD Card",
        currency = "USD",
        balance = 1516.41,
        color = AccountPossibleColors().pink.toAccountColorWithName(),
        isActive = true
    )
)
private val dateRangeMenuUiState = DateRangeEnum.ThisMonth.getDateRangeMenuUiState()
private const val isCustomDateRangeWindowOpened = false
private val recordEntityList = listOf(
    RecordEntity(
        id = 1,
        recordNum = 1,
        date = getTodayDateLong(),
        type = RecordType.Expense.asChar(),
        accountId = 1,
        amount = 42.43,
        quantity = null,
        categoryId = 1,
        subcategoryId = 13,
        note = null,
        includeInBudgets = true
    ),
    RecordEntity(
        id = 2,
        recordNum = 2,
        date = getTodayDateLong(),
        type = RecordType.Expense.asChar(),
        accountId = 1,
        amount = 42.43,
        quantity = null,
        categoryId = 1,
        subcategoryId = 14,
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
        amountLimit = 1000.0,
        categoryId = 1,
        name = "Food & drinks",
        repeatingPeriod = RepeatingPeriod.Monthly.name,
    ),
    BudgetEntity(
        id = 2,
        amountLimit = 250.0,
        categoryId = 3,
        name = "Shopping",
        repeatingPeriod = RepeatingPeriod.Weekly.name,
    ),
    BudgetEntity(
        id = 3,
        amountLimit = 2000.0,
        categoryId = 2,
        name = "Housing",
        repeatingPeriod = RepeatingPeriod.Yearly.name,
    ),
)
private val budgetAccountAssociationList = listOf(
    BudgetAccountAssociation(budgetId = 1, accountId = 1),
    BudgetAccountAssociation(budgetId = 1, accountId = 3),
    BudgetAccountAssociation(budgetId = 2, accountId = 2),
    BudgetAccountAssociation(budgetId = 3, accountId = 1),
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
        accountsUiState = accountsUiState,
        dateRangeMenuUiState = dateRangeMenuUiState,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        recordList = recordEntityList,
        budgetsOnWidget = budgetEntityList.toBudgetList(
            categoryWithSubcategoriesList = defaultCategoriesPackage.expense,
            associationList = budgetAccountAssociationList,
            accountList = accountsUiState.accountList
        ).take(2),
        widgetNamesList = listOf(
            WidgetName.TotalForPeriod,
            WidgetName.ChosenBudgets,
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
        accountList = accountsUiState.accountList,
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
        accountList = accountsUiState.accountList,
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
        accountList = accountsUiState.accountList,
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
    BudgetStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsUiState.accountList
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
            account = accountsUiState.activeAccount,
            type = CategoryType.Expense,
            dateTimeState = DateTimeState()
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
                index = 0,
                categoryWithSubcategory = categoriesWithSubcategories.expense[0]
                    .getWithFirstSubcategory(),
                note = "",
                amount = "42.43",
                quantity = "2",
                collapsed = false
            )
        )
    )

    RecordCreationScreenPreview(
        appTheme = appTheme,
        accountsUiState = accountsUiState,
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
    val accountList = accountsUiState.accountList
    val transferDraft = TransferDraft(
        isNew = true,
        sender = TransferDraftSenderReceiver(
            account = accountList[0],
            recordNum = 0,
            amount = "300.0",
        ),
        receiver = TransferDraftSenderReceiver(
            account = accountList[1],
            recordNum = 0,
            amount = "100.0",
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
        accountList = accountsUiState.accountList
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
        account = accountsUiState.accountList.first()
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
        accountList = accountsUiState.accountList
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
        accountList = accountsUiState.accountList,
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
        isBottomBarVisible = isBottomBarVisible,
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
