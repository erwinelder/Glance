package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.screen.CurrencyPickerScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountScreenPreview
import com.ataglance.walletglance.account.presentation.screen.EditAccountsScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.DeleteAccountScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.EmailUpdateEmailVerificationScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.FinishSignUpScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.ProfileScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.RequestEmailUpdateScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.RequestPasswordResetScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.ResetPasswordScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.SignInScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.SignUpEmailVerificationScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.SignUpScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.UpdatePasswordScreenPreview
import com.ataglance.walletglance.auth.presentation.screen.VerifyEmailUpdateScreenPreview
import com.ataglance.walletglance.budget.data.model.BudgetAccountAssociationDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModel
import com.ataglance.walletglance.budget.data.model.BudgetDataModelWithAssociations
import com.ataglance.walletglance.budget.domain.utils.fillUsedAmountsByTransactions
import com.ataglance.walletglance.budget.mapper.budget.toDomainModel
import com.ataglance.walletglance.budget.presentation.component.widget.ChosenBudgetsWidgetPreview
import com.ataglance.walletglance.budget.presentation.screen.BudgetStatisticsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.BudgetsScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetScreenPreview
import com.ataglance.walletglance.budget.presentation.screen.EditBudgetsScreenPreview
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.presentation.component.CategoriesStatisticsWidgetPreview
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
import com.ataglance.walletglance.core.domain.date.DateRangeWithEnum
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.domain.date.RepeatingPeriod
import com.ataglance.walletglance.core.presentation.component.widget.ExpensesIncomeWidgetPreview
import com.ataglance.walletglance.core.utils.toTimestamp
import com.ataglance.walletglance.notification.presentation.screen.NotificationsScreenPreview
import com.ataglance.walletglance.personalization.domain.model.WidgetName
import com.ataglance.walletglance.personalization.presentation.screen.PersonalizationScreenPreview
import com.ataglance.walletglance.transaction.presentation.component.RecentRecordsWidgetPreview
import com.ataglance.walletglance.record.presentation.model.RecordDraft
import com.ataglance.walletglance.record.presentation.model.RecordDraftItem
import com.ataglance.walletglance.record.presentation.model.RecordDraftWithItems
import com.ataglance.walletglance.record.presentation.screen.RecordCreationScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.LanguageScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.ResetDataScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.SettingsHomeScreenPreview
import com.ataglance.walletglance.settings.presentation.screen.StartSetupScreenPreview
import com.ataglance.walletglance.transaction.domain.model.Record
import com.ataglance.walletglance.transaction.domain.model.RecordItem
import com.ataglance.walletglance.transaction.domain.model.RecordWithItems
import com.ataglance.walletglance.transaction.domain.model.Transfer
import com.ataglance.walletglance.transaction.domain.model.TransferItem
import com.ataglance.walletglance.transaction.presentation.screen.TransactionsScreenPreview
import com.ataglance.walletglance.transfer.presentation.model.TransferDraft
import com.ataglance.walletglance.transfer.presentation.model.TransferDraftItem
import com.ataglance.walletglance.transfer.presentation.screen.TransferCreationScreenPreview
import kotlinx.datetime.LocalDateTime

private const val device = "spec:width=1440px,height=3120px,dpi=560" // default
//private const val device = "spec:width=1440px,height=2988px,dpi=560" // phone promo
//private const val device = "spec:width=1840px,height=2150px,dpi=400" // tablet 7inch promo
//private const val device = "spec:width=2560px,height=1561px,dpi=320" // tablet 10inch promo

private val appTheme: AppTheme = AppTheme.LightDefault
private const val langCode: String = "en"
private const val isAppSetUp: Boolean = true

private val accounts = listOf(

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
    accounts = accounts,
    activeAccount = accounts.find { it.isActive } ?: accounts.first()
)
private val dateRangeWithEnum = DateRangeWithEnum.fromEnum(enum = DateRangeEnum.ThisMonth)
private const val isCustomDateRangeWindowOpened = false
private val transactions = listOf(
    RecordWithItems(
        record = Record(
            id = 1,
            date = LocalDateTime(2024, 9, 24, 12, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount!!.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 1,
                recordId = 1,
                totalAmount = 68.43,
                quantity = null,
                categoryId = 1,
                subcategoryId = 13,
                note = "bread, milk"
            ),
            RecordItem(
                id = 2,
                recordId = 1,
                totalAmount = 178.9,
                quantity = null,
                categoryId = 3,
                subcategoryId = 24,
                note = "shampoo"
            )
        )
    ),
    Transfer(
        id = 1,
        date = LocalDateTime(2024, 9, 23, 0, 0).toTimestamp(),
        sender = TransferItem(
            accountId = accountsAndActiveOne.activeAccount.id,
            amount = 3000.0,
            rate = 1.0
        ),
        receiver = TransferItem(
            accountId = accountsAndActiveOne.accounts[1].id,
            amount = 3000.0,
            rate = 1.0
        ),
        includeInBudgets = true
    ),
    RecordWithItems(
        record = Record(
            id = 4,
            date = LocalDateTime(2024, 9, 18, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 4,
                recordId = 4,
                totalAmount = 120.9,
                quantity = null,
                categoryId = 6,
                subcategoryId = 40,
                note = "Music platform"
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 5,
            date = LocalDateTime(2024, 9, 15, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 5,
                recordId = 5,
                totalAmount = 799.9,
                quantity = null,
                categoryId = 3,
                subcategoryId = 21,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 6,
            date = LocalDateTime(2024, 9, 12, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 6,
                recordId = 6,
                totalAmount = 3599.9,
                quantity = null,
                categoryId = 1,
                subcategoryId = 13,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 7,
            date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 7,
                recordId = 7,
                totalAmount = 8500.0,
                quantity = null,
                categoryId = 2,
                subcategoryId = 15,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 8,
            date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
            type = CategoryType.Income,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 8,
                recordId = 8,
                totalAmount = 42600.0,
                quantity = null,
                categoryId = 72,
                subcategoryId = null,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 9,
            date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 9,
                recordId = 9,
                totalAmount = 799.9,
                quantity = null,
                categoryId = 6,
                subcategoryId = 38,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 10,
            date = LocalDateTime(2024, 6, 4, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.accounts[1].id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 10,
                recordId = 10,
                totalAmount = 450.41,
                quantity = null,
                categoryId = 9,
                subcategoryId = 50,
                note = null
            )
        )
    ),
    RecordWithItems(
        record = Record(
            id = 10,
            date = LocalDateTime(2024, 9, 4, 0, 0).toTimestamp(),
            type = CategoryType.Expense,
            accountId = accountsAndActiveOne.activeAccount.id,
            includeInBudgets = true
        ),
        items = listOf(
            RecordItem(
                id = 10,
                recordId = 10,
                totalAmount = 690.56,
                quantity = null,
                categoryId = 10,
                subcategoryId = 58,
                note = null
            )
        )
    ),
)

private val categoryCollectionsWithIdsByType = CategoryCollectionsWithIdsByType(
    mixed = listOf(
        CategoryCollectionWithIds(
            id = 1,
            orderNum = 1,
            type = CategoryCollectionType.Mixed,
            name = "Essentials",
            categoryIds = listOf(13, 14, 25, 30)
        ),
        CategoryCollectionWithIds(
            id = 2,
            orderNum = 2,
            type = CategoryCollectionType.Mixed,
            name = "Other",
            categoryIds = listOf(15, 16, 21, 30)
        ),
        CategoryCollectionWithIds(
            id = 3,
            orderNum = 3,
            type = CategoryCollectionType.Mixed,
            name = "All stuff",
            categoryIds = (13..34).toList()
        )
    ),
)
private val budgetDataModelsWithAssociations = listOf(
    BudgetDataModelWithAssociations(
        budget = BudgetDataModel(
            id = 1,
            amountLimit = 5000.0,
            categoryId = 1,
            name = "Food & drinks",
            repeatingPeriod = RepeatingPeriod.Monthly.name,
        ),
        associations = listOf(
            BudgetAccountAssociationDataModel(budgetId = 1, accountId = 1),
            BudgetAccountAssociationDataModel(budgetId = 1, accountId = 4)
        )
    ),
    BudgetDataModelWithAssociations(
        budget = BudgetDataModel(
            id = 2,
            amountLimit = 1000.0,
            categoryId = 9,
            name = "Travels",
            repeatingPeriod = RepeatingPeriod.Yearly.name,
        ),
        associations = listOf(
            BudgetAccountAssociationDataModel(budgetId = 2, accountId = 2)
        )
    ),
    BudgetDataModelWithAssociations(
        budget = BudgetDataModel(
            id = 3,
            amountLimit = 1000.0,
            categoryId = 6,
            name = "Digital life",
            repeatingPeriod = RepeatingPeriod.Monthly.name,
        ),
        associations = listOf(
            BudgetAccountAssociationDataModel(budgetId = 3, accountId = 1)
        )
    ),
    BudgetDataModelWithAssociations(
        budget = BudgetDataModel(
            id = 4,
            amountLimit = 1500.0,
            categoryId = 10,
            name = "Entertainment",
            repeatingPeriod = RepeatingPeriod.Monthly.name,
        ),
        associations = listOf(
            BudgetAccountAssociationDataModel(budgetId = 4, accountId = 1)
        )
    ),
)



@Preview(
    name = "ExpensesIncomeWidget",
    group = "Widgets",
    locale = langCode,
    device = device
)
@Composable
private fun ExpensesIncomeWidgetPreview_() {
    ExpensesIncomeWidgetPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "RecentRecordsWidget",
    group = "Widgets",
    locale = langCode,
    device = device
)
@Composable
private fun RecentRecordsWidgetPreview_() {
    RecentRecordsWidgetPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "CategoriesStatisticsWidget",
    group = "Widgets",
    locale = langCode,
    device = device
)
@Composable
private fun CategoriesStatisticsWidgetPreview_() {
    CategoriesStatisticsWidgetPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "ChosenBudgetsWidget",
    group = "Widgets",
    locale = langCode,
    device = device
)
@Composable
private fun ChosenBudgetsWidgetPreview_() {
    ChosenBudgetsWidgetPreview(
        appTheme = appTheme
    )
}



@Preview(
    name = "HomeScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun HomeScreenPreview_() {
    val defaultCategoriesPackage = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    val budgetsOnWidget = budgetDataModelsWithAssociations.mapNotNull { budget ->
        budget.toDomainModel(
            groupedCategoriesList = defaultCategoriesPackage.expense,
            accounts = accountsAndActiveOne.accounts
        )
    }

    HomeScreenPreview(
        appTheme = appTheme,
        accountsAndActiveOne = accountsAndActiveOne,
        dateRangeWithEnum = dateRangeWithEnum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        budgetsOnWidget = budgetsOnWidget
            .fillUsedAmountsByTransactions(transactions = transactions)
            .take(1),
        widgetNames = listOf(
            WidgetName.TotalForPeriod,
            WidgetName.ChosenBudgets,
        ),
        transactions = transactions
    )
}

@Preview(
    name = "RecordsScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun RecordsScreenPreview_() {
    val context = LocalContext.current
    val collectionType = CategoryCollectionType.Mixed
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(context.getString(R.string.all_categories))
        .getByType(collectionType)

    TransactionsScreenPreview(
        appTheme = appTheme,
        accounts = accountsAndActiveOne.accounts,
        currentDateRangeEnum = dateRangeWithEnum.enum,
        isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
        collectionType = collectionType,
        collectionList = categoryCollectionWithIdsList,
        transactions = transactions
    )
}

@Preview(
    name = "CategoryStatisticsScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun CategoryStatisticsScreenPreview_() {
    val categoryType = CategoryType.Expense
    val currentCollectionType = CategoryCollectionType.Expense
    val categoryCollectionWithIdsList = categoryCollectionsWithIdsByType
        .appendDefaultCollection(name = stringResource(R.string.all_categories))
        .getByType(type = currentCollectionType)

    CategoryStatisticsScreenPreview(
        appTheme = appTheme,
        accounts = accountsAndActiveOne.accounts,
        currentDateRangeEnum = dateRangeWithEnum.enum,
        categoryType = categoryType,
        currentCollectionType = currentCollectionType,
        collectionList = categoryCollectionWithIdsList,
        selectedCollection = categoryCollectionWithIdsList[0],
        transactions = transactions,
        parentCategory = null
    )
}

@Preview(
    name = "BudgetsScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun BudgetsScreenPreview_() {
    BudgetsScreenPreview(
        appTheme = appTheme,
        budgetDataModelsWithAssociations = budgetDataModelsWithAssociations,
        accounts = accountsAndActiveOne.accounts,
        transactions = transactions
    )
}

@Preview(
    name = "BudgetStatisticsScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun BudgetStatisticsScreenPreview_() {
    val defaultCategories = DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
    val budget = budgetDataModelsWithAssociations
        .mapNotNull {
            it.toDomainModel(groupedCategoriesList = defaultCategories.expense, accounts = accounts)
        }
        .fillUsedAmountsByTransactions(transactions = transactions)[0]

    BudgetStatisticsScreenPreview(
        appTheme = appTheme,
        groupedCategoriesByType = defaultCategories,
        accountList = accountsAndActiveOne.accounts.let { listOf(it[0], it[3]) },
        budget = budget,
        totalAmounts = listOf(4800.0, 5000.0, 4500.0, 5200.0, 4600.0),
    )
}

@Preview(
    name = "RecordCreationScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun RecordCreationScreenPreview_() {
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()
    val recordDraftWithItems = RecordDraftWithItems(
        record = RecordDraft(
            recordId = 0,
            account = accountsAndActiveOne.activeAccount,
            type = CategoryType.Expense,
            dateTimeState = DateTimeState.fromCurrentTime()
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
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
        recordDraftWithItems = recordDraftWithItems
    )
}

@Preview(
    name = "TransferCreationScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun TransferCreationScreenPreview_() {
    val accountList = accountsAndActiveOne.accounts
    val transferDraft = TransferDraft(
        id = 0,
        sender = TransferDraftItem(
            account = accountList[0],
            amount = "3000.0",
            rate = "22.44"
        ),
        receiver = TransferDraftItem(
            account = accountList[1],
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
    locale = langCode,
    device = device
)
@Composable
private fun StartSetupScreenPreview_() {
    StartSetupScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "SettingsHomeScreen",
    group = "SettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun SettingsHomeScreenPreview_() {
    SettingsHomeScreenPreview(
        appTheme = appTheme,
        isSignedIn = true
    )
}

@Preview(
    name = "EditAccountsScreen",
    group = "AccountsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditAccountsScreenPreview_() {
    EditAccountsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        accountList = accountsAndActiveOne.accounts
    )
}

@Preview(
    name = "EditAccountScreen",
    group = "AccountsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditAccountScreenPreview_() {
    EditAccountScreenPreview(
        appTheme = appTheme,
        account = accountsAndActiveOne.accounts.first()
    )
}

@Preview(
    name = "CurrencyPickerScreen",
    group = "AccountsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun CurrencyPickerScreenPreview_() {
    CurrencyPickerScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EditBudgetsScreen",
    group = "BudgetsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditBudgetsScreenPreview_() {
    EditBudgetsScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        budgetDataModelsWithAssociations = budgetDataModelsWithAssociations,
        accounts = accountsAndActiveOne.accounts
    )
}

@Preview(
    name = "EditBudgetScreen",
    group = "BudgetsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditBudgetScreenPreview_() {
    EditBudgetScreenPreview(
        appTheme = appTheme,
        accountList = accountsAndActiveOne.accounts,
        budgetDataModelWithAssociations = budgetDataModelsWithAssociations.first()
    )
}

@Preview(
    name = "EditCategoriesScreen",
    group = "CategoriesSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditCategoriesScreenPreview_() {
    EditCategoriesScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp,
        categoryType = CategoryType.Expense
    )
}

@Preview(
    name = "EditSubcategoriesScreen",
    group = "CategoriesSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditSubcategoriesScreenPreview_() {
    EditSubcategoriesScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EditCategoryScreen",
    group = "CategoriesSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditCategoryScreenPreview_() {
    EditCategoryScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EditCategoryCollectionsScreen",
    group = "CollectionsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditCategoryCollectionsScreenPreview_() {
    EditCategoryCollectionsScreenPreview(
        appTheme = appTheme,
        collectionType = CategoryCollectionType.Mixed,
        categoryCollectionsWithIdsByType = categoryCollectionsWithIdsByType,
    )
}

@Preview(
    name = "EditCategoryCollectionScreen",
    group = "CollectionsSettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EditCategoryCollectionScreenPreview_() {
    EditCategoryCollectionScreenPreview(
        appTheme = appTheme,
        collectionWithIds = categoryCollectionsWithIdsByType.mixed.first()
    )
}

@Preview(
    name = "PersonalizationScreen",
    group = "SettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun PersonalizationScreenPreview_() {
    PersonalizationScreenPreview(
        appTheme = appTheme,
        isAppSetUp = isAppSetUp
    )
}

@Preview(
    name = "NotificationsScreen",
    group = "SettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun NotificationsScreenPreview_() {
    NotificationsScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "LanguageScreen",
    group = "SettingsScreens",
    locale = langCode,
    device = device
)
@Composable
private fun LanguageScreenPreview_() {
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
    locale = langCode,
    device = device
)
@Composable
private fun ResetDataScreenPreview_() {
    ResetDataScreenPreview(
        appTheme = appTheme
    )
}


@Preview(
    name = "SignInScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun SignInScreenPreview_() {
    SignInScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "SignUpScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun SignUpScreenPreview_() {
    SignUpScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EmailVerificationScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun SignUpEmailVerificationScreenPreview_() {
    SignUpEmailVerificationScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "FinishSignUpScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun FinishSignUpScreenPreview_() {
    FinishSignUpScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "ProfileScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun ProfileScreenPreview_() {
    ProfileScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "RequestEmailUpdateScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun RequestEmailUpdateScreenPreview_() {
    RequestEmailUpdateScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "EmailUpdateEmailVerification",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun EmailUpdateEmailVerificationScreenPreview_() {
    EmailUpdateEmailVerificationScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "VerifyEmailUpdate",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun VerifyEmailUpdateScreenPreview_() {
    VerifyEmailUpdateScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "UpdatePasswordScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun UpdatePasswordScreenPreview_() {
    UpdatePasswordScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "RequestPasswordResetScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun RequestPasswordResetScreenPreview_() {
    RequestPasswordResetScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "ResetPasswordScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun ResetPasswordScreenPreview_() {
    ResetPasswordScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "DeleteAccountScreen",
    group = "AuthScreens",
    locale = langCode,
    device = device
)
@Composable
private fun DeleteAccountScreenPreview_() {
    DeleteAccountScreenPreview(
        appTheme = appTheme
    )
}

@Preview(
    name = "FinishSetupScreen",
    group = "MainScreens",
    locale = langCode,
    device = device
)
@Composable
private fun FinishSetupScreenPreview_() {
    FinishSetupScreenPreview(
        appTheme = appTheme
    )
}