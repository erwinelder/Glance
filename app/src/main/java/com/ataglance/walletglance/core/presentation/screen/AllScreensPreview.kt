package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreenPreview
import com.ataglance.walletglance.auth.domain.model.AuthResultSuccessScreenType
import com.ataglance.walletglance.auth.presentation.screen.DeleteAccountScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.EmailVerificationErrorScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.SignInScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.UpdateEmailScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreenPreview
import com.ataglance.walletglance.budget.data.local.model.BudgetAccountAssociation
import com.ataglance.walletglance.budget.data.local.model.BudgetEntity
import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByRecords
import com.ataglance.walletglance.budget.mapper.budget.toDomainModels
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.screen.CategoryStatisticsScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditCategoryScreenPreview
import com.ataglance.walletglance.category.presentation.screen.EditSubcategoriesScreenPreview
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionScreenPreview
import com.ataglance.walletglance.categoryCollection.presentation.screen.EditCategoryCollectionsScreenPreview
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.domain.date.YearMonthDay
import com.ataglance.walletglance.errorHandling.presentation.screen.AuthResultSuccessScreenPreview
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.screen.PersonalisationScreenPreview
import com.ataglance.walletglance.record.data.local.model.RecordEntity
import com.ataglance.walletglance.record.domain.model.RecordType
import com.ataglance.walletglance.record.domain.utils.asChar
import com.ataglance.walletglance.record.mapper.toDomainModels
import com.ataglance.walletglance.record.presentation.screen.RecordsScreenPreview
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraft
import com.ataglance.walletglance.recordCreation.presentation.model.transfer.TransferDraftUnits
import com.ataglance.walletglance.recordCreation.presentation.screen.RecordCreationScreenPreview
import com.ataglance.walletglance.recordCreation.presentation.screen.TransferCreationScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.ResetDataScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreenPreview

private const val device = "spec:width=1440px,height=3120px,dpi=560" // default
//private const val device = "spec:width=1440px,height=2988px,dpi=560" // phone promo
//private const val device = "spec:width=1840px,height=2150px,dpi=400" // tablet 7inch promo
//private const val device = "spec:width=2560px,height=1561px,dpi=320" // tablet 10inch promo

private val appTheme: AppTheme = AppTheme.LightDefault
private const val langCode: String = "en"
private const val isAppSetUp: Boolean = true
private const val isBottomBarVisible: Boolean = true

private val accountList = listOf(
    Account(
        id = 1,
        orderNum = 1,
        name = "Czech Local Card",
        currency = "CZK",
        balance = 43551.63,
        color = AccountColors.Pink,
        isActive = true
    ),
    Account(
        id = 2,
        orderNum = 2,
        name = "Main USD Card",
        currency = "USD",
        balance = 1516.41,
        color = AccountColors.Blue,
        isActive = false
    ),
    Account(
        id = 3,
        orderNum = 3,
        name = "Work Card",
        currency = "USD",
        balance = 412.0,
        color = AccountColors.Camel,
        isActive = false
    ),
    Account(
        id = 4,
        orderNum = 4,
        name = "Secondary Card CZK",
        currency = "CZK",
        balance = 5000.0,
        color = AccountColors.Default,
        isActive = false
    ),
)
private val accountsAndActiveOne: AccountsAndActiveOne = AccountsAndActiveOne(
    accounts = accountList,
    activeAccount = accountList.find { it.isActive } ?: accountList.first()
)
private val dateRangeMenuUiState = DateRangeMenuUiState.fromEnum(DateRangeEnum.ThisMonth)
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
        note = accountsAndActiveOne.accounts[1].id.toString(),
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
        accountId = accountsAndActiveOne.accounts[1].id,
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
    device = device
)
@Composable
fun MainAppContentHomeScreenPreview() {
    val defaultCategoriesPackage = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    HomeScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible,
        accountsAndActiveOne = accountsAndActiveOne,
        dateRangeMenuUiState = dateRangeMenuUiState,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        recordList = recordEntityList,
        budgetsOnWidget = budgetEntityList.toDomainModels(
            groupedCategoriesList = defaultCategoriesPackage.expense,
            associations = budgetAccountAssociationList,
            accounts = accountsAndActiveOne.accounts
        ).fillUsedAmountsByRecords(recordEntityList.toDomainModels()).take(1),
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
    device = device
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
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsAndActiveOne.accounts,
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
    device = device
)
@Composable
fun MainAppContentCategoryStatisticsScreenPreview() {
    val context = LocalContext.current
    val currentCollectionType = CategoryCollectionType.Expense
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByType(currentCollectionType)

    CategoryStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible,
        accountList = accountsAndActiveOne.accounts,
        currentDateRangeEnum = dateRangeMenuUiState.dateRangeWithEnum.enum,
        currentCollectionType = currentCollectionType,
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
    device = device
)
@Composable
fun MainAppContentBudgetsScreenPreview() {
    BudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible,
        budgetEntityList = budgetEntityList,
        budgetAccountAssociationList = budgetAccountAssociationList,
        accountList = accountsAndActiveOne.accounts,
        recordList = recordEntityList
    )
}

@Preview(
    name = "BudgetStatisticsScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentBudgetStatisticsScreenPreview() {
    val defaultCategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
    val budget = budgetEntityList
        .toDomainModels(
            groupedCategoriesList = defaultCategories.expense,
            associations = budgetAccountAssociationList,
            accounts = accountList
        )
        .fillUsedAmountsByRecords(recordEntityList.toDomainModels())[0]

    BudgetStatisticsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible,
        groupedCategoriesByType = defaultCategories,
        accountList = accountsAndActiveOne.accounts.let { listOf(it[0], it[3]) },
        budget = budget,
        totalAmounts = listOf(4800.0, 5000.0, 4500.0, 5200.0, 4600.0),
    )
}

@Preview(
    name = "FinishSetupScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentFinishSetupScreenPreview() {
    SetupFinishScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
    )
}

@Preview(
    name = "RecordCreationScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
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
            dateTimeState = DateTimeState.fromCurrentTime()
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
                index = 0,
                categoryWithSub = categoriesWithSubcategories.expense[0]
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
        groupedCategoriesByType = categoriesWithSubcategories,
        recordDraft = recordDraft
    )
}

@Preview(
    name = "TransferCreationScreen",
    group = "MainScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentTransferCreationScreenPreview() {
    val accountList = accountsAndActiveOne.accounts
    val transferDraft = TransferDraft(
        isNew = true,
        sender = TransferDraftUnits(
            account = accountList[0],
            recordNum = 0,
            amount = "3000.0",
            rate = "22.44"
        ),
        receiver = TransferDraftUnits(
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
    device = device
)
@Composable
fun MainAppContentStartSetupScreenPreview() {
    StartSetupScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
    )
}

@Preview(
    name = "SettingsHomeScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentSettingHomeScreenPreview() {
    SettingsHomeScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible,
    )
}

@Preview(
    name = "EditAccountsScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditAccountsScreenPreview() {
    EditAccountsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        accountList = accountsAndActiveOne.accounts
    )
}

@Preview(
    name = "EditAccountScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditAccountScreenPreview() {
    EditAccountScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        account = accountsAndActiveOne.accounts.first()
    )
}

@Preview(
    name = "CurrencyPickerScreen",
    group = "AccountsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentCurrencyPickerScreenPreview() {
    CurrencyPickerScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
    )
}

@Preview(
    name = "EditBudgetsScreen",
    group = "BudgetsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditBudgetsScreenPreview() {
    EditBudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        budgetEntityList = budgetEntityList,
        budgetAccountAssociationList = budgetAccountAssociationList,
        accountList = accountsAndActiveOne.accounts
    )
}

@Preview(
    name = "EditBudgetScreen",
    group = "BudgetsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditBudgetScreenPreview() {
    EditBudgetScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        accountList = accountsAndActiveOne.accounts,
        budgetEntity = budgetEntityList.first(),
        budgetAccountAssociationList = budgetAccountAssociationList
    )
}

@Preview(
    name = "EditCategoriesScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditCategoriesScreenPreview() {
    EditCategoriesScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        categoryType = CategoryType.Expense
    )
}

@Preview(
    name = "EditSubcategoriesScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditSubcategoriesScreenPreview() {
    EditSubcategoriesScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
    )
}

@Preview(
    name = "EditCategoryScreen",
    group = "CategoriesSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditCategoryScreenPreview() {
    EditCategoryScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
    )
}

@Preview(
    name = "EditCategoryCollectionsScreen",
    group = "CollectionsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditCategoryCollectionsScreenPreview() {
    EditCategoryCollectionsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        collectionType = CategoryCollectionType.Mixed,
        categoryCollectionsWithIdsByType = categoryCollectionsWithIdsByType,
    )
}

@Preview(
    name = "EditCategoryCollectionScreen",
    group = "CollectionsSettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEditCategoryCollectionScreenPreview() {
    EditCategoryCollectionScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        collectionWithIds = categoryCollectionsWithIdsByType.mixed.first()
    )
}

@Preview(
    name = "AppearanceScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentAppearanceScreenPreview() {
    PersonalisationScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        isBottomBarVisible = isBottomBarVisible
    )
}

@Preview(
    name = "LanguageScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentLanguageScreenPreview() {
    LanguageScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        appLanguage = AppLanguage.English.languageCode,
        selectedLanguage = AppLanguage.German.languageCode
    )
}

@Preview(
    name = "SettingsDataScreen",
    group = "SettingsScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentSettingsDataScreenPreview() {
    ResetDataScreenPreview(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible,
    )
}


@Preview(
    name = "SignInScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentSignInScreenPreview() {
    SignInScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "SignUpScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentSignUpScreenPreview() {
    SignUpScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "ProfileScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentProfileScreenPreview() {
    ProfileScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "UpdateEmailScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentUpdateEmailScreenPreview() {
    UpdateEmailScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "UpdatePasswordScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentUpdatePasswordScreenPreview() {
    UpdatePasswordScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "RequestPasswordResetScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentRequestPasswordResetScreenPreview() {
    RequestPasswordResetScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "ResetPasswordScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentResetPasswordScreenPreview() {
    ResetPasswordScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "DeleteAccountScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentDeleteAccountScreenPreview() {
    DeleteAccountScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EmailVerificationFailedScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentEmailVerificationFailedScreenPreview() {
    EmailVerificationErrorScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "AuthResultSuccessScreen",
    group = "AuthScreens",
    apiLevel = 34,
    locale = langCode,
    device = device
)
@Composable
fun MainAppContentAuthResultSuccessScreenPreview() {
    AuthResultSuccessScreenPreview(
        appTheme = appTheme,
        screenType = AuthResultSuccessScreenType.PasswordUpdate
    )
}